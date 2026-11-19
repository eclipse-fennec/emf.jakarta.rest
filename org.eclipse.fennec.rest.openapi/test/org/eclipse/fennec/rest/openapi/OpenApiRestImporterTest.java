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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.openapi.ecore.OpenApiImporter;
import org.eclipse.fennec.rest.model.rest.Access;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestResource;
import org.eclipse.fennec.rest.runtime.ModelledApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Imports a small pet store and checks what the server side is derived to: the service EClass
 * with one EOperation per path operation, and the binding model that exposes them.
 */
class OpenApiRestImporterTest {

	static final String DELEGATE_URI = "https://example.org/petstore/logic";
	static final String NS_URI = "https://example.org/petstore";

	static final String DOCUMENT = """
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

	private ImportedApplication imported;
	private EClass service;

	@BeforeEach
	void importDocument() {
		imported = OpenApiRestImporter.importApplication(
				OpenApiImporter.fromJson(DOCUMENT.getBytes(StandardCharsets.UTF_8)),
				new OpenApiRestImporter.Options("petstore", "shop", NS_URI, DELEGATE_URI));
		service = (EClass) imported.servicePackage().getEClassifier("PetstoreService");
	}

	// --- the service: operations like Java methods

	@Test
	void everyPathOperationBecomesAnOperationOfTheServiceClass() {
		assertThat(service).isNotNull();
		assertThat(service.getEOperations()).extracting(EOperation::getName).containsExactlyInAnyOrder("createPet",
				"listPets", "getPet", "updatePet", "deletePet");
	}

	@Test
	void requestParametersBecomeIndividualParameters() {
		EOperation getPet = operation("getPet");

		assertThat(getPet.getEParameters()).extracting(EParameter::getName).containsExactly("petId", "verbose",
				"X_Trace_Id");
		EParameter petId = getPet.getEParameters().get(0);
		assertThat(petId.getEType().getInstanceClass()).isIn(Long.class, long.class);
		assertThat(petId.getLowerBound()).isEqualTo(1);
		assertThat(getPet.getEParameters().get(1).getEType().getInstanceClass()).isIn(Boolean.class, boolean.class);
		assertThat(getPet.getEParameters().get(1).getLowerBound()).isZero();
	}

	@Test
	void requestBodyBecomesAParameterOfTheSchemaType() {
		EOperation create = operation("createPet");

		assertThat(create.getEParameters()).hasSize(1);
		assertThat(create.getEParameters().get(0).getName()).isEqualTo("body");
		assertThat(create.getEParameters().get(0).getEType()).isSameAs(pet());
	}

	@Test
	void parametersAndBodyAreCombined() {
		assertThat(operation("updatePet").getEParameters()).extracting(EParameter::getName).containsExactly("petId",
				"body");
	}

	@Test
	void responseSchemaBecomesTheResultType() {
		assertThat(operation("getPet").getEType()).isSameAs(pet());
		assertThat(operation("getPet").isMany()).isFalse();
		assertThat(operation("listPets").getEType()).isSameAs(pet());
		assertThat(operation("listPets").isMany()).isTrue();
		assertThat(operation("deletePet").getEType()).isNull();
	}

	@Test
	void operationsAreBoundToTheDelegateUri() {
		assertThat(EcoreUtil.getInvocationDelegates(imported.servicePackage())).containsExactly(DELEGATE_URI);
		for (EOperation operation : service.getEOperations()) {
			assertThat(operation.getEAnnotation(DELEGATE_URI)).as(operation.getName()).isNotNull();
		}
		assertThat(operation("getPet").getEAnnotation(DELEGATE_URI).getDetails().map()).containsEntry("operationId",
				"getPet").containsEntry("method", "GET").containsEntry("path", "/pets/{petId}");
	}

	@Test
	void generatedPackagesGetTheirOwnNamespace() {
		assertThat(imported.servicePackage().getNsURI()).isEqualTo(NS_URI);
		assertThat(imported.schemasPackage().getNsURI()).isEqualTo(NS_URI + "/schemas");
	}

	@Test
	void generatedClassesAreAddressedByTheirNamespace() {
		// this is the form in which a type is named towards the codec
		assertThat(EcoreUtil.getURI(pet())).hasToString(NS_URI + "/schemas#//Pet");
		assertThat(EcoreUtil.getURI(service)).hasToString(NS_URI + "#//PetstoreService");
	}

	// --- the binding

	@Test
	void applicationTakesNameAndBasePathFromTheOptions() {
		assertThat(imported.application().getName()).isEqualTo("petstore");
		assertThat(imported.application().getBasePath()).isEqualTo("shop");
	}

	@Test
	void everyPathBecomesAResourceOfTheService() {
		assertThat(imported.application().getResources()).extracting(RestResource::getPath)
				.containsExactlyInAnyOrder("/pets", "/pets/{petId}");
		assertThat(imported.application().getResources()).allSatisfy(
				resource -> assertThat(resource.getTarget()).isSameAs(service));
		assertThat(resource("/pets").getMethods()).extracting(RestMethod::getHttpMethod)
				.containsExactlyInAnyOrder(HttpMethod.POST, HttpMethod.GET);
		assertThat(method("/pets/{petId}", HttpMethod.GET).getOperation()).isSameAs(operation("getPet"));
	}

	@Test
	void parametersAreBoundWhereTheDocumentPutsThem() {
		List<ParameterBinding> bindings = method("/pets/{petId}", HttpMethod.GET).getParameters();

		assertThat(bindings).extracting(ParameterBinding::getSource).containsExactly(ParameterSource.PATH,
				ParameterSource.QUERY, ParameterSource.HEADER);
		// the wire name survives, although it is no valid parameter name
		assertThat(bindings.get(2).getName()).isEqualTo("X-Trace-Id");
		assertThat(bindings.get(2).getParameter().getName()).isEqualTo("X_Trace_Id");

		List<ParameterBinding> update = method("/pets/{petId}", HttpMethod.PUT).getParameters();
		assertThat(update).extracting(ParameterBinding::getSource).containsExactly(ParameterSource.PATH,
				ParameterSource.BODY);
	}

	@Test
	void jsonIsConsumedAndProducedWhereThereIsABodyOrAResult() {
		RestMethod create = method("/pets", HttpMethod.POST);
		assertThat(create.getConsumes()).containsExactly("application/json");
		assertThat(create.getProduces()).containsExactly("application/json");

		RestMethod delete = method("/pets/{petId}", HttpMethod.DELETE);
		assertThat(delete.getConsumes()).isEmpty();
		assertThat(delete.getProduces()).isEmpty();
	}

	@Test
	void declaredSuccessStatusIsKeptWhenItIsNotTheDefault() {
		assertThat(method("/pets", HttpMethod.POST).getSuccessStatus()).isEqualTo(201);
		assertThat(method("/pets", HttpMethod.GET).getSuccessStatus()).isZero();
		assertThat(method("/pets/{petId}", HttpMethod.DELETE).getSuccessStatus()).isZero();
	}

	// --- security

	@Test
	void operationWithoutSecurityStaysPublic() {
		RestMethod list = method("/pets", HttpMethod.GET);

		assertThat(list.getAccess()).isEqualTo(Access.INHERIT);
		assertThat(list.getRolesAllowed()).isEmpty();
	}

	@Test
	void securityWithoutScopesRequiresAnAuthenticatedCaller() {
		RestMethod get = method("/pets/{petId}", HttpMethod.GET);

		assertThat(get.getAccess()).isEqualTo(Access.AUTHENTICATED);
		assertThat(get.getRolesAllowed()).isEmpty();
	}

	@Test
	void scopesBecomeRoles() {
		RestMethod delete = method("/pets/{petId}", HttpMethod.DELETE);

		assertThat(delete.getRolesAllowed()).containsExactly("write:pets");
	}

	@Test
	void securityThatRolesCannotExpressExactlyIsReported() {
		// updatePet needs both scopes, roles mean "one of"
		assertThat(method("/pets/{petId}", HttpMethod.PUT).getRolesAllowed()).containsExactly("write:pets", "admin");
		assertThat(imported.diagnostics()).anyMatch(
				diagnostic -> diagnostic.contains("updatePet") && diagnostic.contains("all of"));
		// deletePet may alternatively come with an API key and no scope at all
		assertThat(imported.diagnostics()).anyMatch(
				diagnostic -> diagnostic.contains("deletePet") && diagnostic.contains("alternative"));
	}

	// --- the whole

	@Test
	void importedApplicationCanBeInterpreted() {
		assertThatCode(() -> new ModelledApplication(imported.application())).doesNotThrowAnyException();
	}

	@Test
	void diagnosticsOfTheDocumentImportAreKept() {
		// nothing to complain about in this document besides the security notes
		assertThat(imported.diagnostics()).allMatch(diagnostic -> diagnostic.contains("security"));
	}

	private EClass pet() {
		EPackage schemas = imported.schemasPackage();
		return (EClass) schemas.getEClassifier("Pet");
	}

	private EOperation operation(String name) {
		return service.getEOperations().stream().filter(operation -> name.equals(operation.getName())).findFirst()
				.orElseThrow();
	}

	private RestResource resource(String path) {
		return imported.application().getResources().stream().filter(resource -> path.equals(resource.getPath()))
				.findFirst().orElseThrow();
	}

	private RestMethod method(String path, HttpMethod httpMethod) {
		return resource(path).getMethods().stream().filter(method -> method.getHttpMethod() == httpMethod)
				.findFirst().orElseThrow();
	}
}
