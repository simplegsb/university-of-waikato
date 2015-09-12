/*
 * $Id: BeanBuilder.java,v 1.3 2004/07/15 00:25:25 davidson1 Exp $
 *
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

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import java.net.URL;

import javax.swing.*;

import javax.swing.filechooser.FileFilter;

import org.jdesktop.bb.model.ObjectHolder;
import org.jdesktop.bb.palette.Palette;
import org.jdesktop.bb.designer.DesignPanel;

import org.jdesktop.bb.util.StatusBar;
import org.jdesktop.bb.util.SpringLayoutPersistenceDelegate;

import com.sun.jaf.ui.ActionManager;
import com.sun.jaf.ui.UIFactory;

/**
 * Top level frame for the Bean Builder.
 *
 * @author  Mark Davidson
 */
public class BeanBuilder extends JFrame {

    // String constants
    public static final String MAIN_TITLE = AboutDialog.PRODUCT_NAME;
    public static final String TITLE_DESIGN = MAIN_TITLE + " - Design Mode";
    public static final String TITLE_RUNTIME = MAIN_TITLE + " - Runtime Mode";

    // Chooser for file IO
    private JFileChooser chooser;

    // Chooser for IO of Jar files
    private JFileChooser jarchooser;

    // The Component Palette
    private Palette palette;

    private ControlPanel control;

    // Status bar
    private StatusBar status;

    // Current design filename
    private String filename = "TestOut.xml";

    private ObjectHolder objectHolder;

    /**
     * Creates the main UI for the Builder.
     */
    public BeanBuilder() {
        super(TITLE_DESIGN);

	initModels();
	initActions();
	initUI();
    }

