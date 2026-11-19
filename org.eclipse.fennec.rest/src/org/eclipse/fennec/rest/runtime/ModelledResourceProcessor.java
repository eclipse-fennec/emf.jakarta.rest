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
import java.util.function.Supplier;

import org.glassfish.jersey.server.model.ModelProcessor;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceModel;

import jakarta.ws.rs.core.Configuration;

/**
 * Adds the interpreted resources to the resource model of the application they belong to.
 * <p>
 * A {@link ModelProcessor} is the hook Jersey offers for resources that do not come from
 * classes. It is a provider, so it travels through a plain
 * {@link jakarta.ws.rs.core.Application#getSingletons()} and needs no Jersey
 * {@code ResourceConfig}.
 * </p>
 */
class ModelledResourceProcessor implements ModelProcessor {

	private final Supplier<List<Resource>> interpretation;
	private List<Resource> resources;

	/**
	 * @param interpretation builds the resources. It is asked once, when Jersey processes the
	 *                       resource model of the application, not before.
	 */
	ModelledResourceProcessor(Supplier<List<Resource>> interpretation) {
		this.interpretation = interpretation;
	}

	private synchronized List<Resource> resources() {
		if (resources == null) {
			resources = List.copyOf(interpretation.get());
		}
		return resources;
	}

	@Override
	public ResourceModel processResourceModel(ResourceModel resourceModel, Configuration configuration) {
		ResourceModel.Builder builder = new ResourceModel.Builder(resourceModel, false);
		resources().forEach(builder::addResource);
		return builder.build();
	}

	@Override
	public ResourceModel processSubResource(ResourceModel subResourceModel, Configuration configuration) {
		return subResourceModel;
	}
}
