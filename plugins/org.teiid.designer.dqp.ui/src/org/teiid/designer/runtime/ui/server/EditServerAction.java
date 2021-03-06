/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.runtime.ui.server;

import static org.teiid.designer.runtime.ui.DqpUiConstants.UTIL;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.teiid.designer.runtime.Server;
import org.teiid.designer.runtime.ServerManager;
import org.teiid.designer.runtime.ui.DqpUiConstants;
import org.teiid.designer.runtime.ui.DqpUiPlugin;
import org.teiid.designer.runtime.ui.views.TeiidView;
import org.teiid.designer.ui.common.util.UiUtil;


/**
 * The <code>EditServerAction</code> runs a UI that allows {@link Server server} properties to be changed.
 *
 * @since 8.0
 */
public final class EditServerAction extends BaseSelectionListenerAction {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The selected server being edited.
     */
    private Server serverBeingEdited;

    /**
     * The server manager used to create and edit servers.
     */
    private final ServerManager serverManager;

    /**
     * The shell used to display the dialog that edits and creates servers.
     */
    private final Shell shell;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param shell the parent shell used to display the dialog
     * @param serverManager the server manager to use when creating and editing servers
     */
    public EditServerAction( Shell shell,
                             ServerManager serverManager ) {
        super(UTIL.getString("editServerActionText")); //$NON-NLS-1$
        setToolTipText(UTIL.getString("editServerActionToolTip")); //$NON-NLS-1$
        setImageDescriptor(DqpUiPlugin.getDefault().getImageDescriptor(DqpUiPlugin.Images.EDIT_SERVER_ICON));
        setEnabled(false);

        this.shell = shell;
        this.serverManager = serverManager;
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
    	// if serverBeingEdited == NULL need to query user to select one in order to continue
    	
    	if( this.serverBeingEdited == null ) {
    		ServerSelectionDialog dialog = new ServerSelectionDialog(this.shell);
    		dialog.open();
    		
    		if (dialog.getReturnCode() == Window.OK) {
    			this.serverBeingEdited = dialog.getServer();
    		}
    	}
    	
    	if( this.serverBeingEdited == null ) return;
    	
        ServerWizard wizard = new ServerWizard(this.serverManager, this.serverBeingEdited);
        WizardDialog dialog = new WizardDialog(this.shell, wizard) {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.wizard.WizardDialog#configureShell(org.eclipse.swt.widgets.Shell)
             */
            @Override
            protected void configureShell( Shell newShell ) {
                super.configureShell(newShell);
                newShell.setImage(DqpUiPlugin.getDefault().getImage(DqpUiPlugin.Images.EDIT_SERVER_ICON));
            }
        };

        int result = dialog.open();
        
        if( result == Window.OK) {
	        
	        if( wizard.shouldAutoConnect() ) {
	            	try {
	    				wizard.getServer().getAdmin();
	    				wizard.getServer().setConnectionError(null);			
	    			} catch (Exception e) {
	    				String msg = UTIL.getString("serverWizardEditServerAutoConnectError"); //$NON-NLS-1$
	    				MessageDialog.openError(this.shell, UTIL.getString("editServerActionAutoConnectProblemTitle"), //$NON-NLS-1$
	    						msg);
	    				UTIL.log(e);
	    				wizard.getServer().setConnectionError(msg);
	    				wizard.getServer().notifyRefresh();
	    			}

	        }
	        
	        // refresh viewer in Teiid View to display latest label
	        TeiidView teiidView = (TeiidView)UiUtil.getViewPart(DqpUiConstants.Extensions.CONNECTORS_VIEW_ID);
	        
	        if (teiidView != null) {
	            teiidView.updateLabel(this.serverBeingEdited);
	        }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.actions.BaseSelectionListenerAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    protected boolean updateSelection( IStructuredSelection selection ) {
        // disable if empty selection or multiple objects selected
        if (selection.isEmpty() || (selection.size() > 1)) {
            this.serverBeingEdited = null;
            return false;
        }

        Object obj = selection.getFirstElement();

        // enable if server is selected
        if (obj instanceof Server) {
            this.serverBeingEdited = (Server)obj;
            return true;
        }

        // disable if non-server is selected
        this.serverBeingEdited = null;
        return false;
    }

}
