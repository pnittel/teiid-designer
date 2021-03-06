/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.vdb.edit.manifest;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Wsdl Options</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link com.metamatrix.vdb.edit.manifest.WsdlOptions#getTargetNamespaceUri <em>Target Namespace Uri</em>}</li>
 * <li>{@link com.metamatrix.vdb.edit.manifest.WsdlOptions#getDefaultNamespaceUri <em>Default Namespace Uri</em>}</li>
 * <li>{@link com.metamatrix.vdb.edit.manifest.WsdlOptions#getVirtualDatabase <em>Virtual Database</em>}</li>
 * </ul>
 * </p>
 * 
 * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getWsdlOptions()
 * @model
 * @generated
 */
public interface WsdlOptions extends EObject {

    /**
     * Returns the value of the '<em><b>Target Namespace Uri</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Namespace Uri</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Target Namespace Uri</em>' attribute.
     * @see #setTargetNamespaceUri(String)
     * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getWsdlOptions_TargetNamespaceUri()
     * @model
     * @generated
     */
    String getTargetNamespaceUri();

    /**
     * Sets the value of the '{@link com.metamatrix.vdb.edit.manifest.WsdlOptions#getTargetNamespaceUri
     * <em>Target Namespace Uri</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Target Namespace Uri</em>' attribute.
     * @see #getTargetNamespaceUri()
     * @generated
     */
    void setTargetNamespaceUri( String value );

    /**
     * Returns the value of the '<em><b>Default Namespace Uri</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default Namespace Uri</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Default Namespace Uri</em>' attribute.
     * @see #setDefaultNamespaceUri(String)
     * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getWsdlOptions_DefaultNamespaceUri()
     * @model
     * @generated
     */
    String getDefaultNamespaceUri();

    /**
     * Sets the value of the '{@link com.metamatrix.vdb.edit.manifest.WsdlOptions#getDefaultNamespaceUri
     * <em>Default Namespace Uri</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Default Namespace Uri</em>' attribute.
     * @see #getDefaultNamespaceUri()
     * @generated
     */
    void setDefaultNamespaceUri( String value );

    /**
     * Returns the value of the '<em><b>Virtual Database</b></em>' container reference. It is bidirectional and its opposite is '
     * {@link com.metamatrix.vdb.edit.manifest.VirtualDatabase#getWsdlOptions <em>Wsdl Options</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Virtual Database</em>' container reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Virtual Database</em>' container reference.
     * @see #setVirtualDatabase(VirtualDatabase)
     * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getWsdlOptions_VirtualDatabase()
     * @see com.metamatrix.vdb.edit.manifest.VirtualDatabase#getWsdlOptions
     * @model opposite="wsdlOptions" required="true"
     * @generated
     */
    VirtualDatabase getVirtualDatabase();

    /**
     * Sets the value of the '{@link com.metamatrix.vdb.edit.manifest.WsdlOptions#getVirtualDatabase <em>Virtual Database</em>}'
     * container reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Virtual Database</em>' container reference.
     * @see #getVirtualDatabase()
     * @generated
     */
    void setVirtualDatabase( VirtualDatabase value );

} // WsdlOptions
