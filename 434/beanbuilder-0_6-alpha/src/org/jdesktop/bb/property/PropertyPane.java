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

package org.jdesktop.bb.property;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

// XXX migrate away from this
import org.jdesktop.bb.ui.CommonUI;

import com.sun.jaf.ui.ActionManager;
import com.sun.jaf.ui.UIFactory;

import org.jdesktop.bb.model.ObjectHolder;
import org.jdesktop.bb.util.StatusBar;

/**
 * The UI for listing, sorting, and setting component
 * properties.
 *
 * @author  John J. Walker
 * @author  Mark Davidson
 */
public class PropertyPane extends JPanel implements ActionListener,
						    TableModelListener,
						    PropertyChangeListener {

    private Object bean;        // Current Bean.
    private Stack beanStack;    // Stack of beans for walking bean hierarchy.

    private JTable table;
    private PropertyColumnModel columnModel;
    private PropertyTableModel tableModel;

    // UI for the property control panel.
    private JLabel nameLabel;
    private JComboBox viewCombo;

    private static final int ROW_HEIGHT = 20;

    // View options.
    private static final String[] VIEW_CHOICES = { "All", "Standard", "Expert", 
						   "Read Only", "Bound", "Constrained", 
						   "Hidden", "Preferred" };

    /**
     * Constructor
     */
    public PropertyPane()  {
        super(new BorderLayout());
        
        tableModel = new PropertyTableModel();
        tableModel.addTableModelListener(this);
        
        columnModel = new PropertyColumnModel();
        table = new JTable(tableModel, columnModel);
        table.setRowHeight(ROW_HEIGHT);
        table.setAutoResizeMode(table.AUTO_RESIZE_LAST_COLUMN);
        table.addMouseListener(new MouseAdapter()  {
            public void mouseClicked(MouseEvent evt)  {
                if (evt.getClickCount() == 2 && 
                        table.getSelectedColumn() == 0)
                    // Double clicking on the first column will call the down
                    // action on the current object.
                    handleDownAction();
            }
        });

        add(createControlPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createNamePanel(), BorderLayout.SOUTH);
    }


    /**
     * Returns the currently selected view option
     *
     * @return one of the PropertyTableModel.VIEW_ constants
     */
    public int getViewOption() {
	return viewCombo.getSelectedIndex();
    }
    
    /** 
     * Creates the panel for selecting the view and sorting order.
     */
    private JToolBar createControlPanel()  {
        viewCombo = CommonUI.createComboBox(VIEW_CHOICES, this, false);
        viewCombo.setSelectedItem(VIEW_CHOICES[PropertyTableModel.VIEW_STANDARD]);

        JToolBar toolbar = UIFactory.getInstance().createToolBar("inspector-toolbar");
        toolbar.addSeparator();
        toolbar.add(viewCombo);
	StatusBar status = StatusBar.getInstance();
	status.registerMouseListener(toolbar.getComponents());

	ActionManager manager = ActionManager.getInstance();
	manager.registerCallback("up-command", this, "handleUpAction");
	manager.registerCallback("down-command", this, "handleDownAction");
	manager.registerCallback("add-command", this, "handleAddAction");
	manager.registerCallback("customizer-command", this, "handleCustomizerAction");

        setButtonState();

        return toolbar;
    }
    
    /** 
     * Handler for UI interactions.
     */
    public void actionPerformed(ActionEvent evt)  {
        Object obj = evt.getSource();
        
        if (obj instanceof JComboBox)  {
            if (obj == viewCombo)  {
                tableModel.setFilter(getViewOption());
            }
        }
    }
    
    
    /** 
     * The name panel shows the current selected item
     */
    private JPanel createNamePanel()  {
        JLabel label = new JLabel("Properties for: ");
        nameLabel = new JLabel("< Current Object >");
        
        JPanel panel = new JPanel();
        FlowLayout layout = (FlowLayout)panel.getLayout();
        layout.setAlignment(FlowLayout.LEFT);
        
        panel.add(label);
        panel.add(nameLabel);
        
        return panel;
    }
    
    /** 
     * Handler for the down action
     */
    public void handleDownAction()  {
        int index = table.getSelectedRow();
        if (index != -1)  {
            Object obj = tableModel.getValueAt(index, PropertyTableModel.COL_VALUE);
            
            if (beanStack == null)  {
                beanStack = new Stack();
            }
            beanStack.push(bean);
            setBean(obj);
        }
        setButtonState();
    }
    
    /** 
     * Handler for the up action
     */
    public void handleUpAction()  {
        if (beanStack != null && !beanStack.empty())  {
            setBean(beanStack.pop());
        }
        setButtonState();
    }
    
    /** 
     * Handle the add gesture. Informs prop change listener to add the selected
     * current property sheet component.
     */
    public void handleAddAction()  {
        int index = table.getSelectedRow();
        if (index != -1)  {
            Object obj = tableModel.getValueAt(index, PropertyTableModel.COL_VALUE);
            
            if (obj != null && !(obj instanceof Component)) {
                String message = obj.getClass().getName();
                message += " sent to design panel";
		StatusBar.getInstance().setMessage(message);
		ObjectHolder.getInstance().add(obj);
            }
        }
    }
    
    /** 
     * Handle the customizer action. Will display a customizer in a dialog
     */
    public void handleCustomizerAction()  {
        Component comp = tableModel.getCustomizer();
        
        if (comp != null)  {
            CustomizerDialog dlg = new CustomizerDialog(comp);
            dlg.setVisible(true);
        }
    }
    
    /** 
     * A customizer dialog which takes a Component which implements the 
     * customizer interface.
     */
    private class CustomizerDialog extends JDialog implements ActionListener {
    
        public CustomizerDialog(Component comp)  {
            super(new JFrame(), "Customizer Dialog");

            Customizer customizer = (Customizer)comp;
            customizer.setObject(bean);

            JPanel okpanel = new JPanel();
            okpanel.add(CommonUI.createButton(CommonUI.BUTTONTEXT_OK, this,
                                               CommonUI.MNEMONIC_OK));
            Container pane = getContentPane();
            pane.add(comp, BorderLayout.CENTER);
            pane.add(okpanel, BorderLayout.SOUTH);
            pack();
            
            CommonUI.centerComponent(this, PropertyPane.this);
        }
    
        public void actionPerformed(ActionEvent evt)  {
            this.dispose();
        }
        
    }

    /** 
     * Sets the state of the up and down buttons based on the contents of the stack.
     */
    private void setButtonState()  {
	ActionManager manager = ActionManager.getInstance();

        if (beanStack == null || beanStack.isEmpty())  {
            manager.setEnabled("up-command", false);
        } else {
            manager.setEnabled("up-command", true);
        }
        
        // Enable customizer button if the model has a customizer.
        manager.setEnabled("customizer-command", tableModel.hasCustomizer());
    }


    /** 
     * Sets the PropertyPane to show the properties of the named bean.
     */
    protected void setBean(Object bean)  {
        this.bean = bean;

        if (bean != null)  {
            nameLabel.setText(bean.getClass().getName());

            tableModel.setObject(bean);
        }
    }


    //
    // Table Model Listener methods
    //
    public void tableChanged(TableModelEvent evt)  {
        // Adjust the preferred height of the row to the the same as
	// the property editor.
	// This seems to be necessary or the # rows doesn't change
	table.setRowHeight(ROW_HEIGHT);
        
        PropertyEditor editor;
        Component comp;
        Dimension prefSize;
        
        for (int i = 0; i < table.getRowCount(); i++) {
            editor = tableModel.getPropertyEditor(i);
            if (editor != null)  {
                comp = editor.getCustomEditor();
                if (comp != null)  {
                    prefSize = comp.getPreferredSize();
                    if (prefSize.height != table.getRowHeight(i))  {
                        table.setRowHeight(i, prefSize.height);
                    }
                }
            }
        }
    }


    //
    // PropertyChangeListener method
    // 

    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        String prop = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();

	if (source == ObjectHolder.getInstance()) {
	    if (prop.equals("selectedItem")) {
		// Sets the selected item for the property sheet.
		beanStack = null;   // Reset the bean stack.
        
		if (newValue != null)  {
		    setBean(newValue);
		    setButtonState();
		}
	    }
	}
    }
}
