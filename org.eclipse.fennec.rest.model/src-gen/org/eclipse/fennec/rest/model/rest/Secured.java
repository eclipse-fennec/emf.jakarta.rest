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
 * A representation of the model object '<em><b>Secured</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Access control of an application, a resource or a method. The most specific element that says something decides: a method before its resource, a resource before its application. An element says something when it has roles or an access other than INHERIT. When nothing is said at all, the method is public.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.Secured#getAccess <em>Access</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.Secured#getRolesAllowed <em>Roles Allowed</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getSecured()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface Secured extends EObject {
	/**
	 * Returns the value of the '<em><b>Access</b></em>' attribute.
	 * The default value is <code>"INHERIT"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.rest.model.rest.Access}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Access</em>' attribute.
	 * @see org.eclipse.fennec.rest.model.rest.Access
	 * @see #setAccess(Access)
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getSecured_Access()
	 * @model default="INHERIT" required="true"
	 * @generated
	 */
	Access getAccess();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.rest.model.rest.Secured#getAccess <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access</em>' attribute.
	 * @see org.eclipse.fennec.rest.model.rest.Access
	 * @see #getAccess()
	 * @generated
	 */
	void setAccess(Access value);

	/**
	 * Returns the value of the '<em><b>Roles Allowed</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The caller must be authenticated and hold at least one of these roles. Roles can only be combined with the access INHERIT or AUTHENTICATED.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Roles Allowed</em>' attribute list.
	 * @see org.eclipse.fennec.rest.model.rest.RestPackage#getSecured_RolesAllowed()
	 * @model
	 * @generated
	 */
	EList<String> getRolesAllowed();

} // Secured
