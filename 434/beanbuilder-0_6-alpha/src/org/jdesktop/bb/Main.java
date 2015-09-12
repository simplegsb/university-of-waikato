/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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

package org.jdesktop.bb;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.beans.XMLDecoder;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.bb.BeanBuilder;
import org.jdesktop.bb.BeanPanel;

import org.jdesktop.bb.designer.DesignPanel;

import org.jdesktop.bb.ui.SplashScreen;

/**
 * Main class for starting the Builder. Configures and manages the frames.
 *
 * @version 1.13 01/15/03
 * @author  Mark Davidson
 */
public class Main extends WindowAdapter {
    private static BeanBuilder controlPanel;
    private static BeanPanel propertySheet;
    private static DesignPanel designer;

    // XXX Hack to get around desktop size minus the taskbar.
    // affects Windows and probably Linix (worse)
    // Should check for desktop property instead.
    private final static int taskBarSize = 20;

    /**
     * Starts the BeanBuilder with the design and the palette file.
     */
    public Main(String design, String paletteFile) {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        controlPanel = new BeanBuilder();
	controlPanel.setPalette(paletteFile);
	controlPanel.pack();
	Rectangle bounds = controlPanel.getBounds();
	//	controlPanel.setSize(screenSize.width, bounds.height);

	propertySheet = new BeanPanel();
	propertySheet.setLocation(bounds.x, bounds.y + bounds.height);

	bounds = propertySheet.getBounds();
	if ((bounds.y + bounds.height) > screenSize.height) {
	    propertySheet.setSize(bounds.width, 
				  screenSize.height - bounds.y - taskBarSize);
	}

	controlPanel.addWindowListener(this);	

	// Design time management
	designer = new DesignPanel();
	designer.setLocation(bounds.x + bounds.width, bounds.y);
	designer.setSize(300, 300);

	// XXX - hack to get around design type switch
	controlPanel.setDesignPanel(designer);

        if (design.equals(""))  {
            // Create a new design.
            controlPanel.handleNew();
        } else {
            controlPanel.handleOpen(design);
	}

	// XXX - The propertySheet has to be shown after all
	// PropEditors are realized or (sometimes) deadlock occurs.
	// This is a hack because I still haven't sorted out the PropertyTableModel 
	// using the Swing PropertyEditors as a renderer and editor. 
	// I'm not proud of this.
        controlPanel.setVisible(true);
	propertySheet.setVisible(true);
    }

    // Hide other frames when Iconified
    public void windowIconified(WindowEvent evt) {
	Object obj = evt.getSource();
	if (obj == controlPanel) {
	    propertySheet.setVisible(false);
	}
    }

    // Show hidden frames when de-Iconified.
    public void windowDeiconified(WindowEvent evt) {
	Object obj = evt.getSource();
	if (obj == controlPanel) {
	    propertySheet.setVisible(true);
	}
    }
    

    /**
     * Main entry point for the BeanBuilder.
     * 
     * Usage: Main [-i] [-p palettefile.xml] [design.xml]
     *    -i: Interpret the design.xml with the XMLDecoder and exit
     *    -p: use the next argument as the palette file. This file must be valid.
     *    design.xml: A JavaBeans archive that should be loaded into the designer
     *                or interpreted
     */
    public static void main(String[] args)  {
	String fileName = "";
	String paletteFile = "palette.xml";
	boolean startBuilder = true;

	// Interpret arguments
	if (args.length != 0) {
	    // The filename is the last argument
	    fileName = args[args.length - 1];

	    for (int i = 0; i < args.length; i++) {
		if (args[i].equals("-i")) {
		    startBuilder = false;
		    break;
		}
		if (args[i].equals("-p")) {
		    if (args.length > i+1 && args[i+1] != null) {
			paletteFile = args[i+1];
		    }
		}
	    }
	    // The fileName can't be the paletteFile
	    if (fileName.equals(paletteFile)) {
		fileName = "";
	    }
	}

	if (startBuilder) {

	    long start = System.currentTimeMillis();
	    // Load the Object Graph into the Builder.
	    SplashScreen ss = new SplashScreen(new JFrame());
	    ss.setVisible(true);

	    // All dialogs will have LAF frame decorations.
	    JDialog.setDefaultLookAndFeelDecorated(true);
	
	    Main main = new Main(fileName, paletteFile);

	    ss.setVisible(false);
	    ss.dispose();

	    // Startup time
	    System.out.println("Bean Builder Startup Time (ms): " + 
			       (System.currentTimeMillis() - start));
	} 
	else {
	    // Interpret the object graph and then exit
	    try {
		InputStream is = new BufferedInputStream(
				 new FileInputStream(fileName)); 
		XMLDecoder d = new XMLDecoder(is);
		Object o = d.readObject();
	    } catch (IOException ex) {
		System.out.println(ex.getMessage());
	    }
	}
	    
    }
}

