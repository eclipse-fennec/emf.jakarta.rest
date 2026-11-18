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
package org.eclipse.fennec.rest.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.rest.model.rest.Access;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestFactory;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.runtime.Harness.Caller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Request context and access control of a modelled application. The resource {@code secured}
 * of the fixture requires the role {@code user}; its methods refine or lift that.
 * <p>
 * Authentication is not part of the interpreter: a caller is whatever the request's
 * {@code SecurityContext} says, which an authentication filter sets in a deployed application.
 * </p>
 */
class AccessControlTest {

	private static final Caller USER = Caller.of("ada", "user");
	private static final Caller ADMIN = Caller.of("grace", "admin");
	private static final Caller NOBODY = Caller.of("eve");

	private final EOperation.Internal.InvocationDelegate.Factory.Registry registry = EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE;
	private GreetingModel model;
	private Harness harness;

	@BeforeEach
	void setUp() {
		registry.put(GreetingModel.DELEGATE_URI, GreetingModel.delegateFactory());
		model = new GreetingModel();
		harness = new Harness(model.application);
	}

	@AfterEach
	void tearDown() {
		registry.remove(GreetingModel.DELEGATE_URI);
	}

	// --- request context

	@Test
	void principalNameIsPassedToAStringParameter() throws Exception {
		Harness.Result result = harness.call(USER, "GET", "secured/whoami", null);

		assertThat(result.status()).isEqualTo(200);
		assertThat(result.body()).isEqualTo("I am ada");
	}

	@Test
	void principalObjectIsPassedToAnyOtherParameterType() throws Exception {
		assertThat(harness.call(USER, "GET", "secured/principal", null).body()).isEqualTo("principal ada");
	}

	@Test
	void mandatoryPrincipalWithoutCallerIsUnauthorized() throws Exception {
		assertThat(harness.call("GET", "secured/open/whoami", null).status()).isEqualTo(401);
	}

	@Test
	void contextObjectIsSelectedByTheInstanceClassNameOfTheParameterType() throws Exception {
		assertThat(harness.call("GET", "secured/open/where", null).body()).isEqualTo("at secured/open/where");
	}

	@Test
	void unknownContextTypeIsRejectedWhenTheApplicationIsBuilt() {
		RestMethod method = model.method(model.secured, HttpMethod.GET, "unknown", model.unknownContext);
		ParameterBinding binding = RestFactory.eINSTANCE.createParameterBinding();
		binding.setParameter(model.unknownContext.getEParameters().get(0));
		binding.setSource(ParameterSource.CONTEXT);
		method.getParameters().add(binding);

		assertThatThrownBy(() -> new Harness(model.application)).hasStackTraceContaining("java.lang.Thread")
				.hasStackTraceContaining("thread");
	}

	@Test
	void mandatoryParameterThatIsMissingIsABadRequest() throws Exception {
		assertThat(harness.call("GET", "secured/open/required", null).status()).isEqualTo(400);
		assertThat(harness.call("GET", "secured/open/required?name=x", null).body()).isEqualTo("got x");
	}

	// --- access control

	@Test
	void rolesOfTheResourceApplyToItsMethods() throws Exception {
		assertThat(harness.call("GET", "secured/whoami", null).status()).isEqualTo(401);
		assertThat(harness.call(NOBODY, "GET", "secured/whoami", null).status()).isEqualTo(403);
		assertThat(harness.call(USER, "GET", "secured/whoami", null).status()).isEqualTo(200);
	}

	@Test
	void rolesOfTheMethodReplaceThoseOfTheResource() throws Exception {
		assertThat(harness.call(USER, "GET", "secured/admin", null).status()).isEqualTo(403);
		assertThat(harness.call(ADMIN, "GET", "secured/admin", null).status()).isEqualTo(200);
	}

	@Test
	void authenticatedMethodAcceptsAnyCallerButNotAnonymous() throws Exception {
		assertThat(harness.call("GET", "secured/any", null).status()).isEqualTo(401);
		assertThat(harness.call(NOBODY, "GET", "secured/any", null).status()).isEqualTo(200);
	}

	@Test
	void publicMethodIsOpenInsideASecuredResource() throws Exception {
		assertThat(harness.call("GET", "secured/open", null).status()).isEqualTo(200);
	}

	@Test
	void deniedMethodIsClosedForEverybody() throws Exception {
		assertThat(harness.call(ADMIN, "GET", "secured/closed", null).status()).isEqualTo(403);
		assertThat(harness.call("GET", "secured/closed", null).status()).isEqualTo(403);
	}

	@Test
	void applicationWithoutAnyAccessRuleIsPublic() throws Exception {
		assertThat(harness.call("GET", "greetings/Ada", null).status()).isEqualTo(200);
	}

	@Test
	void applicationCanBeClosedByDefault() throws Exception {
		model.application.setAccess(Access.DENY);
		harness = new Harness(model.application);

		// says nothing itself, so the application decides
		assertThat(harness.call(ADMIN, "GET", "greetings/Ada", null).status()).isEqualTo(403);
		// more specific rules still win
		assertThat(harness.call("GET", "secured/open", null).status()).isEqualTo(200);
		assertThat(harness.call(USER, "GET", "secured/whoami", null).status()).isEqualTo(200);
	}

	@Test
	void accessIsCheckedBeforeTheRequestIsLookedAt() throws Exception {
		// the parameter is missing, but an unauthorized caller must not learn that
		model.secured.getMethods().stream().filter(method -> "open/required".equals(method.getPath())).findFirst()
				.orElseThrow().setAccess(Access.INHERIT);
		harness = new Harness(model.application);

		assertThat(harness.call(NOBODY, "GET", "secured/open/required", null).status()).isEqualTo(403);
	}

	@Test
	void rolesCombinedWithPublicAccessAreRejectedWhenTheApplicationIsBuilt() {
		model.secured.setAccess(Access.PUBLIC);

		assertThatThrownBy(() -> new Harness(model.application)).hasStackTraceContaining("secured")
				.hasStackTraceContaining("PUBLIC");
	}
}
