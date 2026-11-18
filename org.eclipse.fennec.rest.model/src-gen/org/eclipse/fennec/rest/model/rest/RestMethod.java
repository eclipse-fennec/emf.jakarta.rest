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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A resource method: an HTTP method and an optional sub path that are bound to one EOperation.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getHttpMethod <em>Http Method</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getOperation <em>Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getConsumes <em>Consumes</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getProduces <em>Produces</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getSuccessStatus <em>Success Status</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestMethod#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod()
 * @model
 * @generated
 */
@ProviderType
public interface RestMethod extends Secured {
	/**
	 * Returns the value of the '<em><b>Http Method</b></em>' attribute.
	 * The default value is <code>"GET"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.rest.model.rest.HttpMethod}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Http Method</em>' attribute.
	 * @see org.eclipse.fennec.rest.model.rest.HttpMethod
	 * @see #setHttpMethod(HttpMethod)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_HttpMethod()
	 * @model default="GET" required="true"
	 * @generated
	 */
	HttpMethod getHttpMethod();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getHttpMethod <em>Http Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Http Method</em>' attribute.
	 * @see org.eclipse.fennec.rest.model.rest.HttpMethod
	 * @see #getHttpMethod()
	 * @generated
	 */
	void setHttpMethod(HttpMethod value);

	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional sub path template below the path of the resource.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_Path()
	 * @model
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The operation that is invoked. It must be an operation of the target EClass of the resource or of one of its super types.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Operation</em>' reference.
	 * @see #setOperation(EOperation)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_Operation()
	 * @model required="true"
	 * @generated
	 */
	EOperation getOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getOperation <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(EOperation value);

	/**
	 * Returns the value of the '<em><b>Consumes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Consumes</em>' attribute list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_Consumes()
	 * @model
	 * @generated
	 */
	EList<String> getConsumes();

	/**
	 * Returns the value of the '<em><b>Produces</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Produces</em>' attribute list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_Produces()
	 * @model
	 * @generated
	 */
	EList<String> getProduces();

	/**
	 * Returns the value of the '<em><b>Success Status</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The status code of a successful invocation. 0 selects the default: 200 with a result, 204 without.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Success Status</em>' attribute.
	 * @see #setSuccessStatus(int)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_SuccessStatus()
	 * @model default="0"
	 * @generated
	 */
	int getSuccessStatus();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestMethod#getSuccessStatus <em>Success Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Success Status</em>' attribute.
	 * @see #getSuccessStatus()
	 * @generated
	 */
	void setSuccessStatus(int value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.rest.model.rest.ParameterBinding}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestMethod_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<ParameterBinding> getParameters();

} // RestMethod
