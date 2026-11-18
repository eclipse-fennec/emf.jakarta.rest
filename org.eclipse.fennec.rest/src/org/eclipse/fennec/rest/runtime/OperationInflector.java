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

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ContainerRequest;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Handles the requests of one modelled resource method: resolves the arguments from the
 * request, invokes the bound {@link EOperation} reflectively on a fresh instance of the target
 * {@link EClass} and turns the result into a {@link Response}.
 * <p>
 * The behaviour of the operation is not in here. {@code eInvoke} ends in the EMF invocation
 * delegate of the operation, for a dynamic model as well as for a generated one.
 * </p>
 */
class OperationInflector implements Inflector<ContainerRequestContext, Response> {

	private static final Map<Class<?>, Class<?>> WRAPPERS = Map.of(boolean.class, Boolean.class, byte.class,
			Byte.class, char.class, Character.class, short.class, Short.class, int.class, Integer.class, long.class,
			Long.class, float.class, Float.class, double.class, Double.class);

	private final EClass target;
	private final EOperation operation;
	private final ParameterBinding[] bindings;
	private final int successStatus;
	private final AccessRule access;

	/**
	 * @param target the class to instantiate per request
	 * @param operation the operation to invoke
	 * @param bindings one entry per parameter of the operation, {@code null} for an unbound one
	 * @param successStatus the status of a successful invocation, 0 for the default
	 * @param access the access rule in effect for the method
	 */
	OperationInflector(EClass target, EOperation operation, ParameterBinding[] bindings, int successStatus,
			AccessRule access) {
		this.access = access;
		this.target = target;
		this.operation = operation;
		this.bindings = bindings;
		this.successStatus = successStatus;
	}

	@Override
	public Response apply(ContainerRequestContext request) {
		// first of all: a caller that is not allowed must not learn anything about the request
		access.check(request.getSecurityContext());
		EList<Object> arguments = new BasicEList<>(bindings.length);
		List<EParameter> parameters = operation.getEParameters();
		for (int i = 0; i < bindings.length; i++) {
			arguments.add(bindings[i] == null ? null : required(parameters.get(i), bindings[i], request));
		}
		EObject instance = EcoreUtil.create(target);
		requireDelegate(instance);
		Object result;
		try {
			result = instance.eInvoke(operation, arguments);
		} catch (InvocationTargetException e) {
			// the delegate decides the response by throwing a WebApplicationException
			if (e.getCause() instanceof WebApplicationException webApplicationException) {
				throw webApplicationException;
			}
			throw new InternalServerErrorException("Operation '" + operation.getName() + "' failed", e.getCause());
		}
		if (result == null) {
			return Response.status(successStatus == 0 ? Response.Status.NO_CONTENT.getStatusCode() : successStatus)
					.build();
		}
		return Response.status(successStatus == 0 ? Response.Status.OK.getStatusCode() : successStatus)
				.entity(result).build();
	}

	/**
	 * A dynamic instance has no behaviour but its invocation delegate. EMF caches a
	 * "not implemented" delegate on the operation for good when it is asked while no factory
	 * is registered, so an operation whose delegate service is not there yet must not be
	 * invoked at all. Any previously cached delegate is dropped as well: its factory is gone.
	 */
	private void requireDelegate(EObject instance) {
		if (instance.eClass().getInstanceClass() == null && EcoreUtil.getInvocationDelegateFactory(operation) == null) {
			((EOperation.Internal) operation).setInvocationDelegate(null);
			throw new ServiceUnavailableException("No invocation delegate is available for operation '"
					+ operation.getEContainingClass().getName() + "." + operation.getName() + "'");
		}
	}

	/** Resolves the argument and refuses the request if a mandatory one is missing. */
	private Object required(EParameter parameter, ParameterBinding binding, ContainerRequestContext request) {
		Object value = resolve(parameter, binding, request);
		boolean missing = value == null || value instanceof List<?> list && list.isEmpty();
		if (missing && parameter.getLowerBound() >= 1) {
			if (binding.getSource() == ParameterSource.PRINCIPAL) {
				throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
			}
			throw new BadRequestException("The mandatory parameter '" + parameter.getName() + "' is missing");
		}
		return value;
	}

	private Object resolve(EParameter parameter, ParameterBinding binding, ContainerRequestContext request) {
		String name = binding.getName() == null || binding.getName().isBlank() ? parameter.getName()
				: binding.getName();
		List<String> literals;
		switch (binding.getSource()) {
		case BODY:
			return readBody(parameter, binding, request);
		case PRINCIPAL:
			SecurityContext security = request.getSecurityContext();
			Principal principal = security == null ? null : security.getUserPrincipal();
			if (principal == null) {
				return null;
			}
			// a string parameter keeps the operation free of any security API
			return String.class.getName().equals(parameter.getEType().getInstanceClassName()) ? principal.getName()
					: principal;
		case CONTEXT:
			return RequestContexts.resolve(parameter.getEType().getInstanceClassName(), request);
		case PATH:
			literals = request.getUriInfo().getPathParameters().get(name);
			break;
		case QUERY:
			literals = request.getUriInfo().getQueryParameters().get(name);
			break;
		case HEADER:
			literals = request.getHeaders().get(name);
			break;
		case COOKIE:
			Cookie cookie = request.getCookies().get(name);
			literals = cookie == null ? null : List.of(cookie.getValue());
			break;
		default:
			throw new IllegalStateException("Unknown parameter source " + binding.getSource());
		}
		if ((literals == null || literals.isEmpty()) && binding.getDefaultValue() != null) {
			literals = List.of(binding.getDefaultValue());
		}
		if (parameter.isMany()) {
			List<Object> values = new ArrayList<>();
			if (literals != null) {
				for (String literal : literals) {
					values.add(convert(parameter, name, literal));
				}
			}
			return values;
		}
		return literals == null || literals.isEmpty() ? null : convert(parameter, name, literals.get(0));
	}

	private Object readBody(EParameter parameter, ParameterBinding binding, ContainerRequestContext request) {
		if (!request.hasEntity()) {
			return binding.getDefaultValue() == null ? null : convert(parameter, "body", binding.getDefaultValue());
		}
		ContainerRequest containerRequest = (ContainerRequest) request;
		EClassifier type = parameter.getEType();
		Class<?> instanceClass = type.getInstanceClass();
		if (type instanceof EDataType && instanceClass == null) {
			// a dynamic data type or enum only has a literal form
			return convert(parameter, "body", containerRequest.readEntity(String.class));
		}
		if (instanceClass == null) {
			instanceClass = EObject.class;
		}
		return containerRequest.readEntity(WRAPPERS.getOrDefault(instanceClass, instanceClass));
	}

	private Object convert(EParameter parameter, String name, String literal) {
		if (!(parameter.getEType() instanceof EDataType dataType)) {
			throw new BadRequestException("Parameter '" + name + "' is of type '" + parameter.getEType().getName()
					+ "', which can only be bound to the body");
		}
		try {
			return EcoreUtil.createFromString(dataType, literal);
		} catch (RuntimeException e) {
			throw new BadRequestException(
					"Parameter '" + name + "' is not a valid " + dataType.getName() + ": " + literal, e);
		}
	}
}
