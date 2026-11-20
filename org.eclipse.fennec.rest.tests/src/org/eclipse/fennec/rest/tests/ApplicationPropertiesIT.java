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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestFactory;
import org.eclipse.fennec.rest.model.rest.RestPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jakartars.whiteboard.JakartarsWhiteboardConstants;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;

import jakarta.ws.rs.core.Application;

/**
 * What a modelled application carries as service properties at the Jakarta RS whiteboard: the
 * {@code properties} of its model and the service properties of the {@link RestApplication}
 * service it comes from. Extensions select applications by these.
 * <p>
 * The applications here are registered as services directly, without the model loader: the
 * whiteboard for modelled applications takes them from wherever they come.
 * </p>
 */
class ApplicationPropertiesIT {

	private static final String NAME = "properties";

	private BundleContext context;
	private ServiceRegistration<RestApplication> registration;

	@BeforeEach
	void context(@InjectBundleContext BundleContext bundleContext,
			@InjectService(timeout = 20000) RestPackage restPackage) {
		context = bundleContext;
	}

	@AfterEach
	void unregister() {
		if (registration != null) {
			registration.unregister();
			registration = null;
		}
	}

	@Test
	void servicePropertiesOfTheModelServiceReachTheApplication() throws Exception {
		register(application(), properties("tenant", "acme", "limit", 5));

		ServiceReference<Application> application = awaitApplication("(tenant=acme)");

		assertThat(application.getProperty("limit")).isEqualTo(5);
	}

	@Test
	void modelledPropertiesReachTheApplication() throws Exception {
		RestApplication model = application();
		model.getProperties().put("stage", "test");
		register(model, properties());

		assertThat(awaitApplication("(stage=test)")).isNotNull();
	}

	/** Who deploys a model decides last: the same model can be deployed twice with different values. */
	@Test
	void servicePropertyReplacesTheModelledPropertyOfTheSameName() throws Exception {
		RestApplication model = application();
		model.getProperties().put("stage", "model");
		register(model, properties("stage", "service"));

		assertThat(awaitApplication("(stage=service)")).isNotNull();
	}

	/** Name and base path are what the model says, and nothing the service says replaces them. */
	@Test
	void whatTheWhiteboardReliesOnComesFromTheModel() throws Exception {
		register(application(), properties(JakartarsWhiteboardConstants.JAKARTA_RS_NAME, "other",
				JakartarsWhiteboardConstants.JAKARTA_RS_APPLICATION_BASE, "/other", "emf", "false", "tenant", "acme"));

		ServiceReference<Application> application = awaitApplication("(tenant=acme)");

		assertThat(application.getProperty(JakartarsWhiteboardConstants.JAKARTA_RS_NAME)).isEqualTo(NAME);
		assertThat(application.getProperty(JakartarsWhiteboardConstants.JAKARTA_RS_APPLICATION_BASE)).isEqualTo("/" + NAME);
		assertThat(application.getProperty("emf")).isEqualTo("true");
	}

	/** The identity of the model service is its own: the application is another service. */
	@Test
	void identityOfTheModelServiceIsNotCopied() throws Exception {
		register(application(), properties("tenant", "acme", Constants.SERVICE_PID, "some.pid", ".private", "x"));

		ServiceReference<Application> application = awaitApplication("(tenant=acme)");

		assertThat(application.getProperty(Constants.SERVICE_ID))
				.isNotEqualTo(registration.getReference().getProperty(Constants.SERVICE_ID));
		assertThat((String[]) application.getProperty(Constants.OBJECTCLASS)).containsExactly(Application.class.getName());
		assertThat(application.getProperty(Constants.SERVICE_PID)).isNull();
		assertThat(application.getProperty(".private")).isNull();
	}

	/** The extensions the model requires and the ones the service requires are all required. */
	@Test
	void extensionSelectOfModelAndServiceAreBothRequired() throws Exception {
		RestApplication model = application();
		model.getExtensionSelect().add("(osgi.jakartars.name=fromModel)");
		register(model, properties("tenant", "acme", JakartarsWhiteboardConstants.JAKARTA_RS_EXTENSION_SELECT,
				new String[] { "(osgi.jakartars.name=fromService)" }));

		ServiceReference<Application> application = awaitApplication("(tenant=acme)");

		assertThat((String[]) application.getProperty(JakartarsWhiteboardConstants.JAKARTA_RS_EXTENSION_SELECT))
				.containsExactlyInAnyOrder("(osgi.jakartars.name=fromModel)", "(osgi.jakartars.name=fromService)");
	}

	@Test
	void changedServicePropertiesReachTheApplication() throws Exception {
		register(application(), properties("tenant", "acme"));
		awaitApplication("(tenant=acme)");

		registration.setProperties(properties("tenant", "umbrella"));

		assertThat(awaitApplication("(tenant=umbrella)")).isNotNull();
		assertThat(context.getServiceReferences(Application.class, modelled("(tenant=acme)"))).isEmpty();
	}

	private static RestApplication application() {
		RestApplication application = RestFactory.eINSTANCE.createRestApplication();
		application.setName(NAME);
		application.setBasePath("/" + NAME);
		return application;
	}

	private static Dictionary<String, Object> properties(Object... keysAndValues) {
		Dictionary<String, Object> properties = new Hashtable<>();
		for (int i = 0; i + 1 < keysAndValues.length; i += 2) {
			properties.put((String) keysAndValues[i], keysAndValues[i + 1]);
		}
		return properties;
	}

	private void register(RestApplication model, Dictionary<String, Object> properties) {
		registration = context.registerService(RestApplication.class, model, properties);
	}

	private static String modelled(String filter) {
		return "(&(fennec.rest.modelled=" + NAME + ")" + filter + ")";
	}

	private ServiceReference<Application> awaitApplication(String filter) throws Exception {
		long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
		while (System.currentTimeMillis() < deadline) {
			var found = context.getServiceReferences(Application.class, modelled(filter));
			if (!found.isEmpty()) {
				return found.iterator().next();
			}
			Thread.sleep(100);
		}
		return fail("No modelled application '" + NAME + "' with " + filter + ". There is: "
				+ context.getServiceReferences(Application.class, "(fennec.rest.modelled=*)").stream()
						.map(reference -> reference.getProperties().toString()).toList());
	}
}