    protected void initModels() {
	// Convenient reference to ObjectHolder singleton
	objectHolder = ObjectHolder.getInstance();
	objectHolder.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
		    String propertyName = evt.getPropertyName();
		    
		    if (propertyName.equals("designTime")) {
			setTitle();
		    }
		}
	    });
	
	// XXX - do not retrieve the normal ComponentBeanInfo - which hides properties.
	java.beans.Introspector.setBeanInfoSearchPath(new String[] {});
    }

    protected void initActions() {
	ActionManager manager = ActionManager.getInstance();
        try {
            manager.loadActions(getClass().getResource("/actions.xml"));
        } catch (Exception ioe) {
            System.out.println("ERROR parsing: " + ioe);
        }

	// Register the callback methods on the actions.
	// For each action id

	manager.registerCallback("new-command", this, "handleNew");
	manager.registerCallback("open-command", this, "handleOpen");
	manager.registerCallback("save-command", this, "handleSave");
	manager.registerCallback("load-command", this, "handleLoad");
	manager.registerCallback("palette-command", this, "handleLoadPalette");
	manager.registerCallback("exit-command", this, "handleExit");

	//	manager.registerCallback("design-command", this, "");
	IconHandler handler = new IconHandler();

	manager.registerCallback("icon32-color-command", handler, "handleIconColor32");
	manager.registerCallback("icon16-color-command", handler, "handleIconColor16");
	manager.registerCallback("icon32-mono-command", handler, "handleIconMono32");
	manager.registerCallback("icon16-mono-command", handler, "handleIconMono16");

	manager.registerCallback("help-command", this, "handleAbout");
	manager.registerCallback("about-command", this, "handleAbout");

	// The initial state of the application: 
	manager.setSelected("design-command", true);
	manager.setSelected("icon32-color-command", true);
    }

    /**
     * Controller class to handle icon selection changes.
     */
    public class IconHandler {
	
	public void handleIconColor32(boolean selected) {
	    if (selected) {
		handleIconChange(BeanInfo.ICON_COLOR_32x32);
	    }
	}
	
	public void handleIconColor16(boolean selected) {
	    if (selected) {
		handleIconChange(BeanInfo.ICON_COLOR_16x16);
	    }
	}

	public void handleIconMono32(boolean selected) {
	    if (selected) {
		handleIconChange(BeanInfo.ICON_MONO_32x32);
	    }
	}

	public void handleIconMono16(boolean selected) {
	    if (selected) {
		handleIconChange(BeanInfo.ICON_MONO_16x16);
	    }
	}
	/**
	 * Sets the type of icon in the palette
	 * @param icon One of BeanInfo.ICON_...
	 */
	public void handleIconChange(int icon)  {
	    palette.setIconType(icon);
	}
    }


    protected void initUI() {
	// Look and Feel decorated Frames.
	setUndecorated(true);
	getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        status = StatusBar.getInstance();

	UIFactory factory = UIFactory.getInstance();

	JMenuBar menubar = factory.createMenuBar("main-menu");
	if (menubar != null) {
	    setJMenuBar(menubar);
	    // iterate over all the children in the menuBar
	    // and add the StatusBar as a MouseListener.
	    status.registerMouseListener(menubar.getSubElements());
	}
	
	JToolBar toolbar = factory.createToolBar("main-toolbar");
	if (toolbar != null) {
	    toolbar.setRollover(true);
	    getContentPane().add(toolbar, BorderLayout.NORTH);
	    status.registerMouseListener(toolbar.getComponents());
	}

	control = new ControlPanel();

	JPanel panel = new JPanel(new BorderLayout());
	panel.add(control, BorderLayout.NORTH);
	panel.add(status, BorderLayout.SOUTH);

        Container pane = getContentPane();
        pane.add(panel, BorderLayout.SOUTH);
    }


    // XXX - hack! we really need some way of externalizing the action
    // associated with changing the type pf editor.
    public void setDesignPanel(DesignPanel panel) {
	control.setDesignPanel(panel);
    }

    /**
     * Sets the palette from a well formed XML document. The palette will be
     * loaded as a resource from the classpath first. If that fails then it
     * will be loaded from disk.
     *
     * Note: if the UI hasn't shown yet, you may have to call pack()
     * after this method is called.
     *
     * @param paletteFile an xml document which represents the tab and palette
     * items.
     */
    public void setPalette(String paletteFile) {
	// Palette loaded from a resource. You can force loading
	// of an alternative "palette.xml" if that file is at the head of
	// the classpath.
	ClassLoader cl = Thread.currentThread().getContextClassLoader();
	URL paletteURL = cl.getResource(paletteFile);

	if (paletteURL == null) {
	    // This URL is not in the classpath load from disk
	    try {
		File file = new File(paletteFile);
		paletteURL = file.toURL();
	    } catch (Exception ex) {
		setStatusLine(ex.getMessage());
		return;
	    }
	}

	if (paletteURL == null) {
	    setStatusLine("Can't find Palette file: " + paletteFile);
	    return;
	}

	// Remove the old palette if it exists.
	Container pane = getContentPane();
	if (palette != null) {
	    pane.remove(palette);
	}

        palette = new Palette(paletteURL);
	Palette.setInstance(palette);
	pane.add(palette, BorderLayout.CENTER);
    }

    /**
     * Handles all the cleanup for closing.
     */
    public void handleExit()  {
        this.dispose();
        System.exit(0);
    }

    /**
     * Creates a new design
     */
    public void handleNew()  {
        this.filename = null;
        setTitle();

	Object root = null;

	// Should provide a choice of items to instantiate
	try {
	    Class rootClass = JFrame.class;
	    String newRoot = System.getProperty("builder.new.root");
	    if (newRoot != null) {
		rootClass = Class.forName(newRoot);
	    }
	    root = rootClass.newInstance();

	} catch (Exception ex) {
	    System.out.println("Error creating root object: " + ex.getMessage());
	    ex.printStackTrace();
	}

	if (root != null) {
	    objectHolder.setRoot(root);
	}
    }

    /**
     * Presents a file chooser to load a palette file from disk.
     * TODO: This is almost the same as handleOpen. Should consolidate.
     */
    public void handleLoadPalette() {
        JFileChooser chooser = getFileChooser();

        if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String filename = selectedFile.getAbsolutePath();

            String extension = BuilderFileFilter.getExtension(filename);
            if (extension == null || extension.equals(""))  {
                filename += ".";

                FileFilter filter = chooser.getFileFilter();
                if (filter instanceof BuilderFileFilter)  {
                    // Use the selected item to set the extension.
                    filename += ((BuilderFileFilter)filter).getExtension();
                }
            }
            setPalette(filename);
        }
    }


    /**
     * Opens an archive from disk using the JFileChooser.
     */
    public void handleOpen()  {
        JFileChooser chooser = getFileChooser();

        if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String filename = selectedFile.getAbsolutePath();

            String extension = BuilderFileFilter.getExtension(filename);
            if (extension == null || extension.equals(""))  {
                filename += ".";

                FileFilter filter = chooser.getFileFilter();
                if (filter instanceof BuilderFileFilter)  {
                    // Use the selected item to set the extension.
                    filename += ((BuilderFileFilter)filter).getExtension();
                }
            }
            handleOpen(filename);
        }
    }

    /**
     * Reads a design into the builder.
     * @param filename Name of the file without the full path i.e., "TestOut.xml"
     */
    public void handleOpen(String filename)  {

        InputStream in = null;
        try {
             in = new FileInputStream(filename);
        } catch (Exception ex1) {
	    // Filename may be in the classpath. 
	    ClassLoader cl = Thread.currentThread().getContextClassLoader();
	    in = cl.getResourceAsStream(filename);
	    if (in == null) {
		// Send a message to the status bar and create a new design.
		setStatusLine("File not found: " + filename);
		handleNew();
		return;
	    }
        }

        String extension = BuilderFileFilter.getExtension(filename);
        XMLDecoder decoder = null;

        try {
            if (extension.equals(XMLFileFilter.EXT)) {
                decoder = new XMLDecoder(in);
            } else {
                setStatusLine("File extension not recognized: " + filename);
                handleNew();
                return;
            }
        } catch (Exception ex) {
            setStatusLine("IO exception for: " + filename);
            handleNew();
            return;
        }

        Object root = null;
        try {
            root = decoder.readObject();
            decoder.close();
        } catch (Exception ex) {
            setStatusLine("Exception reading object: " + filename);
            handleNew();
            return;
        }

        if (root != null)  {
	    objectHolder.setRoot(root);
	}

	this.filename = filename;
	setTitle();
    }

    /**
     * Saves an object graph to the class variable filename. If filename is
     * null or empty then the file chooser is used to select a filename.
     * Note: This method has the side effect of setting the class variable
     *       filename
     */
    public void handleSave()  {
        if (filename == null || filename.equals(""))  {
            filename = getFilename();
            if (filename == null || filename.equals(""))  {
                return;
            }
        }
        handleSave(filename);
    }

    /**
     * Saves an object graph to the filename selected with JFileChooser.
     * Note: This method has the side effect of setting the class variable
     *       filename.
     */
    public void handleSaveAs()  {
        String retFilename = getFilename();
        if (retFilename == null || retFilename.equals(""))  {
            return;
        }
        this.filename = retFilename;

        handleSave(filename);
    }

    /**
     * Gets a file name with an extension using the FileChooser dialog.
     * @return User selected filename or the empty string if the operation
     *         was cancelled.
     */
    private String getFilename()  {
        JFileChooser chooser = getFileChooser();
        String filename = "";

        // Prompt for a filename.
        if (chooser.showSaveDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            filename = selectedFile.getAbsolutePath();

            String ext = BuilderFileFilter.getExtension(selectedFile);
            if (ext == null)  {
                filename += ".";

                FileFilter filter = chooser.getFileFilter();
                if (filter instanceof BuilderFileFilter)  {
                    // Use the selected item to set the extension.
                    filename += ((BuilderFileFilter)filter).getExtension();
                } else {
                    // File will be an XML file by default.
                    filename += XMLFileFilter.EXT;
                }
            }
        }

        return filename;
    }

    /**
     * Saves the object graph that was constructed in the design panel
     * out to disk.
     * @param filename Name of the file to save as.
     */
    public void handleSave(String filename)  {
        try {
            setStatusLine("Writing " + filename + "...");
            BufferedOutputStream out =
		new BufferedOutputStream(new FileOutputStream(filename));

            String ext = BuilderFileFilter.getExtension(filename);

            XMLEncoder encoder = new XMLEncoder(out);

	    // Set the very complicated persistence delegate for SpringLayout.
	    SpringLayoutPersistenceDelegate slpd = new SpringLayoutPersistenceDelegate();	 
	    slpd.configure(encoder);

	    objectHolder.setDesignTime(false);

	    encoder.writeObject(objectHolder.getRoot());
	    encoder.close();

	    objectHolder.setDesignTime(true);

            this.filename = filename;
            setTitle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Pops up a jar file chooser to select a jar file.
     */
    public void handleLoad()  {
        if (jarchooser == null)  {
            // File chooser for jar files
            jarchooser = new JFileChooser(new File(".").getAbsolutePath());
            jarchooser.addChoosableFileFilter(new JarFileFilter());
        }

        if (jarchooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jarchooser.getSelectedFile();
            handleLoad(selectedFile.getAbsolutePath());
        }

    }

    /**
     * Loads the jar file.
     */
    public void handleLoad(String filename)  {
        palette.addJarFile(filename);
    }

    /**
     * Displays the online help contents.
     */
    public void handleHelp()  {
//        HelpBrowser help = new HelpBrowser(getFrame());
//        help.showHelpContents();
        setStatusLine("Help not implemented yet");
    }

    /**
     * Displays the About box.
     */
    public void handleAbout()  {
        AboutDialog dlg = new AboutDialog(new JFrame());
        dlg.setVisible(true);
    }


    /**
     * Sets the text of the status bar
     */
    public void setStatusLine(String message) {
	status.setMessage(message);
    }

    /**
     * Sets the title of the frame based on the current state.
     */
    public void setTitle()  {
        String title;

        if (Beans.isDesignTime())  {
            title = TITLE_DESIGN;
        } else {
            title = TITLE_RUNTIME;
        }

        if (filename != null)  {
            title += " [" + filename + "]";
        }

        super.setTitle(title);
    }

    /**
     * Returns the Top level frame for the application.
     */
    public JFrame getFrame() {
	return this;
    }


    /**
     * Gets the file chooser for saving/loading documents.
     * This is the file chooser for Open and Save Actions.
     */
    private JFileChooser getFileChooser()  {
        if (chooser == null)  {
	    // Open to the current dir
	    chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.addChoosableFileFilter(new XMLFileFilter());
        }
        return chooser;
    }



}

/**
 * Filter for XML files *.xml
 */
class XMLFileFilter extends BuilderFileFilter  {

    public static final String EXT = "xml";

    public String getExtension()  {
        return EXT;
    }

    /** Returns the description of this filter */
    public String getDescription()  {
        return "XML Archives (*.xml)";
    }

}

/**
 * Filter for Jar files *.jar
 */
class JarFileFilter extends BuilderFileFilter  {

    public static final String EXT = "jar";

    public String getExtension()  {
        return EXT;
    }

    /** Returns the description of this filter */
    public String getDescription()  {
        return "Java Archives (*.jar)";
    }

}

/**
 * Generic FileFilter to handle Builder File types.
 */
abstract class BuilderFileFilter extends FileFilter  {

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     */
    public boolean accept(File f)  {
        if (f != null)  {
            if (f.isDirectory())  {
                return true;
            }
        }
        String extension = getExtension(f);
        if (extension != null && extension.equals(getExtension()))
            return true;

        return false;
    }

    /** Returns the extension for the file */
    public static String getExtension(File file)  {
        if (file != null)  {
            return getExtension(file.getName());
        }
        return null;
    }

    /** Returns the extension for the file */
    public static String getExtension(String filename)  {
        if (filename != null)  {
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < (filename.length() - 1))  {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
     * Returns the extension for this filter.
     */
    public abstract String getExtension();

    /**
     * Determines if the filename is the type described by the FileFilter.
     */
    public boolean isFileType(String filename)  {
        String ext = getExtension(filename);
        if (ext != null && ext.length() > 0)  {
            return getExtension().equals(ext);
        } else {
            return false;
        }
    }
}

