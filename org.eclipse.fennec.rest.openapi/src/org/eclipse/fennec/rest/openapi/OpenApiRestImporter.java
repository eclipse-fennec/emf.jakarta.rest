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
package org.eclipse.fennec.rest.openapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.model.oas.Operation;
import org.eclipse.fennec.model.oas.PathItem;
import org.eclipse.fennec.openapi.ecore.OpenApiAnnotations;
import org.eclipse.fennec.openapi.ecore.OpenApiModel;
import org.eclipse.fennec.openapi.ecore.OpenApiOperation;
import org.eclipse.fennec.rest.model.rest.Access;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestFactory;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestResource;

/**
 * Derives the server side of an OpenAPI document: a service EClass whose EOperations are the
 * path operations, and the REST binding model that exposes them.
 * <p>
 * The document itself is loaded and linked by the {@code OpenApiImporter} of emf.util, which
 * also serves the client side. So client and server share the EClasses of the schemas. What is
 * added here:
 * </p>
 * <ul>
 * <li>an operation reads like a Java method: the importer folds the request parameters into
 * one request EClass, here every one of them is a parameter of the operation again, bound to
 * the place the document puts it (path, query, header, cookie, body);</li>
 * <li>every operation carries the delegate URI, so one EMF invocation delegate factory
 * registered for that URI provides the behaviour of the whole application. The operation's
 * annotation names the {@code operationId}, the HTTP method and the path;</li>
 * <li>a security requirement becomes an access rule: an authenticated caller, and the scopes
 * as roles. Roles mean "one of", so requirements that need several scopes at once or offer
 * alternatives are not expressed exactly; that is reported as a diagnostic.</li>
 * </ul>
 * <p>
 * The import registers nothing. It does change the imported model in one respect: the schemas
 * package gets its own namespace and becomes the root of its own resource.
 * </p>
 */
public final class OpenApiRestImporter {

	private static final String JSON = "application/json";

	/**
	 * @param name the name of the application; the service EClass is named after it
	 * @param basePath the base path of the application
	 * @param nsUri the namespace of the service package; the schemas package gets
	 *        {@code nsUri + "/schemas"}, so several imported documents do not collide
	 * @param delegateUri the URI of the invocation delegate that implements the operations
	 */
	public record Options(String name, String basePath, String nsUri, String delegateUri) {
	}

	private final OpenApiModel model;
	private final Options options;
	private final List<String> diagnostics = new ArrayList<>();
	private final Map<String, RestResource> resources = new LinkedHashMap<>();
	private EClass service;

	private OpenApiRestImporter(OpenApiModel model, Options options) {
		this.model = model;
		this.options = options;
	}

	/**
	 * Derives service package and binding model from an imported document.
	 *
	 * @param model the document as imported by {@code OpenApiImporter}
	 * @param options how to name and bind the result
	 */
	public static ImportedApplication importApplication(OpenApiModel model, Options options) {
		return new OpenApiRestImporter(model, options).run();
	}

	private ImportedApplication run() {
		diagnostics.addAll(model.diagnostics());
		EPackage schemasPackage = model.schemasPackage();
		if (schemasPackage != null) {
			schemasPackage.setNsURI(options.nsUri() + "/schemas");
			selfContained(schemasPackage);
		}

		String identifier = identifier(options.name());
		EPackage servicePackage = EcoreFactory.eINSTANCE.createEPackage();
		servicePackage.setName(identifier);
		servicePackage.setNsPrefix(identifier);
		servicePackage.setNsURI(options.nsUri());
		EcoreUtil.setInvocationDelegates(servicePackage, List.of(options.delegateUri()));
		service = EcoreFactory.eINSTANCE.createEClass();
		service.setName(capitalize(identifier) + "Service");
		servicePackage.getEClassifiers().add(service);
		selfContained(servicePackage);

		RestApplication application = RestFactory.eINSTANCE.createRestApplication();
		application.setName(options.name());
		application.setBasePath(options.basePath());

		for (OpenApiOperation operation : model.operations()) {
			importOperation(operation);
		}
		application.getResources().addAll(resources.values());
		return new ImportedApplication(servicePackage, schemasPackage, application, diagnostics);
	}

