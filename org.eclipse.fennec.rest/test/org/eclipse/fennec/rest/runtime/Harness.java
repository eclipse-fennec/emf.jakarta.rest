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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Runs a modelled application in a bare Jersey {@link ApplicationHandler}: no HTTP server and
 * no OSGi. A caller is simulated by handing the request a {@link SecurityContext}, which is
 * what an authentication filter does in a deployed application.
 */
final class Harness {

	private static final URI BASE = URI.create("http://localhost/");

	private final ApplicationHandler handler;

	Harness(RestApplication model) {
		this.handler = new ApplicationHandler(new ModelledApplication(model));
	}

	record Result(int status, String body, MultivaluedMap<String, String> headers) {
	}

	/** A caller with a name and roles; {@code null} stands for an unauthenticated request. */
	record Caller(String name, Set<String> roles) implements SecurityContext, Principal {

		static Caller of(String name, String... roles) {
			return new Caller(name, Set.of(roles));
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Principal getUserPrincipal() {
			return this;
		}

		@Override
		public boolean isUserInRole(String role) {
			return roles.contains(role);
		}

		@Override
		public boolean isSecure() {
			return false;
		}

		@Override
		public String getAuthenticationScheme() {
			return "TEST";
		}
	}

	Result call(String method, String path, String body, String... headers) throws Exception {
		return call(null, method, path, body, headers);
	}

	Result call(Caller caller, String method, String path, String body, String... headers) throws Exception {
		ContainerRequest request = new ContainerRequest(BASE, BASE.resolve(path), method, caller,
				new MapPropertiesDelegate(), null);
		for (int i = 0; i + 1 < headers.length; i += 2) {
			request.header(headers[i], headers[i + 1]);
		}
		if (body != null) {
			request.setEntityStream(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ContainerResponse response = handler.apply(request, out).get(10, TimeUnit.SECONDS);
		return new Result(response.getStatus(), out.toString(StandardCharsets.UTF_8), response.getStringHeaders());
	}
}
