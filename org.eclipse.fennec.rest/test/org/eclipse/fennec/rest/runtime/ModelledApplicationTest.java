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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestFactory;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Parameter binding, result mapping and error mapping of a modelled application, without HTTP,
 * without OSGi and without a single annotated resource class. Every assertion therefore pins
 * the path model -&gt; Jersey resource -&gt; {@code eInvoke} -&gt; invocation delegate.
 */
class ModelledApplicationTest {

	private final EOperation.Internal.InvocationDelegate.Factory.Registry registry = EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE;
	private GreetingModel model;
	private Harness harness;

	@BeforeEach
	void setUp() {
		registry.put(GreetingModel.DELEGATE_URI, GreetingModel.delegateFactory());
		model = new GreetingModel();
		harness = new Harness(model.application);
	}

	@AfterEach
	void tearDown() {
		registry.remove(GreetingModel.DELEGATE_URI);
	}

	@Test
	void pathParameterIsPassedToTheOperation() throws Exception {
		Harness.Result result = call("GET", "greetings/Ada", null);

		assertThat(result.status()).isEqualTo(200);
		assertThat(result.body()).isEqualTo("Hello, Ada!");
	}

	@Test
	void queryParameterIsConvertedToTheParameterType() throws Exception {
		Harness.Result result = call("GET", "greetings/Ada?n=2", null);

		assertThat(result.body()).isEqualTo("Hello, Ada! Hello, Ada!");
	}

	@Test
	void headerParameterOverridesTheModelledDefault() throws Exception {
		Harness.Result result = call("GET", "greetings/Ada", null, "X-Salutation", "Moin");

		assertThat(result.body()).isEqualTo("Moin, Ada!");
	}

	@Test
	void manyValuedQueryParameterArrivesAsList() throws Exception {
		Harness.Result result = call("GET", "greetings/tags?tag=a&tag=b", null);

		assertThat(result.body()).isEqualTo("a|b");
	}

	@Test
	void bodyIsPassedAndTheModelledSuccessStatusIsUsed() throws Exception {
		Harness.Result result = call("POST", "greetings/echo", "payload", "Content-Type", "text/plain");

		assertThat(result.status()).isEqualTo(201);
		// the delegate sees an instance of the modelled target class
		assertThat(result.body()).isEqualTo("GreetingService:payload");
	}

	@Test
	void operationWithoutResultAnswersNoContent() throws Exception {
		Harness.Result result = call("DELETE", "greetings", null);

		assertThat(result.status()).isEqualTo(204);
		assertThat(result.body()).isEmpty();
	}

	@Test
	void webApplicationExceptionOfTheDelegateDecidesTheStatus() throws Exception {
		assertThat(call("GET", "greetings/missing", null).status()).isEqualTo(404);
	}

	@Test
	void otherFailureOfTheDelegateIsAServerError() throws Exception {
		assertThat(call("GET", "greetings/broken", null).status()).isEqualTo(500);
	}

	@Test
	void unconvertibleParameterIsABadRequest() throws Exception {
		assertThat(call("GET", "greetings/Ada?n=many", null).status()).isEqualTo(400);
	}

	@Test
	void operationWithoutRegisteredDelegateIsUnavailable() throws Exception {
		assertThat(call("GET", "greetings/orphan", null).status()).isEqualTo(503);
	}

	@Test
	void delegateThatArrivesLaterIsPickedUp() throws Exception {
		assertThat(call("GET", "greetings/orphan", null).status()).isEqualTo(503);

		EOperation.Internal.InvocationDelegate.Factory late = operation -> (target, arguments) -> "late";
		registry.put(GreetingModel.NO_DELEGATE_URI, late);
		try {
			Harness.Result result = call("GET", "greetings/orphan", null);
			assertThat(result.status()).isEqualTo(200);
			assertThat(result.body()).isEqualTo("late");
		} finally {
			registry.remove(GreetingModel.NO_DELEGATE_URI);
		}
	}

	@Test
	void responseOfTheDelegateIsPassedThroughUnchanged() throws Exception {
		Harness.Result result = call("POST", "greetings/created", null);

		// the modelled success status (200) does not apply: the delegate has decided
		assertThat(result.status()).isEqualTo(201);
		assertThat(result.headers().getFirst("Location")).isEqualTo("http://localhost/greetings/42");
		assertThat(result.headers().getFirst("X-Extra")).isEqualTo("1");
		assertThat(result.body()).isEqualTo("made");
	}

	@Test
	void matrixParameterIsPassedToTheOperation() throws Exception {
		assertThat(call("GET", "greetings/matrix;color=red", null).body()).isEqualTo("color red");
		assertThat(call("GET", "greetings/matrix", null).body()).isEqualTo("color none");
	}

	@Test
	void formFieldsArePassedToTheOperation() throws Exception {
		Harness.Result result = call("POST", "greetings/form", "name=Ada&name=Bob&age=36", "Content-Type",
				"application/x-www-form-urlencoded");

		assertThat(result.status()).isEqualTo(200);
		assertThat(result.body()).isEqualTo("Ada|Bob aged 36");
	}

	@Test
	void formAndBodyOnOneMethodAreRejectedWhenTheApplicationIsBuilt() {
		RestMethod method = model.method(HttpMethod.PUT, "form", model.form);
		ParameterBinding field = RestFactory.eINSTANCE.createParameterBinding();
		field.setParameter(model.form.getEParameters().get(0));
		field.setSource(ParameterSource.FORM);
		ParameterBinding body = RestFactory.eINSTANCE.createParameterBinding();
		body.setParameter(model.form.getEParameters().get(1));
		body.setSource(ParameterSource.BODY);
		method.getParameters().add(field);
		method.getParameters().add(body);

		assertThatThrownBy(() -> new Harness(model.application)).hasStackTraceContaining("PUT greetings/form")
				.hasStackTraceContaining("form");
	}

	@Test
	void unmodelledPathIsNotFound() throws Exception {
		assertThat(call("GET", "nothing", null).status()).isEqualTo(404);
	}

	@Test
	void operationOfAnotherClassIsRejectedWhenTheApplicationIsBuilt() {
		model.method(HttpMethod.GET, "foreign", model.foreign);

		assertThatThrownBy(() -> new Harness(model.application))
				.hasStackTraceContaining("foreign")
				.hasStackTraceContaining("GreetingService");
	}

	private Harness.Result call(String method, String path, String body, String... headers) throws Exception {
		return harness.call(method, path, body, headers);
	}
}
