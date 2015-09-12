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

import java.awt.Component;

import java.awt.event.MouseListener;

import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.ButtonGroup;

/**
 * Encapsulates a set of components in a panel.
 *
 * @version 1.4 02/27/02
 * @author  Mark Davidson
 */
public class PalettePanel extends JPanel {

    private ButtonGroup group;
    
    /** The name of this palette */
    private String paletteName;
    
    // For Status bar messages
    private MouseListener listener;
    
    // Mapping between PaletteItems and PaletteButtons
    private HashMap items;

    /** 
     * Creates a palette with the name.
     */
    public PalettePanel(String name)  {
        setPaletteName(name);
        group = new ButtonGroup();
        items = new HashMap();
    }
    
    /** 
     * @param mouse MouseListener which handles mouse over (for status bar);
     */
    public PalettePanel(MouseListener mouse, String name, String[] components) {
        this(name);

        setMouseListener(mouse);
        
        for (int i = 0; i < components.length; i++) {
            try {
                addItem(new PaletteItem(components[i]));
            } catch (Exception ex) {
                // if there is an error creating the item it wont be added
                // to the palette. Continue.
                System.out.println("Palette constructor Error adding " + components[i]);
            }
        }
    }
    
    /** 
     * Adds a PaletteItem to the panel. The mouse listener is registered with
     * the button.
     */    
    public void addItem(PaletteItem item)  {
        PaletteButton button = new PaletteButton(item);

        if (listener != null)
            button.addMouseListener(listener);
            
        items.put(item, button);
        group.add(button);
        add(button);
    }
    
    public void removePaletteItem(PaletteItem item)  {
        PaletteButton button = (PaletteButton)items.get(item);

        if (button != null)  {
            if (listener != null)
                button.removeMouseListener(listener);
                
            items.remove(item);
            group.remove(button);
            remove(button);
        }
    }
    
    /** 
     * Sets the type of icon in the palette
     * @param icontype One of BeanInfo.ICON_... Contants which indicate which 
     *                 icon to use.
     * Note: It may be better to have the Palette listen to the RadioButton menu
     * command instead of tunneling from BeanTest.
     */
    public void setIconType(int icontype)  {
        Component[] comps = getComponents();
        
        PaletteButton button;
        for (int i = 0; i < comps.length; i++) {
            button = (PaletteButton)comps[i];
            button.setIconType(icontype);
        }
    }

    public void setPaletteName(String paletteName) {
        this.paletteName = paletteName;
    }
    
    public String getPaletteName() {
        return paletteName;
    }
    
    public void setMouseListener(MouseListener listener)  {
        this.listener = listener;
    }
}

