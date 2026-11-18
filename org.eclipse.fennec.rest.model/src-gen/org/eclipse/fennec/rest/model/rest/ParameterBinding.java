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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EParameter;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Binds one EParameter of the operation to a part of the HTTP request. A parameter without a binding receives null.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#isEncoded <em>Encoded</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getParameterBinding()
 * @model
 * @generated
 */
@ProviderType
public interface ParameterBinding extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' reference.
	 * @see #setParameter(EParameter)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getParameterBinding_Parameter()
	 * @model required="true"
	 * @generated
	 */
	EParameter getParameter();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getParameter <em>Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter</em>' reference.
	 * @see #getParameter()
	 * @generated
	 */
	void setParameter(EParameter value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * The default value is <code>"QUERY"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.rest.model.rest.ParameterSource}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterSource
	 * @see #setSource(ParameterSource)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getParameterBinding_Source()
	 * @model default="QUERY" required="true"
	 * @generated
	 */
	ParameterSource getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see org.eclipse.fennec.rest.model.rest.ParameterSource
	 * @see #getSource()
	 * @generated
	 */
	void setSource(ParameterSource value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the path, query, header or cookie parameter. Defaults to the name of the EParameter. Unused for BODY.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getParameterBinding_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Encoded</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Passes the value as it stands in the request, without URL decoding. Applies to PATH, QUERY, MATRIX and FORM, as jakarta.ws.rs.Encoded.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoded</em>' attribute.
	 * @see #setEncoded(boolean)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getParameterBinding_Encoded()
	 * @model default="false"
	 * @generated
	 */
	boolean isEncoded();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#isEncoded <em>Encoded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoded</em>' attribute.
	 * @see #isEncoded()
	 * @generated
	 */
	void setEncoded(boolean value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The literal that is used when the request does not carry the parameter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(String)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getParameterBinding_DefaultValue()
	 * @model
	 * @generated
	 */
	String getDefaultValue();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.ParameterBinding#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(String value);

} // ParameterBinding
