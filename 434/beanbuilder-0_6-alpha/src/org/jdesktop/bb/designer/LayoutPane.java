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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.jdesktop.bb.model.ObjectHolder;
import org.jdesktop.bb.util.BeanInfoFactory;
import org.jdesktop.bb.util.SpringLayoutPersistenceDelegate;

/**
 * A panel which is responsible for manipulating the layout of the
 * root panel.
 *
 * @version 1.30 02/27/02
 * @author Mark Davidson
 */
public class LayoutPane extends GlassPane implements ContainerListener,
    ComponentListener {

    // Reference to the LayoutManager
    private SpringLayout layoutManager;

    // Handles mouse events in the LayoutHandle to create a constraint
    private EdgeLinkHandler linkHandler;

    public LayoutPane() {
	objectHolder.addContainerListener(this);

	linkHandler = new EdgeLinkHandler(this);

        setBorder(BorderFactory.createEtchedBorder(Color.red, Color.white));
    }

    /**
     * Overridden since this view doesn't allow the selection of non-visual
     * components.
     */
    public void setSelectedItem(Object item) {
	if (!(item instanceof Component)) {
	    return;
	}
	super.setSelectedItem(item);
    }


    /**
     * Overloaded to install handles on the Container as well as all the component
     * on the container.
     */
    protected void installHandles(Container container) {
	addHandles(container);
	super.installHandles(container);
    }

    /**
     * Overloaded to remove the handles from the container.
     */
    protected void uninstallHandles(Container container) {
	removeHandles(container);
	super.uninstallHandles(container);
    }

    /**
     * Adds LayoutHandles to the compoent only if it's the root
     * content pane or a child of the root content pane.
     *
     * @param comp Component to add handles.
     */
    protected void addHandles(Component comp) {
	// Shoud handles be added?
	Container container = objectHolder.getRootContainer();
	if (comp != container && comp.getParent() != container) {
	    return;
	}
	objectHolder.addComponentListener(comp, this);

	LayoutHandle[] handles = new LayoutHandle[4];
	handles[0] = new LayoutHandle(comp, Handle.NORTH);
	handles[1] = new LayoutHandle(comp, Handle.SOUTH);
	handles[2] = new LayoutHandle(comp, Handle.EAST);
	handles[3] = new LayoutHandle(comp, Handle.WEST);

	// Add the handles to the panel.
	for (int i = 0; i < handles.length; i++) {
	    add(handles[i]);
	    handles[i].addMouseListener(linkHandler);
	    handles[i].addMouseMotionListener(linkHandler);
	}

	if (handleTable == null) {
	    handleTable = new HashMap();
	}
	handleTable.put(comp, handles);
    }


    /**
     * Removes the handles for a component from the layout pane.
     */
    protected void removeHandles(Component comp) {
	Handle[] handles = (Handle[])handleTable.remove(comp);
	if (handles != null) {
	    // Remove the handles from the pane.
	    for (int i = 0; i < handles.length; i++) {
		remove(handles[i]);
		handles[i].removeMouseListener(linkHandler);
		handles[i].removeMouseMotionListener(linkHandler);
		handles[i].setComponent(null);
	    }
	}
    }

    /**
     * Edit the Layout arc as a result of a gesture
     * @param arc to be edited. Should be in the design already
     */
    protected void editArc(Arc arc) {
	LayoutHandle source = (LayoutHandle)arc.getSource();
	LayoutHandle destination = (LayoutHandle)arc.getDestination();

	Component comp = source.getComponent();
	String edge = source.getConstraint();

	Spring spring = layoutManager.getConstraint(edge, comp);
	// Edit the spring
	/*
	System.out.println("\nSource Component: " + comp.getClass().getName()
			   + " edge: " + edge);
	System.out.print("Spring size min value: " + spring.getMinimumValue());
	System.out.print(" pref value: " + spring.getPreferredValue());
	System.out.println(" max value: " + spring.getMaximumValue()); */

	//dumpConstraints();

	/*
	layoutManager.putConstraint(edge, comp, spring,
				    destination.getConstraint(),
				    destination.getComponent()); */
    }



    /**
     * Deletes the Arc from the Panel and deletes the SpringLayout constraint
     * that it represents from the SpringLayout LayoutManager.
     * @param arc Arc to delete.
     */
    protected void deleteArc(Arc arc) {
	LayoutHandle source = (LayoutHandle)arc.getSource();

	Component comp = source.getComponent();
	String edge = source.getConstraint();

	SpringLayout.Constraints constraints = layoutManager.getConstraints(comp);
	constraints.setConstraint(edge, null);
	// Must reset the position of the arc.
	constraints.setX(Spring.constant(comp.getX()));
	constraints.setY(Spring.constant(comp.getY()));

	// XXX -
	System.out.println("*** getConstraints(" + comp.getClass().getName() +
			   ").setConstraint(" + edge + ",null)");
	//	dumpConstraints();


	// Resubmit the updated constraints to the layout manager
	// XXX - not sure if this is still required.
	layoutManager.addLayoutComponent(comp, constraints);

	updateConnectors();
    }


    /**
     * Adds the constraint which spans the two handles to the layout manager,
     * and create the visual representation of the edge.
     */
    public void addConstraint(LayoutHandle source, LayoutHandle target, int delta) {
	// Add the constraint to the layout manager.
	String sCons = source.getConstraint();
	String tCons = target.getConstraint();

	Component sComp = source.getComponent();
	Component tComp = target.getComponent();

	// Reject constraint arcs which go to the same component
	if (sComp == tComp) {
	    return;
	}

	// Only edges added along an axis is supported.
	if (sCons.equals(SpringLayout.NORTH) || sCons.equals(SpringLayout.SOUTH)) {
	    if (tCons.equals(SpringLayout.EAST) || tCons.equals(SpringLayout.WEST)) {
		return;
	    }
	} else {
	    if (tCons.equals(SpringLayout.NORTH) || tCons.equals(SpringLayout.SOUTH)) {
		return;
	    }
	}

	// Add the constraint to the layout manager.
	layoutManager.putConstraint(sCons, sComp, delta,
				    tCons, tComp);

	//System.out.println("*** putConstaints(" + sCons + "," + sComp.getClass().getName() +
	//	   "," + delta + "," + tCons + "," + tComp.getClass().getName() + ")");
	//dumpConstraints();

	// Visual representation of the constraint
	addArc(sComp, new Arc(source, target));

	updateConnectors();
    }

    /**
     * This method will install a SpringLayout if the container
     * has a layout manager. If the layout manager is a
     * SpringLayout then the arcs will be restored.
     */
    private void installLayoutManager(Container container) {
	LayoutManager lm = container.getLayout();
	if (lm == null) {
	    return;
	}

	if (!(lm instanceof SpringLayout)) {
	    // Set the layout manager to use the spring layout manager
	    layoutManager = new SpringLayout();
	    container.setLayout(layoutManager);
	}
	else if (lm instanceof SpringLayout) {
	    layoutManager = (SpringLayout)lm;

	    updateConnectors(container);
	}
    }

    /**
     * Adds the all edge connectors which represent SpringLayout constraints
     * for the Component.
     *
     * @param comp Component to add constraints. Can also be the root container
     */
    private void addEdgeConnectors(Component comp) {
	SpringLayout.Constraints constraints;
	Spring spring;

	constraints = layoutManager.getConstraints(comp);
	// North
	spring = constraints.getConstraint(SpringLayout.NORTH);
	if (spring != null) {
	    addEdgeConnector(layoutManager, comp, SpringLayout.NORTH);
	}

	// South
	spring = constraints.getConstraint(SpringLayout.SOUTH);
	if (spring != null) {
	    addEdgeConnector(layoutManager, comp, SpringLayout.SOUTH);
	}

	// East
	spring = constraints.getConstraint(SpringLayout.EAST);
	if (spring != null) {
	    addEdgeConnector(layoutManager, comp, SpringLayout.EAST);
	}

	// West
	spring = constraints.getConstraint(SpringLayout.WEST);
	if (spring != null) {
	    addEdgeConnector(layoutManager, comp, SpringLayout.WEST);
	}
    }


    /**
     * Adds the visual representation of an edge connector from
     * the SpringLayoutManager.
     */
    private void addEdgeConnector(SpringLayout lm, Component comp, String edgeName) {
	// Visual representation of the constraint
	Object[] elems = SpringLayoutPersistenceDelegate.getEdge(lm, comp, edgeName);
	if (elems != null) {
	    Handle source = getHandle(comp,
		   LayoutHandle.getHandleTypeFromSpringEdge(edgeName));
	    Handle target = getHandle((Component)elems[2],
		   LayoutHandle.getHandleTypeFromSpringEdge((String)elems[1]));
	    if (source != null && target != null) {
		addArc(comp, new Arc(source, target));
	    }
	}
    }

    private void uninstallLayoutManager(Container container) {
	layoutManager = null;
	removeArcs();
    }


    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	// Paint the unfinished link line
	LayoutHandle source = linkHandler.getSourceHandle();
	LayoutHandle target = linkHandler.getTargetHandle();
	if (source != null) {
	    if (target != null && target != source) {
		Arc.drawConnector(g, source, target, Arc.RED_CLR);
	    }
	    else {
		Point startPt = linkHandler.getStartPt();
		Point endPt = linkHandler.getEndPt();
		if (startPt != null && endPt != null) {
		    Arc.drawConnector(g, source.getHandleType(), -1,
				      startPt, endPt, Arc.RED_CLR);
		}
	    }
	}
    }

    //
    // ComponentListener events
    //

    public void componentHidden(ComponentEvent evt) {}
    public void componentShown(ComponentEvent evt) {}

    public void componentMoved(ComponentEvent evt) {
	Component comp = evt.getComponent();
	Container cont = comp.getParent();

	// Do not add layout for nested components.
	if (cont == objectHolder.getRootContainer()) {
	    setLocationConstraints(comp);

	    validate();
	    repaint();
	}
    }

    public void componentResized(ComponentEvent evt) {
	evt.getComponent().getParent().validate();
	validate();
    }

    //
    // ContainerListener methods
    //

    public void componentAdded(ContainerEvent evt) {
	Component comp = evt.getChild();
	Container cont = evt.getContainer();

	// Do not add layout for nested components.
	if (cont == objectHolder.getRootContainer()) {
	    setLocationConstraints(comp);
	    addHandles(comp);

	    validate();
	    repaint();
	}
    }


    private void setLocationConstraints(Component comp) {
	SpringLayout.Constraints cons = layoutManager.getConstraints(comp);

	cons.setX(Spring.constant(comp.getX()));
	cons.setY(Spring.constant(comp.getY()));

	//dumpConstraints();

	updateConnectors();
    }

    private void updateConnectors() {
	updateConnectors(objectHolder.getRootContainer());
    }

    /**
     * Updates the visual connectors as a result of the changes in the constraints.
     */
    private void updateConnectors(Container container) {
	removeArcs();

	// Display the edges.
	addEdgeConnectors(container);
	Component[] components = container.getComponents();
	for (int i = 0; i < components.length; i++) {
	    addEdgeConnectors(components[i]);
	}
	container.validate();
	container.repaint();
    }


    // for debugging
    private void dumpConstraints() {
	System.out.println("\n******** Dump Constraints ********");
	Container cont = objectHolder.getRootContainer();
	
	//  printConstraints(cont);
	Component[] comps = cont.getComponents();
	for (int i = 0; i < comps.length; i++) {
	    printConstraints(comps[i]);
	}
    }

    private void printConstraints(Component comp) {
	System.out.println("\nConstraints for " + comp.getClass().getName());

	SpringLayout.Constraints cons = layoutManager.getConstraints(comp);
	Spring x = cons.getX();
	Spring y = cons.getY();
	Spring width = cons.getWidth();
	Spring height = cons.getHeight();

	Spring north = cons.getConstraint(SpringLayout.NORTH);
	Spring south = cons.getConstraint(SpringLayout.SOUTH);
	Spring east = cons.getConstraint(SpringLayout.EAST);
	Spring west = cons.getConstraint(SpringLayout.WEST);

	System.out.println("X = " + x);
	System.out.println("Y = " + y);
	System.out.println("Width = " + width);
	System.out.println("Height = " + height);
	System.out.println("\nWest = " + west);
	System.out.println("North = " + north);
	System.out.println("East = " + east);
	System.out.println("South = " + south);
    }


    public void componentRemoved(ContainerEvent evt) {
	Component comp = evt.getChild();
	objectHolder.removeComponentListener(comp, this);

	removeHandles(comp);
	removeArcs(comp);
	repaint();
    }

    //
    // PropertyChangeListener method
    //

    public void propertyChange(PropertyChangeEvent evt) {
	super.propertyChange(evt);

        Object source = evt.getSource();
        String prop = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();

	if (source == objectHolder) {
	    if (prop.equals("rootContainer")) {
		// XXX - same algoritm in EventPane. Should refactor.
		if (oldValue != null) {
		    Container cont = (Container)oldValue;
		    uninstallHandles(cont);
		    uninstallLayoutManager(cont);
		}
		if (newValue != null) {
		    Container cont = (Container)newValue;
		    installHandles(cont);
		    installLayoutManager(cont);
		}
	    }
	}
    }
}
