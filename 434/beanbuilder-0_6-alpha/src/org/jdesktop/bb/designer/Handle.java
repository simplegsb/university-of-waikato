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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;

import org.jdesktop.bb.model.ObjectHolder;

/**
 * Represents a resizing rectangle. This class implements the behavior such
 * that when the object is dragged, it will send a message to it's parent component
 * to set the constraints to itself.
 *
 * @version 1.16 02/27/02
 * @author  Mark Davidson
 */
public class Handle extends JComponent implements MouseListener, 
    MouseMotionListener, ComponentListener {
  
    public static final int DOT_DIM = 6;
  
    // Const for handle locations
    public static final int NW_HANDLE = 0;
    public static final int N_HANDLE  = 1;
    public static final int NE_HANDLE = 2;
    public static final int E_HANDLE  = 3;
    public static final int SE_HANDLE = 4;
    public static final int S_HANDLE  = 5;
    public static final int SW_HANDLE = 6;
    public static final int W_HANDLE  = 7;
    public static final int C_HANDLE  = 8;

    public static final int NORTH = N_HANDLE;
    public static final int NORTH_EAST = NE_HANDLE;
    public static final int NORTH_WEST = NW_HANDLE;
    public static final int SOUTH = S_HANDLE;
    public static final int SOUTH_EAST = SE_HANDLE;
    public static final int SOUTH_WEST = SW_HANDLE;
    public static final int EAST = E_HANDLE;
    public static final int WEST = W_HANDLE;
    public static final int CENTER = C_HANDLE;
  
    protected int cursorType; // One of Cursor.xxx constants
    protected int handleType; // a handle location constant

    // Convenience. So may as well make it static.
    protected static ObjectHolder objectHolder;

    private Component component;
    private HandleDragInfo dinfo;
    
    public Handle() {	
	this(null, N_HANDLE);
    }

    // Constructors
    public Handle(Component component, int type) {
	if (objectHolder == null) {
	    objectHolder = ObjectHolder.getInstance();
	}
	setHandleType(type);
        setComponent(component);
      
        // Register this to recieve events
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * Returns the pre-fix for the tool tip text.
     */
    protected String getToolTipPrefix() {
	return "move/resize handle on ";
    }

    /**
     * Sets the compass point that this Handle represents.
     *
     * @param type one of the compass constants. i.e, N_HANDLE;
     */ 
    public void setHandleType(int type) {
        this.handleType = type;
        // Set the bounds of the Handle depending on the type of cursor
        switch(type) {
            case NW_HANDLE:   cursorType = Cursor.NW_RESIZE_CURSOR; break;
            case N_HANDLE:    cursorType =  Cursor.N_RESIZE_CURSOR; break;
            case NE_HANDLE:   cursorType = Cursor.NE_RESIZE_CURSOR; break;
            case W_HANDLE:    cursorType = Cursor.W_RESIZE_CURSOR;  break;
            case E_HANDLE:    cursorType = Cursor.E_RESIZE_CURSOR;  break;
            case SW_HANDLE:   cursorType = Cursor.SW_RESIZE_CURSOR; break;
            case S_HANDLE:    cursorType = Cursor.S_RESIZE_CURSOR;  break;
            case SE_HANDLE:   cursorType = Cursor.SE_RESIZE_CURSOR; break;
            case C_HANDLE:    cursorType = Cursor.MOVE_CURSOR; break;
        }
    }

    public int getHandleType() {
	return handleType;
    }

    /**
     * Returns the mirror type of the handle. For example, if type == NORTH then
     * SOUTH will be returned.
     */
    public static int getMirrorType(int type) {
	int newType = C_HANDLE;

        switch(type) {
            case NW_HANDLE:   newType = SE_HANDLE; break;
            case N_HANDLE:    newType = S_HANDLE; break;
            case NE_HANDLE:   newType = SW_HANDLE; break;
            case W_HANDLE:    newType = E_HANDLE; break;
            case E_HANDLE:    newType = W_HANDLE; break;
            case SW_HANDLE:   newType = NE_HANDLE; break;
            case S_HANDLE:    newType = N_HANDLE; break;
            case SE_HANDLE:   newType = NW_HANDLE; break;
            case C_HANDLE:    newType = C_HANDLE; break;
        }
	return newType;
    }

    /**
     * The hot point represents the point of origin for this handle.
     */
    public Point getHotPt() {
	Rectangle bounds = getBounds();

	int midX = bounds.width /2 -1;
	int midY = bounds.height /2 -1;

        switch(handleType) {
            case NW_HANDLE:   
		break;
            case N_HANDLE:    
		bounds.x += midX; 
		break;
            case NE_HANDLE:   
		bounds.x += bounds.width;
		break;
            case W_HANDLE:    
		bounds.y += midY; 
		break;
            case E_HANDLE:    
		bounds.x += bounds.width; 
		bounds.y += midY;
		break;
            case SW_HANDLE:   
		bounds.y += bounds.height;
		break;
            case S_HANDLE:    
		bounds.x += midX;
		bounds.y += bounds.height;
		break;
            case SE_HANDLE:
		bounds.x += bounds.width; 
		bounds.y += bounds.height;
		break;
            case C_HANDLE:    
		bounds.x += midX;
		bounds.y += midY; 
		break;
        }
	return new Point(bounds.x, bounds.y);
    }	
    
    /**
     * Set the Component for which this handle represents.
     */
    public void setComponent(Component component)  {
	Component oldComponent = this.component;
        this.component = component;

	// ObjectHolder acts as a proxy for ComponentEvents
	// thereby avoiding registering the listeners on the 
	// actual designed objects.
	if (oldComponent != null) {
	    objectHolder.removeComponentListener(oldComponent, this);
	}

	if (component != null) {
	    objectHolder.addComponentListener(component, this);

	    // Set the tooltip text. If the component is a proxy component
	    // the text should be the object that the proxy represents
	    Object obj = ObjectHolder.getInstance().getProxyObject(component);
	    if (obj == null) {
		obj = component;
	    }
	    setToolTipText(getToolTipPrefix() + obj.getClass().getName());

	    positionHandle();
	}
    }

    public Component getComponent() {
	return component;
    }

    /**
     * Color to paint the handle
     */
    protected Color getColor() {
	return Color.blue;
    }
    
    public void mouseClicked(MouseEvent evt)  {
    }
    
    public void mouseEntered(MouseEvent evt)  {
        if (isEnabled())
            setCursor(Cursor.getPredefinedCursor(cursorType));
    }
    
    public void mouseExited(MouseEvent evt)  {
        if (isEnabled())
            setCursor(Cursor.getDefaultCursor());
    }
    
    public void mousePressed(MouseEvent evt)  {
        if (isEnabled())
            startDragGesture(evt.getX(), evt.getY());
    }
    
    public void mouseReleased(MouseEvent evt)  {
        if (isEnabled())
            endDragGesture(evt.getX(), evt.getY());
    }
    
    public void mouseDragged(MouseEvent evt)  {
        if (isEnabled())
            dragGesture(evt.getX(), evt.getY());
    }
    
    public void mouseMoved(MouseEvent evt)  {
    }

    //
    // ComponentListener implementation
    //

    public void componentMoved(ComponentEvent e) {
	if (component == e.getComponent()) {
	    positionHandle();
	}
    }
    public void componentResized(ComponentEvent e) {
	if (component == e.getComponent()) {
	    positionHandle();
	}
    }
    
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {}

    
    // Paint the object
    protected void paintComponent(Graphics g) {
        g.setPaintMode();
              
        Dimension size = getSize();
      
	g.setColor(Color.white);
	g.fillRect(0, 0, size.width, size.height);
	g.setColor(getColor());
	g.drawRect(0, 0, size.width - 1, size.height - 1);
    }
    
    /********************************************************************
      Private gesture methods.
     ********************************************************************/
    
    /**
     * Start of a drag gesture.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param rightBtn was it the right button.
     */
    private void startDragGesture(int x, int y) {
        Point pt = new Point(x, y);
        dinfo = null;
        
        // set up the node drag information.
        HandleDragInfo info = new HandleDragInfo();
        info.comp = component;
        info.startPt = pt;
        info.lastPt = null;
        info.bounds = new Rectangle(info.comp.getBounds());
        info.btext = null;
        
        dinfo = info;
    }

    /**
     * On going dragging gesture.
     * 
     * Note: The point in this mouse event is relative to the location 
     * of the handle and represents the delta for which the component will
     * be resized.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    private void dragGesture(int ptx, int pty) {
        if (dinfo == null) 
            return;

        Rectangle bounds = dinfo.comp.getBounds();
        Rectangle newbounds = new Rectangle(bounds);

        // Calculate the new width, height and position 
        // XXX - there may be a simpler way of calculating this using
        // diffs on dinfo.bounds.
        
        int x = bounds.x + ptx;
        int y = bounds.y + pty;
        int width = bounds.width + (bounds.x - x);
        int height = bounds.height + (bounds.y - y);
        
        // Recalc for the width
        if (handleType == NE_HANDLE || handleType == E_HANDLE || 
            handleType == SE_HANDLE)
            width = bounds.width + (ptx - dinfo.startPt.x);
            
        // Recalc for the height
        if (handleType == SW_HANDLE || handleType == S_HANDLE || 
            handleType == SE_HANDLE)
            height = bounds.height + (pty - dinfo.startPt.y);
        
        // don't let the component have -ve values.
        if (handleType != C_HANDLE && (height < 0 || width < 0))
            return;
        
        switch (handleType) {
            case NW_HANDLE:
                newbounds.setBounds(x, y, width, height);
                break;
        
            case N_HANDLE:
                newbounds.setBounds(bounds.x, y, bounds.width, height);
                break;
        
            case NE_HANDLE:
                newbounds.setBounds(bounds.x, y, width, height);
                break;
        
            case W_HANDLE:
                newbounds.setBounds(x, bounds.y, width, bounds.height);
                break;
        
            case E_HANDLE:
                newbounds.setSize(width, bounds.height);
                break;
        
            case SW_HANDLE:
                newbounds.setBounds(x, bounds.y, width, height);
                break;
        
            case S_HANDLE:
                newbounds.setSize(bounds.width, height);
                break;
        
            case SE_HANDLE:
                newbounds.setSize(width, height);
                break;
        
            case C_HANDLE:
                newbounds.setLocation(x, y);
                break;
        }
            
        // Don't draw if the new rectangle is the same as the last one
        if (newbounds.x == dinfo.bounds.x && 
            newbounds.y == dinfo.bounds.y && 
            newbounds.width == dinfo.bounds.width && 
            newbounds.height == dinfo.bounds.height) {
            return;
        }

        // Tricky: draw on the graphics context of the component's parent.
        Graphics g = dinfo.comp.getParent().getGraphics();
        if (g != null) {

            // erase the old XOR residue if it exists.
            if (dinfo.lastPt != null)  {
                g.setXORMode(getBackground());
                dinfo.draw(g);
                g.setPaintMode();
            } else {
                dinfo.lastPt = dinfo.startPt;
            }
            
            // Set the new bounds values and text.
            dinfo.bounds = newbounds;
            dinfo.btext = String.valueOf(dinfo.bounds.width) + 'x' + 
                            dinfo.bounds.height;
            
            // draw the new stuff.
            g.setXORMode(getBackground());
            dinfo.draw(g);
            g.setPaintMode();
            g.dispose();
        }
    }

    /**
     * End of the drag gesture.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    private void endDragGesture(int x, int y) {
        if (dinfo == null) 
            return;

        // Tricky: draw on the graphics context of the component's parent. Not
        // the enclosed LayoutEditor which may or may not be the same thing.
        Graphics g = dinfo.comp.getParent().getGraphics();
        if (g != null) {
            if (dinfo.lastPt != null)  {
                g.setXORMode(getBackground());
                dinfo.draw(g);
                g.setPaintMode();
            }
            g.dispose();
        }

        // Set the new bounds on the component
	Point oldLocation = component.getLocation();
	if (!oldLocation.equals(dinfo.bounds.getLocation())) {
	    component.setLocation(dinfo.bounds.getLocation());
	    objectHolder.fireComponentMoved(component);
	}

	if (component.getSize() != null && 
	    !component.getSize().equals(dinfo.bounds.getSize())) {
	    // Only set the prefsize/size if it actually changed
	    if (component instanceof JComponent) {
		((JComponent)component).setPreferredSize(dinfo.bounds.getSize());
		// Relayout the Container
		component.getParent().doLayout();
		component.getParent().repaint();
	    }
	    else {
		component.setBounds(dinfo.bounds);
	    }
	    objectHolder.fireComponentResized(component);
	}
        

        dinfo = null;
    }
    
    /** 
     * Position the handle based on the bounds of the component.
     */
    public void positionHandle()  {
        Rectangle rect = component.getBounds();
        Rectangle bounds = new Rectangle(rect.x, rect.y, DOT_DIM, DOT_DIM);
        
        switch(handleType) {
            case NW_HANDLE: 
                break;
            case N_HANDLE:  
                bounds.translate((rect.width - DOT_DIM)/2, 0); 
                break;
            case NE_HANDLE: 
                bounds.translate((rect.width - DOT_DIM), 0); 
                break;
            case W_HANDLE:  
                bounds.translate(0, (rect.height - DOT_DIM)/2);  
                break;
            case SW_HANDLE: 
                bounds.translate(0, (rect.height - DOT_DIM)); 
                break;
            case E_HANDLE:  
                bounds.translate((rect.width - DOT_DIM), (rect.height - DOT_DIM)/2);  
                break;
            case S_HANDLE:
                bounds.translate((rect.width - DOT_DIM)/2, (rect.height - DOT_DIM));  
                break;
            case SE_HANDLE:
                bounds.translate((rect.width - DOT_DIM), (rect.height - DOT_DIM));  
                break;            
            case C_HANDLE:
                bounds.translate((rect.width - DOT_DIM)/2, (rect.height - DOT_DIM)/2);  
                break;
        }
        this.setBounds(bounds);
    }
    

    /** 
     * Holds information on the drag handle information
     */
    private class HandleDragInfo {

        // Starting point of the drag.
        public Point startPt;

        // Last saved point in the drag.
        public Point lastPt;

        // The component getting resized.
        public Component comp;
        
        // Bounds of the handle after it's resized.
        public Rectangle bounds;
        
        // String that corresponds to the rect bounds.
        public String btext;
        
        /** 
         * Return the bounding retangle
         */
        public Rectangle getBounds()  {
            return bounds;
        }
        
        /** 
         * Paint the drag mode
         * 
         * @param g the graphics context
         */
        public void draw(Graphics g)  {
            g.setColor(Color.black);
            g.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
            
            if (btext != null)  {
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(btext);
                int textHeight = fm.getHeight();
                
                g.drawString(btext, bounds.x + (bounds.width - textWidth)/2, 
                                          bounds.y + (bounds.height + textHeight)/2);
            }

        }
    }

}
