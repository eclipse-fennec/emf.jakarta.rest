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

import org.eclipse.fennec.rest.model.rest.Access;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestResource;
import org.eclipse.fennec.rest.model.rest.Secured;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The access rule that is in effect for one resource method, resolved once from the model.
 * <p>
 * The most specific element that says something decides: the method, then its resource, then
 * the application. An element says something when it has roles or an access other than
 * {@link Access#INHERIT}. When nothing is said at all, the method is public.
 * </p>
 * <p>
 * The rule only authorizes. Who the caller is comes from the {@link SecurityContext} of the
 * request, which an authentication filter of the application has to provide.
 * </p>
 *
 * @param access {@link Access#PUBLIC}, {@link Access#AUTHENTICATED} or {@link Access#DENY}
 * @param roles the roles of which the caller needs one; empty if any authenticated caller will do
 */
record AccessRule(Access access, List<String> roles) {

	private static final AccessRule OPEN = new AccessRule(Access.PUBLIC, List.of());

	/**
	 * Resolves the rule for a method.
	 *
	 * @throws IllegalArgumentException if an element combines roles with public or denied access
	 */
	static AccessRule of(RestMethod method) {
		RestResource resource = (RestResource) method.eContainer();
		Secured application = resource.eContainer() instanceof RestApplication restApplication ? restApplication
				: null;
		AccessRule rule = null;
		// all three are validated, also those a more specific element overrules
		for (Secured secured : new Secured[] { method, resource, application }) {
			if (secured == null) {
				continue;
			}
			AccessRule own = own(secured);
			if (rule == null) {
				rule = own;
			}
		}
		return rule == null ? OPEN : rule;
	}

	private static AccessRule own(Secured secured) {
		Access access = secured.getAccess();
		if (!secured.getRolesAllowed().isEmpty()) {
			if (access == Access.PUBLIC || access == Access.DENY) {
				throw new IllegalArgumentException("The " + describe(secured) + " combines the roles "
						+ secured.getRolesAllowed() + " with the access " + access);
			}
			return new AccessRule(Access.AUTHENTICATED, List.copyOf(secured.getRolesAllowed()));
		}
		return access == Access.INHERIT ? null : new AccessRule(access, List.of());
	}

	private static String describe(Secured secured) {
		if (secured instanceof RestApplication application) {
			return "application '" + application.getName() + "'";
		}
		if (secured instanceof RestResource resource) {
			return "resource '" + resource.getPath() + "'";
		}
		RestMethod method = (RestMethod) secured;
		return "method '" + method.getHttpMethod() + " " + ((RestResource) method.eContainer()).getPath()
				+ (method.getPath() == null ? "" : "/" + method.getPath()) + "'";
	}

	/**
	 * Lets the request pass or refuses it: 401 for a missing caller, 403 for a caller that is
	 * not allowed.
	 */
	void check(SecurityContext security) {
		if (access == Access.PUBLIC) {
			return;
		}
		if (access == Access.DENY) {
			throw new ForbiddenException();
		}
		if (security == null || security.getUserPrincipal() == null) {
			throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		if (!roles.isEmpty() && roles.stream().noneMatch(security::isUserInRole)) {
			throw new ForbiddenException();
		}
	}
}
