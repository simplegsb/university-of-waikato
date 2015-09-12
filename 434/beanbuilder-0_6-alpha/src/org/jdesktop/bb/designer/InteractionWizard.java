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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import java.lang.reflect.Method;

import java.util.Arrays;    // Collections class for sorting Since JDK 1.2
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventListener;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.bb.ui.CommonUI;
import org.jdesktop.bb.ui.WizardDlg;

/**
 * A Wizard which allows for the selection of interaction components. This
 * wizard also uses the JDK 1.2 collection classes to implement sorting.
 *
 * @version 1.23 02/27/02
 * @author  Mark Davidson
 */
public class InteractionWizard extends BeanWizard implements ActionListener {

    private final static String COLUMN_TYPE = "Type";
    private final static String COLUMN_VALUE = "Value";

    private final static int COLUMN_TYPE_NUM = 0;
    private final static int COLUMN_VALUE_NUM = 1;

    private boolean isPropertyHookup = false;

    // List of panels in the wizard
    private Vector panels = null;

    // Components in the control panel.
    private JRadioButton propertyRB;
    private JRadioButton eventRB;
    private JEditorPane editPane;
    
    // Property panels in the wizard
    private JPanel propPanel;

    private JList targetPropsList;

    // Event panels in the wizard
    private JPanel sourcePanel;
    private JPanel targetPanel;
    private JPanel argsPanel;
    private JPanel summaryPanel;

    // Components in the wizard
    private JList eventsList;
    private JList methodList;
    private JList targetMethodsList;
    private JList sourceMethodsList;

    // Argument label on Arguments page will change according to method selection.
    private JLabel sourceEventLabel;
    private JLabel targetMethodLabel;
    private JLabel argsLabel;

    private final static Dimension preferredSize = new Dimension(550, 400);

    private final static Font labelFont = new Font("Dialog", Font.BOLD, 16);

    // Dummy label used to set the initial size
    private final static String DUMMY_LABEL = "                                       ";

    /**
     * Creates the interaction wizard with a default frame.
     * The data is initialized with the BeanInfo information from the source
     * and target objects.
     *
     * @param source Source component.
     * @param target Target component.
     */
    public InteractionWizard(Object source, Object target) {
        this(new JFrame(), source, target);
    }

    /**
     * Creates the interaction wizard
     *
     * @param frame Parent frame of the wizard
     * @param source Source component.
     * @param target Target component.
     */
    public InteractionWizard(JFrame frame, Object source, Object target) {
        super(source, target);
	initUI(frame);
	setData(source, target);
    }

    private void initUI(JFrame frame) {
        // This wizard is configured for Event Hookups by default
        wizard = new WizardDlg(frame, "Interaction Wizard", getEventPanels());
        wizard.setWestPanel(createControlPanel());
        
        // Regsister listeners
        wizard.addCancelListener(new CancelHandler());
        wizard.addFinishListener(new FinishHandler());
    }	

    /**
     * Sets the source and target objects for the wizard.
     */
    public void setData(Object source, Object target) {
	setSource(source);
	setTarget(target);
	initData();
    }

    /**
     * Sets the data Descriptors correstponding to the decriptors
     * Introspected from the BeanInfo classes.
     */
    private void initData() {
	// XXX - Should throw an exception if the UI hasn't been initialized.
	setTextData();

	if (isPropertyHookup) {
	    setPropertyData();
	} else {
	    setSourceEventData();
	    setTargetData();
	}
	wizard.doLayout();
    }

    private void setPropertyData() {
	// Initialize PropertyDescriptor list.
        PropertyDescriptor[] properties = targetInfo.getPropertyDescriptors();
	if (properties == null || properties.length == 0) {
	    return;
	}
        
        // Property List. Use collections to filter out unwritable properties
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(properties));
        
