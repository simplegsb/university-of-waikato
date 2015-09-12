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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class draws and manages a representation of a relationship between two
 * objects. For example, if the relationship expressed is layout management,
 * the the arc represents a constraint object. If the relationship represents 
 * event interactions then the relationship represents an EventHandler.
 *
 * The Arc draws a line between the two hot points represented from the Handles.

 * TODO: A Shape which represents the vertices should probably be used.
 *      - This would be desireable to be a Component so that it can draw itself
 * and listeners can be added to it.
 *
 * @author John Cho
 * @author Mark Davidson
 */
public class Arc {

    private final static int ARC_WIDTH = 3;
    private final static int ARC_PADDING = 4;

    // Color values
    private final static Color COLOR_GREEN1 = new Color(184, 210, 107);
    private final static Color COLOR_GREEN2 = new Color(77, 166, 25);
    private final static Color COLOR_GREEN3 = new Color(0, 88, 42);
    private final static Color COLOR_ORANGE1 = new Color(253, 202, 138);
    private final static Color COLOR_ORANGE2 = new Color(236, 152, 0);
    private final static Color COLOR_ORANGE3 = new Color(154, 100, 0);
    private final static Color COLOR_BLUE1 = new Color(142, 205, 240);
    private final static Color COLOR_BLUE2 = new Color(0, 160, 221);
    private final static Color COLOR_BLUE3 = new Color(0, 104, 144);
    private final static Color COLOR_BROWN1 = new Color(199, 159, 109);
    private final static Color COLOR_BROWN2 = new Color(166, 126, 78);
    private final static Color COLOR_BROWN3 = new Color(137, 98, 53);
    private final static Color COLOR_PURPLE1 = new Color(178, 174, 234);
    private final static Color COLOR_PURPLE2 = new Color(156, 151, 223);
    private final static Color COLOR_PURPLE3 = new Color(137, 132, 210);
    private final static Color COLOR_RED1 = new Color(255, 0, 0);
    private final static Color COLOR_RED2 = new Color(196, 0, 38);
    private final static Color COLOR_RED3 = new Color(128, 0, 25);
    private final static Color COLOR_DARK1 = new Color(128, 128, 128);
    private final static Color COLOR_DARK2 = new Color(100, 100, 100);
    private final static Color COLOR_DARK3 = new Color(72, 72, 72);

    // constants for the Color sets
    public final static int DEFAULT_CLR = 0;
    public final static int GREEN_CLR = 1;
    public final static int ORANGE_CLR = 2;
    public final static int BLUE_CLR = 3;
    public final static int BROWN_CLR = 4;
    public final static int PURPLE_CLR = 5;
    public final static int RED_CLR = 6;
    public final static int DARK_CLR = 7;

    // Source and destination handle of the Arc.
    private Handle source;
    private Handle destination;

    // Initial values to see if vertices should be recalculated
    private Point sourcePoint;
    private Point destinationPoint;

    private Vector vertices;
    private Rectangle tmpRect;
    private int color;

    /**
     * Ctor
     */
    protected Arc(Handle source, Handle destination) {
	this(source, destination, DEFAULT_CLR);
    }

    protected Arc(Handle source, Handle destination, int color) {
	this.source = source;
	this.destination = destination;

	this.sourcePoint = source.getHotPt();
	this.destinationPoint = destination.getHotPt();
	this.color = color;

	vertices = calcDefaultVertices(source, destination);
    }

    /**
     * Source handle
     */
    public Handle getSource() {
	return source;
    }

    public Handle getDestination() {
	return destination;
    }
    
