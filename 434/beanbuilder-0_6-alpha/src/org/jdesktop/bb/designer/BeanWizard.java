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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.BeanInfo;

import java.lang.reflect.Method;

import org.jdesktop.bb.util.BeanInfoFactory;
import org.jdesktop.bb.util.DescriptorComparator;

import org.jdesktop.bb.ui.WizardDlg;

/**
 * A base class which handles the wizard interactions for Bean
 * components. This class creates the beaninfos for the source
 * and target Objects.
 * 
 * Subclasses are responsible for creating the panels and the Wizard
 * and implementing the handler for the next button.
 *
 * @version %I% %G%
 * @author  Mark Davidson
 */
public class BeanWizard {

    protected Object source;
    protected Object target;
    
    protected BeanInfo sourceInfo;
    protected BeanInfo targetInfo;

    // Shared instance of a comparator
    protected static DescriptorComparator comparator = new DescriptorComparator();
    
    // Wizard that handles the panel vector.
    protected WizardDlg wizard;

    // Flag to indicate that the finished button was pressed.
    private boolean finished = false;
    
    public BeanWizard() {
    }

    /**
     * Creates the BeanWizard and initializes the source and target objects.
     */
    public BeanWizard(Object source, Object target) {
	setSource(source);
	setTarget(target);
    }

    public void setSource(Object source) {
	this.source = source;
        sourceInfo = BeanInfoFactory.getBeanInfo(source.getClass());
	// XXX - should this be a bound property?
    }

    public void setTarget(Object target) {
        this.target = target;
	targetInfo = BeanInfoFactory.getBeanInfo(target.getClass());
    }

    /** 
     * Check to see if the Finished button was pressed.
     */
    public boolean isFinished()  {
        return finished;
    }
    
    /** 
     * Shows/Hides the wizard dialog
     */
    public void setVisible(boolean visible)  {
        //  nameTF.requestFocus();
        wizard.setVisible(true);
    }
    
    /** 
     * Returns the name of the root class from a full class path.
     * @param fullName The full name of the class i.e, java.awt.event.ActionListener
     * @return The root name of the class i.e., ActionListener
     */
    public String getRootName(String fullName)  {
        int i = fullName.lastIndexOf('.');
        String name = fullName.substring(i + 1);
        return name;
    }

    /** 
     * Returns a string that formats the parameters for a methods for display
     * @param method The method to format.
     */
    public String formatParameters(Method method)  {
        StringBuffer params = new StringBuffer("( ");
        
	if (method != null) {
	    Class[] paramTypes = method.getParameterTypes();
	    if (paramTypes != null)  {
		for (int i = 0; i < paramTypes.length; i++) {
		    params.append(getRootName(paramTypes[i].getName()));;
		    params.append((i == paramTypes.length - 1) ? " " : ", ");
		}
	    }
	}
        params.append(" )");
        
        return params.toString();
    }

    /** 
     * Handles the cancel button
     */
    protected class CancelHandler implements ActionListener  {
        public void actionPerformed(ActionEvent evt)  {
	    wizard.reset();
	    finished = false;
        }        
    }

    /** 
     * Handles the wizard Finish button.
     */
    protected class FinishHandler implements ActionListener  {
        public void actionPerformed(ActionEvent evt)  {
	    wizard.reset();
            finished = true;
        }
    }
}
