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
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.EventHandler;
import java.beans.Introspector;
import java.beans.EventHandler;
import java.beans.MethodDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.jdesktop.bb.palette.Palette;
import org.jdesktop.bb.util.BeanInfoFactory;
import org.jdesktop.bb.util.StatusBar;

/**
 * A pane which is on top of the design panel which intercepts mouse events.
 * The pane allows for the instantiation of Components from the palette
 * and it also allows for the selection of source and target objects for
 * event interactions.
 *
 * @version 1.40 02/27/02
 * @author Mark Davidson
 */
public class EventPane extends GlassPane implements ContainerListener {
    public static final int GRID_SIZE = 10;

    // Default location of a new non-visual bean.
    private Point newPoint = new Point();

    // Shared instance of the InteractionWizard
    private InteractionWizard wizard; 

    private EventLinkHandler linkHandler;

    public EventPane()  {
	objectHolder.addContainerListener(this);
 
	linkHandler = new EventLinkHandler(this);

        setBorder(BorderFactory.createEtchedBorder(Color.white, Color.blue));
    }
    
    /**
     * Adds EventHandles to the compoent only if it's the root
     * content pane or a child of the root content pane.
     *
     * @param comp Component to add handles.
     */
    protected void addHandles(Component comp) {
	// Shoud handles be added? Only if this component is
	// a direct child of the root container or a proxy
	// component for a non-visual bean.
	Container container = objectHolder.getRootContainer();
	if (comp != container && comp.getParent() != container
	    && !objectHolder.isProxyComponent(comp)) {
	    return;
	}

	EventHandle[] handles = new EventHandle[4];
	handles[0] = new EventHandle(comp, Handle.NORTH); 
	handles[1] = new EventHandle(comp, Handle.SOUTH); 
	handles[2] = new EventHandle(comp, Handle.EAST); 
	handles[3] = new EventHandle(comp, Handle.WEST);
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

    protected void removeHandles(Component comp) {
	if (handleTable == null) {
	    return;
	}

	Handle[] handles = (Handle[])handleTable.remove(comp);
	if (handles != null) {
	    // Remove the handles from the pane.
	    for (int i = 0; i < handles.length; i++) {
		remove(handles[i]);
		handles[i].removeMouseListener(linkHandler);
		handles[i].removeMouseMotionListener(linkHandler);
	    }
	}
    }

    /**
     * Call back method for editing the arc
     */
    protected void editArc(Arc arc) {
	if (arc instanceof PropertyArc) {
	    // no editing of PropertyArcs (yet)
	    return;
	}
	Component sComp = arc.getSource().getComponent();
	Object source = objectHolder.getProxyObject(sComp);
	if (source == null) {
	    source = sComp;
	}

	Object proxy = ((EventArc)arc).getProxy();
	EventHandler handler = null;
	
	try {
	    handler = (EventHandler)Proxy.getInvocationHandler(proxy);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return;
	}
	Object target = handler.getTarget();

	// XXX - the interface to EventHandler makes it really difficult to
	// get reasonable information out of it since it uses a stupid . notation
	// and works with Strings instead of methods or method descriptors.
	EventSetDescriptor eventSet = findEventSet(source, proxy);

	System.out.println("\nSource: " + source.getClass().getName());
	System.out.println("Target: " + target.getClass().getName());
	System.out.println("Action: " + target.getClass().getName() + "." 
			   + handler.getAction());
	System.out.println("Listener type: " 
			   + eventSet.getListenerType().getName());
	System.out.println("handler.getListenerMethodName() = " 
			   + handler.getListenerMethodName());	
	System.out.println("handler.getEventPropertyName() = " 
			   + handler.getEventPropertyName());
	// This is weak but good enough for a demo.
	// Create a new event and delete the old one if it fails.
	if (doEventHookup(source, target) != null) {
	    deleteArc(arc);
	}
    }

    /**
     * This method is pretty awesome. It actually removed deletes the EventListener
     * that was added to the source component in the arc.
     */
    protected void deleteArc(Arc arc) {
	if (arc instanceof PropertyArc) {
	    // no editing of PropertyArcs (yet)
	    return;
	}
	Component sComp = arc.getSource().getComponent();
	Object source = objectHolder.getProxyObject(sComp);
	if (source == null) {
	    source = sComp;
	}
	Object proxy = ((EventArc)arc).getProxy();

	EventSetDescriptor eventSet = findEventSet(source, proxy);
	if (eventSet != null) {
	    Method removeMethod = eventSet.getRemoveListenerMethod();

	    // If we have a valid proxyListener here then we found the listener
	    // Invoke the remove listener method on the component
	    try {
		removeMethod.invoke(source, new Object[] { proxy });
	    
		// Internally remove the Arc and repaint
		removeArc(sComp, arc);
		repaint();
	    } catch (Exception ex) {
		// Shouldn't reach here.
		ex.printStackTrace();
	    }
	}
    }
    
    /**
     * Helper method that gets the EventSetDescriptor for the component
     * which corresponds to the Component
     */
    private EventSetDescriptor findEventSet(Object comp, Object proxy) {
	BeanInfo info = BeanInfoFactory.getBeanInfo(comp.getClass());
	EventSetDescriptor[] eventSets = info.getEventSetDescriptors();
	EventListener[] listeners = null;

	// Seach for the listener and retrieve the proper remove
	// method to remove the listener from the Component.
	for (int j = 0; j < eventSets.length; j++) {
	    Class listenerType = eventSets[j].getListenerType();
	    
	    // The EventSetDescriptor.getGetListenerMethod is new in 1.4
	    // and is part of the get<Foo>Listeners BeansSpec ammnendment.
	    // This method can be called directly when 1.3 support is dropped.
	    Object result = BeanInfoFactory.executeMethod("getGetListenerMethod",
							  eventSets[j]);
	    if (result == null) {
		// Components have a  getListeners method.
		result = BeanInfoFactory.executeMethod("getListeners", 
							      comp, listenerType);
	    }
	    if (result != null && result.getClass().isArray()) {
		listeners = (EventListener[])result;
	    }
	    //listeners = comp.getListeners(listenerType);
	    for (int k = 0; k < listeners.length; k++) {
		if (Proxy.isProxyClass(listeners[k].getClass())) {
		    // This listener is the dynamically generated proxy.
		    if (listeners[k] == proxy) {
			return eventSets[j];
		    }
		}
	    }
	}
	return null;
    }
	

    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	// Paint the unfinished link line
	EventHandle source = linkHandler.getSourceHandle();
	EventHandle target = linkHandler.getTargetHandle();
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

    /**
     * Select the component under the mouse cursor
     * This has been overloaded to manange the case in which
     * the a visual component is used for a non-visual stand in.
     */
    public void mouseClicked(MouseEvent evt)  {
	Component comp = (Component)evt.getSource();
	Object bean = objectHolder.getProxyObject(comp);
	if (bean != null) {
	    objectHolder.setSelectedItem(bean);
	} else {
	    super.mouseClicked(evt);
	}
    }

    /**
     * Overriden selection method to handle non-visual beans.
     * This can be a component or a non-visual bean.
     * You should never pass a BeanWrapper as a selected item.
     */
    public void setSelectedItem(Object item) {
	Component component = null;

	if (item instanceof Component) {
	    component = (Component)item;
	} else {
            // The selected item is non-visual so create a wrapper and add it
            // to the glass pane.
	    component = objectHolder.getProxyComponent(item);
	}
	super.setSelectedItem(component);
    }


    /**
     * Iterates through the event handlers and creates Arcs which
     * represent an interaction between the components within the
     * container.
     */
    private void installEventArcs(Container container) {
	Component[] components = container.getComponents();
	Component comp;
	EventListener[] listeners;

	for (int i = 0; i < components.length; i++) {
	    comp = components[i];
	    
	    listeners = getProxyListeners(comp);
	    for (int j = 0; j < listeners.length; j++) {
		InvocationHandler handler = 
		    Proxy.getInvocationHandler(listeners[j]);
		if (handler instanceof EventHandler) {
		    addEventAdapter(comp, listeners[j]);
		}
	    }		
	}
    }

    /**
     * Helper method which returns an array of proxy listeners
     * that have been registered on the component. A proxy
     * listener is one which has been dynamically created with 
     * java.lang.reflect.Proxy.
     */
    private EventListener[] getProxyListeners(Component comp) {
	BeanInfo info = BeanInfoFactory.getBeanInfo(comp.getClass());
	EventSetDescriptor[] eventSets = info.getEventSetDescriptors();
	EventListener[] listeners;

	List returnList = new ArrayList();

	for (int j = 0; j < eventSets.length; j++) {
	    // For 1.4, the EventSetDescriptor contains a
	    // getGetListeners() method.
	    Class listenerType = eventSets[j].getListenerType();
	    listeners = comp.getListeners(listenerType);
	    for (int k = 0; k < listeners.length; k++) {
		if (Proxy.isProxyClass(listeners[k].getClass())) {
		    // This listener is a dynamically generated proxy.
		    returnList.add(listeners[k]);
		}
	    }
	}
	return (EventListener[])returnList.toArray(new EventListener[returnList.size()]);
    }

    /**
     * Adds the visual representation of the Dynamic Proxy EventHandler to the designer.
     * @param comp The source component of the interaction
     * @param proxy The dynamic proxy listener
     */
    private void addEventAdapter(Component comp, Object proxy) {
	Component target = null;
	
	try {
	    EventHandler handler = (EventHandler)Proxy.getInvocationHandler(proxy);
	    Object obj = handler.getTarget();
	    if (obj instanceof Component) {
		target = (Component)obj;
	    } else {
		target = objectHolder.getProxyComponent(obj);
		if (obj != null) {
		    // This is a result of initialization. The non-visual component
		    // isn't on the panel yet.
		    doAddObject(obj, getDefaultPoint());
		    target = objectHolder.getProxyComponent(obj);
		}
	    }
	} catch (IllegalArgumentException ex) {
	    // proxy is not a Proxy
	    ex.printStackTrace();
	    return;
	} catch (Exception ex) {
	    // If this is a class cast exception then something is really screwed up.
	    ex.printStackTrace();
	    return;
	}
	    
	if (target == null) {
	    return;
	}

	// We want to find the two handles on each component which are
	// the closest so that we can link them.
	Handle[] srcHandles = getHandles(comp);
	Handle[] targetHandles = getHandles(target);

	if (srcHandles != null && targetHandles != null) {
	    Handle srcHandle = srcHandles[0];
	    Handle targetHandle = targetHandles[0];
	
	    double distance = 5000; // A very large number
	    double tempDist;

	    for (int i = 0; i < srcHandles.length; i++) {
		for (int j = 0; j < targetHandles.length; j++) {
		    tempDist = distanceBetweenPoints(srcHandles[i].getLocation(), 
						     targetHandles[j].getLocation());
		    if (tempDist < distance) {
			srcHandle = srcHandles[i];
			targetHandle = targetHandles[j];
			distance = tempDist;
		    }
		}
	    }

	    if (srcHandle != null && targetHandle != null) {
		addArc(comp, new EventArc(srcHandle, targetHandle, proxy));
	    }
	}
    }

    /**
     * Utility method to calculate the distance between two points
     */
    private static double distanceBetweenPoints(Point p1, Point p2) {
	double dx = p1.getX() - p2.getX();
	double dy = p1.getX() - p2.getX();
	return Math.sqrt(dx * dx + dy * dy);
    }

    private void uninstallEventArcs(Container container) {
	removeArcs();
	// Remove all non-visual component
	Iterator keys = objectHolder.getProxyObjects();
	if (keys != null) {
	    while (keys.hasNext()) {
		doRemoveObject(keys.next());
	    }
	}
    }

    /**
     * Returns a default point based on the last location of the point and an
     * increment.
     */
    protected Point getDefaultPoint()  {
        Dimension size = this.getSize();

        if (newPoint.x + BeanWrapper.WRAPPER_SIZE > size.width ||
            newPoint.y + BeanWrapper.WRAPPER_SIZE > size.height)  {
            // Component will be placed off the panel to reset it to the origin.
            newPoint.move(0, 0);
        }
        newPoint.translate(GRID_SIZE, GRID_SIZE);

        return newPoint;
    }

    /**
     * Adds the interaction on the target object to the source object. 
     * This method uses the JDK 1.3 Dynamic Proxy Class API.
     * Formerly, this type of behaviour was achieved by dynamically
     * generating listener classes using a technique that's described in
     * http://java.sun.com/products/jfc/tsc/tech_topics/generic-listener/listener.html
     * <p>
     * There is a lot of massaging of the values to get it to work with the
     * new JavaBeans EventHandler. Perhaps a future version will be a little
     * cleaner.
     *
     * @param source The object which is the originator of the interaction.
     * @param sourceEvent The event which triggers the interaction.
     * @param sourceMethod The method that the source object will call.
     * @param target The object wish is the target of the interaction.
     * @param targetMethod The target method that will be called.
     * @param targetArg A MethodDescriptor which represents the property 
     *                  from the source object that should be used as the 
     *                  argument on the target method.
     * @return the Dyamic proxy EventListener created or null
     */
    public Object addInteraction(Object source, EventSetDescriptor sourceEvent,
                               MethodDescriptor sourceMethod, Object target,
                               MethodDescriptor targetMethod, MethodDescriptor targetArg) {
	// Create method chain for the EventHandler.
	String eventPropertyName = null;
	String targetMethodName = null;
	String sourceMethodName = null;

	if (targetArg != null) {
	    String targetArgName = targetArg.getMethod().getName();
	    // remove the "get" to make things cleaner.
	    if (targetArgName.startsWith("get")) {
		targetArgName = Introspector.decapitalize(targetArgName.substring(3));
	    }
	    eventPropertyName = "source." + targetArgName;
	}

	// If the event set contains only one listener method then pass null
	// as the default source method argument. This simplifies
	// the writing of the persistence file.
	Method[] methods = sourceEvent.getListenerMethods();
	if (methods.length > 1 && sourceMethod != null) {
	    sourceMethodName = sourceMethod.getMethod().getName();
	}

	// Using the new java.beans.EventHandler
	Object proxy = EventHandler.create(sourceEvent.getListenerType(),
					   target,
					   targetMethod.getMethod().getName(),
					   eventPropertyName,
					   sourceMethodName);
	if (proxy != null) {
	    Method addMethod = sourceEvent.getAddListenerMethod();

	    try {
		// Add the listener to the source.
		addMethod.invoke(source, new Object[] { proxy });
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return proxy;
    }

    /**
     * Adds the non visual bean to the design panel.
     * <p>
     * Non visual beans are wrapped with component and added to the glass pane.
     * @param obj non-visual object.
     * @return the Component which is a proxy for the object.
     */ 
    private Component doAddObject(Object obj, Point point)  {
	Component component = objectHolder.getProxyComponent(obj);
	if (component != null) {
	    // Component already added.
	    return component;
	} else {
	    // See if this Bean has a proxy component defined in it's BeanDescriptor
	    component = BeanInfoFactory.getProxyComponent(obj);
	    if (component == null) {
		// The BeanInfo doesn't define a visual proxy so create a wrapper
		component = new BeanWrapper(obj);
	    }
	}
	objectHolder.registerProxy(obj, component);

	component.setLocation(point);
	component.addMouseListener(this);

	addHandles(component);

	// Add the component to the glass pane
	this.add(component);
	this.revalidate();

	return component;
    }

    /**
     * Remove the non-visual bean from the designer.
     */
    private void doRemoveObject(Object obj)  {
	Component comp = objectHolder.getProxyComponent(obj);

	if (comp != null && comp.getParent() == this) {
	    comp.removeMouseListener(this);
	    removeHandles(comp);
	    removeArcs(comp);

	    this.remove(comp);
	    objectHolder.unregisterProxy(obj, comp);

	    this.revalidate();
	    this.repaint();
	}
    }

    /**
     * Callback to attempt an Event hookup between the objects represented 
     * by the two handles.
     * @return the Dynamic proxy represents the hookup or null.
     */
    public Object doEventHookup(EventHandle source, EventHandle target) {
	Component sComp = source.getComponent();
	Component tComp = target.getComponent();

	Object sObj = objectHolder.getProxyObject(sComp);
	if (sObj == null) {
	    sObj = sComp;
	}
	Object tObj = objectHolder.getProxyObject(tComp);
	if (tObj == null) {
	    tObj = tComp;
	}

	Object proxy = doEventHookup(sObj, tObj);
	if (proxy != null) {
	    // An EventHandler has been successfully added
	    addArc(sComp, new EventArc(source, target, proxy));
	} 
	else if (wizard.isPropertyHookup() && wizard.isFinished()) {
	    // Add the visual representation of the Property Association.
	    // Note: Not too safe because the property association 
	    // may have failed.
	    addArc(sComp, new PropertyArc(source, target));
	}
	return proxy;
    }

    /**
     * Callback for event hookup gesture. Presents a wizard to the user in order to
     * prompt for the source and target methods
     *
     * @param source source object for event hookup
     * @param target target object for event hookup
     * @return the Dynamic proxy represents the hookup or null.
     */
    public Object doEventHookup(Object source, Object target)  {
	Object proxy = null;
	if (wizard == null) {
	    wizard = new InteractionWizard(source, target);
	} else {
	    wizard.setData(source, target);
	}

	wizard.setVisible(true);

	if (wizard.isFinished())  {

	    if (wizard.isPropertyHookup())  {
		// Perform a property association between the source object and
		// the target object The target method is called on the target 
		// with the source object as the argument.
		//
		// i.e., If the source object is a TableModel and the target is
		// a JTable, the targetMethod should be setModel(TableModel).
		MethodDescriptor targetMethod = wizard.getTargetMethod();
		if (targetMethod != null)  {
		    Method method = targetMethod.getMethod();
		    try {
			// Set the source on the target listener.
			method.invoke(target, new Object[] { source });
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		}
	    } else {
		// Get the information from wizard and set up the interaction.
		// This creates an event adapter from the Proxy API with the
		// selected methods and arguments from the Wizard.
		EventSetDescriptor sourceEvent = wizard.getSourceEvent();
		MethodDescriptor sourceMethod = wizard.getSourceMethod();
		MethodDescriptor targetMethod = wizard.getTargetMethod();
		MethodDescriptor targetArg = wizard.getTargetArgument();

		if (sourceEvent != null && sourceMethod != null &&
		    targetMethod != null)  {
		    proxy = addInteraction(source, sourceEvent, sourceMethod,
				   target, targetMethod, targetArg);
		}
	    }
	}
	return proxy;
    }

    /**
     * Private class which shows property associations
     * XXX - this really should be in it's own class.
     */
    public class PropertyArc extends Arc {
	
	public PropertyArc(Handle source, Handle target) {
	    super(source, target, Arc.GREEN_CLR);
	}
    }
	

    //
    // ContainerListener methods
    //

    public void componentAdded(ContainerEvent evt) {
	addHandles(evt.getChild());
    }
    
    public void componentRemoved(ContainerEvent evt) {
	Component comp = evt.getChild();
	Container cont = evt.getContainer();

	removeHandles(comp);
	removeArcs(comp);
	
	if (cont == this) {
	    comp.removeMouseListener(this);
	}

	repaint();
    }

    //
    // PropertyChangeListener method
    // 

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
		// XXX - same algoritm in LayoutPane. Should refactor.
		if (oldValue != null) {
		    Container cont = (Container)oldValue;
		    uninstallHandles(cont);
		    uninstallEventArcs(cont);
		}
		if (newValue != null) {
		    Container cont = (Container)newValue; 
		    installHandles(cont);
		    installEventArcs(cont);
		}
	    }

	    // Not too happy about these two properties.
	    // Seem non-deterministic

	    if (prop.equals("addBean")) {
		// Add a non-visual bean
		
		// XXX - bit of a hack. 
		Point pt = instantiationHandler.getStartPt();
		if (pt == null) {
		    pt = getDefaultPoint();
		}
		doAddObject(newValue, pt);
	    }
	    if (prop.equals("removeBean")) {
		// Removes a non-visual bean
		doRemoveObject(newValue);
	    }
	}
    }

} // end class EventPane