    /**
     * Is the given point on this arc?
     * @param pt the point.
     * @return true or false.
     */
    protected boolean isOnArc(Point pt) {
        Enumeration en;
        Point pt1 = source.getHotPt();
        Point pt2 = null;
        Point pt3 = destination.getHotPt();

	if (tmpRect == null) {
	    tmpRect = new Rectangle();
	}

        en = vertices.elements();
        while (en.hasMoreElements()) {
            pt2 = (Point)en.nextElement();
            if (pt1.x == pt2.x) {
                if (pt1.y < pt2.y) {
                    tmpRect.x = pt1.x - ARC_PADDING;
                    tmpRect.y = pt1.y;
                    tmpRect.width = 2 * ARC_PADDING + ARC_WIDTH;
                    tmpRect.height = pt2.y - pt1.y + 1;
                } else {
                    tmpRect.x = pt2.x - ARC_PADDING;
                    tmpRect.y = pt2.y;
                    tmpRect.width = 2 * ARC_PADDING + ARC_WIDTH;
                    tmpRect.height = pt1.y - pt2.y + 1;
                }
            } else {
                if (pt1.x < pt2.x) {
                    tmpRect.x = pt1.x;
                    tmpRect.y = pt1.y - ARC_PADDING;
                    tmpRect.width = pt2.x - pt1.x + 1;
                    tmpRect.height = 2 * ARC_PADDING + ARC_WIDTH;
                } else {
                    tmpRect.x = pt2.x;
                    tmpRect.y = pt2.y - ARC_PADDING;
                    tmpRect.width = pt1.x - pt2.x + 1;
                    tmpRect.height = 2 * ARC_PADDING + ARC_WIDTH;
                }
            }

            if (tmpRect.contains(pt)) return true;
            pt1 = pt2;
        }

        pt2 = pt3;
        if (pt1.x == pt2.x) {
            if (pt1.y < pt2.y) {
                tmpRect.x = pt1.x - ARC_PADDING;
                tmpRect.y = pt1.y;
                tmpRect.width = 2 * ARC_PADDING + ARC_WIDTH;
                tmpRect.height = pt2.y - pt1.y + 1;
            } else {
                tmpRect.x = pt2.x - ARC_PADDING;
                tmpRect.y = pt2.y;
                tmpRect.width = 2 * ARC_PADDING + ARC_WIDTH;
                tmpRect.height = pt1.y - pt2.y + 1;
            }
        } else {
            if (pt1.x < pt2.x) {
                tmpRect.x = pt1.x;
                tmpRect.y = pt1.y - ARC_PADDING;
                tmpRect.width = pt2.x - pt1.x + 1;
                tmpRect.height = 2 * ARC_PADDING + ARC_WIDTH;
            } else {
                tmpRect.x = pt2.x;
                tmpRect.y = pt2.y - ARC_PADDING;
                tmpRect.width = pt1.x - pt2.x + 1;
                    tmpRect.height = 2 * ARC_PADDING + ARC_WIDTH;
            }
        }
        
        if (tmpRect.contains(pt)) 
	    return true;
        else 
	    return false;
    }


    /**
     * Draw the connector line between two handles.
     */
    public static void drawConnector(Graphics g, Handle source, 
				     Handle destination, int color) {
	Arc.drawConnector(g, calcDefaultVertices(source, destination),
			  source.getHotPt(), destination.getHotPt(), color);
    }

    /**
     * Draw the connector line from a starting point to an end point.
     *
     * @param g the graphics context.
     * @param sType starting type of handle one of Handle.NORTH. Use -1 if it 
     *              will be calculated
     * @param tType target type of handle. Use -1 if this should be calculated.
     * @param startPt starting point to draw. 
     * @param endPt ending point to draw.
     */
    public static void drawConnector(Graphics g, int sType, int tType, 
				     Point startPt, Point endPt) {
	Arc.drawConnector(g, sType, tType, startPt, endPt, DEFAULT_CLR);
    }
	
    /**
     * Draw the connector line from a starting point to an end point.
     *
     * @param g the graphics context.
     * @param sType starting type of handle one of Handle.NORTH. Use -1 if it 
     *              will be calculated
     * @param tType target type of handle. Use -1 if this should be calculated.
     * @param startPt starting point to draw. 
     * @param endPt ending point to draw.
     * @param color color set to use.
     */
    public static void drawConnector(Graphics g, int sType, int tType, 
				     Point startPt, Point endPt, int color) {
        Vector pts = Arc.calcDefaultVertices(sType, tType, startPt, endPt);
	Arc.drawConnector(g, pts, startPt, endPt, color);
    }
	

