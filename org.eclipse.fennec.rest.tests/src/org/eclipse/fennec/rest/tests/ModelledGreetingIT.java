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
import static org.assertj.core.api.Assertions.fail;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.jakartars.runtime.JakartarsServiceRuntime;
import org.osgi.service.jakartars.runtime.dto.RuntimeDTO;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;

/**
 * Deploys a completely modelled REST application and drives it over HTTP through the real
 * Jakarta RS whiteboard.
 * <p>
 * Three things are deployed, none of them a resource class: the dynamic domain model
 * {@code model/greeting.ecore} (through the dynamic package loader of Fennec EMF OSGi), the
 * binding model {@code model/greeting.rest} (through the REST model loader) and the
 * {@link GreetingDelegateFactory} that carries the behaviour of the operations.
 * </p>
 */
class ModelledGreetingIT {

	/** Matches {@code configs/config.json} plus the base path of {@code greeting.rest}. */
	private static final String APPLICATION_URL = "http://localhost:8198/rest/greeting";
	private static final String BASE_URL = APPLICATION_URL + "/greetings";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();
	private static final List<Configuration> CONFIGURATIONS = new ArrayList<>();

	@BeforeAll
	static void deploy(@InjectBundleContext BundleContext context, @InjectService ConfigurationAdmin configAdmin,
			@InjectService(timeout = 20000) JakartarsServiceRuntime runtime) throws Exception {
		Dictionary<String, Object> domain = new Hashtable<>();
		domain.put("emf.dynamicEcoreUri", context.getBundle().getEntry("model/greeting.ecore").toString());
		CONFIGURATIONS.add(configure(configAdmin, "DynamicModelConfigurator", domain));

		Dictionary<String, Object> binding = new Hashtable<>();
		binding.put("model.uri", context.getBundle().getEntry("model/greeting.rest").toString());
		// only load the binding model once both the REST and the domain package are known
		binding.put("resourceSet.target", "(&(emf.name=rest)(emf.name=greeting))");
		CONFIGURATIONS.add(configure(configAdmin, "org.eclipse.fennec.rest.RestModel", binding));

		awaitApplication(runtime, "greeting");
	}

	@AfterAll
	static void undeploy() throws Exception {
		for (Configuration configuration : CONFIGURATIONS) {
			configuration.delete();
		}
		CONFIGURATIONS.clear();
	}

	@Test
	void pathAndQueryParametersReachTheDelegate() throws Exception {
		HttpResponse<String> response = get("/Ada?n=2", "text/plain");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).isEqualTo("Hello, Ada! Hello, Ada!");
	}

	@Test
	void modelledDefaultValueIsUsed() throws Exception {
		assertThat(get("/Ada", "text/plain").body()).isEqualTo("Hello, Ada!");
	}

	@Test
	void textBodyReachesTheDelegate() throws Exception {
		HttpResponse<String> response = post("/shout", "text/plain", "text/plain", "quiet");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).isEqualTo("QUIET");
	}

	@Test
	void dynamicEObjectResultIsWrittenByTheCodec() throws Exception {
		HttpResponse<String> response = get("/Ada/details", "application/json");

		assertThat(response.statusCode()).as(response.body()).isEqualTo(200);
		assertThat(response.body()).contains("\"name\"").contains("Ada").contains("Hello, Ada!");
	}

	@Test
	void dynamicEObjectBodyIsReadByTheCodec() throws Exception {
		HttpResponse<String> response = post("/reply", "application/json", "application/json",
				"{\"_type\":\"https://eclipse.org/fennec/rest/example/greeting/1.0#//Greeting\",\"name\":\"Ada\",\"text\":\"Hi\"}");

		assertThat(response.statusCode()).as(response.body()).isEqualTo(200);
		assertThat(response.body()).contains("Re: Hi");
	}

	@Test
	void unmodelledPathIsNotFound() throws Exception {
		assertThat(get("/Ada/unknown", "text/plain").statusCode()).isEqualTo(404);
	}

	@Test
	void responseBuiltByTheDelegateIsSentAsIs() throws Exception {
		HttpResponse<String> response = CLIENT.send(
				HttpRequest.newBuilder(URI.create(BASE_URL + "/Ada")).PUT(BodyPublishers.noBody()).build(),
				BodyHandlers.ofString());

		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(response.headers().firstValue("Location")).hasValue(BASE_URL + "/Ada");
		assertThat(response.headers().firstValue("X-Registered")).hasValue("Ada");
	}

	@Test
	void securedResourceRefusesAnAnonymousCaller() throws Exception {
		assertThat(whoami().statusCode()).isEqualTo(401);
	}

	@Test
	void securedResourceRefusesACallerWithoutTheRole() throws Exception {
		assertThat(whoami("X-Test-User", "eve", "X-Test-Roles", "guest").statusCode()).isEqualTo(403);
	}

	@Test
	void principalOfTheAuthenticationFilterReachesTheDelegate() throws Exception {
		HttpResponse<String> response = whoami("X-Test-User", "ada", "X-Test-Roles", "guest,user");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).isEqualTo("I am ada");
	}

	private static HttpResponse<String> whoami(String... headers) throws Exception {
		HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(APPLICATION_URL + "/account/whoami")).GET();
		for (int i = 0; i + 1 < headers.length; i += 2) {
			request.header(headers[i], headers[i + 1]);
		}
		return CLIENT.send(request.build(), BodyHandlers.ofString());
	}

	private static HttpResponse<String> get(String path, String accept) throws Exception {
		return CLIENT.send(HttpRequest.newBuilder(URI.create(BASE_URL + path)).header("Accept", accept).GET().build(),
				BodyHandlers.ofString());
	}

	private static HttpResponse<String> post(String path, String contentType, String accept, String body)
			throws Exception {
		return CLIENT.send(HttpRequest.newBuilder(URI.create(BASE_URL + path)).header("Content-Type", contentType)
				.header("Accept", accept).POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build(),
				BodyHandlers.ofString());
	}

	private static Configuration configure(ConfigurationAdmin configAdmin, String factoryPid,
			Dictionary<String, Object> properties) throws Exception {
		Configuration configuration = configAdmin.getFactoryConfiguration(factoryPid, "greeting", "?");
		configuration.update(properties);
		return configuration;
	}

	/**
	 * Waits until the whiteboard has deployed the application, and fails with the whiteboard's
	 * own view when it does not. Without this a wiring problem only shows as an opaque 404.
	 */
	private static void awaitApplication(JakartarsServiceRuntime runtime, String name) throws Exception {
		long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);
		RuntimeDTO dto = null;
		while (System.currentTimeMillis() < deadline) {
			dto = runtime.getRuntimeDTO();
			if (Arrays.stream(dto.applicationDTOs).anyMatch(application -> name.equals(application.name))) {
				return;
			}
			Thread.sleep(200);
		}
		fail("Application '" + name + "' was not deployed. Deployed: "
				+ (dto == null ? "-" : Arrays.stream(dto.applicationDTOs).map(a -> a.name).toList()) + ", failed: "
				+ (dto == null ? "-"
						: Arrays.stream(dto.failedApplicationDTOs).map(a -> a.name + " (reason " + a.failureReason + ")")
								.toList()));
	}
}
