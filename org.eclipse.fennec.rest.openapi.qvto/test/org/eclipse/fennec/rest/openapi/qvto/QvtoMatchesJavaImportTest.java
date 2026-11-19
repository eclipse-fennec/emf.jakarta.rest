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
package org.eclipse.fennec.rest.openapi.qvto;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.openapi.ecore.OpenApiImporter;
import org.eclipse.fennec.openapi.ecore.OpenApiModel;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestResource;
import org.eclipse.fennec.rest.openapi.ImportedApplication;
import org.eclipse.fennec.rest.openapi.OpenApiRestImporter;
import org.junit.jupiter.api.Test;

/**
 * The QVT-O transformation against its Java counterpart: both import the same document, and
 * what they derive has to be the same, element by element. The Java importer has its own,
 * explicit tests; this test makes them apply to the transformation as well.
 */
class QvtoMatchesJavaImportTest {

	private static final OpenApiRestImporter.Options OPTIONS = new OpenApiRestImporter.Options("petstore", "shop",
			"https://example.org/petstore", "https://example.org/petstore/logic");

	/** The document of {@code OpenApiRestImporterTest}. */
	private static final String DOCUMENT = """
			{
			  "openapi": "3.0.3",
			  "info": { "title": "Mini Petstore", "version": "1.0" },
			  "paths": {
			    "/pets": {
			      "post": {
			        "operationId": "createPet",
			        "requestBody": { "content": { "application/json": {
			          "schema": { "$ref": "#/components/schemas/Pet" } } } },
			        "responses": { "201": { "description": "created", "content": { "application/json": {
			          "schema": { "$ref": "#/components/schemas/Pet" } } } } }
			      },
			      "get": {
			        "operationId": "listPets",
			        "responses": { "200": { "description": "ok", "content": { "application/json": {
			          "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Pet" } } } } } }
			      }
			    },
			    "/pets/{petId}": {
			      "get": {
			        "operationId": "getPet",
			        "parameters": [
			          { "name": "petId", "in": "path", "required": true, "schema": { "type": "integer" } },
			          { "name": "verbose", "in": "query", "schema": { "type": "boolean" } },
			          { "name": "X-Trace-Id", "in": "header", "schema": { "type": "string" } }
			        ],
			        "security": [ { "apiKey": [] } ],
			        "responses": { "200": { "description": "ok", "content": { "application/json": {
			          "schema": { "$ref": "#/components/schemas/Pet" } } } } }
			      },
			      "put": {
			        "operationId": "updatePet",
			        "parameters": [
			          { "name": "petId", "in": "path", "required": true, "schema": { "type": "integer" } }
			        ],
			        "requestBody": { "content": { "application/json": {
			          "schema": { "$ref": "#/components/schemas/Pet" } } } },
			        "security": [ { "oauth": [ "write:pets", "admin" ] } ],
			        "responses": { "200": { "description": "ok", "content": { "application/json": {
			          "schema": { "$ref": "#/components/schemas/Pet" } } } } }
			      },
			      "delete": {
			        "operationId": "deletePet",
			        "parameters": [
			          { "name": "petId", "in": "path", "required": true, "schema": { "type": "integer" } }
			        ],
			        "security": [ { "oauth": [ "write:pets" ] }, { "apiKey": [] } ],
			        "responses": { "204": { "description": "gone" } }
			      }
			    }
			  },
			  "components": {
			    "schemas": {
			      "Pet": { "type": "object", "properties": {
			        "id": { "type": "integer", "format": "int64" },
			        "name": { "type": "string" } } }
			    },
			    "securitySchemes": {
			      "apiKey": { "type": "apiKey", "name": "X-Api-Key", "in": "header" },
			      "oauth": { "type": "oauth2", "flows": { "clientCredentials": {
			        "tokenUrl": "https://example.org/token",
			        "scopes": { "write:pets": "modify pets", "admin": "everything" } } } }
			    }
			  }
			}
			""";