    /**
     * Draw the connector line from a starting point to an end point folloing
     * a path defined by the vector of points.
     *
     * @param g the graphics context.
     * @param pts a Vector of points
     * @param startPt starting point to draw. 
     * @param endPt ending point to draw.
     * @param color color set to use.
     */
    public static void drawConnector(Graphics g, Vector pts,
				     Point startPt, Point endPt, int color) {
        Point ptA = startPt;
	Point ptB = endPt;
        Point pt1 = ptA;
        Point pt2 = null;

        Color[] colors = getColors(color);

        Enumeration e = pts.elements();
        while (e.hasMoreElements()) {
            pt2 = (Point)e.nextElement();

            if (pt1.equals(ptA)) {
                Arc.drawSegment(g, pt1, pt2, colors[0], colors[1], colors[2]);
                Arc.drawAnchor(g, pt1, pt2, colors[0], colors[1], colors[2]);
            } else {
                Arc.drawSegment(g, pt1, pt2, colors[0], colors[1], colors[2]);
            }

            pt1 = pt2;
        }

        if (pt1.equals(ptA)) {
            Arc.drawSegment(g, pt1, ptB, colors[0], colors[1], colors[2]);
            Arc.drawAnchor(g, pt1, ptB, colors[0], colors[1], colors[2]);
            Arc.drawArrow(g, pt1, ptB, colors);
        } else {
            Arc.drawSegment(g, pt1, ptB, colors[0], colors[1], colors[2]);
            Arc.drawArrow(g, pt1, ptB, colors);
        }
    }

    /**
     * Non-static version draws the connector with the current values
     * for color, startPoint, etc...
     *
     * @param g the graphics context.
     */
    public void drawConnector(Graphics g) {
        Point ptA = source.getHotPt();
	Point ptB = destination.getHotPt();
        Point pt1 = ptA;
        Point pt2 = null;

        Color[] colors = getColors(color);

        Enumeration e = vertices.elements();
        while (e.hasMoreElements()) {
            pt2 = (Point)e.nextElement();

            if (pt1.equals(ptA)) {
                Arc.drawSegment(g, pt1, pt2, colors[0], colors[1], colors[2]);
                Arc.drawAnchor(g, pt1, pt2, colors[0], colors[1], colors[2]);
            } else {
                Arc.drawSegment(g, pt1, pt2, colors[0], colors[1], colors[2]);
            }

            pt1 = pt2;
        }

        if (pt1.equals(ptA)) {
            Arc.drawSegment(g, pt1, ptB, colors[0], colors[1], colors[2]);
            Arc.drawAnchor(g, pt1, ptB, colors[0], colors[1], colors[2]);
            Arc.drawArrow(g, pt1, ptB, colors);
        } else {
            Arc.drawSegment(g, pt1, ptB, colors[0], colors[1], colors[2]);
            Arc.drawArrow(g, pt1, ptB, colors);
        }
    }

    /**
     * Draw the starting segment.
     * @param g the graphics context.
     * @param pt1 the first point.
     * @param pt2 the second point.
     * @param c1 the light color.
     * @param c2 the medium color.
     * @param c3 the dark color.
     */
    protected static void drawAnchor(Graphics g, Point pt1, Point pt2,
                                      Color c1, Color c2, Color c3) {

	// Determine the bounds of the Anchor rectangle
	Rectangle rect = new Rectangle(pt1.x, pt1.y, 7, 7);
        if (pt1.x == pt2.x) {
            // Anchor along the vertical axis
	    if (pt1.y > pt2.y) {
		// Point 2 above the anchor point
		rect.translate(-4, -8);
	    } else {
		rect.translate(-4, 0);
	    }		
        } else {
            // Anchor along the horizontal axis.
	    if (pt1.x > pt2.x) {
		// Point 2 left of anchor point
		rect.translate(-8, -4);
	    } else {
		rect.translate(0, -4);
	    }		
        }

	g.setColor(Color.black);
	g.drawRect(rect.x, rect.y, rect.width, rect.height);

	rect.setRect(rect.x + 1, rect.y + 1, 6, 2);
	g.setColor(c1);
	g.fillRect(rect.x, rect.y, rect.width, rect.height);
	
	rect.translate(0, 2);
	g.setColor(c2);
	g.fillRect(rect.x, rect.y, rect.width, rect.height);
	
	rect.translate(0, 2);
	g.setColor(c3);
	g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Draw the ending segment.
     *
     * @param g the graphics context.
     * @param pt1 the origin point used to determine direction
     * @param pt2 the second point used as the hot point of arrow)
     * @param colors Array of color elements from lightest to darkest
     */
    protected static void drawArrow(Graphics g, Point pt1, Point pt2,
				    Color[] colors) {
	int arrowSize = 10;

        if (pt1.x == pt2.x) {
            // vertical segment.
	    if (pt2.y < pt1.y) {
		// North arrow
		paintTriangle(g, pt2.x, pt2.y, arrowSize, Handle.NORTH, colors);
	    } else {
		// South arrow
		paintTriangle(g, pt2.x, pt2.y, arrowSize, Handle.SOUTH, colors);
	    }
        } else {
            // horizontal segment.
	    if (pt2.x < pt1.x) {
		// East arrow
		paintTriangle(g, pt2.x, pt2.y, arrowSize, Handle.WEST, colors);
	    } else {
		// West arrow
		paintTriangle(g, pt2.x, pt2.y, arrowSize, Handle.EAST, colors);
	    }
        }
    }

