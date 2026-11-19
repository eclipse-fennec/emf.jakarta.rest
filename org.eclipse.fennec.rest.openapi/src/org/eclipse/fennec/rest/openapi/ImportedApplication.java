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
package org.eclipse.fennec.rest.openapi;

import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.rest.model.rest.RestApplication;

/**
 * The server side derived from an OpenAPI document.
 *
 * @param servicePackage the package with the service EClass, whose EOperations are the path
 *        operations of the document
 * @param schemasPackage the package the codec generated from {@code components/schemas}, or
 *        {@code null} if the document has none
 * @param application the binding model that maps the paths to the operations
 * @param diagnostics what could not be imported faithfully, from the document import and from
 *        the derivation of the server side
 * @see OpenApiRestImporter
 */
public record ImportedApplication(EPackage servicePackage, EPackage schemasPackage, RestApplication application,
		List<String> diagnostics) {

	public ImportedApplication {
		diagnostics = List.copyOf(diagnostics);
	}
}
