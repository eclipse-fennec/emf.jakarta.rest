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

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsApplicationSelect;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsExtension;
import org.osgi.service.jakartars.whiteboard.propertytypes.JakartarsName;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Stand-in for a real authentication filter (e.g. a JWT filter): it takes the caller from the
 * headers {@code X-Test-User} and {@code X-Test-Roles} and puts it into the security context.
 * A request without {@code X-Test-User} stays anonymous.
 * <p>
 * It is an ordinary whiteboard extension. It attaches to every application that carries the
 * service property {@code test.secured=true}, which the modelled application declares in its
 * {@code properties}. Authentication is thereby not part of the model runtime, only
 * authorization is.
 * </p>
 */
@Component(service = ContainerRequestFilter.class, scope = ServiceScope.PROTOTYPE)
@JakartarsExtension
@JakartarsName(HeaderAuthFilter.NAME)
@JakartarsApplicationSelect("(test.secured=true)")
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class HeaderAuthFilter implements ContainerRequestFilter {

	static final String NAME = "testAuth";

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		String user = request.getHeaderString("X-Test-User");
		if (user == null) {
			return;
		}
		String roles = request.getHeaderString("X-Test-Roles");
		request.setSecurityContext(new Caller(user, roles == null ? Set.of() : Set.of(roles.split(","))));
	}

	private record Caller(String name, Set<String> roles) implements SecurityContext, Principal {

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
}
