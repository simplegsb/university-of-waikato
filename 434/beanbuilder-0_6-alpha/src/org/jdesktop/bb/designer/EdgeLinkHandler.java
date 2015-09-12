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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

/**
 * MouseHandler for the LayoutHandler knows how to interpret mouse movements
 * into constraints on the LayoutPane.
 *
 * @version 1.5 02/27/02
 * @author Mark Davidson
 */
public class EdgeLinkHandler implements MouseListener, MouseMotionListener {
    
    private LayoutPane parent;

    private Point startPt;
    private Point endPt;

    private LayoutHandle source;
    private LayoutHandle target;

    public EdgeLinkHandler(LayoutPane cont) {
	parent = cont;
    }

    public Point getStartPt() {
	return startPt;
    }

    public Point getEndPt() {
	return endPt;
    }

    public LayoutHandle getSourceHandle() {
	return source;
    }

    public LayoutHandle getTargetHandle() {
	return target;
    }

    public void mouseClicked(MouseEvent evt) {}
    public void mouseEntered(MouseEvent evt)  {}
    public void mouseExited(MouseEvent evt)  {}

    /**
     * Start a gesture
     */
    public void mousePressed(MouseEvent evt)  {
	// Translate point to LayoutPane co-ords.
	source = (LayoutHandle)evt.getSource();
	startPt = SwingUtilities.convertPoint(source, evt.getPoint(), parent);
    }

    /**
     * End of the gesture.
     */
    public void mouseReleased(MouseEvent evt)  {
	// Linking edges
	if (source != null && target != null && source != target) {
	    // Calculate the distance between the components:
	    String const1 = source.getConstraint();

	    int delta;
	    // Fixed distance should be diff in x or y.
	    if (const1.equals(SpringLayout.NORTH) || 
		const1.equals(SpringLayout.SOUTH)) {
		delta = source.getLocation().y - target.getLocation().y;
	    } 
	    else {
		delta = source.getLocation().x - target.getLocation().x;
	    }
	    parent.addConstraint(source, target, delta);
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
	if (comp instanceof LayoutHandle) {
	    target = (LayoutHandle)comp;
	} else {
	    target = null;
	}
	parent.repaint();
    }

    public void mouseMoved(MouseEvent evt)  {}
    
}

