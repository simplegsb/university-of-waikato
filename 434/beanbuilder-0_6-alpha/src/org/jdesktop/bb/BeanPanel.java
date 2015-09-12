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

package org.jdesktop.bb;

import java.awt.BorderLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.jdesktop.bb.property.PropertyPane;

import org.jdesktop.bb.model.ObjectHolder;

/**
 * Displays the tree, panel and property sheet. Also holds the Palette
 * but the palette is constructed in the main frame.
 *
 * @version 1.20 02/27/02
 * @author  Mark Davidson
 */
public class BeanPanel extends JFrame implements PropertyChangeListener {

    private TreePanel treePanel;
    private PropertyPane propPanel;

    // Frame title
    private static final String TITLE = "Property Inspector - ";
    
    /**
     * Constructs the BeanPanel
     */
    public BeanPanel() {
	super(TITLE);

	// Look and Feel decorated Frames. 
	LookAndFeel laf = UIManager.getLookAndFeel();
	// XXX - undecorated not supported in non-Metal LAFs
	// Should recognize and throw exception.
	setUndecorated(true);
	getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	ObjectHolder objectHolder = ObjectHolder.getInstance();

        // Tree panel shows containment hierarchy.
        treePanel = new TreePanel();

        // Prop panel for editing properties of the selected object.
        propPanel = new PropertyPane();

        JSplitPane propertyPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        propertyPane.setTopComponent(treePanel);
        propertyPane.setBottomComponent(propPanel);

	// The main UI panels listen to changes in the root object.
	objectHolder.addPropertyChangeListener(treePanel);
	objectHolder.addPropertyChangeListener(propPanel);
	objectHolder.addPropertyChangeListener(this);
	
        getContentPane().add(propertyPane, BorderLayout.CENTER);
	pack();
    }

    /**
     * Shows or hides the Property Panel
     * @param show
     */
    public void showProperties(boolean show)  {
        if (propPanel != null)  {
            propPanel.setVisible(show);
            doLayout();
        }
    }
    //
    // PropertyChangeListener method
    // 

    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        String prop = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();

	ObjectHolder holder = ObjectHolder.getInstance();

	if (source == holder) {
	    if (prop.equals("selectedItem")) {
		if (newValue != null) {
		    setTitle(TITLE + newValue.getClass().getName());
		}
	    }
	}
    }

}
