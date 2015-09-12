/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

/*
 * $Id: OkCancelDialog.java,v 1.1.1.1 2004/07/14 03:37:59 davidson1 Exp $
 */

package org.jdesktop.bb.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Dialog which uses the OkCancelButtonPanel. Puts a panel in the center of the
 * a border layout.
 *
 * @version $Revision: 1.1.1.1 $
 * @author  Mark Davidson
 */
public class OkCancelDialog extends JDialog implements ActionListener{
    
    private boolean okPressed;
    
    /**
     * Creates the dialog as modal
     *
     * @param title Name of the dialog in the title bar
     * @param panel Panel to stick in the center of the dialog
     */
    public OkCancelDialog(String title, JPanel panel) {
        this(title, panel, true);
    }
    
    /**
     * Creates the dialog
     *
     * @param title Name of the dialog in the title bar
     * @param panel Panel to stick in the center of the dialog
     * @param modal Flag which indicates if the dialog should be modal
     */
    public OkCancelDialog(String title, JPanel panel, boolean modal) {
       this.setTitle(title);
       this.setModal(modal);
       
       Container pane = this.getContentPane();
       pane.setLayout(new BorderLayout());
       
       pane.add(panel, BorderLayout.CENTER);
       pane.add(new OkCancelButtonPanel(this), BorderLayout.SOUTH);
       
       this.pack();
       CommonUI.centerComponent(this);
    }

    /** 
     * Returns a flag which indicates if the OK button was pressed.
     */
    public boolean isOk()  {
        return okPressed;
    }
    
    //
    // Action listener methods.
    //
    
    public void actionPerformed(ActionEvent evt)  {
        String command = evt.getActionCommand();
        
        if (command.equals(CommonUI.BUTTON_CMD_OK)) {
            okPressed = true;
            this.setVisible(false);
            this.dispose();
            
        } else if (command.equals(CommonUI.BUTTON_CMD_CANCEL)) {
            okPressed = false;
            this.setVisible(false);
            this.dispose();
            
        }
    } // end actionPerformed
}