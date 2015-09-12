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

import java.awt.Image;

import java.beans.BeanInfo;
import java.beans.BeanDescriptor;
import java.beans.Beans;

import java.io.IOException;

import org.jdesktop.bb.util.BeanInfoFactory;
import org.jdesktop.bb.util.JarClassLoader;

/**
 * Encapsulates an item that can be instantiated by the tool
 * A palette item also knows how to instantiate itself.
 * 
 * Contains most of the BeanInfo fields
 *
 * @version 1.6 02/27/02
 * @author Mark Davidson
 */
public class PaletteItem {
    private String beanName;
    private Class beanClass;

    private BeanInfo info;
    private BeanDescriptor descriptor;
    
    /**
     * Constructs a palette item from the fully qualified bean name
     *
     * @param beanName - The name of the bean that this item represents.
     *      i.e. javax.swing.JButton
     *
     * @exception java.lang.ClassNotFoundException
     */
    public PaletteItem(String beanName) throws ClassNotFoundException {
        this.beanName = beanName;

        beanClass = Class.forName(beanName);
        info = BeanInfoFactory.getBeanInfo(beanClass);
        descriptor = info.getBeanDescriptor();
    }
    
    /** 
     * This is the ctor to call for creating a PaletteItem from a serialized
     * bean.
     * @param obj An instance of the deserizizd bean.
     * @param beanName The name of the resouce without the extension 
     *                 i.e, sun.demo.buttons.OrangeButton
     */
    public PaletteItem(Object obj, String beanName)  {
        this.beanName = beanName;
        
        beanClass = obj.getClass();
        info = BeanInfoFactory.getBeanInfo(beanClass);
        descriptor = info.getBeanDescriptor();
        
        // Should tweak the contents of the BeanDescriptor since the
        // BeanInfo will reflect the base class 
        descriptor.setName(beanName);
        descriptor.setShortDescription("Serialized Object: " + beanName);
        
        String shortName = beanName;
        int ix = beanName.lastIndexOf('.');
        if (ix >= 0) {
		    shortName = beanName.substring(ix+1);
		}
        descriptor.setDisplayName(shortName);
    }
    
    /** 
     * Constructs a palette item from a class.
     * @param cls - A valid java bean class.
     */
    public PaletteItem(Class cls)  {
        beanClass = cls;
        beanName = cls.getName();
        
        info = BeanInfoFactory.getBeanInfo(beanClass);
        descriptor = info.getBeanDescriptor();
    }

    /**
     * Creates a new bean from the encapsulated class. Sets some initial values
     * from the method description such as setText.
     * Usually called from the Palette class.
     *
     * @exception java.lang.ClassNotFoundException
     * @exception java.io.IOException
     * @see java.beans.Beans#instantiate
     */
    public Object makeNewBean() throws IOException, ClassNotFoundException {
        return Beans.instantiate(JarClassLoader.getJarClassLoader(), beanName);
    }

    // getter methods 
    public String getBeanName() {
        return beanName;
    }
        
    public Class getBeanClass() {
        return beanClass;
    }
    
    public BeanInfo getBeanInfo()  {
        return info;
    }
    
    public BeanDescriptor getBeanDescriptor()  {
        return descriptor;
    }
        
    /** 
     * Retrieves the icon for the beaninfo
     * @param type - One of BeanInfo.ICON_
     */
    public Image getIcon(int type) {
        return info.getIcon(type);
    }
    
    public String getName() {
        return descriptor.getName();
    }
    
    public String getDisplayName() {
        return descriptor.getDisplayName();
    }
    
    public String getShortDescription() {
        return descriptor.getShortDescription();
    }
    
    public Class getCustomizerClass() {
        return descriptor.getCustomizerClass();
    }
}
