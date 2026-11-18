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

import org.eclipse.emf.ecore.EClass;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A root resource. It binds a path to an EClass of a domain model, whose EOperations are exposed by the contained resource methods.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestResource#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestResource#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestResource#getConsumes <em>Consumes</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestResource#getProduces <em>Produces</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.RestResource#getMethods <em>Methods</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestResource()
 * @model
 * @generated
 */
@ProviderType
public interface RestResource extends Secured {
	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The path template of the resource, as in jakarta.ws.rs.Path.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestResource_Path()
	 * @model required="true"
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestResource#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The EClass that is instantiated per request and on which the operations are invoked.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(EClass)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestResource_Target()
	 * @model required="true"
	 * @generated
	 */
	EClass getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.RestResource#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(EClass value);

	/**
	 * Returns the value of the '<em><b>Consumes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Default request media types for all methods of this resource.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Consumes</em>' attribute list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestResource_Consumes()
	 * @model
	 * @generated
	 */
	EList<String> getConsumes();

	/**
	 * Returns the value of the '<em><b>Produces</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Default response media types for all methods of this resource.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Produces</em>' attribute list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestResource_Produces()
	 * @model
	 * @generated
	 */
	EList<String> getProduces();

	/**
	 * Returns the value of the '<em><b>Methods</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.rest.model.rest.RestMethod}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Methods</em>' containment reference list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getRestResource_Methods()
	 * @model containment="true"
	 * @generated
	 */
	EList<RestMethod> getMethods();

} // RestResource
