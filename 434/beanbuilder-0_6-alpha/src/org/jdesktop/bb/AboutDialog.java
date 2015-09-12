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
package org.jdesktop.bb;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MediaTracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;

import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import org.jdesktop.bb.ui.CommonUI;

/**
 * About box with information and a listing of System properties
 *
 * @version 1.15 02/27/02
 * @author  Mark Davidson
 */
public class AboutDialog extends JDialog implements ActionListener {

    // These strings should be in a singleton product info class.
    public static final String TITLE = "About The Bean Builder";
    public static final String PRODUCT_NAME = "The Bean Builder";
    public static final String VERSION = "Version: 1.0-beta January 2002";

    private static final String COLUMN_PROP = "Property";
    private static final String COLUMN_VALUE = "Value";
    
    private static final String BORDERTEXT_PROPS = "System Properties";

    private static final String BUTTONTEXT_PROPS = "Properties";
    private static final char MNEMONIC_PROPS = 'P';

    private static ImageIcon screen;
    
    private JButton okButton;
    private JButton propsButton;
    
    private JTable table;
    // Data model should be updated on notification.
    private AboutTableModel dataModel;

    /**
     * ctor for About Box
     */
    public AboutDialog(JFrame parent) {
        super(parent, TITLE, true);

        if (screen == null) {
	    URL url = this.getClass().getResource("/images/SplashScreen.jpg");
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
    
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
    
        pane.add(CommonUI.createLabel(VERSION), BorderLayout.NORTH);
	pane.add(new JLabel(screen), BorderLayout.CENTER);
        pane.add(createButtonPanel(), BorderLayout.SOUTH);
    
        addWindowListener(new WindowAdapter()  {
            public void windowClosing(WindowEvent evt)  {
                JDialog dialog = (JDialog)evt.getSource();
                dialog.dispose();
            }
        });
    
        this.pack();
        // Center the window.
        CommonUI.centerComponent(this);
    }
    
    private JPanel createTitlePanel()  {
        JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(screen));
        
        return panel;
    }

    private JPanel createPropertiesPanel()  {
        // Create the data model
        Properties props = System.getProperties();
	Iterator keys = props.keySet().iterator();
        String key;
	
	// Store key values in a TreeMap which will sort the
	// keys alphabetically.
	TreeMap properties = new TreeMap();

        while (keys.hasNext()) {
            key = (String)keys.next();
	    properties.put(key, props.getProperty(key));
        }
	dataModel = new AboutTableModel(properties);

        DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
        TableColumn column = new TableColumn();
        column.setHeaderValue(COLUMN_PROP);
        column.setPreferredWidth(150);
        column.setMinWidth(25);
        columnModel.addColumn(column);
    
        column = new TableColumn(1);
        column.setHeaderValue(COLUMN_VALUE);
        column.setPreferredWidth(200);
        columnModel.addColumn(column);
    
	// Put the table and the UI together.
        table = new JTable(dataModel, columnModel);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(350, 200));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(pane, BorderLayout.CENTER);
        panel.setBorder(CommonUI.createBorder(BORDERTEXT_PROPS));
        
        return panel;
    }
    
    private JPanel createButtonPanel()  {
        okButton = CommonUI.createButton(CommonUI.BUTTONTEXT_OK, this,
                                         CommonUI.MNEMONIC_OK);
        propsButton = CommonUI.createButton(BUTTONTEXT_PROPS, this,
					    MNEMONIC_PROPS);
        JPanel panel = new JPanel();
        panel.add(okButton);
	panel.add(propsButton);
        
        return panel;
    }
    
    // ActionListener method.
    public void actionPerformed(ActionEvent evt)  {
	Object source = evt.getSource();
	if (source == okButton) {
	    this.dispose();
	}
	else if (source == propsButton) {
	    final JDialog dialog = new JDialog(this, 
					       System.getProperty("java.runtime.name") + 
					       System.getProperty("java.runtime.version"));

	    dialog.getContentPane().add(createPropertiesPanel(), BorderLayout.CENTER);
	    dialog.getContentPane().add(CommonUI.createButton(CommonUI.BUTTONTEXT_OK, 
		      new ActionListener() {
			      public void actionPerformed(ActionEvent evt) {
				  dialog.dispose();
			      }
			  }, CommonUI.MNEMONIC_OK), BorderLayout.SOUTH);
	    dialog.pack();
	    CommonUI.centerComponent(dialog);
	    dialog.setVisible(true);
	}
    }

    /**
     * TableModel for the AboutBox properties table.
     * Uses a TreeMap as it's data structure.
     *
     * This model is immutable.
     */
    private class AboutTableModel extends AbstractTableModel {
	private TreeMap treeMap;
	
	// keys and values cache from the map.
	private Object[] keys;
	private Object[] values;

	public AboutTableModel(TreeMap map) {
	    this.treeMap = map;

	    // We need to convert the TreeMap to a
	    // couple of arrays to satisfy the 
	    // indices arguments of the getValueAt method.
	    
	    keys = new Object[getRowCount()];
	    values = new Object[getRowCount()];

	    keys = treeMap.keySet().toArray();
	    values = treeMap.values().toArray();
	}
	
	//
	// AbstractTableModel methods
	//
	
	public int getRowCount() {
	    return treeMap.size();
	}

	public int getColumnCount() {
	    return 2;
	}

	public Object getValueAt(int row, int column) {
	    if (column == 0) {
		// Keys
		return keys[row];
	    } 
	    else if (column == 1) {
		return values[row];
	    }
	    return null;
	}
    }

    // For testing
    public static void main(String[] args)  {
        JFrame frame = new JFrame();
        frame.setLocation(200, 200);
        AboutDialog dlg = new AboutDialog(frame);
        dlg.setVisible(true);
        
        System.exit(0);
    }
    
}
