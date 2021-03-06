/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.diagram.ui.custom;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.teiid.designer.core.ModelerCore;
import org.teiid.designer.diagram.ui.DiagramUiConstants;
import org.teiid.designer.diagram.ui.DiagramUiPlugin;
import org.teiid.designer.diagram.ui.PluginConstants;
import org.teiid.designer.diagram.ui.figure.AbstractDiagramFigureFactory;
import org.teiid.designer.diagram.ui.notation.NotationFigureGenerator;
import org.teiid.designer.ui.common.graphics.GlobalUiColorManager;


/**
 * PackageDiagramFigureFactory
 *
 * @since 8.0
 */
public class CustomDiagramFigureFactory extends AbstractDiagramFigureFactory implements DiagramUiConstants {
    private static final int CUSTOM_DIAGRAM = 0;

    /**
     * Construct an instance of UmlFigureFactory.
     * 
     */
    public CustomDiagramFigureFactory() {
        super();
    }

    @Override
    public Figure createFigure(Object modelObject, String sNotationId) {

        Figure newFigure = null;
        switch( getObjectType(modelObject) ) {
            
            case CUSTOM_DIAGRAM: {
                newFigure = new FreeformLayer();
                newFigure.setLayoutManager(new FreeformLayout());
                // Don't know why, but if you don't setOpaque(true), you cannot move by drag&drop!
                newFigure.setOpaque(true);
                RGB currentBkgdColor = 
                    PreferenceConverter.getColor(
                        DiagramUiPlugin.getDefault().getPreferenceStore(),
                        PluginConstants.Prefs.Appearance.CUSTOM_BKGD_COLOR);
                newFigure.setBackgroundColor(GlobalUiColorManager.getColor(currentBkgdColor));
            } break;
            
            default: {
                // Here's where we get the notation manager and tell it to create a figure
                // for this modelObject.  So it'll come back in whatever "Notation" it desires.
                NotationFigureGenerator generator = DiagramUiPlugin.getDiagramNotationManager().getFigureGenerator(sNotationId);
                if( generator != null )
                    newFigure = generator.createFigure(modelObject);
                else {
                    ModelerCore.Util.log( IStatus.ERROR, Util.getString(Errors.FIGURE_GENERATOR_FAILURE));
                }
                    
            } break;
        }

        return newFigure;
    }
    
    protected int getObjectType( Object modelObject ) {
        int objectType = -1;
        
        if( modelObject != null ) {
            if (modelObject instanceof CustomDiagramNode) {
                objectType = CUSTOM_DIAGRAM;
            }
        }
        return objectType;
    }
}
