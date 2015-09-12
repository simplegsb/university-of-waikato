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

import java.io.IOException;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.jar.Attributes;

/**
 * A class loader for loading jar files, both local and remote.
 * Adapted from the Java Tutorial.
 *
 * http://java.sun.com/docs/books/tutorial/jar/api/index.html
 * 
 * @version 1.3 02/27/02
 * @author Mark Davidson
 */
public class JarClassLoader extends URLClassLoader {

    // These manifest attributes were left out of Attributes.Name
    // They have to go somewhere so the chaces are if you need them,
    // then you are playing with this class loader.
    public static final Attributes.Name Attributes_Name_JAVA_BEAN = new Attributes.Name("Java-Bean");
    public static final Attributes.Name Attributes_Name_NAME = new Attributes.Name("Name");

    private static JarClassLoader loader = null;
    
    /** 
     * Null ctor DO NOT USE. This will result in an NPE if the class loader is
     * used. So this class loader isn't really Bean like.
     */
    public JarClassLoader()  {
        this(null);
    }

    /**
     * Creates a new JarClassLoader for the specified url.
     *
     * @param url The url of the jar file i.e. http://www.xxx.yyy/jarfile.jar
     *            or file:c:\foo\lib\testbeans.jar
     */
    public JarClassLoader(URL url) {
        super(new URL[] { url });
    }
    
    /** 
     * Adds the jar file with the following url into the class loader. This can be 
     * a local or network resource.
     * 
     * @param url The url of the jar file i.e. http://www.xxx.yyy/jarfile.jar
     *            or file:c:\foo\lib\testbeans.jar
     */
    public void addJarFile(URL url)  {
        addURL(url);
    }

    /** 
     * Adds a jar file from the filesystems into the jar loader list.
     * 
     * @param jarfile The full path to the jar file.
     */
    public void addJarFile(String jarfile)  {
        try {
            URL url = new URL("file:" + jarfile);
            addURL(url);
        } catch (IOException ex) {
            // XXX - debug
            System.out.println("JarClassLoader. Bad URL: " + jarfile);
        }
    }
    
    //
    // Static methods for handling the shared instance of the JarClassLoader.
    //

    /** 
     * Returns the shared instance of the class loader.
     */
    public static JarClassLoader getJarClassLoader()  {
        return loader;
    }
    
    /** 
     * Sets the static instance of the class loader.
     */
    public static void setJarClassLoader(JarClassLoader cl)  {
        loader = cl;
    }
}