    public static void paintTriangle(Graphics g, int x, int y, int size, 
					int direction, Color[] colors) {
	Graphics2D g2 = (Graphics2D)g;
	Color oldColor = g.getColor();

	Polygon arrow = new Polygon();
	arrow.addPoint(x, y);
	
	int mid = size / 2;

	Point pt1 = new Point(x, y);
	Point pt2 = new Point(x, y);

	switch(direction)       {
	case Handle.NORTH:
	    arrow.addPoint(x - mid, y + size);
	    arrow.addPoint(x + mid, y + size);
	    break;
	case Handle.SOUTH:
	    arrow.addPoint(x - mid, y - size);
	    arrow.addPoint(x + mid, y - size);
	    break;
	case Handle.WEST:
	    arrow.addPoint(x + size, y - mid);
	    arrow.addPoint(x + size, y + mid);
	    break;
	case Handle.EAST:
	    arrow.addPoint(x - size, y - mid);
	    arrow.addPoint(x - size, y + mid);
	    break;
	}
	
	g2.setColor(colors[1]);
	g2.fill(arrow);
	g2.setColor(Color.black);
	g2.draw(arrow);

	g2.setColor(oldColor);
    }


    /**
     * Draw a segment.
     * @param g the graphics context.
     * @param pt1 the first point.
     * @param pt2 the second point.
     * @param c1 the light color.
     * @param c2 the medium color.
     * @param c3 the dark color.
     */
    protected static void drawSegment(Graphics g, Point pt1, Point pt2,
                                    Color c1, Color c2, Color c3) {
        if (pt1.x == pt2.x) {
            // vertical segment.
            g.setColor(c1);
            g.drawLine(pt1.x - 1, pt1.y, pt2.x - 1, pt2.y);
            g.setColor(c2);
            g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
            g.setColor(c3);
            g.drawLine(pt1.x + 1, pt1.y, pt2.x + 1, pt2.y);
        } else {
            // horizontal segment.
            g.setColor(c1);
            g.drawLine(pt1.x, pt1.y - 1, pt2.x, pt2.y - 1);
            g.setColor(c2);
            g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
            g.setColor(c3);
            g.drawLine(pt1.x, pt1.y + 1, pt2.x, pt2.y + 1);
        }
    }


    /**
     * Set the vertices.
     * @param vertices the replacement vector of vertices.
     */
    protected void setVertices(Vector vertices) {
        this.vertices = vertices;
    }

    /**
     * Return this arc's bounds.
     * @return the bounds.
     */
    protected Rectangle getDrawArea() {
        Point ptA = source.getHotPt();
        Point ptB = destination.getHotPt();

        int x1, x2, y1, y2;

        if (ptA.x < ptB.x) {
            x1 = ptA.x;
            x2 = ptB.x;
        } else {
            x1 = ptB.x;
            x2 = ptA.x;
        }

        if (ptA.y < ptB.y) {
            y1 = ptA.y;
            y2 = ptB.y;
        } else {
            y1 = ptB.y;
            y2 = ptA.y;
        }

        Enumeration en = vertices.elements();
        while (en.hasMoreElements()) {
            ptB = (Point)en.nextElement();
            if (ptB.x < x1) {
                x1 = ptB.x;
            } else if (ptB.x > x2) {
                x2 = ptB.x;
            }
            if (ptB.y < y1) {
                y1 = ptB.y;
            } else if (ptB.y > y2) {
                y2 = ptB.y;
            }
        }

        return new Rectangle(x1 - 3, y1 - 3, x2 - x1 + 6, y2 - y1 + 6);
    }

