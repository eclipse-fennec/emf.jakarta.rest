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

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.glassfish.jersey.server.model.Resource;

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
 * The model is interpreted once: in the constructor, or, for a {@link #deferred} application,
 * when Jersey initialises it. A model that cannot be turned into resources is rejected there
 * with an {@link IllegalArgumentException}, instead of failing on the first request. Later
 * changes of the model are not picked up; create a new application.
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
		List<Resource> resources = new ResourceInterpreter().interpret(model);
		this.singletons = Set.of(new ModelledResourceProcessor(() -> resources));
	}

	private ModelledApplication(Supplier<List<Resource>> interpretation) {
		this.singletons = Set.of(new ModelledResourceProcessor(interpretation));
	}

	/**
	 * An application whose model is interpreted only when Jersey initialises it.
	 * <p>
	 * Interpreting builds Jersey resources. Where the application is created on a thread that has
	 * nothing to do with Jersey, e.g. the one that delivers an OSGi service while the framework
	 * still starts, Jersey may not be usable yet; touching it there can leave it broken for
	 * every application. Deferred, the model is interpreted in the context Jersey sets up for
	 * the application itself.
	 * </p>
	 * <p>
	 * The price: an inconsistent model is not rejected here. It makes the initialisation of the
	 * application fail, with the same {@link IllegalArgumentException}, which is passed to
	 * {@code rejected} first.
	 * </p>
	 *
	 * @param model    the application model
	 * @param rejected told why a model could not be interpreted, before the exception is thrown
	 */
	public static ModelledApplication deferred(RestApplication model, Consumer<RuntimeException> rejected) {
		return new ModelledApplication(() -> {
			try {
				return new ResourceInterpreter().interpret(model);
			} catch (RuntimeException e) {
				rejected.accept(e);
				throw e;
			}
		});
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
