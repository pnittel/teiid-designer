/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.metamodels.relational.compare;

import org.eclipse.emf.ecore.EObject;
import org.teiid.designer.core.compare.AbstractEObjectNameMatcher;
import org.teiid.designer.metamodels.relational.RelationalEntity;


/**
 * RelationalEntityNameToNameMatcher
 *
 * @since 8.0
 */
public class RelationalEntityNameInSourceToNameInSourceMatcher extends AbstractEObjectNameMatcher {

    /**
     * Construct an instance of RelationalEntityNameInSourceToNameInSourceMatcher.
     * 
     */
    public RelationalEntityNameInSourceToNameInSourceMatcher() {
        super();
    }

    @Override
    protected String getInputKey( final EObject entity ) {
        if(entity instanceof RelationalEntity) {
            return ((RelationalEntity)entity).getNameInSource();
        }
        return null;
    }

    @Override
    protected String getOutputKey( final EObject entity ) {
        if(entity instanceof RelationalEntity) {
            return ((RelationalEntity)entity).getNameInSource();
        }
        return null;
    }
}
