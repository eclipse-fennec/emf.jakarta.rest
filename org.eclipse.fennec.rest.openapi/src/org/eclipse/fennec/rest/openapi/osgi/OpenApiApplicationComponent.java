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
package org.eclipse.fennec.rest.openapi.osgi;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.configurator.EPackageConfigurator;
import org.eclipse.fennec.emf.osgi.constants.EMFNamespaces;
import org.eclipse.fennec.emf.osgi.fingerprint.util.ModelPropertiesHelper;
import org.eclipse.fennec.openapi.ecore.OpenApiImporter;
import org.eclipse.fennec.openapi.ecore.OpenApiModel;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.openapi.ImportedApplication;
import org.eclipse.fennec.rest.openapi.OpenApiRestImporter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Brings up a complete REST application from an OpenAPI document. One factory configuration
 * names the document and the delegate URI; the component then
 * <ol>
 * <li>imports the document and derives service package and binding model
 * ({@link OpenApiRestImporter}),</li>
 * <li>registers the generated EPackages as dynamic models, so the codec can read and write
 * their objects,</li>
 * <li>registers the {@link RestApplication}, which the modelled-application whiteboard deploys
 * at the Jakarta RS whiteboard.</li>
 * </ol>
 * <p>
 * The behaviour is not part of this: it comes from an EMF invocation delegate factory that is
 * registered as an OSGi service for the configured delegate URI. Until it is there, the
 * application answers 503.
 * </p>
 */
@Component(name = OpenApiApplicationComponent.PID, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = OpenApiApplicationComponent.Config.class, factory = true)
public class OpenApiApplicationComponent {

	/** The factory PID. */
	public static final String PID = "org.eclipse.fennec.rest.OpenApiApplication";

	private static final Logger LOGGER = Logger.getLogger(OpenApiApplicationComponent.class.getName());

	@ObjectClassDefinition(name = "Fennec OpenAPI REST Application", description = "Serves the operations of one OpenAPI document as a Jakarta RS application whose logic comes from an EMF invocation delegate")
	public @interface Config {

		@AttributeDefinition(description = "Name of the application (osgi.jakartars.name). Must be unique; it also names the generated service class and, by default, the namespace of the generated models.")
		String name();

		@AttributeDefinition(description = "URL the OpenAPI document is read from")
		String documentUrl();

		@AttributeDefinition(description = "Format of the document: json or yaml")
		String format() default "json";

		@AttributeDefinition(description = "URI of the EMF invocation delegate that implements the operations")
		String delegateUri();

		@AttributeDefinition(description = "Base path of the application. Defaults to the name.", required = false)
		String basePath() default "";

		@AttributeDefinition(description = "Namespace URI of the generated service package; the schemas get <nsUri>/schemas. Defaults to https://eclipse.org/fennec/rest/openapi/<name>.", required = false)
		String nsUri() default "";

		@AttributeDefinition(description = "Additional service properties of the application as key=value, e.g. the flag an authentication filter selects on", required = false)
		String[] applicationProperties() default {};

		@AttributeDefinition(description = "LDAP filters of whiteboard extensions the application requires", required = false)
		String[] extensionSelect() default {};
	}

	private final List<ServiceRegistration<?>> registrations = new ArrayList<>();
	private final List<EPackage> packages = new ArrayList<>();

	@Activate
	public OpenApiApplicationComponent(BundleContext context, Config config) {
		byte[] document = fetch(config.documentUrl());
		OpenApiModel model = "yaml".equalsIgnoreCase(config.format()) ? OpenApiImporter.fromYaml(document)
				: OpenApiImporter.fromJson(document);
		ImportedApplication imported = OpenApiRestImporter.importApplication(model,
				new OpenApiRestImporter.Options(config.name(),
						config.basePath().isBlank() ? config.name() : config.basePath(),
						config.nsUri().isBlank() ? "https://eclipse.org/fennec/rest/openapi/" + config.name()
								: config.nsUri(),
						config.delegateUri()));
		imported.diagnostics().forEach(
				diagnostic -> LOGGER.warning(() -> "OpenAPI application '" + config.name() + "': " + diagnostic));

		RestApplication application = imported.application();
		for (String property : config.applicationProperties()) {
			int separator = property.indexOf('=');
			if (separator > 0) {
				application.getProperties().put(property.substring(0, separator).trim(),
						property.substring(separator + 1).trim());
			}
		}
		application.getExtensionSelect().addAll(List.of(config.extensionSelect()));

		try {
			if (imported.schemasPackage() != null) {
				register(context, imported.schemasPackage());
			}
			register(context, imported.servicePackage());
			// last: the application must not be deployed before its models are known
			Dictionary<String, Object> properties = new Hashtable<>();
			properties.put("fennec.rest.model.name", config.name());
			properties.put("fennec.rest.openapi.document", config.documentUrl());
			registrations.add(context.registerService(RestApplication.class, application, properties));
		} catch (RuntimeException e) {
			deactivate();
			throw e;
		}
	}

	@Deactivate
	void deactivate() {
		// reverse order: the application goes before its models
		for (int i = registrations.size() - 1; i >= 0; i--) {
			try {
				registrations.get(i).unregister();
			} catch (IllegalStateException e) {
				// already unregistered, e.g. because the bundle is stopping
			}
		}
		registrations.clear();
		packages.forEach(ePackage -> EPackage.Registry.INSTANCE.remove(ePackage.getNsURI()));
		packages.clear();
	}

	/** Registers a generated package the way the dynamic package loader of Fennec EMF OSGi does. */
	private void register(BundleContext context, EPackage ePackage) {
		Dictionary<String, Object> properties = new Hashtable<>();
		ModelPropertiesHelper.modelProperties(ePackage).forEach(properties::put);
		properties.put(EMFNamespaces.EMF_MODEL_REGISTRATION, EMFNamespaces.MODEL_REGISTRATION_DYNAMIC);
		EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
		packages.add(ePackage);
		registrations.add(context.registerService(EPackageConfigurator.class, new EPackageConfigurator() {

			@Override
			public void configureEPackage(EPackage.Registry registry) {
				registry.put(ePackage.getNsURI(), ePackage);
			}

			@Override
			public void unconfigureEPackage(EPackage.Registry registry) {
				registry.remove(ePackage.getNsURI());
			}
		}, properties));
		registrations.add(context.registerService(EPackage.class, ePackage, properties));
	}

	private static byte[] fetch(String documentUrl) {
		try (InputStream in = URI.create(documentUrl).toURL().openStream()) {
			return in.readAllBytes();
		} catch (IOException e) {
			throw new UncheckedIOException("Could not read the OpenAPI document " + documentUrl, e);
		}
	}
}
