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
package org.eclipse.fennec.rest.model.rest.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.fennec.rest.model.rest.Access;
import org.eclipse.fennec.rest.model.rest.RestPackage;
import org.eclipse.fennec.rest.model.rest.Secured;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Secured</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.impl.SecuredImpl#getAccess <em>Access</em>}</li>
 *   <li>{@link org.eclipse.fennec.rest.model.rest.impl.SecuredImpl#getRolesAllowed <em>Roles Allowed</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class SecuredImpl extends MinimalEObjectImpl.Container implements Secured {
	/**
	 * The default value of the '{@link #getAccess() <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccess()
	 * @generated
	 * @ordered
	 */
	protected static final Access ACCESS_EDEFAULT = Access.INHERIT;

	/**
	 * The cached value of the '{@link #getAccess() <em>Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccess()
	 * @generated
	 * @ordered
	 */
	protected Access access = ACCESS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRolesAllowed() <em>Roles Allowed</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRolesAllowed()
	 * @generated
	 * @ordered
	 */
	protected EList<String> rolesAllowed;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SecuredImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RestPackage.Literals.SECURED;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Access getAccess() {
		return access;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAccess(Access newAccess) {
		Access oldAccess = access;
		access = newAccess == null ? ACCESS_EDEFAULT : newAccess;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestPackage.SECURED__ACCESS, oldAccess, access));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getRolesAllowed() {
		if (rolesAllowed == null) {
			rolesAllowed = new EDataTypeUniqueEList<String>(String.class, this, RestPackage.SECURED__ROLES_ALLOWED);
		}
		return rolesAllowed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RestPackage.SECURED__ACCESS:
				return getAccess();
			case RestPackage.SECURED__ROLES_ALLOWED:
				return getRolesAllowed();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RestPackage.SECURED__ACCESS:
				setAccess((Access)newValue);
				return;
			case RestPackage.SECURED__ROLES_ALLOWED:
				getRolesAllowed().clear();
				getRolesAllowed().addAll((Collection<? extends String>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case RestPackage.SECURED__ACCESS:
				setAccess(ACCESS_EDEFAULT);
				return;
			case RestPackage.SECURED__ROLES_ALLOWED:
				getRolesAllowed().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case RestPackage.SECURED__ACCESS:
				return access != ACCESS_EDEFAULT;
			case RestPackage.SECURED__ROLES_ALLOWED:
				return rolesAllowed != null && !rolesAllowed.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (access: ");
		result.append(access);
		result.append(", rolesAllowed: ");
		result.append(rolesAllowed);
		result.append(')');
		return result.toString();
	}

} //SecuredImpl