	/**
	 * Makes the package the root of a resource named by its namespace. Only then the URI of an
	 * EClass is {@code <nsURI>#//<Name>}, which is how a type is named towards the codec and
	 * how a registered package is looked up.
	 */
	private static void selfContained(EPackage ePackage) {
		new ResourceImpl(URI.createURI(ePackage.getNsURI())).getContents().add(ePackage);
	}

	private void importOperation(OpenApiOperation source) {
		HttpMethod httpMethod = HttpMethod.get(source.httpMethod());
		if (httpMethod == null) {
			diagnostics.add("Operation '" + source.name() + "': HTTP method " + source.httpMethod()
					+ " is not supported, the operation is skipped");
			return;
		}
		EOperation operation = EcoreFactory.eINSTANCE.createEOperation();
		operation.setName(uniqueOperationName(source));
		operation.setEType(source.responseType());
		operation.setUpperBound(source.responseMany() ? -1 : 1);
		EAnnotation delegate = EcoreFactory.eINSTANCE.createEAnnotation();
		delegate.setSource(options.delegateUri());
		delegate.getDetails().put("operationId", source.name());
		delegate.getDetails().put("method", source.httpMethod());
		delegate.getDetails().put("path", source.pathTemplate());
		operation.getEAnnotations().add(delegate);
		service.getEOperations().add(operation);

		RestMethod method = RestFactory.eINSTANCE.createRestMethod();
		method.setHttpMethod(httpMethod);
		method.setOperation(operation);
		method.setSuccessStatus(successStatus(source));
		boolean body = importParameters(source, operation, method);
		if (body) {
			method.getConsumes().add(JSON);
		}
		if (source.responseType() != null) {
			method.getProduces().add(JSON);
		}
		importSecurity(source, method);

		resources.computeIfAbsent(source.pathTemplate(), path -> {
			RestResource resource = RestFactory.eINSTANCE.createRestResource();
			resource.setPath(path);
			resource.setTarget(service);
			return resource;
		}).getMethods().add(method);
	}

	/**
	 * Unfolds the request type of the importer into parameters of the operation.
	 *
	 * @return whether the operation has a body
	 */
	private boolean importParameters(OpenApiOperation source, EOperation operation, RestMethod method) {
		EClass request = source.requestType();
		if (request == null) {
			return false;
		}
		if (request.getEPackage() != model.requestsPackage() || model.requestsPackage() == null) {
			// no parameters, the request type is the body itself
			parameter(operation, method, "body", request, 0, 1, ParameterSource.BODY);
			return true;
		}
		boolean body = false;
		for (EStructuralFeature feature : request.getEStructuralFeatures()) {
			ParameterSource parameterSource = source(source, feature);
			if (parameterSource == null) {
				continue;
			}
			body |= parameterSource == ParameterSource.BODY;
			parameter(operation, method, feature.getName(), feature.getEType(), feature.getLowerBound(),
					feature.getUpperBound(), parameterSource);
		}
		return body;
	}

	private void parameter(EOperation operation, RestMethod method, String wireName,
			EClassifier type, int lowerBound, int upperBound, ParameterSource source) {
		EParameter parameter = EcoreFactory.eINSTANCE.createEParameter();
		parameter.setName(identifier(wireName));
		parameter.setEType(type);
		parameter.setLowerBound(lowerBound);
		parameter.setUpperBound(upperBound);
		operation.getEParameters().add(parameter);

		ParameterBinding binding = RestFactory.eINSTANCE.createParameterBinding();
		binding.setParameter(parameter);
		binding.setSource(source);
		if (source != ParameterSource.BODY && !wireName.equals(parameter.getName())) {
			// the name on the wire is no valid parameter name, e.g. X-Trace-Id
			binding.setName(wireName);
		}
		method.getParameters().add(binding);
	}

	private ParameterSource source(OpenApiOperation operation, EStructuralFeature feature) {
		String in = OpenApiAnnotations.in(feature);
		if (in != null) {
			switch (in) {
			case OpenApiAnnotations.IN_PATH:
				return ParameterSource.PATH;
			case OpenApiAnnotations.IN_QUERY:
				return ParameterSource.QUERY;
			case OpenApiAnnotations.IN_HEADER:
				return ParameterSource.HEADER;
			case OpenApiAnnotations.IN_COOKIE:
				return ParameterSource.COOKIE;
			case OpenApiAnnotations.IN_BODY:
				return ParameterSource.BODY;
			default:
				break;
			}
		}
		diagnostics.add("Operation '" + operation.name() + "': parameter '" + feature.getName() + "' in '" + in
				+ "' is not supported, it is skipped");
		return null;
	}

