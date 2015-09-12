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

package org.jdesktop.bb.util;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * Persistence delegate for the SpringLayout
 *
 * @author Philip Milne
 * @author Mark Davidson
 */
public class SpringLayoutPersistenceDelegate {

    // Held references to the BeanInfo classes so they don't get gc'd.

    private BeanInfo sbi;
    private BeanInfo scbi;
    private BeanInfo spbi;
    private BeanInfo ssbi;
    private BeanInfo sumbi;

    /**
     * Construct a new Persistence Delegate.
     */
    public SpringLayoutPersistenceDelegate() {
	try {
	    sbi = BeanInfoFactory.getBeanInfo(javax.swing.SpringLayout.class);
	    scbi = BeanInfoFactory.getBeanInfo(javax.swing.SpringLayout.Constraints.class);
	    spbi = BeanInfoFactory.getBeanInfo(Class.forName("javax.swing.SpringLayout$SpringProxy"));
	    ssbi = BeanInfoFactory.getBeanInfo(Class.forName("javax.swing.Spring$StaticSpring"));
	    sumbi = BeanInfoFactory.getBeanInfo(Class.forName("javax.swing.Spring$SumSpring"));
	} catch (Exception ex) {
	    System.out.println("Error instantiating components " + ex.getMessage());
	}
    }

    static String[] edgeNames = {SpringLayout.SOUTH, SpringLayout.NORTH,
				  SpringLayout.EAST, SpringLayout.WEST};

    static ExceptionListener defaultExceptionListener = new ExceptionListener() {
        public void exceptionThrown(Exception e) {
           // e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println("Continuing ...");
        }
    };

    private static Object getPrivateField(Object instance, String name, ExceptionListener el) {
        return getPrivateField(instance, instance.getClass(), name, el);
    }

    private static Object getPrivateField(Object instance, Class declaringClass,
					  String name, ExceptionListener el) {
        try {
            // System.out.println("Getting field: " + name  + " of " + declaringClass);
            Field f = declaringClass.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(instance);
        }
        catch (Exception e) {
            el.exceptionThrown(e);
        }
        return null;
    }

    private static Constructor getPrivateConstructor(Object instance, Class declaringClass,
						Class[] parameterTypes, ExceptionListener el) {
        try {
            Constructor ctor = declaringClass.getConstructor(parameterTypes);
            ctor.setAccessible(true);
            return ctor;
        } catch (Exception e) {
            el.exceptionThrown(e);
        }
        return null;
    }

    private static class CompoundSpringPersistenceDelegate extends PersistenceDelegate {
        private static Class compoundSpringClass;
        private String functionName;

        static {
            try{
                compoundSpringClass = Class.forName("javax.swing.Spring$CompoundSpring");
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }

        }

        // Construct with the name of the function to be used.
        public CompoundSpringPersistenceDelegate(String functionName) {
            this.functionName = functionName;
        }

        protected Expression instantiate(Object oldInstance, Encoder out) {
            ExceptionListener el = out.getExceptionListener();
            Object s1 = getPrivateField(oldInstance, compoundSpringClass, "s1", el);
            Object s2 = getPrivateField(oldInstance, compoundSpringClass, "s2", el);
            return new Expression(oldInstance, Spring.class, functionName,
              new Object[]{s1, s2});
        }
    }

    private static Container getParent(Map constraints) {
        Iterator it = constraints.keySet().iterator();
        Container result = (JComponent)it.next();
        while(it.hasNext()){
            Container c = (JComponent)it.next();
            if (c.getParent() == result){
                // Fine, result is indeed the parent.
            }
            else if (result.getParent() == c){
                // Have just hit the parent.
                result = c;
            }
            else{
		// Both c and result are children.
		result = c.getParent();
                // throw new RuntimeException("Cannot find parent for SpringLayout.");
            }
        }
        return result;
    }

