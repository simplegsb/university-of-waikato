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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.jdesktop.bb.model.ObjectHolder;
import org.jdesktop.bb.palette.Palette;

import org.jdesktop.bb.util.BeanInfoFactory;
import org.jdesktop.bb.util.StatusBar;

/**
 * MouseHandler which knows how to interpret mouse events into 
 * instatiation of Palette items.
 *
 * @author Mark Davidson
 */
public class InstantiationHandler implements MouseListener, MouseMotionListener,
					     KeyListener {
    public static final int GRID_SIZE = 10;

    // Default location of a new non-visual bean.
    private Point newPoint = new Point();

    private Point startPt;
    private Point endPt;

    // Design repository.
    private ObjectHolder objectHolder;

    private final int MIN_WIDTH = 10;
    private final int MIN_HEIGHT = 10;

    private Container parent;

    public InstantiationHandler(Container cont) {
	parent = cont;
	objectHolder = ObjectHolder.getInstance();
    }

    public Point getStartPt() {
	return startPt;
    }

    public Point getEndPt() {
	return endPt;
    }

    //
    // KeyListener methods
    //

    public void keyTyped(KeyEvent evt) {}
    public void keyPressed(KeyEvent evt) {}

    /**
     * Deletes the selected bean.
     */
    public void keyReleased(KeyEvent evt) {
	if (evt.getKeyCode() == KeyEvent.VK_DELETE)  {
	    // Remove the object
	    doRemoveBean(objectHolder.getSelectedItem());
	}
    }

    //
    // MouseListener methods
    //

    public void mouseClicked(MouseEvent evt)  {}

    public void mouseEntered(MouseEvent evt)  {
	if (Palette.getInstance().isItemSelected()) {
	    // If a palette item is selected change the cursor for the root
	    // Frame.
	    Component root = SwingUtilities.getRoot(parent);
	    root.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
    }
    public void mouseExited(MouseEvent evt)  {}

    /**
     * Start a gesture
     */
    public void mousePressed(MouseEvent evt)  {
	startPt = evt.getPoint();
    }
    
    /**
     * End of the gesture.
     */
    public void mouseReleased(MouseEvent evt)  {
	// Add the palette bean to the design.
	doAddBean(evt.getPoint());

	// Reset the values
	startPt = null;
	endPt = null;

	parent.repaint();
    }

    //
    // MouseMotionListener methods
    //

    public void mouseDragged(MouseEvent evt)  {
	// Instantiate 
	endPt = evt.getPoint();
	parent.repaint();
    }

    public void mouseMoved(MouseEvent evt)  {}

    /**
     * Remove the bean from the object repository.
     *
     * @param the actual bean to remove. Should not be a proxy component
     */
    private void doRemoveBean(Object bean) {
	Container cont = objectHolder.getRootContainer();
	if (bean == null || bean == cont) {
	    return;
	}
	
	if (!objectHolder.isComponent(bean)) {
	    // Special case of removing BeanWrappers (which are not in the
	    // containment hierarchy
	    Component comp = objectHolder.getProxyComponent(bean);
	    if (comp != null && comp instanceof BeanWrapper) {
		objectHolder.remove(comp.getParent(), comp);
		objectHolder.unregisterProxy(bean, comp);
		objectHolder.setSelectedItem(cont);
		return;
	    }
	}
	objectHolder.remove(bean);
    }

    /**
     * Add the selected Palette item to the design.
     */
    private void doAddBean(Point mousePt)  {
	// Instantiate a new component from the palette
	Object bean = Palette.getInstance().getNewPaletteBean();
	if (bean == null)  {
	    return;
	}
	
	if (bean instanceof Component)  {
	    // Set the location
	    if (startPt == null)  {
		startPt = mousePt;
	    }
	    doAddComponent((Component)bean, startPt, endPt);
	} else {
	    doAddObject(bean, startPt, endPt);
	}

	// Reset the cursor
	Component root = SwingUtilities.getRoot(parent);
	root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


    /**
     * Sets the location and size of the Component and adds it to the 
     * the design repository. The Component will be added to a 
     * Container in the design if that was indicated.
     */
    private void doAddComponent(Component comp, Point startPt, Point endPt)  {
	setComponentBounds(comp, startPt, endPt);

	// Add the component to the design repository
	Container container = getDropContainer(startPt);
	if (container != null) {
	    objectHolder.add(container, comp);
	    objectHolder.setSelectedItem(comp);
	} else {
	    objectHolder.add(comp);
	}
    }

    /**
     * Adds a bean to the design repository. If there are proxy
     * Containers and Components, representing the root object
     * and the current object then the proxy Component will be
     * added to the proxy Container.
     */
    private void doAddObject(Object obj, Point startPt, Point endPt) {
	// Add the object to design repository.
	objectHolder.add(obj);
	
	Component comp = objectHolder.getProxyComponent(obj);
	Container cont = objectHolder.getRootContainer();
	if (comp != null && cont != null 
	    && !(comp instanceof BeanWrapper)) {
	    setComponentBounds(comp, startPt, endPt);
	    objectHolder.add(cont, comp);
	    //objectHolder.setSelectedItem(obj);
	}
    }

    /**
     * Helper method to set the location and the bounds of the component
     * If the end point is non null then the preferred size will be set
     * to the rectangle defined by the start and end points.
     */
    private void setComponentBounds(Component comp, Point startPt, Point endPt)  {
	comp.setLocation(startPt);

	if (endPt != null && ! endPt.equals(startPt))  {
	    // The two points define a rect which is the pref size
	    Dimension size = new Dimension(endPt.x - startPt.x, endPt.y - startPt.y);
	    Dimension minSize = comp.getMinimumSize();
	    size.width = Math.max(size.width, minSize.width);
	    size.height = Math.max(size.height, minSize.height);
	
	    if (comp instanceof JComponent) {
		((JComponent)comp).setPreferredSize(size);
	    }
	    else {
		comp.setSize(size.width, size.height);
	    }
	}
    }

    /**
     * Returns a container delegate for the swing component under the mouse point.
     * <p>
     * This method will return the actual Container delegate that a
     * Component should be added. The containerDelegate is retrieved
     * through the BeanInfo BeanDescriptor attributes.
     *
     * @return Container container delegate for which components are added
     */
    private Container getDropContainer(Point point)  {
	Container container = null;
	
	// Find the Component on the the root container
	Component comp = objectHolder.getRootContainer().getComponentAt(point);
	if (comp != null && BeanInfoFactory.isContainer(comp))  {
	    // Get the Container delegate. i.e, if the container
	    // that was under the point is a JScrollPane then 
	    // retrieve the JViewport.
	    container = BeanInfoFactory.getContainerDelegate(comp);
	}

	if (container == null && comp instanceof Container) {
	    container = (Container)comp;
	}

	return container;
    }
}
