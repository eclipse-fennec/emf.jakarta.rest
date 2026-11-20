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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestPackage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Loads a REST binding model from a configured URI and registers every {@link RestApplication}
 * in it as an OSGi service, where the {@link ModelledApplicationWhiteboard} picks it up.
 * <p>
 * The binding model references the EClasses and EOperations of a domain model, so the domain
 * EPackage has to be known to the {@link ResourceSet} before the binding model is loaded. That
 * ordering is expressed declaratively with the {@code resourceSet.target} configuration
 * property, e.g. {@code (&(emf.name=rest)(emf.name=greeting))}: the loader is then only
 * activated once a ResourceSet with both packages exists, and deactivated when one of them
 * goes away.
 * </p>
 * <p>
 * The ResourceSet is kept for the lifetime of the component, because the loaded model stays
 * attached to it.
 * </p>
 */
@Component(name = RestModelLoader.PID, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = RestModelLoader.Config.class, factory = true)
public class RestModelLoader {

	/** The factory PID of the loader. */
	public static final String PID = "org.eclipse.fennec.rest.RestModel";

	@ObjectClassDefinition(name = "Fennec Modelled REST Application", description = "Loads a REST binding model and deploys the applications it contains at the Jakarta RS whiteboard")
	public @interface Config {

		@AttributeDefinition(description = "The URI of the REST binding model, e.g. an XMI file")
		String model_uri();

		@AttributeDefinition(description = "LDAP filter for the ResourceSet that loads the model. Use it to wait for the EPackages the model needs, e.g. (&(emf.name=rest)(emf.name=mydomain))", required = false)
		String resourceSet_target() default "(emf.name=rest)";
	}

	/** What the configuration says to the loader itself. */
	private static final Set<String> OWN = Set.of("model.uri", "resourceSet.target");

	private final ComponentServiceObjects<ResourceSet> resourceSets;
	private final ResourceSet resourceSet;
	private final List<ServiceRegistration<RestApplication>> registrations = new ArrayList<>();

	/**
	 * @param configuration the properties of the configuration. Those that are not for the loader
	 *                      become service properties of the applications of the model, and through
	 *                      the whiteboard for modelled applications service properties of the
	 *                      Jakarta RS applications.
	 * @param restPackage the package of the binding model as a service. It is not used, it is
	 *                    waited for: a ResourceSet announces a package as soon as the package
	 *                    puts itself into the EMF registry, which a generated package does while
	 *                    its {@code eINSTANCE} is still being initialised. A loader activated at
	 *                    that moment, on the same thread, finds the package half built. The
	 *                    service is only there once the package is complete.
	 */
	@Activate
	public RestModelLoader(BundleContext context, Config config, Map<String, Object> configuration,
			@Reference(name = "resourceSet") ComponentServiceObjects<ResourceSet> resourceSets,
			@Reference RestPackage restPackage) {
		this.resourceSets = resourceSets;
		this.resourceSet = resourceSets.getService();
		try {
			for (RestApplication application : load(config.model_uri())) {
				Dictionary<String, Object> properties = new Hashtable<>();
				// whatever else the configuration says is meant for the application
				configuration.forEach((key, value) -> {
					if (!OWN.contains(key)) {
						properties.put(key, value);
					}
				});
				properties.put("fennec.rest.model.uri", config.model_uri());
				properties.put("fennec.rest.model.name", application.getName());
				registrations.add(context.registerService(RestApplication.class, application, properties));
			}
		} catch (RuntimeException e) {
			deactivate();
			throw e;
		}
	}

	@Deactivate
	void deactivate() {
		for (ServiceRegistration<RestApplication> registration : registrations) {
			try {
				registration.unregister();
			} catch (IllegalStateException e) {
				// already unregistered, e.g. because the bundle is stopping
			}
		}
		registrations.clear();
		resourceSets.ungetService(resourceSet);
	}

	private List<RestApplication> load(String modelUri) {
		Resource resource = resourceSet.getResource(URI.createURI(modelUri), true);
		EcoreUtil.resolveAll(resource);
		Map<EObject, Collection<EStructuralFeature.Setting>> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer
				.find(resource);
		if (!unresolved.isEmpty()) {
			throw new IllegalStateException("The REST model '" + modelUri
					+ "' references elements that cannot be resolved; is the EPackage registered? "
					+ unresolved.keySet().stream().map(EcoreUtil::getURI).toList());
		}
		List<RestApplication> applications = new ArrayList<>();
		for (EObject content : resource.getContents()) {
			if (content instanceof RestApplication application) {
				applications.add(application);
			}
		}
		if (applications.isEmpty()) {
			throw new IllegalStateException("The REST model '" + modelUri + "' contains no RestApplication");
		}
		return applications;
	}
}
