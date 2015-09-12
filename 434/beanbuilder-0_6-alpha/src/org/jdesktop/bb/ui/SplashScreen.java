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

package org.jdesktop.bb.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ImageIcon;

/**
 * This class puts up the splash screen.
 *
 * @author John Cho
 * @author Mark Davidson
 */
public class SplashScreen extends Window {
    
    private ImageIcon screen;

    /**
     * Constructor.
     * @param f the parent frame.
     */
    public SplashScreen(Frame f) {
        super(f);
        setBackground(Color.white);

        java.net.URL url = this.getClass().getResource("resources/SplashScreen.jpg");
	if (url != null) {
	    screen = new ImageIcon(url);
	    MediaTracker mt = new MediaTracker(this);
	    mt.addImage(screen.getImage(), 0);
	    try {
		mt.waitForAll();
	    } catch(Exception ex) {
	    }
	}
    }

    /**
     * Override the setVisible call.
     * @param val show or not show.
     */
    public void setVisible(boolean val) {
	if (screen == null) {
	    return;
	}
        if (val == true) {
            setSize(screen.getIconWidth(), screen.getIconHeight());
            setLocation(-500, -500);
            super.setVisible(true);
        
            Dimension d = getToolkit().getScreenSize();
            Insets i = getInsets();
            int w = screen.getIconWidth() + i.left + i.right;
            int h = screen.getIconHeight() + i.top + i.bottom;
            setSize(w, h);
            setLocation(d.width / 2 - w / 2, d.height / 2 - h / 2);
        } else {
            super.setVisible(false);
        }
    }

    /**
     * Override the paint call.
     * @param g the graphics context.
     */
    public void paint(Graphics g) {
        if (screen != null) {
            Dimension d = getSize();
            g.setColor(Color.black);
            g.drawRect(0, 0, d.width - 1, d.height - 1);
            g.drawImage(screen.getImage(), 1, 1, this);
        }
    }

}
