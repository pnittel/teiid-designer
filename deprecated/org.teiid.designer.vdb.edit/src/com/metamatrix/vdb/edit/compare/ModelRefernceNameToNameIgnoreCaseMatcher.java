/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.vdb.edit.compare;

import org.eclipse.emf.ecore.EObject;
import com.metamatrix.modeler.core.compare.AbstractEObjectNameMatcher;
import com.metamatrix.vdb.edit.manifest.ModelReference;


/** 
 * @since 4.2
 */
public class ModelRefernceNameToNameIgnoreCaseMatcher extends AbstractEObjectNameMatcher {

    /** 
     * 
     * @since 4.2
     */
    public ModelRefernceNameToNameIgnoreCaseMatcher() {
        super();
    }

    /** 
     * @see com.metamatrix.modeler.core.compare.AbstractEObjectNameMatcher#getInputKey(org.eclipse.emf.ecore.EObject)
     * @since 4.2
     */
    @Override
    protected String getInputKey(EObject entity) {
        if(entity instanceof ModelReference) {
            final String name = ((ModelReference) entity).getName();
            if(name != null) {
                return name.toUpperCase();
            }
        }
        return null;
    }

    /** 
     * @see com.metamatrix.modeler.core.compare.AbstractEObjectNameMatcher#getOutputKey(org.eclipse.emf.ecore.EObject)
     * @since 4.2
     */
    @Override
    protected String getOutputKey(EObject entity) {
        if(entity instanceof ModelReference) {
            final String name = ((ModelReference) entity).getName();
            if(name != null) {
                return name.toUpperCase();
            }
        }
        return null;
    }

}
