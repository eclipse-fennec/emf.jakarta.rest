/**
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.rest.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestResource;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

/**
 * Turns a {@link RestApplication} model into Jersey resources, using the programmatic resource
 * API. Every modelled method becomes a Jersey resource method that is handled by an
 * {@link OperationInflector}.
 * <p>
 * The interpreter validates what it can decide from the model alone and rejects an
 * inconsistent model with an {@link IllegalArgumentException} that names the offending
 * element. Whether an invocation delegate exists for an operation is deliberately not checked
 * here: delegates are services and may come and go, so that is decided per request.
 * </p>
 */
class ResourceInterpreter {

	List<Resource> interpret(RestApplication application) {
		List<Resource> resources = new ArrayList<>();
		for (RestResource resource : application.getResources()) {
			resources.add(interpret(resource));
		}
		return resources;
	}

	private Resource interpret(RestResource resource) {
		String path = resource.getPath();
		if (path == null || path.isBlank()) {
			throw new IllegalArgumentException("A resource of application '"
					+ ((RestApplication) resource.eContainer()).getName() + "' has no path");
		}
		EClass target = resource.getTarget();
		if (target == null || target.eIsProxy()) {
			throw new IllegalArgumentException("Resource '" + path + "' has no resolvable target EClass: " + target);
		}
		if (target.isAbstract() || target.isInterface()) {
			throw new IllegalArgumentException("Target EClass '" + target.getName() + "' of resource '" + path
					+ "' cannot be instantiated");
		}
		Resource.Builder root = Resource.builder(path);
		// Jersey wants one child resource per sub path, carrying all HTTP methods of that path
		Map<String, Resource.Builder> children = new LinkedHashMap<>();
		Map<Resource.Builder, Set<String>> methodsOf = new LinkedHashMap<>();
		for (RestMethod method : resource.getMethods()) {
			String subPath = method.getPath();
			Resource.Builder owner = subPath == null || subPath.isBlank() ? root
					: children.computeIfAbsent(subPath, root::addChildResource);
			interpret(resource, method, owner);
			methodsOf.computeIfAbsent(owner, key -> new LinkedHashSet<>()).add(method.getHttpMethod().getName());
		}
		AccessRule ofApplication = resource.eContainer() instanceof RestApplication application
				? AccessRule.of(application)
				: null;
		if (ofApplication != null) {
			methodsOf.forEach((owner, methods) -> guardOptions(owner, methods, ofApplication));
		}
		return root.build();
	}

	/**
	 * Jersey answers OPTIONS for every path by itself, with the methods the path has. No modelled
	 * method describes that answer, so no rule of a method or resource is in effect for it: it
	 * falls under the rule of the application. An application that restricts access answers
	 * OPTIONS itself, after that rule has let the caller pass.
	 */
	private void guardOptions(Resource.Builder owner, Set<String> methods, AccessRule ofApplication) {
		if (methods.contains(HttpMethod.OPTIONS)) {
			return;
		}
		Set<String> allowed = new LinkedHashSet<>(methods);
		if (allowed.contains(HttpMethod.GET)) {
			allowed.add(HttpMethod.HEAD);
		}
		allowed.add(HttpMethod.OPTIONS);
		owner.addMethod(HttpMethod.OPTIONS).handledBy(new GuardedOptions(ofApplication, allowed));
	}

	/** A class of its own: Jersey reads the types of an inflector from its class, which a lambda does not carry. */
	private static final class GuardedOptions implements Inflector<ContainerRequestContext, Response> {

		private final AccessRule rule;
		private final Set<String> allowed;

		GuardedOptions(AccessRule rule, Set<String> allowed) {
			this.rule = rule;
			this.allowed = allowed;
		}

		@Override
		public Response apply(ContainerRequestContext request) {
			rule.check(request.getSecurityContext());
			return Response.ok().allow(allowed).build();
		}
	}

	private void interpret(RestResource resource, RestMethod method, Resource.Builder owner) {
		EClass target = resource.getTarget();
		EOperation operation = method.getOperation();
		String where = method.getHttpMethod() + " " + resource.getPath()
				+ (method.getPath() == null ? "" : "/" + method.getPath());
		if (operation == null || operation.eIsProxy()) {
			throw new IllegalArgumentException("Method '" + where + "' has no resolvable EOperation: " + operation);
		}
		if (!target.getEAllOperations().contains(operation)) {
			throw new IllegalArgumentException("Method '" + where + "' is bound to operation '"
					+ operation.getEContainingClass().getName() + "." + operation.getName()
					+ "', which is not an operation of the target EClass '" + target.getName() + "'");
		}
		List<EParameter> parameters = operation.getEParameters();
		ParameterBinding[] bindings = new ParameterBinding[parameters.size()];
		boolean bodyBound = false;
		boolean formBound = false;
		for (ParameterBinding binding : method.getParameters()) {
			int index = parameters.indexOf(binding.getParameter());
			if (index < 0) {
				throw new IllegalArgumentException("Method '" + where + "' binds a parameter that operation '"
						+ operation.getName() + "' does not have: " + binding.getParameter());
			}
			if (bindings[index] != null) {
				throw new IllegalArgumentException("Method '" + where + "' binds parameter '"
						+ parameters.get(index).getName() + "' more than once");
			}
			if (binding.getSource() == ParameterSource.BODY) {
				if (bodyBound) {
					throw new IllegalArgumentException("Method '" + where + "' binds more than one parameter to the body");
				}
				if (parameters.get(index).isMany()) {
					throw new IllegalArgumentException("Method '" + where + "' binds the many-valued parameter '"
							+ parameters.get(index).getName() + "' to the body, which is not supported yet");
				}
				bodyBound = true;
			}
			formBound |= binding.getSource() == ParameterSource.FORM;
			if (binding.getSource() == ParameterSource.CONTEXT
					&& !RequestContexts.supports(parameters.get(index).getEType().getInstanceClassName())) {
				throw new IllegalArgumentException("Method '" + where + "' binds parameter '"
						+ parameters.get(index).getName() + "' to the request context, but its type '"
						+ parameters.get(index).getEType().getInstanceClassName() + "' is none of "
						+ RequestContexts.supported());
			}
			bindings[index] = binding;
		}
		if (bodyBound && formBound) {
			throw new IllegalArgumentException("Method '" + where
					+ "' binds parameters to form fields and to the body; the body can only be read as one of them");
		}

		ResourceMethod.Builder builder = owner.addMethod(method.getHttpMethod().getName());
		List<String> consumes = method.getConsumes().isEmpty() ? resource.getConsumes() : method.getConsumes();
		if (!consumes.isEmpty()) {
			builder.consumes(consumes.toArray(String[]::new));
		}
		List<String> produces = method.getProduces().isEmpty() ? resource.getProduces() : method.getProduces();
		if (!produces.isEmpty()) {
			builder.produces(produces.toArray(String[]::new));
		}
		builder.handledBy(new OperationInflector(target, operation, bindings, method.getSuccessStatus(),
				AccessRule.of(method)));
	}
}
