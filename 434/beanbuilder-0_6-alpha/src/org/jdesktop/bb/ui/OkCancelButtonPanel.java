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
 * $Id: OkCancelButtonPanel.java,v 1.1.1.1 2004/07/14 03:37:59 davidson1 Exp $
 */

package org.jdesktop.bb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A simple panel which features an Ok and a Cancel button in a flow panel.
 *
 * This class will send a <code>CommonUI.BUTTON_CMD_OK</code> or a 
 * <code>BUTTON_CND_CANCEL</code> as the action command to the registered 
 * action listener.
 *
 * @version $Revision: 1.1.1.1 $
 * @author  Mark Davidson
 */
public class OkCancelButtonPanel extends JPanel implements ActionListener {
    
    private JButton okButton;
    private JButton cancelButton;
    
    // Listener which will be sent a message.
    private ActionListener listener;

    /**
     * ctor
     *
     * @param listener - ActionListener which will get get the ActionEvent
     */
    public OkCancelButtonPanel(ActionListener listener) {
        this.listener = listener;
        okButton = CommonUI.createButton(CommonUI.BUTTONTEXT_OK, this, 
                                            CommonUI.MNEMONIC_OK);

        cancelButton = CommonUI.createButton(CommonUI.BUTTONTEXT_CANCEL, this,
                                            CommonUI.MNEMONIC_CANCEL);        
        add(okButton);
        add(cancelButton);
        
    }
    
    /** 
     * Forward the event to the the ActionListener that constructed the panel.
     * The ActionEvetn.actionCommand contain the command.
     */
    public void actionPerformed(ActionEvent evt)  {
        Object obj = evt.getSource();
        
        if (obj instanceof JButton)  {
            JButton button = (JButton)obj;
            
            if (button == okButton)  {
                listener.actionPerformed(new ActionEvent(evt.getSource(), 
                                        evt.getID(), CommonUI.BUTTON_CMD_OK));
            }
            
            if (button == cancelButton)  {
                listener.actionPerformed(new ActionEvent(evt.getSource(), 
                                        evt.getID(), CommonUI.BUTTON_CMD_CANCEL));
            }
        }
    }
}