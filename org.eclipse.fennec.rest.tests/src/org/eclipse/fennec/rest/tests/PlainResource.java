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
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsName;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsResource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * An ordinary, compiled whiteboard resource. It selects no application, so it belongs to the
 * default application of the whiteboard, whichever that is: the built-in one, or a modelled
 * application that has replaced it.
 */
@Component(service = PlainResource.class)
@JakartarsResource
@JakartarsName(PlainResource.NAME)
@Path("plain")
public class PlainResource {

	static final String NAME = "testPlain";

	@GET
	@Path("{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String plain(@PathParam("name") String name) {
		return "Plain " + name;
	}
}
