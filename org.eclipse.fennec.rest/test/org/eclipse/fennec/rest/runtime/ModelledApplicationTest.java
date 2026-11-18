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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Runs a modelled application in a bare Jersey {@link ApplicationHandler}: no HTTP server, no
 * OSGi, and not a single annotated resource class. Every assertion therefore pins the path
 * model -&gt; Jersey resource -&gt; {@code eInvoke} -&gt; invocation delegate.
 */
class ModelledApplicationTest {

	private static final URI BASE = URI.create("http://localhost/");

	private final EOperation.Internal.InvocationDelegate.Factory.Registry registry = EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE;
	private GreetingModel model;
	private ApplicationHandler handler;

	@BeforeEach
	void setUp() {
		registry.put(GreetingModel.DELEGATE_URI, GreetingModel.delegateFactory());
		model = new GreetingModel();
		handler = new ApplicationHandler(new ModelledApplication(model.application));
	}

	@AfterEach
	void tearDown() {
		registry.remove(GreetingModel.DELEGATE_URI);
	}

	@Test
	void pathParameterIsPassedToTheOperation() throws Exception {
		Result result = call("GET", "greetings/Ada", null);

		assertThat(result.status).isEqualTo(200);
		assertThat(result.body).isEqualTo("Hello, Ada!");
	}

	@Test
	void queryParameterIsConvertedToTheParameterType() throws Exception {
		Result result = call("GET", "greetings/Ada?n=2", null);

		assertThat(result.body).isEqualTo("Hello, Ada! Hello, Ada!");
	}

	@Test
	void headerParameterOverridesTheModelledDefault() throws Exception {
		Result result = call("GET", "greetings/Ada", null, "X-Salutation", "Moin");

		assertThat(result.body).isEqualTo("Moin, Ada!");
	}

	@Test
	void manyValuedQueryParameterArrivesAsList() throws Exception {
		Result result = call("GET", "greetings/tags?tag=a&tag=b", null);

		assertThat(result.body).isEqualTo("a|b");
	}

	@Test
	void bodyIsPassedAndTheModelledSuccessStatusIsUsed() throws Exception {
		Result result = call("POST", "greetings/echo", "payload", "Content-Type", "text/plain");

		assertThat(result.status).isEqualTo(201);
		// the delegate sees an instance of the modelled target class
		assertThat(result.body).isEqualTo("GreetingService:payload");
	}

	@Test
	void operationWithoutResultAnswersNoContent() throws Exception {
		Result result = call("DELETE", "greetings", null);

		assertThat(result.status).isEqualTo(204);
		assertThat(result.body).isEmpty();
	}

	@Test
	void webApplicationExceptionOfTheDelegateDecidesTheStatus() throws Exception {
		assertThat(call("GET", "greetings/missing", null).status).isEqualTo(404);
	}

	@Test
	void otherFailureOfTheDelegateIsAServerError() throws Exception {
		assertThat(call("GET", "greetings/broken", null).status).isEqualTo(500);
	}

	@Test
	void unconvertibleParameterIsABadRequest() throws Exception {
		assertThat(call("GET", "greetings/Ada?n=many", null).status).isEqualTo(400);
	}

	@Test
	void operationWithoutRegisteredDelegateIsUnavailable() throws Exception {
		assertThat(call("GET", "greetings/orphan", null).status).isEqualTo(503);
	}

	@Test
	void delegateThatArrivesLaterIsPickedUp() throws Exception {
		assertThat(call("GET", "greetings/orphan", null).status).isEqualTo(503);

		EOperation.Internal.InvocationDelegate.Factory late = operation -> (target, arguments) -> "late";
		registry.put(GreetingModel.NO_DELEGATE_URI, late);
		try {
			Result result = call("GET", "greetings/orphan", null);
			assertThat(result.status).isEqualTo(200);
			assertThat(result.body).isEqualTo("late");
		} finally {
			registry.remove(GreetingModel.NO_DELEGATE_URI);
		}
	}

	@Test
	void unmodelledPathIsNotFound() throws Exception {
		assertThat(call("GET", "nothing", null).status).isEqualTo(404);
	}

	@Test
	void operationOfAnotherClassIsRejectedWhenTheApplicationIsBuilt() {
		model.method(HttpMethod.GET, "foreign", model.foreign);

		assertThatThrownBy(() -> new ApplicationHandler(new ModelledApplication(model.application)))
				.hasStackTraceContaining("foreign")
				.hasStackTraceContaining("GreetingService");
	}

	private Result call(String method, String path, String body, String... headers) throws Exception {
		ContainerRequest request = new ContainerRequest(BASE, BASE.resolve(path), method, null,
				new MapPropertiesDelegate(), null);
		for (int i = 0; i + 1 < headers.length; i += 2) {
			request.header(headers[i], headers[i + 1]);
		}
		if (body != null) {
			request.setEntityStream(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ContainerResponse response = handler.apply(request, out).get(10, TimeUnit.SECONDS);
		return new Result(response.getStatus(), out.toString(StandardCharsets.UTF_8));
	}

	private record Result(int status, String body) {
	}
}
