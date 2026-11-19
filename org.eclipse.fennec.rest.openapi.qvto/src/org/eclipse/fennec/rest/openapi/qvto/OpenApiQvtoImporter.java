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
package org.eclipse.fennec.rest.openapi.qvto;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.fennec.codec.jsonschema.model.JsonSchemaPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.model.oas.OasPackage;
import org.eclipse.fennec.openapi.ecore.OpenApiModel;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestPackage;
import org.eclipse.fennec.rest.openapi.ImportedApplication;
import org.eclipse.fennec.rest.openapi.OpenApiRestImporter;

/**
 * Derives the server side of an OpenAPI document with the QVT-O transformation
 * {@code OpenApiToRest.qvto} next to this class instead of Java.
 * <p>
 * The transformation does the whole mapping. What is left to Java is what a transformation
 * with a read-only input cannot do, or what is outside of its world:
 * </p>
 * <ul>
 * <li>loading the document (the {@code OpenApiImporter} of emf.util is only used for that; its
 * linking of the operations is what the transformation replaces),</li>
 * <li>giving the schemas package, which belongs to the input, its own namespace,</li>
 * <li>making the generated packages the roots of resources named by their namespace.</li>
 * </ul>
 */
public final class OpenApiQvtoImporter {

	private static final String UNIT = "OpenApiToRest";
	private static final String SOURCE = UNIT + ".qvto";

	private OpenApiQvtoImporter() {
	}

	/**
	 * Runs the transformation on an imported document.
	 *
	 * @param model the loaded document
	 * @param options how to name and bind the result
	 * @throws IllegalStateException if the transformation does not parse or fails
	 */
	public static ImportedApplication importApplication(OpenApiModel model, OpenApiRestImporter.Options options) {
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder().registerPackage(OasPackage.eINSTANCE)
				.registerPackage(JsonSchemaPackage.eINSTANCE).registerPackage(RestPackage.eINSTANCE)
				.registerPackage(EcorePackage.eINSTANCE).build());
		OperationalTransformation transformation;
		try {
			transformation = engine.parse(source(), UNIT);
		} catch (QvtoParseException e) {
			List<String> errors = new ArrayList<>();
			for (Resource.Diagnostic error : e.getErrors()) {
				errors.add(error.getLine() + ":" + error.getColumn() + " " + error.getMessage());
			}
			throw new IllegalStateException(UNIT + " does not parse: " + e.getMessage() + " " + errors, e);
		}

		BasicQvtoModelExtent api = new BasicQvtoModelExtent(model.document());
		api.setReadOnly(true);
		BasicQvtoModelExtent service = new BasicQvtoModelExtent();
		BasicQvtoModelExtent binding = new BasicQvtoModelExtent();
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(List.of(api, service, binding),
						Map.of("applicationName", options.name(), "applicationBasePath", options.basePath(),
								"serviceNsUri", options.nsUri(), "delegateUri", options.delegateUri())));
		List<String> diagnostics = new ArrayList<>(model.diagnostics());
		List<String> errors = new ArrayList<>();
		for (Diagnostic diagnostic : result.diagnostics()) {
			(diagnostic.getSeverity() >= Diagnostic.ERROR ? errors : diagnostics).add(diagnostic.getMessage());
		}
		if (!result.isSuccess()) {
			throw new IllegalStateException(UNIT + " failed: " + errors);
		}

		EPackage servicePackage = single(service, EPackage.class);
		RestApplication application = single(binding, RestApplication.class);
		EPackage schemasPackage = model.schemasPackage();
		if (schemasPackage != null) {
			schemasPackage.setNsURI(options.nsUri() + "/schemas");
			selfContained(schemasPackage);
		}
		selfContained(servicePackage);
		return new ImportedApplication(servicePackage, schemasPackage, application, diagnostics);
	}

	/**
	 * The one root of an output extent. The extent lists every object the transformation
	 * created, also those that ended up contained in another one, so the roots are picked here.
	 */
	private static <T> T single(BasicQvtoModelExtent extent, Class<T> type) {
		List<EObject> roots = extent.getContents().stream().filter(object -> object.eContainer() == null).toList();
		if (roots.size() != 1 || !type.isInstance(roots.get(0))) {
			throw new IllegalStateException(
					UNIT + " produced the roots " + roots + " where one " + type.getSimpleName() + " was expected");
		}
		return type.cast(roots.get(0));
	}

	private static void selfContained(EPackage ePackage) {
		new ResourceImpl(URI.createURI(ePackage.getNsURI())).getContents().add(ePackage);
	}

	private static String source() {
		try (InputStream in = OpenApiQvtoImporter.class.getResourceAsStream(SOURCE)) {
			if (in == null) {
				throw new IllegalStateException("Missing " + SOURCE);
			}
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