        ListIterator iterator = list.listIterator();
        PropertyDescriptor desc;
        while (iterator.hasNext()) {
            desc = (PropertyDescriptor)iterator.next();
            Class propType = desc.getPropertyType();
            Class sourceType = source.getClass();
            
            if (desc.getWriteMethod() == null || !propType.isAssignableFrom(sourceType))  {
                // Remove read only methods.
                iterator.remove();
            }
        }
        properties = (PropertyDescriptor[])list.toArray(new PropertyDescriptor[list.size()]);
        Arrays.sort(properties, comparator);

	// Create a list model
	targetPropsList.setListData(properties);
    }

    // Sets the text for the interaction wizard.
    private void setTextData() {
        // Build HTML text for JEditorPane.
        StringBuffer message = new StringBuffer("<html><b>Source Object:</b><ul><li>");
        message.append(getRootName(source.getClass().getName()));
        message.append("</li></ul><p><b>Target Object:</b><ul><li>");
        message.append(getRootName(target.getClass().getName()));
        message.append("</li></ul></html>");
	editPane.setText(message.toString());
    }

    private void setSourceEventData() {
	// Initialize the EventSetDescriptor list
        EventSetDescriptor[] descriptors = sourceInfo.getEventSetDescriptors();
	if (descriptors == null || descriptors.length == 0) {
	    return;
	}
        Arrays.sort(descriptors, comparator);

	// Create the list model
	eventsList.setListData(descriptors);
	eventsList.setSelectedIndex(0);

        sourceEventLabel.setText("Select Event Method for " + 
                                    getRootName(source.getClass().getName()));
	sourceEventLabel.invalidate();
    }

    /**
     * Fills the Target methods list with elements of the method
     * descriptors from the target object
     */
    private void setTargetData() {
	// Target methods list.
        MethodDescriptor[] mdescriptors = targetInfo.getMethodDescriptors();

        // Filter the methods
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(mdescriptors));

        ListIterator iterator = list.listIterator();
        MethodDescriptor desc;
        Class type;
        Class[] smParams;
        while (iterator.hasNext()) {
            desc = (MethodDescriptor)iterator.next();
            smParams = desc.getMethod().getParameterTypes();

            if (smParams != null)  {
                // Reject listener registration methods and methods which 
                // require more than one Parameter.
                if (smParams.length > 1 || 
                    smParams.length == 1 && EventListener.class.isAssignableFrom(smParams[0])) {
                    iterator.remove();
                }
            }
        }

        // Write the filtered MethodDescriptor array and sort
        mdescriptors = (MethodDescriptor[])list.toArray(new MethodDescriptor[list.size()]);
        Arrays.sort(mdescriptors, comparator);

	targetMethodsList.setListData(mdescriptors);

        targetMethodLabel.setText("Select Target Method for " + 
                                        getRootName(target.getClass().getName()));
    }

    /**
     * Fills the target methods list with writable Properties from
     * the target BeanInfo PropertyDescriptors. 
     */
    private void setTargetPropertyData() {
	PropertyDescriptor[] properties = targetInfo.getPropertyDescriptors();
	if (properties == null || properties.length == 0) {
	    return;
	}
	
        // Property List. Use collections to filter out unwritable properties
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(properties));
        
        ListIterator iterator = list.listIterator();
        PropertyDescriptor desc;
        while (iterator.hasNext()) {
            desc = (PropertyDescriptor)iterator.next();
            if (desc.getWriteMethod() == null) {
                // Remove read only methods.
                iterator.remove();
            }
        }
        properties = (PropertyDescriptor[])list.toArray(new PropertyDescriptor[list.size()]);
        Arrays.sort(properties, comparator);

	targetMethodsList.setListData(properties);
        targetMethodLabel.setText("Select Target Property for " + 
                                        getRootName(target.getClass().getName()));
    }

    /** 
     * The control panel is the west panel in the wizard which allows for
     * the selection of Property vs. Event Adapter.
     */
    private JPanel createControlPanel()  {
        propertyRB = CommonUI.createRadioButton("Set Property", 'S', this);
        propertyRB.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        eventRB = CommonUI.createRadioButton("Event Adapter", 'E', this, true);
        eventRB.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        ButtonGroup group = new ButtonGroup();
        group.add(propertyRB);
        group.add(eventRB);

        // Build HTML text for JEditorPane.
        StringBuffer message = new StringBuffer("<html><b>Source Object:</b><ul><li>");
        message.append(getRootName(source.getClass().getName()));
        message.append("</li></ul><p><b>Target Object:</b><ul><li>");
        message.append(getRootName(target.getClass().getName()));
        message.append("</li></ul></html>");

        editPane = new JEditorPane();
	editPane.setContentType("text/html");
        editPane.setEditable(false);
        editPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        editPane.setBackground(UIManager.getColor("control"));

        JPanel panel = new JPanel();
        panel.setBorder(CommonUI.createBorder("Create Interaction"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(propertyRB);
        panel.add(eventRB);
        panel.add(editPane);
        
        return panel;
    }

    /** 
     * Handles the RadioButton selection.
     */
    public void actionPerformed(ActionEvent evt)  {
        Object obj = evt.getSource();
        if (obj instanceof JRadioButton)  {
            // Reconfigurure the Wizard depending on the user selection.
	    // Reset the panels Vector
	    panels = null;
        
            if (obj == propertyRB)  {
                // Property hookup selected
                isPropertyHookup = true;
                wizard.setPanels(getPropertyPanels());
		setPropertyData();
            } else if (obj == eventRB) {
                // Event hookup selected
                isPropertyHookup = false;
                wizard.setPanels(getEventPanels());
		setSourceEventData();
		setTargetData();
            }
	    wizard.doLayout();
	    wizard.repaint();
        }
    }
    
    /** 
     * Flag which indicates if the mode is property hookup.
     */
    public boolean isPropertyHookup()  {
        return isPropertyHookup;
    }
    
    /** 
     * Return the panels for an Event adapter hookup. Create them if they 
     * haven't beeen created yet.
     * @return A vector containing the panels.
     */
    private Vector getEventPanels()  {
	if (panels == null) {
	    panels = new Vector(3);

	    //	    if (sourcePanel == null)
	    sourcePanel = createSourceEventPanel();
	    //if (targetPanel == null)
	    targetPanel = createTargetPanel();
	    //    if (argsPanel == null)
	    argsPanel = createArgsPanel();
	    
	    panels.addElement(sourcePanel);
	    panels.addElement(targetPanel);
	    panels.addElement(argsPanel);
	}
        return panels;
    }

    /** 
     * Construct the panels for a property assocation.
     */    
    private Vector getPropertyPanels()  {
	if (panels == null) {
	    panels = new Vector(1);

	    if (propPanel == null)
		propPanel = createPropertyPanel();
	    panels.addElement(propPanel);
	}
        return panels;
    }

    /**
     * Retrieves the selected source event descriptor or null.
     */
    public EventSetDescriptor getSourceEvent()  {
        return (EventSetDescriptor)eventsList.getSelectedValue();
    }

    /**
     * Retrieves the selected source method descriptor or null if a
     * method hasn't been selected.
     */
    public MethodDescriptor getSourceMethod()  {
        return (MethodDescriptor)methodList.getSelectedValue();

    }

    /**
     * Retrieves the selected target method descriptor or null if a
     * method hasn't been selected.
     */
    public MethodDescriptor getTargetMethod()  {
        MethodDescriptor desc = null;
        
        if (isPropertyHookup)  {
            // Get the method descriptor from the selected property
            // XXX - maybe we should treat properties differently from method selection.
            PropertyDescriptor prop;
            prop = (PropertyDescriptor)targetPropsList.getSelectedValue();
            if (prop != null)  {
                desc = new MethodDescriptor(prop.getWriteMethod());
            }
        } else {
            desc = (MethodDescriptor)targetMethodsList.getSelectedValue();
        }
        return desc;
    }


    /**
     * Retrieves the selected target property descriptor or null if
     * it hasn't been selected.
     * 
     * Note: This method will only work if the Target Methods list
     * has been initiaizized with target PropertyDescriptors
     */
    public PropertyDescriptor getTargetProperty() {
	return (PropertyDescriptor)targetMethodsList.getSelectedValue();
    }

    /**
     * Returns the MethodDescriptor which represents the argument to the target
     * method.
     *
     * @return a MethodDescriptor or null if a method was not selected
     */
    public MethodDescriptor getTargetArgument()  {
	return (MethodDescriptor)sourceMethodsList.getSelectedValue();
    }

    //
    // Private methods.
    //

    /** 
     * Creates the panel for selecting the target property type
     * Only PropertyDescriptors which has the same type as the source object
     * will be displayed.
     */
    private JPanel createPropertyPanel()  {
        targetPropsList = new JList();
        targetPropsList.setCellRenderer(new PropertyListRenderer());

        JPanel p1 = new JPanel();
        JLabel label = CommonUI.createLabel("Source Object will be the Property Value");
        label.setFont(labelFont);
        label.setForeground(Color.black);
        p1.add(label);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("Property Type Selection");
        panel.add(p1, BorderLayout.NORTH);
        panel.add(CommonUI.createListPane(targetPropsList, "Property Set Methods on Target"), 
		  BorderLayout.CENTER);
        panel.setPreferredSize(preferredSize);

        return panel;
    }

    /** 
     * Renders a Property string in a list of PropertyDescriptors
     */
    private class PropertyListRenderer extends DefaultListCellRenderer  {
	// NOTE: This should perhaps be consolodated with the MethodListRenderer
	// to handle general lists of FeatureDescriptors
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)  {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof PropertyDescriptor)  {
                PropertyDescriptor desc = (PropertyDescriptor)value;
                this.setText(desc.getDisplayName() + formatParameters(desc.getWriteMethod()));
            }
            return this;
        }
    }

    /**
     * A panel which allows for the selection of a Event activity.
     */
    private JPanel createSourceEventPanel()  {
        eventsList = new JList();
        eventsList.setCellRenderer(new EventSetListRenderer());
        eventsList.addListSelectionListener(new EventSetListListener());

        // The list of methods is loaded depending on the Event set selection.
        methodList = new JList();
        methodList.setCellRenderer(new MethodListRenderer());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("Source Events and Methods");

        JPanel p1 = new JPanel();
        sourceEventLabel = CommonUI.createLabel(DUMMY_LABEL);
        sourceEventLabel.setFont(labelFont);
        sourceEventLabel.setForeground(Color.black);
        p1.add(sourceEventLabel);

        JPanel p2 = new JPanel(new GridLayout(1, 2));
        p2.add(CommonUI.createListPane(eventsList, "Event Sets"));
        p2.add(CommonUI.createListPane(methodList, "Event Methods"));

        panel.add(p1, BorderLayout.NORTH);
        panel.add(p2, BorderLayout.CENTER);
        panel.setPreferredSize(preferredSize);

        return panel;
    }

    /** 
     * Creates the panel which allows for the selection of the target object
     * method to be invoked.
     * Available methods will only have a zero or one parameter.
     */
    private JPanel createTargetPanel()  {
        targetMethodsList = new JList();
        targetMethodsList.setCellRenderer(new MethodListRenderer());
        targetMethodsList.addListSelectionListener(new MethodListListener());

        JPanel p1 = new JPanel();
        targetMethodLabel = CommonUI.createLabel(DUMMY_LABEL);
        targetMethodLabel.setFont(labelFont);
        targetMethodLabel.setForeground(Color.black);
        p1.add(targetMethodLabel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("Target Method Selection");
        panel.add(p1, BorderLayout.NORTH);
        panel.add(CommonUI.createListPane(targetMethodsList, "Target Methods"),
                                    BorderLayout.CENTER);
        panel.setPreferredSize(preferredSize);

        return panel;
    }


    /**
     * Creates the panel which allows for the selection of a target
     * object property setter method to be invoked.
     * 
     * This method is similar to createTargetPanel except that it
     * displays writable Properties from the BeanInfo 
     * PropertyDescriptors.
     */
    private JPanel createTargetPropertyPanel() {
        targetMethodsList = new JList();
        targetMethodsList.setCellRenderer(new PropertyListRenderer());
	targetMethodsList.addListSelectionListener(new PropertyListListener());

        JPanel p1 = new JPanel();
        targetMethodLabel = CommonUI.createLabel(DUMMY_LABEL);
        targetMethodLabel.setFont(labelFont);
        targetMethodLabel.setForeground(Color.black);
        p1.add(targetMethodLabel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("Target Method Selection");
        panel.add(p1, BorderLayout.NORTH);
        panel.add(CommonUI.createListPane(targetMethodsList, "Target Properties"),
                                    BorderLayout.CENTER);
        panel.setPreferredSize(preferredSize);

        return panel;
    }

    /**
     * Handler when the event listeners list changes. The methods from the
     * selected EventSetDescriptor are loaded into the methods list.
     */
    private class EventSetListListener implements ListSelectionListener  {
        public void valueChanged(ListSelectionEvent evt)  {
            if (!evt.getValueIsAdjusting())  {
                EventSetDescriptor value = (EventSetDescriptor)eventsList.getSelectedValue();

                if (value != null)  {
                    MethodDescriptor[] descriptors =  value.getListenerMethodDescriptors();
                    Arrays.sort(descriptors, comparator);
                    methodList.setListData(descriptors);
                    methodList.setSelectedIndex(0);
                }
            }
        }
    }

    /**
     * Renders an EventSetDescriptor in the list of event sets
     */
    private class EventSetListRenderer extends DefaultListCellRenderer  {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)  {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof EventSetDescriptor)  {
                EventSetDescriptor desc = (EventSetDescriptor)value;
                this.setText(desc.getDisplayName());
            }
            return this;
        }
    }


    /**
     * Handler when the Target method list changes. The arguments types from
     * the selected method are populated in the Arguments panel.
     */
    private class MethodListListener implements ListSelectionListener  {
        public void valueChanged(ListSelectionEvent evt)  {
            if (!evt.getValueIsAdjusting())  {
                // Set the argument panel based on the target method.
                MethodDescriptor value = getTargetMethod();

                if (value != null)  {
                    Method method = value.getMethod();
                    Class[] params = method.getParameterTypes();
                    if (params != null && params.length > 0)  {
                        // Set the label of the class.method.
                        StringBuffer buffer = new StringBuffer();

                        buffer.append(getRootName(target.getClass().getName()));
                        buffer.append(".");
                        buffer.append(method.getName());
                        buffer.append(formatParameters(method));

                        argsLabel.setText("Arguments for: " + buffer.toString());

                        setArgsData(method);
                    } else {
                        // No argments, reset the models.
                        sourceMethodsList.setModel(new DefaultListModel());
                        argsLabel.setText("No Arguments Required");
                    }
                }
            }
        }

        /**
         * Set the argument method list based on the target method arguments
         * and source methods whose return value matches the arguments.
	 * 
	 * @param method - The selected target method
         */
        protected void setArgsData(Method method)  {
            Class[] params = method.getParameterTypes();

            // If there is a single parameter, then allow a choice of getting the
            // value from the source object that matches the signature.
            // i.e., allow for the configuration of target.setText(source.getText())
            if (params.length == 1) {
                MethodDescriptor[] descs = sourceInfo.getMethodDescriptors();
                ArrayList list = new ArrayList();
                list.addAll(Arrays.asList(descs));

                ListIterator iterator = list.listIterator();
                MethodDescriptor desc;
                Class type;
                Class[] smParams;
                while (iterator.hasNext()) {
                    desc = (MethodDescriptor)iterator.next();
                    type = desc.getMethod().getReturnType();
                    smParams = desc.getMethod().getParameterTypes();

                    if (!params[0].isAssignableFrom(type) || smParams.length > 0)  {
                        // Reject null return types, type mismatches, or methods
                        // that requires additional parameters.
                        iterator.remove();
                    }
                }

                // Write the filtered MethodDescriptor array.
                descs = (MethodDescriptor[])list.toArray(new MethodDescriptor[list.size()]);
                sourceMethodsList.setListData(descs);
            } else {
                // Reset the list.
                sourceMethodsList.setModel(new DefaultListModel());
            }
        } // end setArgsData

    }

    /**
     * Handler when the target property changes. Similar to the
     * MethodListListener except that it handles PropertyDescriptor
     * changes.
     */
    private class PropertyListListener extends MethodListListener {
        public void valueChanged(ListSelectionEvent evt)  {
            if (!evt.getValueIsAdjusting())  {
                // Set the argument panel based on the target method.
		// NOTE: This should be consolidated with 
                PropertyDescriptor value = getTargetProperty();

                if (value != null)  {
                    Method method = value.getWriteMethod();
                    Class[] params = method.getParameterTypes();
                    if (params != null && params.length > 0)  {
                        // Set the label of the class.method.
                        StringBuffer buffer = new StringBuffer();

                        buffer.append(getRootName(target.getClass().getName()));
                        buffer.append(".");
                        buffer.append(method.getName());
                        buffer.append(formatParameters(method));

                        argsLabel.setText("Arguments for: " + buffer.toString());

                        setArgsData(method);
                    } else {
                        // No argments, reset the models.
                        sourceMethodsList.setModel(new DefaultListModel());
                        argsLabel.setText("No Arguments Required");
                    }
                }
            }
        }
    }

    /**
     * Renders a Method in the list of methods.
     */
    private class MethodListRenderer extends DefaultListCellRenderer  {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)  {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof MethodDescriptor)  {
                MethodDescriptor desc = (MethodDescriptor)value;

                /* XXX - this is the way that the paramter list should be built.
                         however, the MethodDescriptor only returns an array that
                         was set. It's not contstructed dynamically from reflecting on
                         the enclosed Method.
                ParameterDescriptor[] descriptors = desc.getParameterDescriptors();
                if (descriptors != null) {
                    for (int i = 0; i < descriptors.length; i++) {
                        params += descriptors[i].getDisplayName();
                        params += (i == descriptors.length - 1) ? " " : ", ";
                    }
                } */

                this.setText(desc.getDisplayName() + formatParameters(desc.getMethod()));
            }
            return this;
        }
    }

    /**
     * Create the panel which will prompt for the target arguments.
     */
    private JPanel createArgsPanel()  {

        argsLabel = CommonUI.createLabel("<target class>.<target method>(param types)");
        argsLabel.setFont(labelFont);
        argsLabel.setForeground(Color.black);
        JPanel p1 = new JPanel();
        p1.add(argsLabel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("Arguments");
        panel.add(p1, BorderLayout.NORTH);
	//        panel.add(pane, BorderLayout.CENTER);

        // A list of source method which match the target args
        sourceMethodsList = new JList();
        sourceMethodsList.setCellRenderer(new MethodListRenderer());

        panel.add(CommonUI.createListPane(sourceMethodsList, 
					  "Matching Source \"getter\" Methods"),
		  BorderLayout.CENTER);
        panel.setPreferredSize(preferredSize);
        return panel;
    }

    // Testing
    public static void main(String[] args)  {
        JFrame frame = new JFrame();
        
        javax.swing.JButton button = new javax.swing.JButton("Button");
        javax.swing.JTextField field = new javax.swing.JTextField("TextField");

        
        InteractionWizard wizard = new InteractionWizard(frame, button, field);
        wizard.setVisible(true);
        
    }

}
