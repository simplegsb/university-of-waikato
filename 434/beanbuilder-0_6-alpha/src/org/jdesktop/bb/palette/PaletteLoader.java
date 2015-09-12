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

/**
 * @(#)PaletteLoader.java	1.5 02/02/27
 */

package org.jdesktop.bb.palette;

import java.io.IOException;
import java.io.FileReader;

import java.net.URL;

import javax.xml.parsers.SAXParserFactory;  
import javax.xml.parsers.SAXParser;  

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Configures the palette by reading a palette configuration file which specifies
 * a set of tabs and beans within each tab.
 *
 * Here is a sample file:
 *  <palette>
 *    <tab name="Swing">
 *       <item>javax.swing.JButton</item>
 *       <item>javax.swing.JCheckBox</item>
 *     </tab>
 *     <tab name="Containers">
 *       <item>javax.swing.JPanel</item>
 *     </tab>
 *   </palette>
 *
 *   @author Mark Chung, SRI
 */
public class PaletteLoader extends DefaultHandler {
    final protected static String PALETTE_ELEMENT = "palette";
    final protected static String TAB_ELEMENT = "tab";
    final protected static String ITEM_ELEMENT = "item";
    final protected static String NAME_ATTRIBUTE = "name";
	
    // name of the current item
    protected StringBuffer itemname = new StringBuffer();
	
    // name of the current element
    protected String elementname;
	
    protected Palette palette;
    protected PalettePanel panel;
	
    public PaletteLoader(Palette palette) {
	this.palette = palette;
    }

    public void load(URL filename) {
	XMLReader xmlReader = null;
	SAXParserFactory spf = SAXParserFactory.newInstance();
	
	try {
	    SAXParser saxParser = spf.newSAXParser();
	    xmlReader = saxParser.getXMLReader();
	    try {
		xmlReader.setContentHandler(this);
		xmlReader.parse(new InputSource(filename.openStream()));
	    } catch (SAXException saxe) {
		System.err.println("Error loading "+ filename + ": " + saxe);
		saxe.printStackTrace();
	    } catch (IOException ioe) {
		System.err.println("Error loading "+ filename + ": " + ioe);
		ioe.printStackTrace();
	    }
	} catch (Exception e) {
	    System.err.println(e);
	    e.printStackTrace();
	}
    }

    //
    // Overloaded DefaultHandler methods
    //
	
    public void startElement(String nameSpace, String localName,
			     String name, Attributes attributes)
	throws SAXException {
    	elementname = name;
    	if (name.equals(TAB_ELEMENT))
	    startTab(name, attributes);
    }
    
    public void endElement(String nameSpace, String localName, 
			   String name) throws SAXException {
    	if (name.equals(ITEM_ELEMENT))
	    endItem(name);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
    	if (elementname != null && elementname.equals(ITEM_ELEMENT))
	    itemname.append(ch, start, length);
    }

    protected void startTab(String name, Attributes attributes) {
	panel = new PalettePanel(attributes.getValue(NAME_ATTRIBUTE));
	panel.setMouseListener(palette);
    	palette.addTab(attributes.getValue(NAME_ATTRIBUTE), panel);
    }
    
    protected void endItem(String name) {
    	try {
	    panel.addItem(new PaletteItem(itemname.toString()));
	} catch (ClassNotFoundException cnfe) {
	    System.err.println("Error adding "+ itemname.toString() + ": "+ cnfe);
	}
     	itemname = new StringBuffer();	
	elementname = null;
    }
	
    
}
