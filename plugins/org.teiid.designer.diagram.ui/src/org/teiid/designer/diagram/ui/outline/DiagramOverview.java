/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.diagram.ui.outline;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.teiid.designer.diagram.ui.DiagramUiConstants;
import org.teiid.designer.diagram.ui.DiagramUiPlugin;
import org.teiid.designer.diagram.ui.PluginConstants;
import org.teiid.designer.ui.editors.ModelEditorPageOutline;


/**
 * DiagramOverview is the ModelEditorPageOutline for the DiagramEditor.
 *
 * @since 8.0
 */
public class DiagramOverview implements IAdaptable, ModelEditorPageOutline {

    // ========================================
    // Variables

    private Canvas overview;
    private boolean overviewInitialized;
    private Thumbnail thumbnail;
//    private SelectionSynchronizer selectionSynchronizer;
    private GraphicalViewer graphicalViewer;
//    private Object model;
    private ImageDescriptor icon;
	private LightweightSystem lws;

    // ========================================
    // Constructors

    public DiagramOverview(
            GraphicalViewer graphicalViewer, 
            SelectionSynchronizer synchronizer,
            Object model) {
//        this.selectionSynchronizer = synchronizer;
        this.graphicalViewer = graphicalViewer;
//        this.model = model;
        this.icon = DiagramUiPlugin.getDefault().getImageDescriptor(PluginConstants.Images.OVERVIEW_ICON);    
    }

    // ========================================
    // Methods

    @Override
	public void createControl(Composite parent) {
        overview = new Canvas(parent, SWT.NONE);
        initializeOverview();
    }

    @Override
	public void dispose() {
        if ( overview != null && !overview.isDisposed() )
            overview.dispose();
    }

    @Override
	public Object getAdapter(Class type) {
        if (type == ZoomManager.class)
            return ((ScalableFreeformRootEditPart)graphicalViewer.getRootEditPart()).getZoomManager();
        return null;
    }

    @Override
	public Control getControl() {
        return overview;
    }
    
    protected void initializeOverview() {
        lws = new LightweightSystem(overview);
        RootEditPart rep = graphicalViewer.getRootEditPart();
        if (rep instanceof FreeformGraphicalRootEditPart) {
            ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)rep;
            thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
            thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
            lws.setContents(thumbnail);
            overviewInitialized = true;
        }
    }

    protected void showOverview() {
        if (!overviewInitialized)
            initializeOverview();
        thumbnail.setVisible(true);
    }
    
    public void resetContents() {
    	if( thumbnail != null ) {
	    	thumbnail.setVisible(false);
			RootEditPart rep = graphicalViewer.getRootEditPart();
			if (rep instanceof FreeformGraphicalRootEditPart) {
				ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)rep;
				thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
				thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
				lws.setContents(thumbnail);
			}
			thumbnail.setVisible(true);
		}
    }

    /* (non-Javadoc)
     * @See org.teiid.designer.ui.editors.ModelEditorPageOutline#getIcon()
     */
    @Override
	public ImageDescriptor getIcon() {
        return icon;
    }

    /* (non-Javadoc)
     * @See org.teiid.designer.ui.editors.ModelEditorPageOutline#getToolTipText()
     */
    @Override
	public String getToolTipText() {
        return DiagramUiConstants.Util.getString("DiagramOverview.toolTipText"); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @See org.teiid.designer.ui.editors.ModelEditorPageOutline#isEnabled()
     */
    @Override
	public boolean isEnabled() {
        return true;
    }

    /* (non-Javadoc)
     * @See org.teiid.designer.ui.editors.ModelEditorPageOutline#isVisible()
     */
    @Override
	public void setVisible(boolean visible) {
        if ( thumbnail != null ) {
            thumbnail.setVisible(visible);
        }
    }

}
