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
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
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
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.jakartars.runtime.JakartarsServiceRuntime;
import org.osgi.service.jakartars.runtime.dto.ApplicationDTO;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;

import jakarta.ws.rs.core.Application;

/**
 * A modelled application in the place of the default application of the whiteboard
 * ({@code model/default.rest}: name {@code .default}, base path {@code /}), with a compiled
 * whiteboard resource ({@link PlainResource}) next to the modelled ones.
 * <p>
 * That is the way to move an existing application to a model piece by piece: its resources
 * select no application and keep their URLs, while one after the other is replaced.
 * </p>
 */
class DefaultApplicationIT {

	/** The root of the whiteboard, see {@code configs/config.json}. */
	private static final String ROOT = "http://localhost:8198/rest";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();
	private static final List<Configuration> CONFIGURATIONS = new ArrayList<>();

	private static JakartarsServiceRuntime whiteboard;
	private static BundleContext bundleContext;

	@BeforeAll
	static void deploy(@InjectBundleContext BundleContext context, @InjectService ConfigurationAdmin configAdmin,
			@InjectService(timeout = 20000) JakartarsServiceRuntime runtime) throws Exception {
		whiteboard = runtime;
		bundleContext = context;
		Dictionary<String, Object> domain = new Hashtable<>();
		domain.put("emf.dynamicEcoreUri", context.getBundle().getEntry("model/greeting.ecore").toString());
		CONFIGURATIONS.add(configure(configAdmin, "DynamicModelConfigurator", domain));

		Dictionary<String, Object> binding = new Hashtable<>();
		binding.put("model.uri", context.getBundle().getEntry("model/default.rest").toString());
		binding.put("resourceSet.target", "(&(emf.name=rest)(emf.name=greeting))");
		CONFIGURATIONS.add(configure(configAdmin, "org.eclipse.fennec.rest.RestModel", binding));

		// the built-in default application is always there, so its name says nothing: wait for the model
		long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);
		while (System.currentTimeMillis() < deadline && get("/modelled/Ada").statusCode() != 200) {
			Thread.sleep(200);
		}
	}

	@AfterAll
	static void undeploy() throws Exception {
		for (Configuration configuration : CONFIGURATIONS) {
			configuration.delete();
		}
		CONFIGURATIONS.clear();
	}

	@Test
	void modelledResourceAnswersAtTheRootOfTheWhiteboard() throws Exception {
		HttpResponse<String> response = get("/modelled/Ada?n=2");

		assertThat(response.statusCode()).as(describe()).isEqualTo(200);
		assertThat(response.body()).isEqualTo("Hello, Ada! Hello, Ada!");
	}

	@Test
	void compiledResourceAnswersNextToIt() throws Exception {
		HttpResponse<String> response = get("/plain/Ada");

		assertThat(response.statusCode()).as(describe()).isEqualTo(200);
		assertThat(response.body()).isEqualTo("Plain Ada");
	}

	@Test
	void modelledApplicationIsTheDefaultApplicationAndHoldsTheCompiledResource() throws Exception {
		ServiceReference<Application> modelled = bundleContext
				.getServiceReferences(Application.class,
						"(fennec.rest.modelled=.default)")
				.stream().findFirst().orElseThrow();

		ApplicationDTO defaultApplication = whiteboard.getRuntimeDTO().defaultApplication;

		assertThat(defaultApplication.serviceId).as(describe()).isEqualTo(modelled.getProperty(Constants.SERVICE_ID));
		assertThat(Arrays.stream(defaultApplication.resourceDTOs).map(resource -> resource.name))
				.contains(PlainResource.NAME);
	}

	private static HttpResponse<String> get(String path) throws Exception {
		return CLIENT.send(HttpRequest.newBuilder(URI.create(ROOT + path)).GET().build(), BodyHandlers.ofString());
	}

	private static String describe() {
		var dto = whiteboard.getRuntimeDTO();
		return "default application: service " + dto.defaultApplication.serviceId + " base " + dto.defaultApplication.base
				+ " resources " + Arrays.stream(dto.defaultApplication.resourceDTOs).map(r -> r.name).toList()
				+ ", applications: " + Arrays.stream(dto.applicationDTOs).map(a -> a.name + " " + a.base).toList()
				+ ", failed: " + Arrays.stream(dto.failedApplicationDTOs)
						.map(a -> a.name + " (reason " + a.failureReason + ")").toList();
	}

	private static Configuration configure(ConfigurationAdmin configAdmin, String factoryPid,
			Dictionary<String, Object> properties) throws Exception {
		Configuration configuration = configAdmin.getFactoryConfiguration(factoryPid, "default", "?");
		configuration.update(properties);
		return configuration;
	}
}
