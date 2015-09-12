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
import java.awt.Rectangle;

import java.awt.event.MouseEvent;

import javax.swing.SpringLayout;

/**
 * A handle which identifies a control point for the SpringLayout manager
 * Valid types are N_HANDLE, S_HANDLE, E_HANDLE, W_HANDLE
 */
public class LayoutHandle extends Handle  {

    public LayoutHandle(Component component, int type) {
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
	return "SpringLayout edge connector on ";
    }

    // Overriden to avoid drag gestures.

    public void mousePressed(MouseEvent evt)  {}
    public void mouseReleased(MouseEvent evt)  {}

    /**
     * Color to paint the handle
     */
    protected Color getColor() {
	return Color.red;
    }

    /**
     * Returns the SpringLayout edge constraint that represents this handle.
     * @return SpringLayout constraint [ "NORTH" | "SOUTH" | "EAST" | "WEST" ]
     */
    public String getConstraint() {
	return getSpringEdgeFromHandleType(getHandleType());
    }

    /**
     * Utility method which maps the Handle type to the SpringLayout edge
     */
    public static String getSpringEdgeFromHandleType(int type) {
	switch (type) {
	case N_HANDLE: return SpringLayout.NORTH;
	case S_HANDLE: return SpringLayout.SOUTH;
	case E_HANDLE: return SpringLayout.EAST;
	default:
	case W_HANDLE: return SpringLayout.WEST;
	}
    }

    /**
     * Utility method which maps the SpringLayout edge to the Handle type
     */
    public static int getHandleTypeFromSpringEdge(String edge) {
	if (edge.equals(SpringLayout.NORTH)) {
	    return Handle.NORTH;
	} else if (edge.equals(SpringLayout.SOUTH)) {
	    return Handle.SOUTH;
	} else if (edge.equals(SpringLayout.EAST)) {
	    return Handle.EAST;
	}
	return Handle.WEST;
    }

}