	/**
	 * An authenticated caller for any requirement, and the scopes as roles. The alternatives of
	 * a requirement are merged, which is exact only if there is one alternative with at most
	 * one scope.
	 */
	private void importSecurity(OpenApiOperation source, RestMethod method) {
		List<Map<String, List<String>>> alternatives = source.security();
		if (alternatives.isEmpty()) {
			return;
		}
		Set<String> roles = new LinkedHashSet<>();
		Set<Set<String>> distinct = new LinkedHashSet<>();
		for (Map<String, List<String>> alternative : alternatives) {
			Set<String> scopes = new LinkedHashSet<>();
			alternative.values().forEach(scopes::addAll);
			if (scopes.size() > 1) {
				diagnostics.add("Operation '" + source.name() + "': its security requires all of the scopes "
						+ scopes + ", as roles one of them is enough");
			}
			roles.addAll(scopes);
			distinct.add(scopes);
		}
		if (distinct.size() > 1) {
			diagnostics.add("Operation '" + source.name() + "': its security offers alternative requirements "
					+ distinct + ", they are merged into the roles " + roles);
		}
		if (roles.isEmpty()) {
			method.setAccess(Access.AUTHENTICATED);
		} else {
			method.getRolesAllowed().addAll(roles);
		}
	}

	/**
	 * The status of the success response the importer chose the result type from, unless it is
	 * what the runtime answers anyway (200 with a result, 204 without).
	 */
	private int successStatus(OpenApiOperation source) {
		Operation declared = declared(source);
		if (declared == null || declared.getResponses() == null) {
			return 0;
		}
		String chosen = null;
		for (String candidate : new String[] { "200", "201" }) {
			if (chosen == null && declared.getResponses().containsKey(candidate)) {
				chosen = candidate;
			}
		}
		if (chosen == null) {
			for (String key : declared.getResponses().keySet()) {
				if (chosen == null && key != null && key.startsWith("2")) {
					chosen = key;
				}
			}
		}
		if (chosen == null) {
			return 0;
		}
		try {
			int status = Integer.parseInt(chosen);
			return status == 200 || status == 204 ? 0 : status;
		} catch (NumberFormatException e) {
			// a range such as 2XX
			return 0;
		}
	}

	private Operation declared(OpenApiOperation source) {
		if (model.document() == null || model.document().getPaths() == null) {
			return null;
		}
		PathItem item = model.document().getPaths().get(source.pathTemplate());
		if (item == null) {
			return null;
		}
		switch (source.httpMethod()) {
		case "GET":
			return item.getGet();
		case "PUT":
			return item.getPut();
		case "POST":
			return item.getPost();
		case "DELETE":
			return item.getDelete();
		case "OPTIONS":
			return item.getOptions();
		case "HEAD":
			return item.getHead();
		case "PATCH":
			return item.getPatch();
		default:
			return null;
		}
	}

	private String uniqueOperationName(OpenApiOperation source) {
		String name = identifier(source.name());
		String unique = name;
		int counter = 2;
		while (hasOperation(unique)) {
			unique = name + counter++;
		}
		if (!unique.equals(name)) {
			diagnostics.add("Operation '" + source.name() + "': the name is used more than once, renamed to '"
					+ unique + "'");
		}
		return unique;
	}

	private boolean hasOperation(String name) {
		return service.getEOperations().stream().anyMatch(operation -> name.equals(operation.getName()));
	}

	/** A name that is valid for an Ecore element: everything else becomes an underscore. */
	private static String identifier(String name) {
		StringBuilder identifier = new StringBuilder();
		for (char c : name.toCharArray()) {
			identifier.append(Character.isJavaIdentifierPart(c) ? c : '_');
		}
		if (identifier.isEmpty() || !Character.isJavaIdentifierStart(identifier.charAt(0))) {
			identifier.insert(0, '_');
		}
		return identifier.toString();
	}

	private static String capitalize(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
}
