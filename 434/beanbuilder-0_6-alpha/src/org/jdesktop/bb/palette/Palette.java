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
package org.jdesktop.bb.palette;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.beans.Beans;
import java.beans.BeanInfo;

import java.lang.reflect.Method;

import java.net.URL;

import java.io.IOException;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.Iterator;

import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jdesktop.bb.util.JarClassLoader; // jar file ClassLoader
import org.jdesktop.bb.util.StatusBar;

/**
 * The main palette which is responsible for the management of Beans.
 * The Palette is a Tabbed Pane which contains panes of PalettePanels.
 *
 * @version 1.18 02/27/02
 * @author  Mark Davidson
 */
public class Palette extends JTabbedPane implements MouseListener {

    // Selected palette item
    private PaletteItem selectedItem;
    
    // Held value of the type of icon.
    private static int icontype = BeanInfo.ICON_COLOR_32x32;

    // Panel in which all user beans are added.
    private PalettePanel userpanel;

    // Shared instance of the Palette
    private static Palette palette;

    /**
     * Default ctor
     */
    public Palette() {
	this(Thread.currentThread().getContextClassLoader().getResource("palette.xml"));
     }

    /**
     * Constructs a new palette from a URL which represents a path to
     * a palette file.
     */
    public Palette(URL paletteFile) {
	new PaletteLoader(this).load(paletteFile);
    }

    /**
     * Returns the shared Palette.
     * 
     * Note: This instance should not be held. It should be used and 
     * then returned since a Palette can change.
     */
    public static Palette getInstance() {
	if (palette == null) {
	    palette = new Palette();
	}
	return palette;
    }

    /**
     * Sets the shared instance of the palette.
     */
    public static void setInstance(Palette p) {
	palette = p;
    }

    /**
     * Sets the type of icon in the palette
     * @param icontype One of BeanInfo.ICON_... Contants which indicate which
     *                 icon to use.
     * Note: It may be better to have the Palette listen to the RadioButton menu
     * command instead of tunneling from BeanTest.
     */
    public void setIconType(int icontype)  {
        if (this.icontype != icontype)  {
            this.icontype = icontype;

            Component[] comps = getComponents();

            PalettePanel panel;
            for (int i = 0; i < comps.length; i++) {
                panel = (PalettePanel)comps[i];
                panel.setIconType(icontype);
            }
        }
    }

    /**
     * Adds the Java Beans in the jar file to the user panel.
     * First it loads all the Beans and resources into the JarClassLoader. 
     * Then it determines which resources are beans (Java-Bean: True) 
     * from the Manifest.
     *
     * @param filename Full path to the jar file.
     */
    public void addJarFile(String filename)  {
        JarClassLoader loader = JarClassLoader.getJarClassLoader();
        if (loader == null)  {
            // XXX - There has  to be a better way of doing this!!
            // The problem is that there is no way to specify a null URL or
            // no arg JarClassLoader. The solution is to define a class
	    // loader for the application.
            try {
                loader = new JarClassLoader(new URL("file:" + filename));
                loader.setJarClassLoader(loader);
            } catch (Exception ex) {
                // XXX - debug
                System.out.println("Bad Class Loader");
                ex.printStackTrace();
                return;
            }
        } else {
            loader.addJarFile(filename);
        }

        JarFile jarfile;
        Manifest mf;

        try {
            jarfile = new JarFile(filename);
            mf = jarfile.getManifest();
        } catch (IOException ex) {
            // XXX - report to a UI.
            System.out.println("Error opening jar file: " + filename);
            return;
        }

        boolean beanAdded = false;
        
        // First of all, determine if there are beans mixed in with the main 
        // manifest block. buttons.jar had this problem in BeanBox 1.0
        Attributes attribs = mf.getMainAttributes();
        if (attribs != null)  {
            // Determine if this is a java bean.
            String isJavaBean = attribs.getValue(loader.Attributes_Name_JAVA_BEAN);

            if (isJavaBean != null && isJavaBean.equalsIgnoreCase("True"))  {
                String beanName = attribs.getValue(loader.Attributes_Name_NAME);
                addUserBean(loader, beanName);
                beanAdded = true;
            }
        }

        // Walk the list of Manifest attributes. The keys to the attributes
        // Are the names of the Jar entries. i.e. sunw/demo/buttons/OurButton.class.
        Iterator iterator = mf.getEntries().keySet().iterator();
        while (iterator.hasNext()) {
            String beanName = (String)iterator.next();

            attribs = mf.getAttributes(beanName);

            if (attribs != null)  {
                String isJavaBean = attribs.getValue(loader.Attributes_Name_JAVA_BEAN);

                if (isJavaBean != null && isJavaBean.equalsIgnoreCase("True"))  {
                    addUserBean(loader, beanName);
                    beanAdded = true;
                }
            }
        }
        
        if (beanAdded == true)  {
            setSelectedComponent(userpanel);
            userpanel.repaint();
        }
    }

