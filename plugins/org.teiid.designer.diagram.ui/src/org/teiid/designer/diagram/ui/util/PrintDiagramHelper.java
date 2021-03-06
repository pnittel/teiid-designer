/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.diagram.ui.util;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.teiid.designer.diagram.ui.DiagramUiConstants;
import org.teiid.designer.diagram.ui.DiagramUiPlugin;



/** 
 * @since 8.0
 */
public class PrintDiagramHelper 
  implements DiagramUiConstants{
    
//    private static final String FILE_DIALOG_TITLE = "SaveDiagramDialog.title"; //$NON-NLS-1$
    private static final long MAX_PRINTABLE_DIAGRAM_SIZE = 9000L * 9000L;
    private static final int COLOR_DEPTH = 16;  // 16 is plenty for our modest pallette of colors
    
    /** 
     * 
     * @since 5.0
     */
    public PrintDiagramHelper() {
        super();
    }
    
    protected static Shell getShell() {
        return DiagramUiPlugin.getDefault().getCurrentWorkbenchWindow().getShell();
    }

    /** 
     * @param imageLoader
     * @param rectFullDiagram
     * @return
     * @since 5.0
     */
    public static Image getImage( Rectangle rectFullDiagram ) {
        Image image = null;
        
        // check for oversize diagram and bail out
        boolean bContinue = checkDiagramSize( rectFullDiagram );
        
        if ( !bContinue ) {
            return image;
        }

        Display display = Display.getDefault();
        
        // first free all the memory and handles you can...
        System.gc();
        
        // the 'scaleTo' call is the workaround for Windows 16M image size limit problem
        ImageData imageData = new ImageData( 10, 10, COLOR_DEPTH, new PaletteData( 0xFF, 0xFF00, 0xFF0000 ) );
        ImageData scaledIData 
            = imageData.scaledTo( rectFullDiagram.width, rectFullDiagram.height );
        
        image = new Image( display, scaledIData );
        return image;
    }

    

    public static boolean checkDiagramSize( Rectangle rect ) {
        
        // check for excessively large diagram regions
        long lRectangleSize = rect.width * rect.height;
//        System.out.println("[SaveDiagramHelper.checkDiagramSize] About to test rectangle, size is: " + lRectangleSize );

        if ( lRectangleSize > MAX_PRINTABLE_DIAGRAM_SIZE ) {
//            System.out.println("[SaveDiagramHelper.checkDiagramSize] Rectangle TOO LARGE TO SAVE: " + lRectangleSize );
            String sMessage = Util.getString( "PrintDiagramWarningDialog1.message", MAX_PRINTABLE_DIAGRAM_SIZE )      //$NON-NLS-1$
                             + Util.getString( "PrintDiagramWarningDialog2.message", lRectangleSize );                //$NON-NLS-1$
            MessageDialog.openWarning( getShell(), Util.getString( "PrintDiagramWarningDialog.title" ), sMessage );   //$NON-NLS-1$
            return false;
        }

        return true;
    }
    
    
}