    /**
     * Set the color of this arc.
     * Used the protected constants defined in this class.
     * @param clr the new color.
     */
    protected void setColor(int clr) {
        color = clr;
    }

    /**
     * Returns an array of 3 colors which represent the color set
     *
     * @param color a color set constant
     * @return an array of colors. The first element represents the lightest.
     */
    protected static Color[] getColors(int color) {
	Color[] colors = new Color[3];
	
	switch (color) {
	case GREEN_CLR:
            colors[0] = COLOR_GREEN1;
            colors[1] = COLOR_GREEN2;
            colors[2] = COLOR_GREEN3;
	    break;
	case ORANGE_CLR:
            colors[0] = COLOR_ORANGE1;
            colors[1] = COLOR_ORANGE2;
            colors[2] = COLOR_ORANGE3;
	    break;
	case BLUE_CLR:
            colors[0] = COLOR_BLUE1;
            colors[1] = COLOR_BLUE2;
            colors[2] = COLOR_BLUE3;
	    break;
	case BROWN_CLR:
            colors[0] = COLOR_BROWN1;
            colors[1] = COLOR_BROWN2;
            colors[2] = COLOR_BROWN3;
	    break;
	case PURPLE_CLR:
            colors[0] = COLOR_PURPLE1;
            colors[1] = COLOR_PURPLE2;
            colors[2] = COLOR_PURPLE3;
	    break;
	case RED_CLR:
            colors[0] = COLOR_RED1;
            colors[1] = COLOR_RED2;
            colors[2] = COLOR_RED3;
	    break;
	case DARK_CLR:
            colors[0] = COLOR_DARK1;
            colors[1] = COLOR_DARK2;
            colors[2] = COLOR_DARK3;
	    break;
	default:
	    colors[0] = Color.lightGray;
            colors[1] = Color.gray;
            colors[2] = Color.darkGray;
	    break;
        }
	return colors;
    }

    /**
     * Paint this instance of the Arc.
     *
     * @param g the graphics context.
     */
    protected void draw(Graphics g) {
	// If one of the Handles have moved, then recalc the Vertices
	Point srcPoint = source.getHotPt();
	Point destPoint = destination.getHotPt();
	
	if (!srcPoint.equals(sourcePoint) || 
	    !destPoint.equals(destinationPoint)) {
	    // One of the points has changed location. Update fields
	    sourcePoint = srcPoint;
	    destinationPoint = destPoint;
	    vertices = calcDefaultVertices(source, destination);
	}
	drawConnector(g);
    }

    /**
     * Returns a flag which indicates if the comp is in the container.
     */
    private static boolean inContainer(Component comp, Container cont) {
	Component[] comps = cont.getComponents();
	for (int i = 0; i < comps.length; i++) {
	    if (comp == comps[i]) {
		return true;
	    }
	}
	return false;
    }    

    //===================================================================
    // ARC VERTICE INFORMATION
    //
    // Regions:     1    2    3
    //              4    x    6
    //              7    8    9
    //
    // x - location of initial node/port
    //

    /**
     * Calculate the region id given the two points.
     * @param ptA the first point.
     * @param ptB the second point.
     * @return the region id.
     */
    protected static int calcRegion(Point ptA, Point ptB) {
        int region;

        if (ptA.x < ptB.x) {
            if (ptA.y < ptB.y) region = 9;
            else if (ptA.y > ptB.y) region = 3;
            else region = 6;
        } else if (ptA.x > ptB.x) {
            if (ptA.y < ptB.y) region = 7;
            else if (ptA.y > ptB.y) region = 1;
            else region = 4;
        } else {
            if (ptA.y < ptB.y) region = 8;
            else region = 2;
        }
        
        return region;
    }