    /** 
     * Adds the bean to the user tab on the panel. This method determines if the
     * bean is from a class or from a serializable file.
     *
     * @param loader ClassLoader to use for instantiation and class resolution
     * @param name Name of the bean and resource to use. The format should be the
     *             full path to the resouce i.e. sunw/demo/buttons/OurButton.class.
     */
    public void addUserBean(ClassLoader loader, String name)  {
        // Create the user panel if it hasn't been created yet.
        if (userpanel == null)  {
            userpanel = new PalettePanel("User");
            userpanel.setMouseListener(this);
            addTab("User", userpanel);
        }

        String classname = name;

        if (name.endsWith(".class"))  {
            classname = name.substring(0, name.length() - 6);
        } else if (name.endsWith(".ser")) {
            // Must deserialize the class.
            classname = name.substring(0, name.length() - 4);
        }
        classname = classname.replace('/', '.');

        PaletteItem item = null;

        try {
            Class cls = loader.loadClass(classname);
            item = new PaletteItem(cls);
        } catch (ClassNotFoundException ex) {
            // The object may be a serialized object. Instantiate it and
            // load it on the palette. This is for backwards compatibility
            // with serialized components on the BeanBox.
            try {
                Object obj = Beans.instantiate(loader, classname);
                item = new PaletteItem(obj, classname);
            } catch (Exception ex2) {
                // XXX - debug
                System.out.println("Palette.addUserBean: Can't instantiate or load " + classname);
            }
        }

        if (item != null)  {
            userpanel.addItem(item);
        }
    }

    /**
     * Instantiates the currently selected palette bean returns the object
     */
    public Object getNewPaletteBean() {
        Object bean = null;

        if(selectedItem != null) {
            try {
                bean = selectedItem.makeNewBean();

                Method methods[]= bean.getClass().getMethods();

                // Set the text if set text exists.
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals("setText"))  {
                        // Get the root class name of the class.
                        String name = bean.getClass().getName();
                        int index = name.lastIndexOf('.');
                        String rootName = name.substring(index + 1);

                        methods[i].invoke(bean, new Object[] { rootName });
                    }
                }
            } catch (Exception e) {
                // XXX - should try to do something intelligent here
                System.out.println("Instantiation error: " + e.getMessage());
                e.printStackTrace();
            }
            // Deselect palette item after instantiation
            unselect();
        }
        return bean;
    }

    // Object selection
    public void selectItem(PaletteItem item) {
        selectedItem = item;
        Component root = SwingUtilities.getRoot(this);
        root.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    
    /** 
     * Indicates if a valid item on the palette is selected.
     * @return True if a Palette item is selected.
     */
    public boolean isItemSelected()  {
        return (selectedItem != null);
    }

    // Object deselection
    private void unselect() {
        if(selectedItem != null) {
            selectedItem = null;
            Component root = SwingUtilities.getRoot(this);
            root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Send the description of the command to the status bar.
     */
    public void mouseEntered(MouseEvent evt)  {
        Object obj = evt.getSource();

        if (obj instanceof PaletteButton)  {
            PaletteButton button = (PaletteButton)obj;
            PaletteItem item = button.getPaletteItem();

            if (item != null)  {
                String message = item.getShortDescription();
		StatusBar.getInstance().setMessage(message);
            }
        }
    }

    public void mouseExited(MouseEvent evt)  {
	// Empty the status bar
	StatusBar.getInstance().setMessage("");
    }

    public void mouseClicked(MouseEvent evt) {
        Object obj = evt.getSource();

        // For the PaletteButton,
        if (obj instanceof PaletteButton) {
            PaletteButton button = (PaletteButton)obj;
            selectItem(button.getPaletteItem());
        }
    }

    public void mousePressed(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt) {}
}



