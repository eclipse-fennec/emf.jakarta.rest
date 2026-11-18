/*
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
package org.eclipse.fennec.rest.model.rest;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.fennec.emf.osgi.annotation.provide.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Binding model that describes a Jakarta RS application without any Java resource classes. It does not carry behaviour itself: every resource method points to an EOperation of a domain model, and the operation is executed through its EMF invocation delegate.
 * <!-- end-model-doc -->
 * @see org.eclipse.fennec.rest.model.rest.RestFactory
 * @model kind="package"
 *        annotation="Version value='1.0'"
 * @generated
 */
@ProviderType
@EPackage(uri = RestPackage.eNS_URI, fingerprint = "fp1:05f4ce5f7db843c088eb50f1fdf2540cf4c65fccda8a4f63be5f1b77a8a1cf81", genModel = "/model/rest.genmodel", genModelSourceLocations = {"model/rest.genmodel","org.eclipse.fennec.rest.model/model/rest.genmodel"}, ecore = "/model/rest.ecore", ecoreSourceLocations = "/model/rest.ecore")
public interface RestPackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "rest";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://eclipse.org/fennec/rest/1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "rest";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RestPackage eINSTANCE = org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.SecuredImpl <em>Secured</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.SecuredImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getSecured()
	 * @generated
	 */
	int SECURED = 0;

	/**
	 * The feature id for the '<em><b>Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECURED__ACCESS = 0;

	/**
	 * The feature id for the '<em><b>Roles Allowed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECURED__ROLES_ALLOWED = 1;

	/**
	 * The number of structural features of the '<em>Secured</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECURED_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Secured</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECURED_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestApplicationImpl <em>Application</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestApplicationImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestApplication()
	 * @generated
	 */
	int REST_APPLICATION = 1;

	/**
	 * The feature id for the '<em><b>Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__ACCESS = SECURED__ACCESS;

	/**
	 * The feature id for the '<em><b>Roles Allowed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__ROLES_ALLOWED = SECURED__ROLES_ALLOWED;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__NAME = SECURED_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__BASE_PATH = SECURED_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extension Select</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__EXTENSION_SELECT = SECURED_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__PROPERTIES = SECURED_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__RESOURCES = SECURED_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION_FEATURE_COUNT = SECURED_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION_OPERATION_COUNT = SECURED_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestResourceImpl <em>Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestResourceImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestResource()
	 * @generated
	 */
	int REST_RESOURCE = 2;

	/**
	 * The feature id for the '<em><b>Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__ACCESS = SECURED__ACCESS;

	/**
	 * The feature id for the '<em><b>Roles Allowed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__ROLES_ALLOWED = SECURED__ROLES_ALLOWED;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__PATH = SECURED_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__TARGET = SECURED_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Consumes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__CONSUMES = SECURED_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Produces</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__PRODUCES = SECURED_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__METHODS = SECURED_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE_FEATURE_COUNT = SECURED_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE_OPERATION_COUNT = SECURED_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestMethodImpl <em>Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestMethodImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestMethod()
	 * @generated
	 */
	int REST_METHOD = 3;

	/**
	 * The feature id for the '<em><b>Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__ACCESS = SECURED__ACCESS;

	/**
	 * The feature id for the '<em><b>Roles Allowed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__ROLES_ALLOWED = SECURED__ROLES_ALLOWED;

	/**
	 * The feature id for the '<em><b>Http Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__HTTP_METHOD = SECURED_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__PATH = SECURED_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__OPERATION = SECURED_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Consumes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__CONSUMES = SECURED_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Produces</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__PRODUCES = SECURED_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Success Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__SUCCESS_STATUS = SECURED_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__PARAMETERS = SECURED_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD_FEATURE_COUNT = SECURED_FEATURE_COUNT + 7;

	/**
	 * The number of operations of the '<em>Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD_OPERATION_COUNT = SECURED_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.ParameterBindingImpl <em>Parameter Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.ParameterBindingImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getParameterBinding()
	 * @generated
	 */
	int PARAMETER_BINDING = 4;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_BINDING__PARAMETER = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_BINDING__SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_BINDING__NAME = 2;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_BINDING__DEFAULT_VALUE = 3;

	/**
	 * The number of structural features of the '<em>Parameter Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_BINDING_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Parameter Binding</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_BINDING_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.HttpMethod <em>Http Method</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.HttpMethod
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getHttpMethod()
	 * @generated
	 */
	int HTTP_METHOD = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.ParameterSource <em>Parameter Source</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.ParameterSource
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getParameterSource()
	 * @generated
	 */
	int PARAMETER_SOURCE = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.Access <em>Access</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.Access
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getAccess()
	 * @generated
	 */
	int ACCESS = 7;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.rest.model.rest.Secured <em>Secured</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Secured</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.Secured
	 * @generated
	 */
	EClass getSecured();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.Secured#getAccess <em>Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Access</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.Secured#getAccess()
	 * @see #getSecured()
	 * @generated
	 */
	EAttribute getSecured_Access();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.fennec.rest.model.rest.Secured#getRolesAllowed <em>Roles Allowed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Roles Allowed</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.Secured#getRolesAllowed()
	 * @see #getSecured()
	 * @generated
	 */
	EAttribute getSecured_RolesAllowed();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.rest.model.rest.RestApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Application</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestApplication
	 * @generated
	 */
	EClass getRestApplication();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestApplication#getName()
	 * @see #getRestApplication()
	 * @generated
	 */
	EAttribute getRestApplication_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getBasePath <em>Base Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Path</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestApplication#getBasePath()
	 * @see #getRestApplication()
	 * @generated
	 */
	EAttribute getRestApplication_BasePath();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getExtensionSelect <em>Extension Select</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Extension Select</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestApplication#getExtensionSelect()
	 * @see #getRestApplication()
	 * @generated
	 */
	EAttribute getRestApplication_ExtensionSelect();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Properties</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestApplication#getProperties()
	 * @see #getRestApplication()
	 * @generated
	 */
	EReference getRestApplication_Properties();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getResources <em>Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resources</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestApplication#getResources()
	 * @see #getRestApplication()
	 * @generated
	 */
	EReference getRestApplication_Resources();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.rest.model.rest.RestResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestResource
	 * @generated
	 */
	EClass getRestResource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.RestResource#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestResource#getPath()
	 * @see #getRestResource()
	 * @generated
	 */
	EAttribute getRestResource_Path();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.rest.model.rest.RestResource#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestResource#getTarget()
	 * @see #getRestResource()
	 * @generated
	 */
	EReference getRestResource_Target();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.fennec.rest.model.rest.RestResource#getConsumes <em>Consumes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Consumes</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestResource#getConsumes()
	 * @see #getRestResource()
	 * @generated
	 */
	EAttribute getRestResource_Consumes();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.fennec.rest.model.rest.RestResource#getProduces <em>Produces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Produces</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestResource#getProduces()
	 * @see #getRestResource()
	 * @generated
	 */
	EAttribute getRestResource_Produces();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.rest.model.rest.RestResource#getMethods <em>Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Methods</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestResource#getMethods()
	 * @see #getRestResource()
	 * @generated
	 */
	EReference getRestResource_Methods();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.rest.model.rest.RestMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Method</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod
	 * @generated
	 */
	EClass getRestMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getHttpMethod <em>Http Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Http Method</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getHttpMethod()
	 * @see #getRestMethod()
	 * @generated
	 */
	EAttribute getRestMethod_HttpMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getPath()
	 * @see #getRestMethod()
	 * @generated
	 */
	EAttribute getRestMethod_Path();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getOperation()
	 * @see #getRestMethod()
	 * @generated
	 */
	EReference getRestMethod_Operation();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getConsumes <em>Consumes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Consumes</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getConsumes()
	 * @see #getRestMethod()
	 * @generated
	 */
	EAttribute getRestMethod_Consumes();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getProduces <em>Produces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Produces</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getProduces()
	 * @see #getRestMethod()
	 * @generated
	 */
	EAttribute getRestMethod_Produces();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getSuccessStatus <em>Success Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Success Status</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getSuccessStatus()
	 * @see #getRestMethod()
	 * @generated
	 */
	EAttribute getRestMethod_SuccessStatus();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.RestMethod#getParameters()
	 * @see #getRestMethod()
	 * @generated
	 */
	EReference getRestMethod_Parameters();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding <em>Parameter Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Binding</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterBinding
	 * @generated
	 */
	EClass getParameterBinding();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parameter</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterBinding#getParameter()
	 * @see #getParameterBinding()
	 * @generated
	 */
	EReference getParameterBinding_Parameter();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterBinding#getSource()
	 * @see #getParameterBinding()
	 * @generated
	 */
	EAttribute getParameterBinding_Source();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterBinding#getName()
	 * @see #getParameterBinding()
	 * @generated
	 */
	EAttribute getParameterBinding_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterBinding#getDefaultValue()
	 * @see #getParameterBinding()
	 * @generated
	 */
	EAttribute getParameterBinding_DefaultValue();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.rest.model.rest.HttpMethod <em>Http Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Http Method</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.HttpMethod
	 * @generated
	 */
	EEnum getHttpMethod();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.rest.model.rest.ParameterSource <em>Parameter Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Source</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterSource
	 * @generated
	 */
	EEnum getParameterSource();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.rest.model.rest.Access <em>Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Access</em>'.
	 * @see org.eclipse.fennec.rest.model.rest.Access
	 * @generated
	 */
	EEnum getAccess();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RestFactory getRestFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.impl.SecuredImpl <em>Secured</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.impl.SecuredImpl
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getSecured()
		 * @generated
		 */
		EClass SECURED = eINSTANCE.getSecured();

		/**
		 * The meta object literal for the '<em><b>Access</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SECURED__ACCESS = eINSTANCE.getSecured_Access();

		/**
		 * The meta object literal for the '<em><b>Roles Allowed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SECURED__ROLES_ALLOWED = eINSTANCE.getSecured_RolesAllowed();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestApplicationImpl <em>Application</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestApplicationImpl
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestApplication()
		 * @generated
		 */
		EClass REST_APPLICATION = eINSTANCE.getRestApplication();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_APPLICATION__NAME = eINSTANCE.getRestApplication_Name();

		/**
		 * The meta object literal for the '<em><b>Base Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_APPLICATION__BASE_PATH = eINSTANCE.getRestApplication_BasePath();

		/**
		 * The meta object literal for the '<em><b>Extension Select</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_APPLICATION__EXTENSION_SELECT = eINSTANCE.getRestApplication_ExtensionSelect();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_APPLICATION__PROPERTIES = eINSTANCE.getRestApplication_Properties();

		/**
		 * The meta object literal for the '<em><b>Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_APPLICATION__RESOURCES = eINSTANCE.getRestApplication_Resources();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestResourceImpl <em>Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestResourceImpl
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestResource()
		 * @generated
		 */
		EClass REST_RESOURCE = eINSTANCE.getRestResource();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_RESOURCE__PATH = eINSTANCE.getRestResource_Path();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_RESOURCE__TARGET = eINSTANCE.getRestResource_Target();

		/**
		 * The meta object literal for the '<em><b>Consumes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_RESOURCE__CONSUMES = eINSTANCE.getRestResource_Consumes();

		/**
		 * The meta object literal for the '<em><b>Produces</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_RESOURCE__PRODUCES = eINSTANCE.getRestResource_Produces();

		/**
		 * The meta object literal for the '<em><b>Methods</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_RESOURCE__METHODS = eINSTANCE.getRestResource_Methods();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestMethodImpl <em>Method</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestMethodImpl
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestMethod()
		 * @generated
		 */
		EClass REST_METHOD = eINSTANCE.getRestMethod();

		/**
		 * The meta object literal for the '<em><b>Http Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_METHOD__HTTP_METHOD = eINSTANCE.getRestMethod_HttpMethod();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_METHOD__PATH = eINSTANCE.getRestMethod_Path();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_METHOD__OPERATION = eINSTANCE.getRestMethod_Operation();

		/**
		 * The meta object literal for the '<em><b>Consumes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_METHOD__CONSUMES = eINSTANCE.getRestMethod_Consumes();

		/**
		 * The meta object literal for the '<em><b>Produces</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_METHOD__PRODUCES = eINSTANCE.getRestMethod_Produces();

		/**
		 * The meta object literal for the '<em><b>Success Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_METHOD__SUCCESS_STATUS = eINSTANCE.getRestMethod_SuccessStatus();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_METHOD__PARAMETERS = eINSTANCE.getRestMethod_Parameters();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.impl.ParameterBindingImpl <em>Parameter Binding</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.impl.ParameterBindingImpl
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getParameterBinding()
		 * @generated
		 */
		EClass PARAMETER_BINDING = eINSTANCE.getParameterBinding();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_BINDING__PARAMETER = eINSTANCE.getParameterBinding_Parameter();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_BINDING__SOURCE = eINSTANCE.getParameterBinding_Source();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_BINDING__NAME = eINSTANCE.getParameterBinding_Name();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_BINDING__DEFAULT_VALUE = eINSTANCE.getParameterBinding_DefaultValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.HttpMethod <em>Http Method</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.HttpMethod
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getHttpMethod()
		 * @generated
		 */
		EEnum HTTP_METHOD = eINSTANCE.getHttpMethod();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.ParameterSource <em>Parameter Source</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.ParameterSource
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getParameterSource()
		 * @generated
		 */
		EEnum PARAMETER_SOURCE = eINSTANCE.getParameterSource();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.rest.model.rest.Access <em>Access</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.rest.model.rest.Access
		 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getAccess()
		 * @generated
		 */
		EEnum ACCESS = eINSTANCE.getAccess();

	}

} //RestPackage
