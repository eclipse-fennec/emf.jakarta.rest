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
package org.eclipse.fennec.rest.model.rest.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.fennec.rest.model.rest.Access;
import org.eclipse.fennec.rest.model.rest.HttpMethod;
import org.eclipse.fennec.rest.model.rest.ParameterBinding;
import org.eclipse.fennec.rest.model.rest.ParameterSource;
import org.eclipse.fennec.rest.model.rest.RestApplication;
import org.eclipse.fennec.rest.model.rest.RestFactory;
import org.eclipse.fennec.rest.model.rest.RestMethod;
import org.eclipse.fennec.rest.model.rest.RestPackage;
import org.eclipse.fennec.rest.model.rest.RestResource;
import org.eclipse.fennec.rest.model.rest.Secured;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RestPackageImpl extends EPackageImpl implements RestPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass securedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass restApplicationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass restResourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass restMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterBindingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum httpMethodEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterSourceEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum accessEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RestPackageImpl() {
		super(eNS_URI, RestFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link RestPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RestPackage init() {
		if (isInited) return (RestPackage)EPackage.Registry.INSTANCE.getEPackage(RestPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredRestPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		RestPackageImpl theRestPackage = registeredRestPackage instanceof RestPackageImpl ? (RestPackageImpl)registeredRestPackage : new RestPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theRestPackage.createPackageContents();

		// Initialize created meta-data
		theRestPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRestPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RestPackage.eNS_URI, theRestPackage);
		return theRestPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSecured() {
		return securedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSecured_Access() {
		return (EAttribute)securedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSecured_RolesAllowed() {
		return (EAttribute)securedEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRestApplication() {
		return restApplicationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestApplication_Name() {
		return (EAttribute)restApplicationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestApplication_BasePath() {
		return (EAttribute)restApplicationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestApplication_ExtensionSelect() {
		return (EAttribute)restApplicationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestApplication_Properties() {
		return (EReference)restApplicationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestApplication_Resources() {
		return (EReference)restApplicationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRestResource() {
		return restResourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestResource_Path() {
		return (EAttribute)restResourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestResource_Target() {
		return (EReference)restResourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestResource_Consumes() {
		return (EAttribute)restResourceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestResource_Produces() {
		return (EAttribute)restResourceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestResource_Methods() {
		return (EReference)restResourceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRestMethod() {
		return restMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestMethod_HttpMethod() {
		return (EAttribute)restMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestMethod_Path() {
		return (EAttribute)restMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestMethod_Operation() {
		return (EReference)restMethodEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestMethod_Consumes() {
		return (EAttribute)restMethodEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestMethod_Produces() {
		return (EAttribute)restMethodEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestMethod_SuccessStatus() {
		return (EAttribute)restMethodEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestMethod_Parameters() {
		return (EReference)restMethodEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getParameterBinding() {
		return parameterBindingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getParameterBinding_Parameter() {
		return (EReference)parameterBindingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getParameterBinding_Source() {
		return (EAttribute)parameterBindingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getParameterBinding_Name() {
		return (EAttribute)parameterBindingEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getParameterBinding_Encoded() {
		return (EAttribute)parameterBindingEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getParameterBinding_DefaultValue() {
		return (EAttribute)parameterBindingEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getHttpMethod() {
		return httpMethodEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getParameterSource() {
		return parameterSourceEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getAccess() {
		return accessEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RestFactory getRestFactory() {
		return (RestFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		securedEClass = createEClass(SECURED);
		createEAttribute(securedEClass, SECURED__ACCESS);
		createEAttribute(securedEClass, SECURED__ROLES_ALLOWED);

		restApplicationEClass = createEClass(REST_APPLICATION);
		createEAttribute(restApplicationEClass, REST_APPLICATION__NAME);
		createEAttribute(restApplicationEClass, REST_APPLICATION__BASE_PATH);
		createEAttribute(restApplicationEClass, REST_APPLICATION__EXTENSION_SELECT);
		createEReference(restApplicationEClass, REST_APPLICATION__PROPERTIES);
		createEReference(restApplicationEClass, REST_APPLICATION__RESOURCES);

		restResourceEClass = createEClass(REST_RESOURCE);
		createEAttribute(restResourceEClass, REST_RESOURCE__PATH);
		createEReference(restResourceEClass, REST_RESOURCE__TARGET);
		createEAttribute(restResourceEClass, REST_RESOURCE__CONSUMES);
		createEAttribute(restResourceEClass, REST_RESOURCE__PRODUCES);
		createEReference(restResourceEClass, REST_RESOURCE__METHODS);

		restMethodEClass = createEClass(REST_METHOD);
		createEAttribute(restMethodEClass, REST_METHOD__HTTP_METHOD);
		createEAttribute(restMethodEClass, REST_METHOD__PATH);
		createEReference(restMethodEClass, REST_METHOD__OPERATION);
		createEAttribute(restMethodEClass, REST_METHOD__CONSUMES);
		createEAttribute(restMethodEClass, REST_METHOD__PRODUCES);
		createEAttribute(restMethodEClass, REST_METHOD__SUCCESS_STATUS);
		createEReference(restMethodEClass, REST_METHOD__PARAMETERS);

		parameterBindingEClass = createEClass(PARAMETER_BINDING);
		createEReference(parameterBindingEClass, PARAMETER_BINDING__PARAMETER);
		createEAttribute(parameterBindingEClass, PARAMETER_BINDING__SOURCE);
		createEAttribute(parameterBindingEClass, PARAMETER_BINDING__NAME);
		createEAttribute(parameterBindingEClass, PARAMETER_BINDING__ENCODED);
		createEAttribute(parameterBindingEClass, PARAMETER_BINDING__DEFAULT_VALUE);

		// Create enums
		httpMethodEEnum = createEEnum(HTTP_METHOD);
		parameterSourceEEnum = createEEnum(PARAMETER_SOURCE);
		accessEEnum = createEEnum(ACCESS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		restApplicationEClass.getESuperTypes().add(this.getSecured());
		restResourceEClass.getESuperTypes().add(this.getSecured());
		restMethodEClass.getESuperTypes().add(this.getSecured());

		// Initialize classes, features, and operations; add parameters
		initEClass(securedEClass, Secured.class, "Secured", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSecured_Access(), this.getAccess(), "access", "INHERIT", 1, 1, Secured.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSecured_RolesAllowed(), ecorePackage.getEString(), "rolesAllowed", null, 0, -1, Secured.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(restApplicationEClass, RestApplication.class, "RestApplication", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRestApplication_Name(), ecorePackage.getEString(), "name", null, 1, 1, RestApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestApplication_BasePath(), ecorePackage.getEString(), "basePath", "/", 0, 1, RestApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestApplication_ExtensionSelect(), ecorePackage.getEString(), "extensionSelect", null, 0, -1, RestApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRestApplication_Properties(), ecorePackage.getEStringToStringMapEntry(), null, "properties", null, 0, -1, RestApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRestApplication_Resources(), this.getRestResource(), null, "resources", null, 0, -1, RestApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(restResourceEClass, RestResource.class, "RestResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRestResource_Path(), ecorePackage.getEString(), "path", null, 1, 1, RestResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRestResource_Target(), ecorePackage.getEClass(), null, "target", null, 1, 1, RestResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestResource_Consumes(), ecorePackage.getEString(), "consumes", null, 0, -1, RestResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestResource_Produces(), ecorePackage.getEString(), "produces", null, 0, -1, RestResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRestResource_Methods(), this.getRestMethod(), null, "methods", null, 0, -1, RestResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(restMethodEClass, RestMethod.class, "RestMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRestMethod_HttpMethod(), this.getHttpMethod(), "httpMethod", "GET", 1, 1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestMethod_Path(), ecorePackage.getEString(), "path", null, 0, 1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRestMethod_Operation(), ecorePackage.getEOperation(), null, "operation", null, 1, 1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestMethod_Consumes(), ecorePackage.getEString(), "consumes", null, 0, -1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestMethod_Produces(), ecorePackage.getEString(), "produces", null, 0, -1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestMethod_SuccessStatus(), ecorePackage.getEInt(), "successStatus", "0", 0, 1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRestMethod_Parameters(), this.getParameterBinding(), null, "parameters", null, 0, -1, RestMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterBindingEClass, ParameterBinding.class, "ParameterBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameterBinding_Parameter(), ecorePackage.getEParameter(), null, "parameter", null, 1, 1, ParameterBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterBinding_Source(), this.getParameterSource(), "source", "QUERY", 1, 1, ParameterBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterBinding_Name(), ecorePackage.getEString(), "name", null, 0, 1, ParameterBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterBinding_Encoded(), ecorePackage.getEBoolean(), "encoded", "false", 0, 1, ParameterBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterBinding_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, ParameterBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(httpMethodEEnum, HttpMethod.class, "HttpMethod");
		addEEnumLiteral(httpMethodEEnum, HttpMethod.GET);
		addEEnumLiteral(httpMethodEEnum, HttpMethod.POST);
		addEEnumLiteral(httpMethodEEnum, HttpMethod.PUT);
		addEEnumLiteral(httpMethodEEnum, HttpMethod.DELETE);
		addEEnumLiteral(httpMethodEEnum, HttpMethod.PATCH);
		addEEnumLiteral(httpMethodEEnum, HttpMethod.HEAD);
		addEEnumLiteral(httpMethodEEnum, HttpMethod.OPTIONS);

		initEEnum(parameterSourceEEnum, ParameterSource.class, "ParameterSource");
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.QUERY);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.PATH);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.HEADER);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.COOKIE);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.BODY);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.PRINCIPAL);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.MATRIX);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.FORM);
		addEEnumLiteral(parameterSourceEEnum, ParameterSource.CONTEXT);

		initEEnum(accessEEnum, Access.class, "Access");
		addEEnumLiteral(accessEEnum, Access.INHERIT);
		addEEnumLiteral(accessEEnum, Access.PUBLIC);
		addEEnumLiteral(accessEEnum, Access.AUTHENTICATED);
		addEEnumLiteral(accessEEnum, Access.DENY);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// Version
		createVersionAnnotations();
	}

	/**
	 * Initializes the annotations for <b>Version</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createVersionAnnotations() {
		String source = "Version";
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "value", "1.0"
		   });
	}

} //RestPackageImpl
