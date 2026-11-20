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
package org.eclipse.fennec.rest.runtime.osgi;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.runtime.ModelledApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jakartars.whiteboard.JakartarsWhiteboardConstants;

import jakarta.ws.rs.core.Application;

/**
 * Whiteboard for modelled applications: every {@link RestApplication} that is registered as an
 * OSGi service is interpreted and published as a Jakarta RS {@link Application} service, which
 * the Jakarta RS whiteboard then deploys. When the model service goes away, so does the
 * application.
 * <p>
 * The application is flagged {@code emf=true}, the property the Fennec codec extensions select
 * on, so EObjects can be read and written as request and response bodies.
 * </p>
 * <p>
 * The {@code properties} of the model become service properties of the application and its
 * {@code extensionSelect} the extensions it requires. That is how whiteboard extensions such
 * as an authentication filter are attached to a modelled application.
 * </p>
 * <p>
 * The service properties of the {@link RestApplication} service are passed on to the application
 * as well, and follow it when they change. They are what the one who deploys a model says, so
 * they replace a modelled property of the same name: the same model can be deployed more than
 * once with different values. Not passed on are the identity of the model service (its id,
 * PID, object class, scope, bundle and component), private properties (the ones that start
 * with a dot) and what the whiteboard relies on: name and base path are the ones of the model,
 * and the extensions the service requires are added to the ones the model requires.
 * </p>
 * <p>
 * A model is interpreted when Jersey initialises its application. A model that cannot be
 * interpreted is logged and makes that initialisation fail; it does not affect the other
 * applications.
 * </p>
 */
@Component
public class ModelledApplicationWhiteboard {

	/** Service property that marks an application as modelled and carries the model name. */
	public static final String MODELLED_APPLICATION = "fennec.rest.modelled";

	private static final Logger LOGGER = Logger.getLogger(ModelledApplicationWhiteboard.class.getName());

	private final BundleContext context;
	/** Service properties that identify the model service, and say nothing about the application. */
	private static final Set<String> IDENTITY = Set.of(Constants.SERVICE_ID, Constants.OBJECTCLASS,
			Constants.SERVICE_SCOPE, Constants.SERVICE_BUNDLEID, Constants.SERVICE_PID, "service.factoryPid",
			"component.name", "component.id");

	private final Map<RestApplication, ServiceRegistration<Application>> registrations = new ConcurrentHashMap<>();

	@Activate
	public ModelledApplicationWhiteboard(BundleContext context) {
		this.context = context;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, updated = "updatedApplication")
	void addApplication(RestApplication model, Map<String, Object> serviceProperties) {
		// deferred: this method runs on whatever thread brings the model, where Jersey must not be touched
		Application application = ModelledApplication.deferred(model, e -> LOGGER.log(Level.SEVERE, e,
				() -> "The modelled REST application '" + model.getName() + "' cannot be deployed: " + e.getMessage()));
		registrations.put(model,
				context.registerService(Application.class, application, properties(model, serviceProperties)));
	}

	void updatedApplication(RestApplication model, Map<String, Object> serviceProperties) {
		ServiceRegistration<Application> registration = registrations.get(model);
		if (registration != null) {
			try {
				registration.setProperties(properties(model, serviceProperties));
			} catch (IllegalStateException e) {
				// already unregistered, e.g. because the bundle is stopping
			}
		}
	}

	/** The service properties of the application: of the model, of its service, and what the whiteboard relies on. */
	private static Dictionary<String, Object> properties(RestApplication model, Map<String, Object> serviceProperties) {
		Dictionary<String, Object> properties = new Hashtable<>();
		// the weakest first: what comes later replaces it
		model.getProperties().forEach(property -> properties.put(property.getKey(), property.getValue()));
		serviceProperties.forEach((key, value) -> {
			if (!IDENTITY.contains(key) && !key.startsWith(".")) {
				properties.put(key, value);
			}
		});
		Set<String> extensions = new LinkedHashSet<>(model.getExtensionSelect());
		extensions.addAll(strings(serviceProperties.get(JakartarsWhiteboardConstants.JAKARTA_RS_EXTENSION_SELECT)));
		if (extensions.isEmpty()) {
			properties.remove(JakartarsWhiteboardConstants.JAKARTA_RS_EXTENSION_SELECT);
		} else {
			properties.put(JakartarsWhiteboardConstants.JAKARTA_RS_EXTENSION_SELECT, extensions.toArray(String[]::new));
		}
		properties.put(JakartarsWhiteboardConstants.JAKARTA_RS_NAME, model.getName());
		properties.put(JakartarsWhiteboardConstants.JAKARTA_RS_APPLICATION_BASE,
				model.getBasePath() == null ? "/" : model.getBasePath());
		properties.put("emf", "true");
		properties.put(MODELLED_APPLICATION, model.getName());
		return properties;
	}

	/** A service property of the type {@code String+}: one string, an array or a collection of them. */
	private static List<String> strings(Object value) {
		if (value instanceof String string) {
			return List.of(string);
		}
		if (value instanceof String[] array) {
			return List.of(array);
		}
		if (value instanceof Collection<?> collection) {
			return collection.stream().map(String::valueOf).toList();
		}
		return List.of();
	}

	void removeApplication(RestApplication model) {
		unregister(registrations.remove(model));
	}

	@Deactivate
	void deactivate() {
		registrations.keySet().forEach(this::removeApplication);
	}

	private static void unregister(ServiceRegistration<Application> registration) {
		if (registration != null) {
			try {
				registration.unregister();
			} catch (IllegalStateException e) {
				// already unregistered, e.g. because the bundle is stopping
			}
		}
	}
}
