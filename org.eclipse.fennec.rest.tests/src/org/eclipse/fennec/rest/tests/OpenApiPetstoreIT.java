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

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.jakartars.runtime.JakartarsServiceRuntime;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;

/**
 * Brings up an application from nothing but an OpenAPI document and one configuration, and
 * drives it over HTTP through the real Jakarta RS whiteboard.
 * <p>
 * Neither the data model nor the service nor the binding exist as files: all three are derived
 * from {@code model/petstore.json} when the configuration appears. The only code is the
 * {@link PetstoreDelegateFactory} with the business logic.
 * </p>
 */
class OpenApiPetstoreIT {

	private static final String PETS = "http://localhost:8198/rest/petstore/pets";
	private static final Pattern ID = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration configuration;

	@BeforeAll
	static void deploy(@InjectBundleContext BundleContext context, @InjectService ConfigurationAdmin configAdmin,
			@InjectService(timeout = 20000) JakartarsServiceRuntime runtime) throws Exception {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("name", "petstore");
		properties.put("documentUrl", context.getBundle().getEntry("model/petstore.json").toString());
		properties.put("delegateUri", PetstoreDelegateFactory.DELEGATE_URI);
		// attaches the authentication filter of this bundle to the application
		properties.put("applicationProperties", new String[] { "test.secured=true" });
		configuration = configAdmin.getFactoryConfiguration("org.eclipse.fennec.rest.OpenApiApplication", "petstore",
				"?");
		configuration.update(properties);

		ModelledGreetingIT.awaitApplication(runtime, "petstore");
	}

	@AfterAll
	static void undeploy() throws Exception {
		if (configuration != null) {
			configuration.delete();
		}
	}

	@Test
	void createdPetCanBeReadBack() throws Exception {
		// a plain JSON body, as any client of the API would send it
		HttpResponse<String> created = post("{\"name\":\"Rex\"}");

		assertThat(created.statusCode()).as(created.body()).isEqualTo(201);
		assertThat(created.body()).contains("Rex");
		String id = id(created.body());

		HttpResponse<String> read = send(HttpRequest.newBuilder(URI.create(PETS + "/" + id)).GET());
		assertThat(read.statusCode()).as(read.body()).isEqualTo(200);
		assertThat(read.body()).contains("Rex").contains(id);
	}

	@Test
	void unknownPetIsNotFound() throws Exception {
		assertThat(send(HttpRequest.newBuilder(URI.create(PETS + "/999999")).GET()).statusCode()).isEqualTo(404);
	}

	@Test
	void pathParameterOfTheWrongTypeIsNotFound() throws Exception {
		// as Jakarta RS answers for a @PathParam that cannot be converted
		assertThat(send(HttpRequest.newBuilder(URI.create(PETS + "/rex")).GET()).statusCode()).isEqualTo(404);
	}

	@Test
	void operationWithSecurityRequirementNeedsACaller() throws Exception {
		String id = id(post("{\"name\":\"Bello\"}").body());
		URI pet = URI.create(PETS + "/" + id);

		assertThat(send(HttpRequest.newBuilder(pet).DELETE()).statusCode()).isEqualTo(401);
		assertThat(send(HttpRequest.newBuilder(pet).GET()).statusCode()).isEqualTo(200);

		assertThat(send(HttpRequest.newBuilder(pet).DELETE().header("X-Test-User", "ada")).statusCode())
				.isEqualTo(204);
		assertThat(send(HttpRequest.newBuilder(pet).GET()).statusCode()).isEqualTo(404);
	}

	private static HttpResponse<String> post(String body) throws Exception {
		return send(HttpRequest.newBuilder(URI.create(PETS)).header("Content-Type", "application/json")
				.header("Accept", "application/json").POST(BodyPublishers.ofString(body)));
	}

	private static HttpResponse<String> send(HttpRequest.Builder request) throws Exception {
		return CLIENT.send(request.build(), BodyHandlers.ofString());
	}

	private static String id(String json) {
		Matcher matcher = ID.matcher(json);
		assertThat(matcher.find()).as("id in " + json).isTrue();
		return matcher.group(1);
	}
}
