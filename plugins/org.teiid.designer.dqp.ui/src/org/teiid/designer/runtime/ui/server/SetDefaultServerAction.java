/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.runtime.ui.server;

import static org.teiid.designer.runtime.ui.DqpUiConstants.UTIL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.teiid.core.util.CoreArgCheck;
import org.teiid.designer.runtime.Server;
import org.teiid.designer.runtime.ServerManager;
import org.teiid.designer.runtime.ui.DqpUiConstants;
import org.teiid.designer.runtime.ui.DqpUiPlugin;
import org.teiid.designer.ui.common.util.WidgetUtil;


/**
 * 
 *
 * @since 8.0
 */
public class SetDefaultServerAction extends BaseSelectionListenerAction {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The server manager used to create and edit servers.
     */
    private final ServerManager serverManager;

    /**
     * The servers being deleted (never <code>null</code>).
     */
    private Server selectedServer;

    /**
     * @param shell the parent shell used to display the dialog
     * @param serverManager the server manager to use when creating and editing servers
     */
    public SetDefaultServerAction( ServerManager serverManager ) {
        super(UTIL.getString("setDefaultServerActionText")); //$NON-NLS-1$
        CoreArgCheck.isNotNull(serverManager, "serverManager"); //$NON-NLS-1$

        if (Platform.isRunning()) {
            setToolTipText(UTIL.getString("setDefaultServerActionToolTip")); //$NON-NLS-1$
            setImageDescriptor(DqpUiPlugin.getDefault().getImageDescriptor(DqpUiConstants.Images.SET_DEFAULT_SERVER_ICON));
        }

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
    	boolean disconnectOldDefault = false;
    	if( this.serverManager.getDefaultServer().isConnected() ) {
	    	disconnectOldDefault = MessageDialog.openQuestion(getShell(), 
	    			UTIL.getString("setDefaultServerActionDisconnectOldTitle"),  //$NON-NLS-1$
	    			UTIL.getString("setDefaultServerActionDisconnectOldMessage", this.serverManager.getDefaultServer().getUrl())); //$NON-NLS-1$
    	}
    	if( disconnectOldDefault ) {
    		this.serverManager.getDefaultServer().disconnect();
    		
    	}
        this.serverManager.setDefaultServer(this.selectedServer);
        if( !this.selectedServer.isConnected() ) {
        	final Server theNewDefaultServer = this.selectedServer;
            BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

                @Override
				public void run() {
			        try {
			        	// Call disconnect() first to clear out Server & admin caches
			        	theNewDefaultServer.getAdmin().refresh();
			        } catch (Exception e) {
			            UTIL.log(e);
			            String msg = UTIL.getString("serverReconnectErrorMsg", theNewDefaultServer.getUrl()); //$NON-NLS-1$
			            WidgetUtil.showError(msg);
			        }
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.actions.BaseSelectionListenerAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    protected boolean updateSelection( IStructuredSelection selection ) {
        // reset selected server collection
        this.selectedServer = null;

        // disable if empty selection
        if (selection.isEmpty() && selection.size() != 1) {
            return false;
        }

        // enable only if selected object is server and not same server
        Object selectedObj = selection.getFirstElement();
        if (selectedObj instanceof Server) {
            this.selectedServer = (Server)selectedObj;
            if (this.serverManager.getDefaultServer() != null
                && !this.selectedServer.equals(this.serverManager.getDefaultServer())) {
                return true;
            }
        }

        return false;
    }
    
    private static Shell getShell() {
    	return DqpUiPlugin.getDefault().getCurrentWorkbenchWindow().getShell();
    }
}
