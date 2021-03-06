/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.metamodels.xsd.aspects.uml;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.xsd.XSDPackage;
import org.teiid.designer.core.metamodel.aspect.MetamodelAspect;
import org.teiid.designer.core.metamodel.aspect.MetamodelAspectFactory;
import org.teiid.designer.core.metamodel.aspect.MetamodelEntity;
import org.teiid.designer.metamodels.xsd.XsdPlugin;


/**
 * RelationalSqlAspectFactoryImpl
 *
 * @since 8.0
 */
public class XsdUmlAspectFactoryImpl implements MetamodelAspectFactory {
    @Override
	public MetamodelAspect create(EClassifier classifier, MetamodelEntity entity) {
        switch (classifier.getClassifierID()) {
            case XSDPackage.XSD_BOUNDED_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_CARDINALITY_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_ENUMERATION_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_FRACTION_DIGITS_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_LENGTH_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_MAX_EXCLUSIVE_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_MAX_INCLUSIVE_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_MAX_LENGTH_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_MIN_EXCLUSIVE_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_MIN_INCLUSIVE_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_MIN_LENGTH_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_NUMERIC_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_ORDERED_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_PATTERN_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_TOTAL_DIGITS_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_WHITE_SPACE_FACET: return new XSDFacetAspect(entity);
            case XSDPackage.XSD_SIMPLE_TYPE_DEFINITION: return null;
            default:
                throw new IllegalArgumentException(XsdPlugin.Util.getString("XsdUmlAspectFactoryImpl.Invalid_ClassiferID,,_for_creating_UML_Aspect_1",classifier)); //$NON-NLS-1$
        }
    }
    
}