	// every import gets a freshly loaded document: an import re-namespaces the schemas package
	private static OpenApiModel document() {
		return OpenApiImporter.fromJson(DOCUMENT.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void transformationDerivesTheSameServiceAndBindingAsTheJavaImporter() {
		ImportedApplication java = OpenApiRestImporter.importApplication(document(), OPTIONS);
		ImportedApplication qvto = OpenApiQvtoImporter.importApplication(document(), OPTIONS);

		assertThat(describe(qvto)).isEqualTo(describe(java));
	}

	@Test
	void generatedClassesAreAddressedByTheirNamespace() {
		ImportedApplication qvto = OpenApiQvtoImporter.importApplication(document(), OPTIONS);

		EClass service = (EClass) qvto.servicePackage().getEClassifier("PetstoreService");
		assertThat(EcoreUtil.getURI(service)).hasToString("https://example.org/petstore#//PetstoreService");
		assertThat(EcoreUtil.getURI(qvto.schemasPackage().getEClassifier("Pet")))
				.hasToString("https://example.org/petstore/schemas#//Pet");
		// the operations use the classes of the schemas package, not copies of them
		assertThat(service.getEOperations().stream().filter(operation -> "getPet".equals(operation.getName()))
				.findFirst().orElseThrow().getEType()).isSameAs(qvto.schemasPackage().getEClassifier("Pet"));
	}

	/** Everything the import derives, as text, so that a difference shows as a readable diff. */
	private static String describe(ImportedApplication imported) {
		StringBuilder out = new StringBuilder();
		EPackage servicePackage = imported.servicePackage();
		out.append("package ").append(servicePackage.getName()).append(" prefix=").append(servicePackage.getNsPrefix())
				.append(" ns=").append(servicePackage.getNsURI()).append(" delegates=")
				.append(EcoreUtil.getInvocationDelegates(servicePackage)).append('\n');
		out.append("schemas ns=").append(imported.schemasPackage().getNsURI()).append('\n');
		for (EClassifier classifier : servicePackage.getEClassifiers()) {
			out.append("class ").append(classifier.getName()).append('\n');
			for (EOperation operation : ((EClass) classifier).getEOperations()) {
				out.append("  operation ").append(operation.getName()).append(" : ").append(type(operation.getEType()))
						.append('[').append(operation.getLowerBound()).append("..").append(operation.getUpperBound())
						.append("]\n");
				operation.getEAnnotations().forEach(annotation -> out.append("    @").append(annotation.getSource())
						.append(' ').append(new TreeMap<>(annotation.getDetails().map())).append('\n'));
				for (EParameter parameter : operation.getEParameters()) {
					out.append("    parameter ").append(parameter.getName()).append(" : ")
							.append(type(parameter.getEType())).append('[').append(parameter.getLowerBound())
							.append("..").append(parameter.getUpperBound()).append("]\n");
				}
			}
		}
		out.append("application ").append(imported.application().getName()).append(" base=")
				.append(imported.application().getBasePath()).append(" access=")
				.append(imported.application().getAccess()).append('\n');
		for (RestResource resource : imported.application().getResources()) {
			out.append("resource ").append(resource.getPath()).append(" -> ").append(resource.getTarget().getName())
					.append('\n');
			for (RestMethod method : resource.getMethods()) {
				out.append("  ").append(method.getHttpMethod()).append(" -> ").append(method.getOperation().getName())
						.append(" status=").append(method.getSuccessStatus()).append(" consumes=")
						.append(method.getConsumes()).append(" produces=").append(method.getProduces())
						.append(" access=").append(method.getAccess()).append(" roles=")
						.append(method.getRolesAllowed()).append('\n');
				for (ParameterBinding binding : method.getParameters()) {
					out.append("    ").append(binding.getSource()).append(' ').append(binding.getName()).append(" -> ")
							.append(binding.getParameter().getName()).append(" of ")
							.append(((EOperation) binding.getParameter().eContainer()).getName()).append('\n');
				}
			}
		}
		return out.toString();
	}

	private static String type(EClassifier type) {
		return type == null ? "-" : type.getEPackage().getName() + "." + type.getName();
	}
}
