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

import java.lang.annotation.Annotation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.codec.rest.annotations.json.RootElement;

/**
 * The codec's {@link RootElement} annotation as an object. A resource class puts
 * {@code @RootElement(rootType = "...")} on a body parameter to tell the Fennec codec which
 * EClass the body is; a modelled method has no parameter to annotate, so the interpreter hands
 * the same information to the message body reader with the read call.
 * <p>
 * Without it a body would have to name its own type ({@code _type}), which a client of a
 * plain REST API does not do.
 * </p>
 */
final class RootType implements RootElement {

	private final String rootType;

	private RootType(String rootType) {
		this.rootType = rootType;
	}

	/** The annotations to read a body of the given EClass with. */
	static Annotation[] of(EClass eClass) {
		return new Annotation[] { new RootType(EcoreUtil.getURI(eClass).toString()) };
	}

	@Override
	public String rootType() {
		return rootType;
	}

	@Override
	public String rootSchema() {
		return "";
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return RootElement.class;
	}
}
