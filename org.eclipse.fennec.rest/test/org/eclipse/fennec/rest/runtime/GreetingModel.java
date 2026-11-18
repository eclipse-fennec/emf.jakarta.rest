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
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestResource;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestFactory;

import jakarta.ws.rs.NotFoundException;

/**
 * Test fixture: a purely dynamic domain model ({@code GreetingService}) whose operations are
 * implemented by an invocation delegate, plus the REST binding model that exposes them.
 * Nothing in here is generated or annotated Java - that is the point of the test.
 */
final class GreetingModel {

	static final String DELEGATE_URI = "http://fennec.eclipse.org/rest/test/delegate";
	static final String NO_DELEGATE_URI = "http://fennec.eclipse.org/rest/test/missing";

	final EPackage domainPackage;
	final EClass service;
	final EOperation greet;
	final EOperation echo;
	final EOperation ping;
	final EOperation tags;
	final EOperation missing;
	final EOperation broken;
	final EOperation orphan;
	final EOperation foreign;
	final RestApplication application;
	final RestResource resource;

	GreetingModel() {
		EcoreFactory ecore = EcoreFactory.eINSTANCE;
		domainPackage = ecore.createEPackage();
		domainPackage.setName("greeting");
		domainPackage.setNsPrefix("greeting");
		domainPackage.setNsURI("http://fennec.eclipse.org/rest/test/greeting");
		EAnnotation delegates = ecore.createEAnnotation();
		delegates.setSource(EcorePackage.eNS_URI);
		delegates.getDetails().put("invocationDelegates", DELEGATE_URI + " " + NO_DELEGATE_URI);
		domainPackage.getEAnnotations().add(delegates);

		service = ecore.createEClass();
		service.setName("GreetingService");
		domainPackage.getEClassifiers().add(service);

		EClassifier string = EcorePackage.Literals.ESTRING;
		EClassifier integer = EcorePackage.Literals.EINT;

		greet = operation(service, "greet", string, DELEGATE_URI);
		parameter(greet, "name", string, 1);
		parameter(greet, "times", integer, 1);
		parameter(greet, "salutation", string, 1);

		echo = operation(service, "echo", string, DELEGATE_URI);
		parameter(echo, "text", string, 1);

		ping = operation(service, "ping", null, DELEGATE_URI);

		tags = operation(service, "tags", string, DELEGATE_URI);
		parameter(tags, "tag", string, -1);

		missing = operation(service, "missing", string, DELEGATE_URI);
		broken = operation(service, "broken", string, DELEGATE_URI);
		orphan = operation(service, "orphan", string, NO_DELEGATE_URI);

		EClass other = ecore.createEClass();
		other.setName("Other");
		domainPackage.getEClassifiers().add(other);
		foreign = operation(other, "foreign", string, DELEGATE_URI);

		RestFactory rest = RestFactory.eINSTANCE;
		application = rest.createRestApplication();
		application.setName("greeting");
		application.setBasePath("/");
		resource = rest.createRestResource();
		resource.setPath("greetings");
		resource.setTarget(service);
		resource.getProduces().add("text/plain");
		application.getResources().add(resource);

		RestMethod greetMethod = method(HttpMethod.GET, "{name}", greet);
		bind(greetMethod, greet.getEParameters().get(0), ParameterSource.PATH, null, null);
		bind(greetMethod, greet.getEParameters().get(1), ParameterSource.QUERY, "n", "1");
		bind(greetMethod, greet.getEParameters().get(2), ParameterSource.HEADER, "X-Salutation", "Hello");

		RestMethod echoMethod = method(HttpMethod.POST, "echo", echo);
		echoMethod.getConsumes().add("text/plain");
		echoMethod.setSuccessStatus(201);
		bind(echoMethod, echo.getEParameters().get(0), ParameterSource.BODY, null, null);

		method(HttpMethod.DELETE, null, ping);

		RestMethod tagsMethod = method(HttpMethod.GET, "tags", tags);
		bind(tagsMethod, tags.getEParameters().get(0), ParameterSource.QUERY, null, null);

		method(HttpMethod.GET, "missing", missing);
		method(HttpMethod.GET, "broken", broken);
		method(HttpMethod.GET, "orphan", orphan);
	}

	/**
	 * The invocation delegate factory that carries the behaviour of all operations. EMF asks
	 * it once per operation, so every delegate knows which operation it stands for.
	 */
	static EOperation.Internal.InvocationDelegate.Factory delegateFactory() {
		return operation -> (target, arguments) -> {
			switch (operation.getName()) {
			case "greet":
				String greeting = arguments.get(2) + ", " + arguments.get(0) + "!";
				return String.join(" ", Collections.nCopies((Integer) arguments.get(1), greeting));
			case "echo":
				return target.eClass().getName() + ":" + arguments.get(0);
			case "ping":
				return null;
			case "tags":
				return String.join("|", ((List<?>) arguments.get(0)).stream().map(String::valueOf).toList());
			case "missing":
				throw new InvocationTargetException(new NotFoundException("no such greeting"));
			case "broken":
				throw new InvocationTargetException(new IllegalStateException("boom"));
			default:
				throw new UnsupportedOperationException(operation.getName());
			}
		};
	}

	RestMethod method(HttpMethod httpMethod, String path, EOperation operation) {
		RestMethod method = RestFactory.eINSTANCE.createRestMethod();
		method.setHttpMethod(httpMethod);
		method.setPath(path);
		method.setOperation(operation);
		resource.getMethods().add(method);
		return method;
	}

	private static void bind(RestMethod method, EParameter parameter, ParameterSource source, String name,
			String defaultValue) {
		ParameterBinding binding = RestFactory.eINSTANCE.createParameterBinding();
		binding.setParameter(parameter);
		binding.setSource(source);
		binding.setName(name);
		binding.setDefaultValue(defaultValue);
		method.getParameters().add(binding);
	}

	private static EOperation operation(EClass owner, String name, EClassifier type, String delegateUri) {
		EOperation operation = EcoreFactory.eINSTANCE.createEOperation();
		operation.setName(name);
		operation.setEType(type);
		EAnnotation body = EcoreFactory.eINSTANCE.createEAnnotation();
		body.setSource(delegateUri);
		body.getDetails().put("body", name);
		operation.getEAnnotations().add(body);
		owner.getEOperations().add(operation);
		return operation;
	}

	private static void parameter(EOperation operation, String name, EClassifier type, int upperBound) {
		EParameter parameter = EcoreFactory.eINSTANCE.createEParameter();
		parameter.setName(name);
		parameter.setEType(type);
		parameter.setUpperBound(upperBound);
		operation.getEParameters().add(parameter);
	}
}
