/*
 * $Id: ControlPanel.java,v 1.2 2004/07/15 00:05:22 davidson1 Exp $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.IOException;

import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jdesktop.bb.designer.DesignPanel;

import org.jdesktop.bb.model.ObjectHolder;
import org.jdesktop.bb.util.StatusBar;

import org.jdesktop.bb.ui.CommonUI;

import com.sun.jaf.ui.ActionManager;

/**
 * Control panel for the Builder
 *
 * @version 1.9 01/15/03
 * @author  Mark Davidson
 */
public class ControlPanel extends JPanel {

    private JTextField beanTF;
    private JComboBox comboBox;
    // XXX - bad dependency
    private DesignPanel designPanel;

    private ObjectHolder objectHolder;

    public ControlPanel() {
        KeyHandler handler = new KeyHandler();
        beanTF = CommonUI.createTextField("", handler);
        handler.setTextField(beanTF);
	
	objectHolder = ObjectHolder.getInstance();

        JLabel label = CommonUI.createLabel("Instantiate Bean ", 'B', beanTF);

        // Configure the design checkbox. Note: the checkbox will not update to
        // reflect the same state as the menu item because the "selected"
        // PropertyChangeEvent is never fired. Hopefully this will be fixed in the
        // near future.
	ActionManager manager = ActionManager.getInstance();
        Action action = manager.getAction("design-command");

	// Must be declared final for the implementation of the PropertyChangeListener
        final JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(manager.isSelected("design-command"));
        checkBox.setAction(action);

	// Add the action as an item listener. This may be an implementation
	// detail since we know that a StateChangeAction is an ItemListener.
	if (manager.isStateChangeAction("design-command") && 
	    action instanceof ItemListener) {
	    checkBox.addItemListener((ItemListener)action);

	    // NOTE: The check box should have a PropertyChangeListener
	    // registered so that it can be in synch with other toggle controls.
	    // This is implemented internally as a ToggleActionPropertyChangeListener.
	    // There should be a more gracefull way of doing this.
	    action.addPropertyChangeListener(new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();

			if (propertyName.equals("selected")) {
			    Boolean selected = (Boolean)evt.getNewValue();
			    checkBox.setSelected(selected.booleanValue());
			}
		    }
		});
	}

	manager.registerCallback("design-command", this, "handleDesigner");

	comboBox = new JComboBox();
	comboBox.addItem(DesignPanel.EVENT_TYPE);
	comboBox.addItem(DesignPanel.LAYOUT_TYPE);
	comboBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    JComboBox combo = (JComboBox)evt.getSource();
		    String item = (String)combo.getSelectedItem();
		    if (item != null) {
			// XXX - should find a better way of setting the designer
			designPanel.setDesignerType(item);
		    }
		}
	    });

        setBorder(CommonUI.createBorder("Control Panel"));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalStrut(5));
	add(comboBox);
        add(Box.createHorizontalStrut(5));
        add(label);
        add(beanTF);
        add(Box.createHorizontalStrut(5));
        add(checkBox);
    }

    /**
     * Callback method for the designer toggle
     */
    public void handleDesigner(boolean state) {
	if (state == true) {
	    objectHolder.setDesignTime(true);
	} else {
	    objectHolder.setDesignTime(false);
	}
    }


    // XXX - hack! we really need some way of externalizing the action
    // associated with changing the type pf editor.
    public void setDesignPanel(DesignPanel panel) {
	designPanel = panel;
    }

    /**
     * Handler for the text field. Will instantiate and add Bean to 
     * the ObjectHolder.
     */
    private class KeyHandler extends KeyAdapter  {
        private JTextField textField;

        public KeyHandler()  {
            this(null);
        }

        public KeyHandler(JTextField textField)  {
            setTextField(textField);
        }

        public void setTextField(JTextField textField) {
            this.textField = textField;
        }

        public void keyReleased(KeyEvent evt)  {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
                // Calls outer class methods
                String bean = textField.getText();
                String message = "New Bean created: " + bean;

                try {
                    Object obj = makeNewBean(bean);
                    ObjectHolder.getInstance().add(obj);
                    textField.setText("");
                } catch (IOException ex) {
                    message = "I\\O Exception for: " + bean;
                    showMessageDialog(message);
                } catch (ClassNotFoundException ex2) {
                    message = "Class Not Found: " + bean;
                    showMessageDialog(message);
                }

		StatusBar.getInstance().setMessage(message);
            }
        }
        
        private void showMessageDialog(String message)  {
            JOptionPane.showMessageDialog(new JFrame(), message, "Control Panel", 
                                            JOptionPane.WARNING_MESSAGE);
        }

        /**
         * This is almost the same method in PaletteItem.
         * Note: In the future we should consolidate all bean behaviours in a
         * set of utility classes.
         */
        private Object makeNewBean(String beanName) throws IOException,
                    ClassNotFoundException {
            return Beans.instantiate(null, beanName);
        }
    }
}