    /**
     * Calculate the default vertice locations for this arc.
     * @return a vector containing the vertice locations.
     */
    protected Vector calcDefaultVertices() {
	return calcDefaultVertices(source, destination);
    }

    /**
     * Calculate the set of vertices between two handles.
     */
    protected static Vector calcDefaultVertices(Handle source, Handle destination) {
    	// Special case: if the dest is the container of the source,
	// or the source is the container of the dest, then flip the compass point
	// of the container.
	int sType = source.getHandleType();
	int dType = destination.getHandleType();

	if (inContainer(source.getComponent(), (Container)destination.getComponent())) {
	    dType = Handle.getMirrorType(dType);
	}

	if (inContainer(destination.getComponent(), (Container)source.getComponent())) {
	    sType = Handle.getMirrorType(sType);
	}
	return Arc.calcDefaultVertices(sType, dType, source.getHotPt(), 
				       destination.getHotPt());
    }

    /**
     * Calculate the default vertice locations given the handle/point info.
     * 
     * @param sSide the source handle. Used to determine location
     * @param dSide the dest port. Use -1 if this should be calculated.
     * @param sPt the source point.
     * @param dPt the dest point.
     * @return a vector containing the vertice locations.
     */
    protected static Vector calcDefaultVertices(int sSide, int dSide,
                                          Point sPt, Point dPt) {
        int sHandleSide = sSide;
        int dHandleSide = dSide;

        int region = calcRegion(sPt, dPt);

	if (sHandleSide == -1) {
            switch (region) {
            case 1: sHandleSide = Handle.WEST; break;
            case 2: sHandleSide = Handle.NORTH; break;
            case 3: sHandleSide = Handle.NORTH; break;
            case 4: sHandleSide = Handle.WEST; break;
            case 6: sHandleSide = Handle.EAST; break;
            case 7: sHandleSide = Handle.SOUTH; break;
            case 8: sHandleSide = Handle.SOUTH; break;
            case 9: sHandleSide = Handle.EAST; break;
            default: sHandleSide = Handle.EAST; break;
            }
        }

        if (dHandleSide == -1) {
            switch (region) {
            case 1: dHandleSide = Handle.EAST; break;
            case 2: dHandleSide = Handle.SOUTH; break;
            case 3: dHandleSide = Handle.WEST; break;
            case 4: dHandleSide = Handle.EAST; break;
            case 6: dHandleSide = Handle.WEST; break;
            case 7: dHandleSide = Handle.EAST; break;
            case 8: dHandleSide = Handle.NORTH; break;
            case 9: dHandleSide = Handle.WEST; break;
            default: dHandleSide = Handle.WEST; break;
            }
        }

        if (sHandleSide == Handle.EAST) {
            if (dHandleSide == Handle.EAST)
                return calcVerticesEastToEast(sPt, dPt, region);
            else if (dHandleSide == Handle.WEST)
                return calcVerticesEastToWest(sPt, dPt, region);
            else if (dHandleSide == Handle.SOUTH)
                return calcVerticesEastToSouth(sPt, dPt, region);
            else return calcVerticesEastToNorth(sPt, dPt, region);
        } else if (sHandleSide == Handle.WEST) {
            if (dHandleSide == Handle.EAST)
                return calcVerticesWestToEast(sPt, dPt, region);
            else if (dHandleSide == Handle.WEST)
                return calcVerticesWestToWest(sPt, dPt, region);
            else if (dHandleSide == Handle.SOUTH)
                return calcVerticesWestToSouth(sPt, dPt, region);
            else return calcVerticesWestToNorth(sPt, dPt, region);
        } else if (sHandleSide == Handle.SOUTH) {
            if (dHandleSide == Handle.EAST)
                return calcVerticesSouthToEast(sPt, dPt, region);
            else if (dHandleSide == Handle.WEST)
                return calcVerticesSouthToWest(sPt, dPt, region);
            else if (dHandleSide == Handle.SOUTH)
                return calcVerticesSouthToSouth(sPt, dPt, region);
            else return calcVerticesSouthToNorth(sPt, dPt, region);
        } else {
            if (dHandleSide == Handle.EAST)
                return calcVerticesNorthToEast(sPt, dPt, region);
            else if (dHandleSide == Handle.WEST)
                return calcVerticesNorthToWest(sPt, dPt, region);
            else if (dHandleSide == Handle.SOUTH)
                return calcVerticesNorthToSouth(sPt, dPt, region);
            else return calcVerticesNorthToNorth(sPt, dPt, region);
        }
    }

