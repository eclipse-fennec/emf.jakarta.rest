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
@EPackage(uri = RestPackage.eNS_URI, fingerprint = "fp1:0e7d4c25b138f76c960a68f7682ee819b34f22b7c3deea77915e12b08dab5f70", genModel = "/model/rest.genmodel", genModelSourceLocations = {"model/rest.genmodel","org.eclipse.fennec.rest.model/model/rest.genmodel"}, ecore = "/model/rest.ecore", ecoreSourceLocations = "/model/rest.ecore")
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
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestApplicationImpl <em>Application</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestApplicationImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestApplication()
	 * @generated
	 */
	int REST_APPLICATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Base Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__BASE_PATH = 1;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION__RESOURCES = 2;

	/**
	 * The number of structural features of the '<em>Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_APPLICATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestResourceImpl <em>Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestResourceImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestResource()
	 * @generated
	 */
	int REST_RESOURCE = 1;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__PATH = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Consumes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__CONSUMES = 2;

	/**
	 * The feature id for the '<em><b>Produces</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__PRODUCES = 3;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE__METHODS = 4;

	/**
	 * The number of structural features of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_RESOURCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.RestMethodImpl <em>Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestMethodImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getRestMethod()
	 * @generated
	 */
	int REST_METHOD = 2;

	/**
	 * The feature id for the '<em><b>Http Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__HTTP_METHOD = 0;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__PATH = 1;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__OPERATION = 2;

	/**
	 * The feature id for the '<em><b>Consumes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__CONSUMES = 3;

	/**
	 * The feature id for the '<em><b>Produces</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__PRODUCES = 4;

	/**
	 * The feature id for the '<em><b>Success Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__SUCCESS_STATUS = 5;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD__PARAMETERS = 6;

	/**
	 * The number of structural features of the '<em>Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_METHOD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.impl.ParameterBindingImpl <em>Parameter Binding</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.impl.ParameterBindingImpl
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getParameterBinding()
	 * @generated
	 */
	int PARAMETER_BINDING = 3;

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
	int HTTP_METHOD = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.rest.model.rest.ParameterSource <em>Parameter Source</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.rest.model.rest.ParameterSource
	 * @see org.eclipse.fennec.rest.model.rest.impl.RestPackageImpl#getParameterSource()
	 * @generated
	 */
	int PARAMETER_SOURCE = 5;


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

	}

} //RestPackage
