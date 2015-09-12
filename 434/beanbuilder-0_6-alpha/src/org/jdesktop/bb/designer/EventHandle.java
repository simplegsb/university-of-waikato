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
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.event.MouseEvent;

/**
 * A handle which identifies a control point for the SpringLayout manager
 * Valid types are N_HANDLE, S_HANDLE, E_HANDLE, W_HANDLE
 */
public class EventHandle extends Handle  {

    // Colors for drawing the handle
    private static Color[] colors = new Color[] { Color.lightGray, Color.gray,
						  Color.darkGray, Color.black };


    public EventHandle(Component component, int type) {
	setHandleType(type);
	setComponent(component);

        // Register this to recieve events (cursor change)
        this.addMouseListener(this);
	// override the cursor type
	cursorType = Cursor.CROSSHAIR_CURSOR;
    }

    /**
     * Returns the pre-fix for the tool tip text.
     */
    protected String getToolTipPrefix() {
	return "Event hookup handle on ";
    }

    // Overriden to avoid drag gestures.

    public void mousePressed(MouseEvent evt)  {}
    public void mouseReleased(MouseEvent evt)  {}

    /**
     * Color to paint the handle
     */
    protected Color getColor() {
	return Color.green;
    }

    protected void paintComponent(Graphics g) {
	g.setPaintMode();
	Dimension size = getSize();

	g.setColor(colors[1]);
	g.fillRect(0, 0, size.width, size.height);

	switch(handleType) {
	case N_HANDLE:
	case S_HANDLE:
	    g.setColor(colors[0]);
	    g.fillRect(0, 0, size.width / 3, size.height);
	    g.setColor(colors[2]);
	    g.fillRect(2 * size.width / 3, 0, size.width / 3, size.height);
	    break;

	case E_HANDLE:
	case W_HANDLE:
	    g.setColor(colors[0]);
	    g.fillRect(0, 0, size.width, size.height / 3);
	    g.setColor(colors[2]);
	    g.fillRect(0, 2 * size.height / 3, size.width, size.height / 3);
	    break;
	}
	g.setColor(colors[3]);
	g.drawRect(0, 0, size.width - 1, size.height - 1);

    }

    /** 
     * Overloaded method to position the handle based on the bounds of the component.
     */
    public void positionHandle()  {
        Rectangle rect = getComponent().getBounds();
        Rectangle bounds = new Rectangle(rect.x, rect.y, DOT_DIM, DOT_DIM);
        
        switch(handleType) {
            case N_HANDLE:  
                bounds.translate(2 * (rect.width/3), -DOT_DIM); 
                break;
            case W_HANDLE:  
                bounds.translate(-DOT_DIM, 2 * (rect.height/3));  
                break;
            case E_HANDLE:  
                bounds.translate(rect.width, rect.height/3);  
                break;
            case S_HANDLE:
                bounds.translate(rect.width/3, rect.height);  
                break;
        }
        this.setBounds(bounds);
    }
}

