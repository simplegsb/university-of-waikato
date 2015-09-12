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
 * $Id: TabsDlg.java,v 1.1.1.1 2004/07/14 03:37:59 davidson1 Exp $
 */

package org.jdesktop.bb.ui;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Container;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

/**
 * Generic property dialog which supports a set of tabbed panels with and Ok, Cancel
 * and Apply button.
 *
 * Very similar to the Wizard dialog.
 *
 * @version $Revision: 1.1.1.1 $
 * @author  Mark Davidson
 */
public class TabsDlg extends JDialog {
    
    private JTabbedPane tabsPanel;
    
    private JButton okButton;
    private JButton cancelButton;
    private JButton applyButton;

    private ActionListener okListener = null;
    private ActionListener cancelListener = null;
    private ActionListener applyListener = null;

    // For testing
    private static TabsDlg tabsDlg;

    /**
     * Creates the tab pane dialog
     *
     * @param title - Title of the panel.
     * @param panels - vector of panels to insert. The name of the panel (getName())
     *        will the be name of the tab.
     */
    public TabsDlg(String title, Vector panels) {
        super(new JFrame(), title, true); 

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

	    tabsPanel = new JTabbedPane();
        
        int numPanels = panels.size();
        
        JPanel panel;
        for (int i = 0; i < numPanels; i++) {
            panel = (JPanel)panels.elementAt(i);
            tabsPanel.addTab(panel.getName(), panel);
        }
        
        pane.add(tabsPanel, BorderLayout.CENTER);
        pane.add(createButtonPanel(), BorderLayout.SOUTH);
        
        this.pack();
        // Center the window.
        CommonUI.centerComponent(this);
    }

    /** 
     * For testing purposes.
     */
    public static void main(String[] args)  {
        
        JPanel p1 = new JPanel();
        p1.add(new JButton("One"));
        p1.setName("One");
        
        JPanel p2 = new JPanel();
        p2.add(new JButton("Two"));
        p2.setName("Two");
        
        JPanel p3 = new JPanel();
        p3.add(new JButton("Three"));
        p3.setName("Three");
        
        JPanel p4 = new JPanel();
        p4.add(new JButton("Four"));
        p4.setName("Four");
        
        Vector panels = new Vector();
        
        panels.addElement(p1);
        panels.addElement(p2);
        panels.addElement(p3);
        panels.addElement(p4);
        
        tabsDlg = new TabsDlg("Test Dialog", panels);
        tabsDlg.addOkListener(new ActionListener()  {
            public void actionPerformed(ActionEvent evt)  {
                System.exit(0);     
            }
        });
        
        tabsDlg.addCancelListener(new ActionListener()  {
            public void actionPerformed(ActionEvent evt)  {
                System.exit(0);     
            }
        });
        
        tabsDlg.setVisible(true);
    }

    /** 
     * Creates the button panel.
     */
    private JPanel createButtonPanel()  {
        JPanel panel = new JPanel();

        okButton = CommonUI.createButton(CommonUI.BUTTONTEXT_OK,
                                    new OkListener(), CommonUI.MNEMONIC_OK);
        panel.add(okButton);

        cancelButton = CommonUI.createButton(CommonUI.BUTTONTEXT_CANCEL,
                                    new CancelListener(), CommonUI.MNEMONIC_CANCEL);
        panel.add(cancelButton);
        
        applyButton = CommonUI.createButton(CommonUI.BUTTONTEXT_APPLY, 
                                    new ApplyListener(), CommonUI.MNEMONIC_APPLY);
        applyButton.setEnabled(false);
        panel.add(applyButton);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(panel, BorderLayout.CENTER);
        p2.add(new JSeparator(), BorderLayout.NORTH);

        return p2;
    }
    
    /** 
     * Enables the apply button on the dialog. This method should be called by
     * the subclass when the data in the panels change.
     */
    public void enableApplyButton(boolean enabled)  {
        applyButton.setEnabled(enabled);
    }
 
    //
    // Listeners for dialog buttons
    //

    private class OkListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (okListener != null) {
                okListener.actionPerformed(evt);
            }
            setVisible(false);
        }
    }

    private class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (cancelListener != null) {
                cancelListener.actionPerformed(evt);
            }
            setVisible(false);
        }
    }

    private class ApplyListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        
            if (applyListener != null) {
                applyListener.actionPerformed(evt);

                // Should disable the apply and cancel buttons
                enableApplyButton(false);
            }
        }
    }

    //
    // Event declarations for OK, Cancel & Apply events
    //

    public synchronized void addOkListener(ActionListener l) {
        okListener = AWTEventMulticaster.add(okListener, l);
    }
    public synchronized void removeOkListener(ActionListener l) {
        okListener = AWTEventMulticaster.remove(okListener, l);
    }

    public synchronized void addCancelListener(ActionListener l) {
        cancelListener = AWTEventMulticaster.add(cancelListener, l);
    }
    public synchronized void removeCancelListener(ActionListener l) {
        cancelListener = AWTEventMulticaster.remove(cancelListener, l);
    }

    public synchronized void addApplyListener(ActionListener l) {
        applyListener = AWTEventMulticaster.add(applyListener, l);
    }
    public synchronized void removeApplyListener(ActionListener l) {
        applyListener = AWTEventMulticaster.remove(applyListener, l);
    }

}