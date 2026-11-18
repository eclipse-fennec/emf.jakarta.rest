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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A modelled Jakarta RS application. One instance becomes one application at the Jakarta RS whiteboard.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestApplication#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestApplication#getBasePath <em>Base Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestApplication#getResources <em>Resources</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestApplication()
 * @model
 * @generated
 */
@ProviderType
public interface RestApplication extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The whiteboard application name (osgi.jakartars.name).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestApplication_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Base Path</b></em>' attribute.
	 * The default value is <code>"/"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The application base path (osgi.jakartars.application.base).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Base Path</em>' attribute.
	 * @see #setBasePath(String)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestApplication_BasePath()
	 * @model default="/"
	 * @generated
	 */
	String getBasePath();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestApplication#getBasePath <em>Base Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Path</em>' attribute.
	 * @see #getBasePath()
	 * @generated
	 */
	void setBasePath(String value);

	/**
	 * Returns the value of the '<em><b>Resources</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.rest.model.rest.RestResource}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resources</em>' containment reference list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestApplication_Resources()
	 * @model containment="true"
	 * @generated
	 */
	EList<RestResource> getResources();

} // RestApplication