    /**
     * Calculate vertices for east 2 east connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesEastToEast(Point startPt, Point endPt,
                                                 int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 2:
        case 7:
        case 8:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, endPt.y));
            break;
        case 3:
        case 9:
            v.addElement(new Point(endPt.x + 20, startPt.y));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        case 4:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, startPt.y + 30));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y + 30));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y));
            break;
        case 6:
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y - 30));
            v.addElement(new Point(endPt.x + 20, startPt.y - 30));
            v.addElement(new Point(endPt.x + 20, startPt.y));
            break;
        }
        
        return v;
    }
    /**
     * Calculate vertices for east 2 west connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesEastToWest(Point startPt, Point endPt,
                                                 int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 2:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x - 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 3:
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y));
            break;
        case 4:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, startPt.y + 30));
            v.addElement(new Point(endPt.x - 20, startPt.y + 30));
            v.addElement(new Point(endPt.x - 20, startPt.y));
            break;
        case 6:
            break;
        case 7:
        case 8:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x - 20,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 9:
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y));
            break;
        }

        return v;
    }

    /**
     * Calculate vertices for east 2 south connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesEastToSouth(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 2:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            break;
        case 3:
            v.addElement(new Point(endPt.x, startPt.y));
            break;
        case 4:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, startPt.y + 30));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        case 6:
        case 9:
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        case 7:
        case 8:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        }

        return v;
    }

    /**
     * Calculate vertices for east 2 north connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesEastToNorth(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 2:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 3:
        case 6:
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 4:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20, endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 7:
        case 8:
            v.addElement(new Point(startPt.x + 20, startPt.y));
            v.addElement(new Point(startPt.x + 20,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            break;
        case 9:
            v.addElement(new Point(endPt.x, startPt.y));
            break;
        }

        return v;
    }

    /**
     * Calculate vertices for west 2 east connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesWestToEast(Point startPt, Point endPt,
                                                 int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 7:
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y));
            break;
        case 2:
        case 3:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x + 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        case 4:
            break;
        case 6:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20, startPt.y + 30));
            v.addElement(new Point(endPt.x + 20, startPt.y + 30));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        case 8:
        case 9:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x + 20,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for west 2 west connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesWestToWest(Point startPt, Point endPt,
                                                 int region) {
        Vector v = new Vector();
        
        switch (region) {
        case 1:
        case 7:
            v.addElement(new Point(endPt.x - 20, startPt.y));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 2:
        case 3:
        case 8:
        case 9:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20, endPt.y));
            break;
        case 4:
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y + 30));
            v.addElement(new Point(endPt.x - 20, startPt.y + 30));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 6:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20, startPt.y + 30));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y + 30));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for west 2 south connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesWestToSouth(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
            v.addElement(new Point(endPt.x, startPt.y));
            break;
        case 2:
        case 3:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            break;
        case 4:
        case 7:
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        case 6:
        case 8:
        case 9:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20, endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for west 2 north connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesWestToNorth(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 4:
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 2:
        case 3:
        case 6:
        case 8:
        case 9:
            v.addElement(new Point(startPt.x - 20, startPt.y));
            v.addElement(new Point(startPt.x - 20, endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 7:
            v.addElement(new Point(endPt.x, startPt.y));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for south 2 east connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesSouthToEast(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 4:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y + 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y));
            break;
        case 2:
        case 3:
        case 6:
        case 8:
        case 9:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point(endPt.x + 20, startPt.y + 20));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        case 7:
            v.addElement(new Point(startPt.x, endPt.y));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for south 2 west connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesSouthToWest(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();
        
        switch (region) {
        case 1:
        case 2:
        case 4:
        case 7:
        case 8:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point(endPt.x - 20, startPt.y + 20));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 3:
        case 6:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y + 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y));
            break;
        case 9:
            v.addElement(new Point(startPt.x, endPt.y));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for south 2 south connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesSouthToSouth(Point startPt, Point endPt,
                                                   int region) {
        Vector v = new Vector();
        
        switch (region) {
        case 1:
        case 3:
        case 4:
        case 6:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point(endPt.x, startPt.y + 20));
            break;
        case 2:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point(startPt.x - 30, startPt.y + 20));
            v.addElement(new Point(startPt.x - 30,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(startPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            break;
        case 7:
        case 9:
            v.addElement(new Point(startPt.x, endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        case 8:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point(startPt.x - 30, startPt.y + 20));
            v.addElement(new Point(startPt.x - 30,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            break;
        }

        return v;
    }

    /**
     * Calculate vertices for south 2 north connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesSouthToNorth(Point startPt, Point endPt,
                                                   int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 4:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y + 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 2:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point(startPt.x - 30, startPt.y + 20));
            v.addElement(new Point(startPt.x - 30, endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 3:
        case 6:
            v.addElement(new Point(startPt.x, startPt.y + 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y + 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 7:
        case 9:
            v.addElement(new Point(startPt.x,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.x,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            break;
        case 8:
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for north 2 east connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesNorthToEast(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
            v.addElement(new Point(startPt.x, endPt.y));
            break;
        case 2:
        case 3:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point(startPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x + 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        case 4:
        case 7:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y - 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y));
            break;
        case 6:
        case 8:
        case 9:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point(endPt.x + 20, startPt.y - 20));
            v.addElement(new Point(endPt.x + 20, endPt.y));
            break;
        }

        return v;
    }

    /**
     * Calculate vertices for north 2 west connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesNorthToWest(Point startPt, Point endPt,
                                                  int region) {
        Vector v = new Vector();
        
        switch (region) {
        case 1:
        case 2:
            v.addElement(new Point(startPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x - 20,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 3:
            v.addElement(new Point(startPt.x, endPt.y));
            break;
        case 4:
        case 7:
        case 8:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point(endPt.x - 20, startPt.y - 20));
            v.addElement(new Point(endPt.x - 20, endPt.y));
            break;
        case 6:
        case 9:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y - 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y));
            break;
        }
        
        return v;
    }

    /**
     * Calculate vertices for north 2 south connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesNorthToSouth(Point startPt, Point endPt,
                                                   int region) {
        Vector v = new Vector();

        switch (region) {
        case 1:
        case 3:
            v.addElement(new Point(startPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(endPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            break;
        case 2:
            break;
        case 4:
        case 7:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   startPt.y - 20));
            v.addElement(new Point((startPt.x - endPt.x) / 2 + endPt.x,
                                   endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        case 6:
        case 9:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   startPt.y - 20));
            v.addElement(new Point((endPt.x - startPt.x) / 2 + startPt.x,
                                   endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        case 8:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point(startPt.x + 30, startPt.y - 20));
            v.addElement(new Point(startPt.x + 30, endPt.y + 20));
            v.addElement(new Point(endPt.x, endPt.y + 20));
            break;
        }

        return v;
    }

    /**
     * Calculate vertices for north 2 north connection.
     * @param startPt the starting point.
     * @param endPt the ending point.
     * @param region the region id.
     */
    private static Vector calcVerticesNorthToNorth(Point startPt, Point endPt,
                                                   int region) {
        Vector v = new Vector();
        
        switch (region) {
        case 1:
        case 3:
            v.addElement(new Point(startPt.x, endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 2:
            v.addElement(new Point(startPt.x,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(startPt.x + 30,
                                   (startPt.y - endPt.y) / 2 + endPt.y));
            v.addElement(new Point(startPt.x + 30, endPt.y - 20));
            v.addElement(new Point(endPt.x, endPt.y - 20));
            break;
        case 4:
        case 6:
        case 7:
        case 9:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point(endPt.x, startPt.y - 20));
            break;
        case 8:
            v.addElement(new Point(startPt.x, startPt.y - 20));
            v.addElement(new Point(startPt.x + 30, startPt.y - 20));
            v.addElement(new Point(startPt.x + 30,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            v.addElement(new Point(endPt.y,
                                   (endPt.y - startPt.y) / 2 + startPt.y));
            break;
        }
        
        return v;
    }

}
