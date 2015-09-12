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

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.jdesktop.bb.model.ObjectHolder;

import org.jdesktop.bb.util.BeanInfoFactory;

/**
 * MouseHandler for the EventPane knows how to interpret mouse movements
 * into event source and targets for creating a java.beans.EventHandler.
 *
 * @author Mark Davidson 
 */
public class EventLinkHandler implements MouseListener, MouseMotionListener {
    
    private EventPane parent;

    private Point startPt;
    private Point endPt;

    private EventHandle source;
    private EventHandle target;

    public EventLinkHandler(EventPane cont) {
	parent = cont;
    }

    public Point getStartPt() {
	return startPt;
    }

    public Point getEndPt() {
	return endPt;
    }

    public EventHandle getSourceHandle() {
	return source;
    }

    public EventHandle getTargetHandle() {
	return target;
    }

    public void mouseClicked(MouseEvent evt) {}
    public void mouseEntered(MouseEvent evt)  {}
    public void mouseExited(MouseEvent evt)  {}

    /**
     * Start a gesture
     */
    public void mousePressed(MouseEvent evt)  {
	// Translate point to parent co-ords.
	// XXX - if the source represents a SwingContainer, it should
	// popup a target selection.
	source = (EventHandle)evt.getSource();
	startPt = SwingUtilities.convertPoint(source, evt.getPoint(), parent);
    }

    /**
     * End of the gesture. Will create an Event hookup if the both the
     * target and source are validxs
     */
    public void mouseReleased(MouseEvent evt)  {
	// Linking edges
	if (source != null && target != null && source != target) {
	    // XXX If the target represents a swing container
	    // TODO: Figure out how to select a source object within
	    // a containment hierarchy.
	    if (BeanInfoFactory.isContainer(target.getComponent())) {
		// If the target is a Swing container, present a popup 
		// menu which allows for the selection of components within it.
		Point newPt = SwingUtilities.convertPoint(source, 
							  evt.getPoint(), 
							  SwingUtilities.getRoot(parent));
		ObjectHolder objectHolder = ObjectHolder.getInstance();

		Object srcObj = objectHolder.getProxyObject(source.getComponent());
		if (srcObj == null) {
		    srcObj = source.getComponent();
		}
		Object trgObj = target.getComponent();

		TargetSelectionPopup popup = new TargetSelectionPopup(srcObj, trgObj);
		popup.show(SwingUtilities.getRoot(parent), newPt.x, newPt.y);
	    } else {
		parent.doEventHookup(source, target);
	    }
	}
	// Reset the values
	source = null;
	target = null;

	startPt = null;
	endPt = null;

	parent.repaint();
    }

    //
    // MouseMotionListener methods
    //

    public void mouseDragged(MouseEvent evt)  {
	// Translate point to LayoutPane co-ords.
	endPt = SwingUtilities.convertPoint((Component)evt.getSource(), 
					    evt.getPoint(), parent);
	Component comp = parent.getComponentAt(endPt);
	if (comp instanceof EventHandle) {
	    target = (EventHandle)comp;
	} else {
	    target = null;
	}
	parent.repaint();
    }

    public void mouseMoved(MouseEvent evt)  {}

    /**
     * A popup menu which presents a list of components in a containment hierarchy 
     * so that a selection can be made.
     */
    private class TargetSelectionPopup extends JPopupMenu implements ActionListener {
	private Object target;
	private Object source;
	private HashMap components;

	public TargetSelectionPopup(Object source, Object target) {
	    this.target = target;
	    this.source = source;
	    this.components = new HashMap();

	    initUI();
	}

	private void initUI() {
	    // Populate the popup menu will a list of nested components.
	    addItem(target);
	    Container delegate = BeanInfoFactory.getContainerDelegate(target);
	    if (delegate != null) {
		addChildren(delegate);
	    }
	}

	private void addChildren(Container cont) {
	    addItem(cont);
	    Component[] components = cont.getComponents();
	    for (int i = 0; i < components.length; i++) {
		if (components[i] instanceof Container) {
		    addChildren((Container)components[i]);
		}
		else {
		    addItem(components[i]);
		}
	    }
	}

	private void addItem(Object item) {
	    String className = item.getClass().getName();
	    components.put(className, item);

	    JMenuItem menuItem = add(className);
	    menuItem.addActionListener(this);
	}

	// Selection callback when one of the menu items are called.
	public void actionPerformed(ActionEvent evt) {
	    JMenuItem menuItem = (JMenuItem)evt.getSource();
	    String compName = menuItem.getText();

	    parent.doEventHookup(source, components.get(compName));
	}
    }

}

