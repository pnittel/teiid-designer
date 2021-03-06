/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.core.types;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.teiid.designer.core.ModelerCoreException;


/** 
 * Internal representation
 * @since 8.0
 */
public interface DatatypeManagerLifecycle {

    void initialize( ResourceSet container ) throws ModelerCoreException;
}
