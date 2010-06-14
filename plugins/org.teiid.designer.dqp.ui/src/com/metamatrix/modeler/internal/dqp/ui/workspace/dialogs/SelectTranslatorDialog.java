/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.dqp.ui.workspace.dialogs;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.teiid.designer.runtime.TeiidTranslator;
import com.metamatrix.modeler.dqp.DqpPlugin;
import com.metamatrix.modeler.dqp.ui.DqpUiConstants;
import com.metamatrix.modeler.internal.dqp.ui.workspace.TeiidViewTreeProvider;

/**
 * @since 5.0
 */
public class SelectTranslatorDialog extends ElementTreeSelectionDialog implements ISelectionChangedListener {

    private static final String DEFAULT_TITLE = DqpUiConstants.UTIL.getString("SelectConnectorBindingDialog.title"); //$NON-NLS-1$

    private TeiidTranslator selectedTranslator;

    /**
     * Construct an instance of ModelWorkspaceDialog. This constructor defaults to the resource root.
     * 
     * @param parent
     */
    public SelectTranslatorDialog( Shell parent ) {

        this(parent, DEFAULT_TITLE, new TeiidViewTreeProvider(false), new TeiidViewTreeProvider(false));
    }

    /**
     * Construct an instance of ModelWorkspaceDialog. This constructor defaults to the resource root.
     * 
     * @param parent
     * @param labelProvider an ILabelProvider for the tree
     * @param contentProvider an ITreeContentProvider for the tree
     */
    public SelectTranslatorDialog( Shell parent,
                                         String title,
                                         ILabelProvider labelProvider,
                                         ITreeContentProvider contentProvider ) {
        super(parent, labelProvider, contentProvider);

        init(title);
    }

    protected void init( String title ) {
        setTitle(DEFAULT_TITLE);

        // default to EObject validator
        // super.setValidator( new EObjectSelectionValidator() );

        // use default root
        setInput(DqpPlugin.getInstance().getServerManager().getServers());

    }

    /**
     * Adds a ViewerFilter to this dialog's TreeViewer
     * 
     * @param filter
     */
    public void addViewerFilter( ViewerFilter filter ) {
        super.getTreeViewer().addFilter(filter);
    }

    /**
     * Sets the Validator for this dialog's TreeViewer
     * 
     * @param filter
     */
    @Override
    public void setValidator( ISelectionStatusValidator validator ) {
        super.setValidator(validator);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createTreeViewer(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected TreeViewer createTreeViewer( Composite parent ) {
        TreeViewer result = super.createTreeViewer(parent);

        result.expandToLevel(2);

        getTreeViewer().addSelectionChangedListener(this);

        return result;
        // listen to selection in the tree

    }

    public void selectionChanged( SelectionChangedEvent event ) {
        IStructuredSelection sel = (IStructuredSelection)getTreeViewer().getSelection();

        if (sel.getFirstElement() instanceof TeiidTranslator) {
        	selectedTranslator = (TeiidTranslator)sel.getFirstElement();
        }
    }

    public TeiidTranslator getSelectedConnector() {
        return selectedTranslator;
    }
}