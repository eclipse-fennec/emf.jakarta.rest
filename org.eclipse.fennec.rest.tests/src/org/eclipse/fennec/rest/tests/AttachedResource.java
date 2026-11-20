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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsApplicationSelect;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsName;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsResource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * An ordinary, compiled whiteboard resource that selects the modelled application
 * {@code greeting} by its name, the way it would select any other application. The model knows
 * nothing about it.
 */
@Component(service = AttachedResource.class)
@JakartarsResource
@JakartarsName(AttachedResource.NAME)
@JakartarsApplicationSelect("(osgi.jakartars.name=greeting)")
@Path("attached")
public class AttachedResource {

	static final String NAME = "testAttached";

	@GET
	@Path("{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String attached(@PathParam("name") String name) {
		return "Attached " + name;
	}
}
