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

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;

/**
 * The Jakarta RS request objects a parameter can be bound to with the source {@code CONTEXT}.
 * <p>
 * The object is selected by the <em>name</em> of the instance class of the parameter's data
 * type, not by the class: a dynamic model cannot load Jakarta RS classes, so its data types
 * have a class name but no class.
 * </p>
 */
final class RequestContexts {

	private static final Map<String, Function<ContainerRequestContext, Object>> CONTEXTS = Map.of(
			"jakarta.ws.rs.core.SecurityContext", ContainerRequestContext::getSecurityContext,
			"jakarta.ws.rs.core.UriInfo", ContainerRequestContext::getUriInfo,
			"jakarta.ws.rs.core.Request", ContainerRequestContext::getRequest,
			"jakarta.ws.rs.core.HttpHeaders", request -> request instanceof HttpHeaders headers ? headers : null,
			"jakarta.ws.rs.container.ContainerRequestContext", request -> request);

	private RequestContexts() {
	}

	static Set<String> supported() {
		return CONTEXTS.keySet();
	}

	static boolean supports(String instanceClassName) {
		return instanceClassName != null && CONTEXTS.containsKey(instanceClassName);
	}

	static Object resolve(String instanceClassName, ContainerRequestContext request) {
		return CONTEXTS.get(instanceClassName).apply(request);
	}
}
