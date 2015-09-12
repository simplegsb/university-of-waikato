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

import java.awt.Dimension;
import java.awt.Image;

import java.beans.BeanInfo;

import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

/** 
 * A single button within the palette.  It signifies a single type of object
 * that can be part of a user interface created by a user in the designer.
 *
 * @version 1.5 02/27/02
 * @author Mark Davidson
 */
public class PaletteButton extends JToggleButton {
    private PaletteItem item; // encapsulated item
    
    // Default constructor
    public PaletteButton()
                throws ClassNotFoundException {
        this("javax.swing.JButton");
    }

    /**
     * Ctor, Creates a PaletteButton
     * @param beanName Full path to the Bean. i.e, "javax.swing.JButton"
     */
    public PaletteButton(String beanName)
                    throws ClassNotFoundException {
        item = new PaletteItem(beanName);
        
        setToolTipText(item.getName());
        setIconType(BeanInfo.ICON_COLOR_32x32);
    }

    /** 
     * Ctor, Creates a PaletteButton from a PaletteItem
     */    
    public PaletteButton(PaletteItem item)  {
        this.item = item;
        setToolTipText(item.getName());
        setIconType(BeanInfo.ICON_COLOR_32x32);
    }

    public Dimension getMinimumSize() {
        return new Dimension(40, 40);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /** 
     * Returns the encapsulated PaletteItem
     */
    public PaletteItem getPaletteItem() {
        return item;
    }

    /** 
     * Sets the type of icon in the palette
     * @param icontype One of BeanInfo.ICON_... Contants which indicate which 
     *                 icon to use.
     */
    public void setIconType(int icontype)  {
        Image image = item.getIcon(icontype);
        if(image != null)
            setIcon(new ImageIcon(image));
    }

    /**
     * Converts this object to a String representation
     * @return a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[item=" + item.getName() + "]"; 
    }
}


