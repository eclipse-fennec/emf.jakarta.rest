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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.annotation.ConfiguratorType;
import org.eclipse.fennec.emf.osgi.annotation.provide.EMFConfigurator;
import org.osgi.service.component.annotations.Component;

import jakarta.ws.rs.NotFoundException;

/**
 * The complete business logic of the pet store that is brought up from
 * {@code model/petstore.json}: an in-memory store behind the operations of the document.
 * <p>
 * It is the only Java in that application. It is found through the delegate URI of the
 * application's configuration and tells the operations apart by the {@code operationId} the
 * import puts on each of them. The {@code Pet} it handles is a dynamic EObject of the EClass
 * generated from the document's schema.
 * </p>
 */
@Component(service = EOperation.Internal.InvocationDelegate.Factory.class)
@EMFConfigurator(configuratorType = ConfiguratorType.OPERATION_INVOCATION_FACTORY, configuratorName = PetstoreDelegateFactory.DELEGATE_URI)
public class PetstoreDelegateFactory implements EOperation.Internal.InvocationDelegate.Factory {

	static final String DELEGATE_URI = "https://eclipse.org/fennec/rest/example/petstore/logic";

	private final Map<Long, EObject> pets = new ConcurrentHashMap<>();
	private final AtomicLong ids = new AtomicLong();

	@Override
	public EOperation.Internal.InvocationDelegate createInvocationDelegate(EOperation operation) {
		String operationId = operation.getEAnnotation(DELEGATE_URI).getDetails().get("operationId");
		switch (operationId) {
		case "createPet":
			return (target, arguments) -> {
				// copies in and out: the stored state does not share objects with a request
				EObject pet = EcoreUtil.copy((EObject) arguments.get(0));
				long id = ids.incrementAndGet();
				pet.eSet(pet.eClass().getEStructuralFeature("id"), id);
				pets.put(id, pet);
				return EcoreUtil.copy(pet);
			};
		case "getPet":
			return (target, arguments) -> {
				EObject pet = pets.get(arguments.get(0));
				if (pet == null) {
					throw new InvocationTargetException(new NotFoundException("No pet " + arguments.get(0)));
				}
				return EcoreUtil.copy(pet);
			};
		case "deletePet":
			return (target, arguments) -> {
				pets.remove(arguments.get(0));
				return null;
			};
		default:
			return null;
		}
	}
}
