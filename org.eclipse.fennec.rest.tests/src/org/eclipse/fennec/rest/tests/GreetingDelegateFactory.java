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
package org.eclipse.fennec.rest.tests;

import java.net.URI;
import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.annotation.ConfiguratorType;
import org.eclipse.fennec.emf.osgi.annotation.provide.EMFConfigurator;
import org.osgi.service.component.annotations.Component;

import jakarta.ws.rs.core.Response;

/**
 * The behaviour of the {@code GreetingService} operations of {@code greeting.ecore}. It is an
 * ordinary EMF invocation delegate factory, published as an OSGi service; Fennec EMF OSGi puts
 * it into the EMF delegate registry under the URI the model declares.
 * <p>
 * It receives the target object and the arguments of {@code eInvoke} and returns the result.
 * Only {@code register} knows about REST: it returns a Jakarta RS {@code Response}, the
 * fallback for everything a model cannot declare.
 * </p>
 */
@Component(service = EOperation.Internal.InvocationDelegate.Factory.class)
@EMFConfigurator(configuratorType = ConfiguratorType.OPERATION_INVOCATION_FACTORY, configuratorName = GreetingDelegateFactory.DELEGATE_URI)
public class GreetingDelegateFactory implements EOperation.Internal.InvocationDelegate.Factory {

	static final String DELEGATE_URI = "https://eclipse.org/fennec/rest/example/greeting/delegate";

	@Override
	public EOperation.Internal.InvocationDelegate createInvocationDelegate(EOperation operation) {
		switch (operation.getName()) {
		case "greet":
			return (target, arguments) -> String.join(" ",
					Collections.nCopies((Integer) arguments.get(1), "Hello, " + arguments.get(0) + "!"));
		case "shout":
			return (target, arguments) -> String.valueOf(arguments.get(0)).toUpperCase();
		case "describe":
			return (target, arguments) -> greeting(target, (String) arguments.get(0),
					"Hello, " + arguments.get(0) + "!");
		case "whoami":
			return (target, arguments) -> "I am " + arguments.get(0);
		case "register":
			// beyond what a model can declare: the delegate builds the response itself
			return (target, arguments) -> Response.created(URI.create("greetings/" + arguments.get(0)))
					.header("X-Registered", arguments.get(0)).build();
		case "reply":
			return GreetingDelegateFactory::reply;
		default:
			return null;
		}
	}

	private static Object reply(InternalEObject target, EList<?> arguments) {
		EObject received = (EObject) arguments.get(0);
		EClass type = received.eClass();
		return greeting(target, (String) received.eGet(type.getEStructuralFeature("name")),
				"Re: " + received.eGet(type.getEStructuralFeature("text")));
	}

	private static EObject greeting(InternalEObject target, String name, String text) {
		EClass type = (EClass) target.eClass().getEPackage().getEClassifier("Greeting");
		EObject greeting = EcoreUtil.create(type);
		greeting.eSet(type.getEStructuralFeature("name"), name);
		greeting.eSet(type.getEStructuralFeature("text"), text);
		return greeting;
	}
}
