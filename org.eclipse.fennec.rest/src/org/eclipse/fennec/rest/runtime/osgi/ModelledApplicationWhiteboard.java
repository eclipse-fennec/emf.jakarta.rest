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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.runtime.ModelledApplication;
import org.osgi.framework.BundleContext;
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
	private final Map<RestApplication, ServiceRegistration<Application>> registrations = new ConcurrentHashMap<>();

	@Activate
	public ModelledApplicationWhiteboard(BundleContext context) {
		this.context = context;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void addApplication(RestApplication model) {
		// deferred: this method runs on whatever thread brings the model, where Jersey must not be touched
		Application application = ModelledApplication.deferred(model, e -> LOGGER.log(Level.SEVERE, e,
				() -> "The modelled REST application '" + model.getName() + "' cannot be deployed: " + e.getMessage()));
		Dictionary<String, Object> properties = new Hashtable<>();
		// modelled properties first, so they cannot replace what the whiteboard relies on
		model.getProperties().forEach(property -> properties.put(property.getKey(), property.getValue()));
		if (!model.getExtensionSelect().isEmpty()) {
			properties.put(JakartarsWhiteboardConstants.JAKARTA_RS_EXTENSION_SELECT,
					model.getExtensionSelect().toArray(String[]::new));
		}
		properties.put(JakartarsWhiteboardConstants.JAKARTA_RS_NAME, model.getName());
		properties.put(JakartarsWhiteboardConstants.JAKARTA_RS_APPLICATION_BASE,
				model.getBasePath() == null ? "/" : model.getBasePath());
		properties.put("emf", "true");
		properties.put(MODELLED_APPLICATION, model.getName());
		registrations.put(model, context.registerService(Application.class, application, properties));
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
