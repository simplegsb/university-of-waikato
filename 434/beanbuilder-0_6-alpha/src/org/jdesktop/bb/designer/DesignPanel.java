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

package org.jdesktop.bb.designer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JFrame;

import org.jdesktop.bb.model.ObjectHolder;

/**
 * A class which manages the design time information and management of the
 * root JFrame object.
 *
 * @version 1.45 02/27/02
 * @author  Mark Davidson
 */
public class DesignPanel implements PropertyChangeListener {

    private EventPane event;
    private LayoutPane layout;

    private ObjectHolder objectHolder;

    // Stored actual values
    private JFrame currentFrame;
    private Point rootLocation;
    private Dimension rootSize;
    private Component glassPane;

    // Faked designer values
    private Point designerLocation;
    private Dimension designerSize;
    private GlassPane designer;

    private SizeListener sizeListener;

    // These represent choices in the type of design.
    public static final String EVENT_TYPE = "Event Management";
    public static final String LAYOUT_TYPE = "Layout Editing";

    public DesignPanel()  {
	objectHolder = ObjectHolder.getInstance();

        event = new EventPane();
	layout = new LayoutPane();

	designerLocation = new Point(200, 200);
	designerSize = new Dimension(300, 300);

	objectHolder.addPropertyChangeListener(this);

        // Design mode on by default to the layout pane
	setDesignerType(EVENT_TYPE);
    }

    /**
     * Sets the mode of the design panel
     * @param design Flag which indicates that the panel is in design mode.
     */
    public void setDesignMode(boolean design)  {
	if (currentFrame != null) {
	    currentFrame.setVisible(false);
	    JComponent contentPane = (JComponent)currentFrame.getContentPane();
	    if (design == false) {
		// Restore original values

		currentFrame.setLocation(rootLocation);
		currentFrame.setGlassPane(glassPane);

		designer.setVisible(false);
	    }
	    else {
		// Set the default pref size
		Dimension prefSize = contentPane.getPreferredSize();
		if (prefSize == null || prefSize.width < 2 || prefSize.height < 2) {
		    contentPane.setPreferredSize(designerSize);
		}

		currentFrame.setLocation(designerLocation);
		currentFrame.setGlassPane(designer);

		designer.setVisible(true);
	    }
	    currentFrame.pack();
	    currentFrame.setVisible(true);       
	    // XXX - hack to ensure that the handles are correctly updated
	    objectHolder.fireComponentResized(objectHolder.getRootContainer());
	}
    }

    /**
     * Changes the type of Designer on the GlassPane of the root object.
     */
    public void setDesignerType(String type) {
	if (sizeListener == null) {
	    sizeListener = new SizeListener(this);
	}

	if (designer != null) {
	    designer.removeComponentListener(sizeListener);
	}

	if (type.equals(EVENT_TYPE)) {
	    designer = event;
	}
	else if (type.equals(LAYOUT_TYPE)) {
	    designer = layout;
	}

	designer.addComponentListener(sizeListener);

	if (currentFrame != null) {
	    currentFrame.setGlassPane(designer);
	    currentFrame.pack();
	}
    }

    /**
     * This method sets the design time location of the frame.
     */
    public void setLocation(int x, int y) {
	designerLocation = new Point(x, y);
	if (currentFrame != null) {
	    currentFrame.setLocation(designerLocation);
	}
    }

    /**
     * Forwards the visibility flag to the designed frame
     */
    public void setVisible(boolean visible) {
	if (currentFrame != null) {
	    currentFrame.setVisible(visible);
	}
    }

    /**
     * Forwards the toFront call to the designed frame
     */
    public void toFront() {
	if (currentFrame != null) {
	    currentFrame.toFront();
	}
    }

    /**
     * Forwards the toBack call to the designed frame
     */
    public void toBack() {
	if (currentFrame != null) {
	    currentFrame.toBack();
	}
    }

    /**
     * This method sets the design time size of the frame.
     */
    public void setSize(int width, int height) {
	designerSize = new Dimension(width, height);

	if (currentFrame != null) {
	    JComponent contentPane = (JComponent)currentFrame.getContentPane();
	    contentPane.setPreferredSize(designerSize);
	    currentFrame.pack();
	    
	    // Notification to ComponentListeners.
	    objectHolder.fireComponentResized(contentPane);
	    Component[] comps = contentPane.getComponents();
	    for (int i = 0; i < comps.length; i++) {
		objectHolder.fireComponentResized(comps[i]);
	    }	    
	}
    }

    /**
     * Small private class to adjust the size of the content pane to be in
     * synch with the designer size.
     */
    private class SizeListener extends ComponentAdapter {

	private DesignPanel dp;

	public SizeListener(DesignPanel designPanel) {
	    this.dp = designPanel;
	}

	public void componentResized(ComponentEvent e) {
	    Dimension size = e.getComponent().getSize();
	    dp.setSize(size.width, size.height);
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

	if (source == ObjectHolder.getInstance()) {
	    if (prop.equals("root")) {
		handleRootChange(oldValue, newValue);
		objectHolder.fireComponentResized(objectHolder.getRootContainer());
	    } else if (prop.equals("designTime")) {
		setDesignMode(((Boolean)newValue).booleanValue());
	    }
	}
    }  // end propertyChange

    /**
     * Callback for the root property change by changing the top level design object
     * Handles a change in the root object by changing the designed object.
     *
     * If the root object is a JFrame, then the designed object will be the
     * actual JFrame. If the root object is a Component, then a new
     * JFrame will be created and the Component will be added to center of
     * the content pane.
     * <p>
     * If the root object is not a non-visual component then the proxyComponent
     * BeanInfo property is retrieved> If the proxyComponent is a JFrame then
     * it's used as the current frame otherwise, it will be wrapped in a new
     * frame.
     *
     * @param oldRoot the previous root object
     * @param newRoot the new root object
     */
    private void handleRootChange(Object oldRoot, Object newRoot) {
	if (oldRoot != null)  {
	    // Restore state of the old frame and dispose
	    currentFrame.setLocation(rootLocation);
	    currentFrame.setGlassPane(glassPane);

	    currentFrame.dispose();
	    currentFrame = null;
	}

	Component root = null;
	if (newRoot instanceof Component) {
	    root = (Component)newRoot;
	}
	else {
	    // non-visual root so get it's proxy Component
	    root = objectHolder.getProxyComponent(newRoot);
	}

	if (root != null) {
	    if (root instanceof JFrame) {
		currentFrame = (JFrame)root;
	    } else {
		currentFrame = new JFrame();
		currentFrame.getContentPane().add(root);
	    }
	}

	if (currentFrame == null) {
	    throw new IllegalArgumentException("Invalid Root in DesignPanel");
	}

	rootLocation = currentFrame.getLocation();
	glassPane = currentFrame.getGlassPane();

	currentFrame.setVisible(true);
    }
}

