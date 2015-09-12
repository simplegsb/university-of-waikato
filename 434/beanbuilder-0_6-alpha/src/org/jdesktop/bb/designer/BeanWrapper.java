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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * This is a component class that wraps a non-visual bean.
 * 
 * @author Mark Davidson
 * @author John Cho (for the great "microchip" rendering).
 */
public class BeanWrapper extends JComponent  {
    
    // Static class variables.
    public static final int WRAPPER_SIZE = 50;
    public static final int BORDER_SIZE = 10;

    private final static Color bkgnd = new Color(140, 210, 189);
    private final static Color frgnd = new Color(50, 125, 125);

    private static Font font = new Font("dialog", Font.PLAIN, 10);

    private Dimension preferredSize;

    // Object that this visual wrapper represents
    private Object bean;
    private String text;

    public BeanWrapper()  {
        this(null);
    }

    public BeanWrapper(Object bean)  {
        setBean(bean);
        setSize(getPreferredSize());
    }

    public void setBean(Object bean)  { 
	this.bean = bean;

	String fullName = bean.getClass().getName();
        int i = fullName.lastIndexOf('.');

        setText(fullName.substring(i + 1));
        setToolTipText(fullName);
    }

    public void setText(String text) {
	this.text = text;
	this.preferredSize = null; // so that pref size is recalculated
    }

    public Object getBean()  {
        return bean;
    }

    public String getText() {
	return text;
    }

    public Font getFont() {
	return font;
    }

    /**
     * Render the component. Pixel pushing by John Cho.
     */
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;

	int x = 0;
	int y = 0;
	int w = getSize().width;
	int h = getSize().height;

        int x2 = w + x;
        int y2 = h + y;

        g.setColor(bkgnd);
        g.fillRect(x, y, w, h);
        g.setColor(Color.black);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(Color.white);
        g.drawLine(x + 1, y + 1, x + w - 2, y + 1);
        g.drawLine(x + 1, y + 1, x + 1, y + h - 2);
        g.setColor(frgnd);
        g.drawLine(x + 2, y + h - 2, x + w - 2, y + h - 2);
        g.drawLine(x + w - 2, y + 2, x + w - 2, y + h - 2);
        for (int i = x + 6; i < x + w - 7; i += 4) {
            g.drawLine(i, y + 2, i, y + h - 3);
            g.drawLine(i + 1, y + 2, i + 1, y + h - 3);
        }
        for (int j = y + 6; j < y + h - 7; j += 4) {
            g.drawLine(x + 2, j, x + w - 3, j);
            g.drawLine(x + 2, j + 1, x + w - 3, j + 1);
        }
        g.setColor(Color.lightGray);
        g.fillRect(x + 5, y + 5, w - 10, h - 10);
        g.setColor(Color.black);
        g.drawLine(x + 4, y + 5, x + 4, y + h - 6);
        g.drawLine(x + 5, y + 4, x + w - 6, y + 4);
        g.drawLine(x + w - 5, y + 5, x + w - 5, y + h - 6);
        g.drawLine(x + 5, y + h - 5, x + w - 6, y + h - 5);
        g.setColor(Color.white);
        g.drawLine(x + 5, y + 5, x + w - 6, y + 5);
        g.drawLine(x + 5, y + 5, x + 5, y + h - 6);
        g.setColor(Color.darkGray);
        g.drawLine(x + 6, y + h - 6, x + w - 6, y + h - 6);
        g.drawLine(x + w - 6, y + 6, x + w - 6, y + h - 6);

	Font oldFont = g.getFont();
	g2.setFont(font);
	LineMetrics lm = font.getLineMetrics(getText(), g2.getFontRenderContext());
	g2.drawString(getText(), x + BORDER_SIZE, y + BORDER_SIZE + (int)lm.getAscent());
	g2.setFont(oldFont);
    }

    public Dimension getPreferredSize()  {
	if (preferredSize == null) {
	    Font font = getFont();
	    FontRenderContext frc = new FontRenderContext(new AffineTransform(),
							  false, false);
	    Rectangle2D rect = font.getStringBounds(text, frc);

	    int width = (int)rect.getWidth() + 2 * BORDER_SIZE;
	    int height = (int)rect.getHeight() + 2 * BORDER_SIZE;
	    preferredSize = new Dimension(width, height);
	}
        return preferredSize;
    }
}
