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

import java.util.Set;

import org.eclipse.fennec.rest.model.rest.RestApplication;

import jakarta.ws.rs.core.Application;

/**
 * A Jakarta RS {@link Application} that has no resource classes at all: its resources are
 * interpreted from a {@link RestApplication} model.
 * <p>
 * Jakarta RS only knows annotated classes, so the application carries a single Jersey
 * {@code ModelProcessor} as singleton, which adds the programmatically built resources to the
 * resource model of the application. Apart from that it is a plain {@link Application} and can
 * be handed to anything that accepts one, e.g. the Jakarta RS whiteboard.
 * </p>
 * <p>
 * The model is interpreted once, in the constructor. A model that cannot be turned into
 * resources is rejected there with an {@link IllegalArgumentException}, instead of failing on
 * the first request. Later changes of the model are not picked up; create a new application.
 * </p>
 */
public class ModelledApplication extends Application {

	private final Set<Object> singletons;

	/**
	 * Interprets the given model.
	 *
	 * @param model the application model
	 * @throws IllegalArgumentException if the model is inconsistent
	 */
	public ModelledApplication(RestApplication model) {
		this.singletons = Set.of(new ModelledResourceProcessor(new ResourceInterpreter().interpret(model)));
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
