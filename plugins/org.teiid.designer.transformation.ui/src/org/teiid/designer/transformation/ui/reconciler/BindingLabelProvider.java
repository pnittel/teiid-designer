/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.transformation.ui.reconciler;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import org.teiid.designer.transformation.ui.PluginConstants;
import org.teiid.designer.transformation.ui.UiPlugin;
import org.teiid.designer.ui.explorer.ModelExplorerLabelProvider;
import org.teiid.query.sql.symbol.AggregateSymbol;
import org.teiid.query.sql.symbol.AliasSymbol;
import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.ExpressionSymbol;
import org.teiid.query.sql.symbol.Function;
import org.teiid.query.sql.symbol.SingleElementSymbol;

/**
 * ModelOutlineLabelProvider is a specialization of ModelExplorerLabelProvider
 * that allows us to display or hide additional features of a model in the outline view.
 *
 * @since 8.0
 */
public class BindingLabelProvider extends ModelExplorerLabelProvider
    implements ITableLabelProvider, PluginConstants.Images {

    private boolean showDatatype = false;
    
    /**
     * Constructor.
     * @param showDatatypes flag whether to show datatypes as part of text string
     */
    public BindingLabelProvider(boolean showDatatype) {
        this.showDatatype = showDatatype;
    }
    
    /**
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
	public String getColumnText(Object element, int columnIndex) {
        String result = PluginConstants.EMPTY_STRING;
        Binding binding = (Binding) element;
        switch (columnIndex) {
            case 0:  // Attribute Column
                result = binding.getAttributeText(showDatatype);
                break;
            case 1 : // SQL Symbol Column
                result = binding.getSqlSymbolText(showDatatype);
                break;
            default :
                break;  
        }
        return result;
    }
    
    /**
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    @Override
	public Image getColumnImage(Object element, int columnIndex) {
            Image image = null;
        Binding binding = (Binding) element;
        switch (columnIndex) {
            case 0:  // Attribute Column
                Object attr = binding.getAttribute();
                if(attr!=null && attr instanceof EObject) {
                    image = super.getImage(attr);
                } 
                break;
            case 1 : // SQL Symbol Column
                Object sqlSymbol = binding.getCurrentSymbol();
                if(sqlSymbol!=null && sqlSymbol instanceof SingleElementSymbol) {
                    // Defect 23945 - added private method to get image for multiple types
                    // of SQL symbols
                    image = getImageForSymbol((SingleElementSymbol)sqlSymbol);
                }
                break;
            default :
                break;  
        }
        return image;
    }
    
    /**
     *  Get the Image for the SingleElementSymbol
     */
    private Image getImageForSymbol(SingleElementSymbol seSymbol) {
        Image result = null;
        
        // If symbol is AliasSymbol, get underlying symbol
        if( seSymbol!=null && seSymbol instanceof AliasSymbol ) {
            seSymbol = ((AliasSymbol)seSymbol).getSymbol();
        }
        // ElementSymbol
        if ( (seSymbol instanceof ElementSymbol) ) {
            result = UiPlugin.getDefault().getImage(SYMBOL_ICON);
        // AggregateSymbol
        } else if ( seSymbol instanceof AggregateSymbol ) {
            result = UiPlugin.getDefault().getImage(FUNCTION_ICON);
        // ExpressionSymbol
        } else if ( seSymbol instanceof ExpressionSymbol ) {
            Expression expression = ((ExpressionSymbol)seSymbol).getExpression();
            if(expression!=null && expression instanceof Constant) {
                result = UiPlugin.getDefault().getImage(CONSTANT_ICON);
            } else if ( expression!=null && expression instanceof Function ) {
                result = UiPlugin.getDefault().getImage(FUNCTION_ICON);
            }
        }
        // Undefined
        if(result==null) {
            result = UiPlugin.getDefault().getImage(UNDEFINED_ICON);
        }
        
        return result; 
    }
    
}