    /**
     * Entry point to configure an Encoder with a series of SpringLayout persistence
     * delegates.
     */
    public static void configure(Encoder e) throws Exception {
	final Class staticSpringClass = Class.forName("javax.swing.Spring$StaticSpring");

        // SpringLayout
	if (BeanInfoFactory.getBeanAttribute(SpringLayout.class, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(SpringLayout.class, new DefaultPersistenceDelegate() {
		    protected void initialize(Class type, Object oldInstance, 
					      Object newInstance, Encoder out) {
			Map constraints = (Map)getPrivateField(oldInstance, SpringLayout.class, 
							       "componentConstraints", 
							       out.getExceptionListener());
			Container parent = getParent(constraints);
			Iterator it = constraints.entrySet().iterator();
			while(it.hasNext()){
			    Map.Entry e = (Map.Entry)it.next();
			    Component c = (Component)e.getKey();
			    SpringLayout.Constraints cts = (SpringLayout.Constraints)e.getValue();
			    Expression ex = new Expression(oldInstance, "getConstraints", new Object[] { c });
			    if (c == parent){
				// Special case the common case of not setting constraints on
				// the parent - to avoid the benign but bloating effect of
				// copying the internal intialization code of the SpringLayout onto
				// its parent into the archive. (See SpringLayout:setParent()).
				if (cts.getConstraint("East").getClass() != staticSpringClass ||
				    cts.getConstraint("West").getClass() != staticSpringClass){
				    out.writeStatement(new Statement(oldInstance, "addLayoutComponent", 
								     new Object[]{c, ex}));
				}
			    }
			    else{
				out.writeStatement(new Statement(oldInstance, "addLayoutComponent", 
								 new Object[]{c, ex}));
			    }
			}
		    }
		});
	}

        // Spring proxies.
	final Class springProxyClass = Class.forName("javax.swing.SpringLayout$SpringProxy");
	
	if (BeanInfoFactory.getBeanAttribute(springProxyClass, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(springProxyClass, new PersistenceDelegate() {
		    protected Expression instantiate(Object oldInstance, Encoder out) {
			ExceptionListener el = out.getExceptionListener();
			Object edgeName = getPrivateField(oldInstance, springProxyClass, "edgeName", el);
			Object c = getPrivateField(oldInstance, springProxyClass, "c", el);
			Object l = getPrivateField(oldInstance, springProxyClass, "l", el);
			return new Expression(oldInstance, l, "getConstraint", new Object[]{edgeName, c});
		    }
		});
	}

        // Spring.Constraints.

        // Width and Height Springs are installed automatically by the layout manager when the
        // constraints object is underconstrained. Check this case and treat instances
        // of the private Height and Width Spring classes like null.
	final Class heightSpringClass = Class.forName("javax.swing.SpringLayout$HeightSpring");
	final Class widthSpringClass = Class.forName("javax.swing.SpringLayout$WidthSpring");

        // Use private fields to initialise the SpringLayout as, for example, the
        // public x, width and east properties of a constraints object depend on each
        // other. Also, the east property, for example, is set using a non-stnadard idiom.
	if (BeanInfoFactory.getBeanAttribute(SpringLayout.Constraints.class, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(SpringLayout.Constraints.class, new DefaultPersistenceDelegate() {
		    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
			ExceptionListener el = out.getExceptionListener();
			String properties[] = {"X", "Y", "Width", "Height", "East", "South"};
			for(int i = 0; i < properties.length; i++){
			    String pName = properties[i];
			    Spring s = (Spring)getPrivateField(oldInstance, SpringLayout.Constraints.class, 
							       pName.toLowerCase(), el);
			    if (s != null && s.getClass() != heightSpringClass && s.getClass() != widthSpringClass || s == null){
				if (pName != "East" && pName != "South"){
				    out.writeStatement(new Statement(oldInstance, "set" + pName, 
								     new Object[]{s}));
				}
				else{
				    out.writeStatement(new Statement(oldInstance, "setConstraint", 
								     new Object[]{pName, s}));
				}
			    }
			}
		    }
		});
	}

        // Spring constants.
	if (BeanInfoFactory.getBeanAttribute(staticSpringClass, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(staticSpringClass, new PersistenceDelegate() {
		    protected Expression instantiate(Object oldInstance, Encoder out) {
			Spring s = (Spring)oldInstance;
			int min = s.getMinimumValue();
			int pref = s.getPreferredValue();
			int max = s.getMaximumValue();
			Object[] args = null;
			// Use the shorter static method if possible.
			if (min == pref && pref == max){
			    args = new Object[]{new Integer(pref)};
			}
			else{
			    args = new Object[]{new Integer(min), new Integer(pref), new Integer(max)};
			}
			return new Expression(oldInstance, Spring.class, "constant", args);
		    }
		});
	}

        // Negative Springs.
	final Class negativeSpringClass = Class.forName("javax.swing.Spring$NegativeSpring");
	if (BeanInfoFactory.getBeanAttribute(negativeSpringClass, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(negativeSpringClass, new PersistenceDelegate() {
		    protected Expression instantiate(Object oldInstance, Encoder out) {
			ExceptionListener el = out.getExceptionListener();
			Object s = getPrivateField(oldInstance, negativeSpringClass, "s", el);
			return new Expression(oldInstance, Spring.class, "minus", new Object[]{s});
		    }
		});
	}

        // Spring sums.
	final Class sumSpringClass = Class.forName("javax.swing.Spring$SumSpring");
	if (BeanInfoFactory.getBeanAttribute(sumSpringClass, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(sumSpringClass,
				     new CompoundSpringPersistenceDelegate("sum"));
	}

        // Spring maxima.
	final Class maxSpringClass = Class.forName("javax.swing.Spring$MaxSpring");
	if (BeanInfoFactory.getBeanAttribute(maxSpringClass, "persistenceDelegate") == null) {
	    e.setPersistenceDelegate(maxSpringClass,
				     new CompoundSpringPersistenceDelegate("max"));
	}
    }

    /**
     * Returns an array of objects from the SpringLayout which represents all
     * the information required to reconstitute the SpringLayout constraint
     *
     * @param l the SpringLayout instance
     * @param c the depenent component
     * @param edgeName the edge of the dependent
     * @return array of 3 objects where element:
     *    0. Integer the fixed distance between dependent and anchor
     *    1. String  the edge of the anchor
     *    2. Component  the component of the anchor
     */
    public static Object[] getEdge(SpringLayout l, Component c, String edgeName) {
	return getEdge(l, c, edgeName, defaultExceptionListener);
    }


    public static Object[] getEdge(SpringLayout l, Component c, String edgeName, ExceptionListener el) {
	SpringLayout.Constraints constraints = l.getConstraints(c);
	Spring s = constraints.getConstraint(edgeName);
	String springClassName = s.getClass().getName();

	if (!s.getClass().getName().equals("javax.swing.Spring$SumSpring")) {
	    return null;
	}
	Class declaringClass = s.getClass().getSuperclass();
	Spring s1 = (Spring)getPrivateField(s, declaringClass, "s1", el);
	Spring s2 = (Spring)getPrivateField(s, declaringClass, "s2", el);

	if (!s1.getClass().getName().equals("javax.swing.Spring$StaticSpring")) {
	    return null;
	}
	Integer pref = (Integer)getPrivateField(s1, "pref", el);

	if (!s2.getClass().getName().equals("javax.swing.SpringLayout$SpringProxy")) {
	    return null;
	}
	String edgeName2 = (String)getPrivateField(s2, "edgeName", el);
	Component c2 = (Component)getPrivateField(s2, "c", el);

	return new Object[] { pref, edgeName2, c2 };
    }
}
