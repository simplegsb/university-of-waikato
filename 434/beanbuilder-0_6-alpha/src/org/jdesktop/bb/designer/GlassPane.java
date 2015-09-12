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
import java.awt.Graphics;
import java.awt.Point;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jdesktop.bb.model.ObjectHolder;
import org.jdesktop.bb.palette.Palette;

/**
 * Base Design pane class which supports the basic instantiation, deleting, 
 * resizing and selection of components. This class 
 * also manages the current selection handles - which can be used to resize/move
 * the selected component.
 * 
 * Also manages the handles on a per component instance.
 * 
 * Also manages the set of Arc Objects. Arcs are used to represet relationships
 * between components. 
 *
 * @version 1.18 02/27/02
 * @author Mark Davidson
 */
public class GlassPane extends JLayeredPane implements PropertyChangeListener,
						       MouseListener {

    // Design repository
    protected ObjectHolder objectHolder;
    
    // Resizing/Moving handles (on current selected item)
    private Handle[] handles;

    // Handles mouse and key events for the instantiation, selection
    // and deletion of components
    protected InstantiationHandler instantiationHandler;

    // Table of component, Arc list association
    private HashMap arcTable;

    // Table of component, handles association.
    protected HashMap handleTable;

    // Vector of Arc Objects which represent connecting relationships between
    // Components.
    private Vector arcs;

    // Current Arc.
    private Arc selectedArc;

    // Popup for editing the arcs
    private JPopupMenu popup;

    public GlassPane() {

	objectHolder = ObjectHolder.getInstance();
	// listens to changes to the root object, selection
	objectHolder.addPropertyChangeListener(this);

	setOpaque(false);
	//	setLayout(null);

	instantiationHandler = new InstantiationHandler(this);

	addMouseListener(instantiationHandler);
	addMouseMotionListener(instantiationHandler);
	addKeyListener(instantiationHandler);

	// Register for selected item changes and clicking on arcs
	addMouseListener(this);
    }

    /**
     * Places the resize/move handles on the selected object. A selected 
     * object can only have handles if it's parent is the root content
     * pane or it represents a non-visual Object (Component proxy)
     * 
     * @param item Object to select. Assumed to be a component including
     *             a Copmonent proxy.
     */
    public void setSelectedItem(Object item)  {
        // Set the visual in the UI
        Component component = (Component)item;

	if (!objectHolder.isProxyComponent(component) && 
	    component.getParent() != objectHolder.getRootContainer()) {
	    // Hide the handles
	    if (handles != null) {
		for (int i = 0; i < handles.length; i++) {
		    handles[i].setVisible(false);
		    handles[i].setLocation(0,0);
		}
	    }
	    return;
	}

        if (handles == null)  {
            // Handles have not been created so create them and put them
            // on the glass pane layer.
            handles = new Handle[9];

            handles[Handle.NW_HANDLE] = new Handle(component, Handle.NW_HANDLE);
            handles[Handle.N_HANDLE] = new Handle(component, Handle.N_HANDLE);
            handles[Handle.NE_HANDLE] = new Handle(component, Handle.NE_HANDLE);
            handles[Handle.E_HANDLE] = new Handle(component, Handle.E_HANDLE);
            handles[Handle.SE_HANDLE] = new Handle(component, Handle.SE_HANDLE);
            handles[Handle.S_HANDLE] = new Handle(component, Handle.S_HANDLE);
            handles[Handle.SW_HANDLE] = new Handle(component, Handle.SW_HANDLE);
            handles[Handle.W_HANDLE] = new Handle(component, Handle.W_HANDLE);
            handles[Handle.C_HANDLE] = new Handle(component, Handle.C_HANDLE);

	    for (int i = 0; i < handles.length; i++) {
		add(handles[i], JLayeredPane.PALETTE_LAYER);
	    }
        }

	for (int i = 0; i < handles.length; i++) {
	    handles[i].setComponent(component);
	    handles[i].setVisible(true);
	}

	if (isShowing()) {
	    requestFocus();
	}
    }

    public Arc getSelectedArc() {
	return selectedArc;
    }

    public void setSelectedArc(Arc arc) {
	this.selectedArc = arc;
    }

    /**
     * Puts a set of handles around each component in the container
     */
    protected void installHandles(Container container) {
	Component[] components = container.getComponents();
	for (int i = 0; i < components.length; i++) {
	    addHandles(components[i]);
	}
    }


    /**
     * Removes the handles from the components within container.
     */
    protected void uninstallHandles(Container container) {
	Component[] components = container.getComponents();
	for (int i = 0; i < components.length; i++) {
	    removeHandles(components[i]);
	}
    }
	
    /**
     * Returns an array of handles for the given component.
     */
    protected Handle[] getHandles(Component comp) {
	return (Handle[])handleTable.get(comp);
    }

    /**
     * Return the Handle which represents the compass position.
     * 
     * @param comp Component to get the handle
     * @param position [ Handle.NORTH | Handle.SOUTH | Handle.EAST | Handle.WEST ]
     */
    protected Handle getHandle(Component comp, int position) {	
	Handle[] handles = getHandles(comp);
	Handle handle = null;
	
	switch(position) {
	case Handle.NORTH:
	    handle = handles[0];
	    break;
	case Handle.SOUTH:
	    handle = handles[1];
	    break;
	case Handle.EAST:
	    handle = handles[2];
	    break;
	case Handle.WEST:
	    handle = handles[3];
	    break;
	}
	return handle;
    }

    /**
     * Adds handles to the current component. The handles added for each component.
     * This is not the same as adding selection handles for the current selected
     * item.
     *
     * @param comp Component to add handles.
     */
    protected void addHandles(Component comp) {}

    /**
     * Removes the handles for a component from the layout pane.
     */
    protected void removeHandles(Component comp) {}

    /**
     * Adds the visual representation of the connecting arc to the designer.
     */
    protected void addArc(Component comp, Arc arc) {
	// Associate the new Arc with the source component
	if (arcTable == null) {
	    arcTable = new HashMap();
	}
	
	List linkedArcs = (List)arcTable.get(comp);
	if (linkedArcs == null) {
	    linkedArcs = new ArrayList();
	    arcTable.put(comp, linkedArcs);
	}
	linkedArcs.add(arc);

	if (arcs == null) {
	    arcs = new Vector();
	}
	arcs.addElement(arc);
    }

    /**
     * Remove the Arc for the component
     *
     * @param comp The component for which the arc has been associated
     * @param arc The Arc to remove.
     */
    protected void removeArc(Component comp, Arc arc) {
	// No checking the data structures for null in this method. 
	// if this was called before addArc then the developer deserves the NPE.

	List linkedArcs = (List)arcTable.get(comp);
	if (linkedArcs == null) {
	    return;
	}
	linkedArcs.remove(arc);
	arcs.remove(arc);
    }

    /**
     * Remove all the arcs associated with this component
     */
    protected void removeArcs(Component comp) {
	if (arcTable == null) {
	    return;
	}

	List linkedArcs = (List)arcTable.get(comp);
	if (linkedArcs != null && arcs != null) {
	    Iterator it = linkedArcs.iterator();
	    while (it.hasNext()) {
		arcs.remove((Arc)it.next());
	    }
	    arcTable.remove(comp);
	}
    }

    /**
     * Removes all the Arcs in the designer
     */
    protected void removeArcs() {
	if (arcTable != null) {
	    arcTable.clear();
	}

	if (arcs != null) {
	    arcs.removeAllElements();
	}
    }


    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Point startPt = instantiationHandler.getStartPt();
	Point endPt = instantiationHandler.getEndPt();

	if (Palette.getInstance().isItemSelected() && 
	    startPt != null && endPt != null)  {
	    // Draw an instantiation rect
	    g.drawRect(startPt.x, startPt.y,
		       endPt.x - startPt.x, endPt.y - startPt.y);
	}
	drawArcs(g);
    }


    /**
     * Redraws all the edge connectors in the layout
     */
    private void drawArcs(Graphics g) {
	if (arcs != null) {
	    Iterator it = arcs.iterator();
	    Arc arc;
	    while (it.hasNext()) {
		arc = (Arc)it.next();
		// XXX - for clipping if (arc.getDrawArea().intersects(cr)) {
		arc.draw(g);
		//}
	    }
	}
    }

    //
    // MouseListener methods
    //

    /**
     * Select the component under the mouse cursor
     */
    public void mouseClicked(MouseEvent evt)  {
	Object obj = getComponentAt(evt.getPoint());
	if (obj == null || obj == this)  {
	    obj = objectHolder.getRootContainer().getComponentAt(evt.getPoint());
	}

	if (obj != null && !(obj instanceof Handle)) {
	    objectHolder.setSelectedItem(obj);
	}
    }

    public void mouseEntered(MouseEvent evt)  {}
    public void mouseExited(MouseEvent evt)  {}

    /**
     * Handles popup events on Arcs, other components.
     */
    public void mousePressed(MouseEvent evt)  {
	if (evt.isPopupTrigger()) {
	    handlePopupEvent(evt);
	}
    }
    
    public void mouseReleased(MouseEvent evt)  {
        if (evt.isPopupTrigger()) {
	    handlePopupEvent(evt);
        }
    }
    
    private void handlePopupEvent(MouseEvent evt) {
	if (arcs == null) {
	    return;
	}
	Iterator it = arcs.iterator();
	Arc arc;
	while (it.hasNext()) {
	    arc = (Arc)it.next();
	    if (arc.isOnArc(evt.getPoint())) {
		handleArcPopup(evt.getPoint(), arc);
		break;
	    }
	}
    }
    
    /**
     * Handles the popup gesture on the Arc. Overloaded methods can popup a menu
     * to allow for aditional selections.
     */
    protected void handleArcPopup(Point pt, Arc arc) {
	setSelectedArc(arc);

	if (popup == null) {
	    popup = new ArcPopup(this);
	}
	popup.show(this, pt.x, pt.y);
    }

    private class EditAction extends AbstractAction {
	private GlassPane pane;
		
	public EditAction(GlassPane pane) {
	    super("Edit...");
	    this.pane = pane;
	}

	public void actionPerformed(ActionEvent evt) {
	    pane.editArc(pane.getSelectedArc());
	}
    }

    private class DeleteAction extends AbstractAction {
	private GlassPane pane;
		
	public DeleteAction(GlassPane pane) {
	    super("Delete...");
	    this.pane = pane;
	}

	public void actionPerformed(ActionEvent evt) {
	    pane.deleteArc(pane.getSelectedArc());
	}
    }	
	

    protected class ArcPopup extends JPopupMenu {
	public ArcPopup(GlassPane pane) {
	    // XXX - force heavyweight popups. Still a bug in 1.4
	    setLightWeightPopupEnabled(false);
	    add(new EditAction(pane));
	    add(new DeleteAction(pane));
	}

    } // end class ArcPopup


    /**
     * Edit the arc. Perhaps this shoud be an abstract method?
     */
    protected void editArc(Arc arc) {	
    }

    protected void deleteArc(Arc arc) {
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
		setSelectedItem(newValue);
	    }
	    else if (prop.equals("root")) {
		if (oldValue != null && handles != null) {
		    // Hide the resize/move handles. 
		    for (int i = 0; i < handles.length; i++) {
			handles[i].setVisible(false);
		    }
		}
	    }
	} // source == holder
    }
}

