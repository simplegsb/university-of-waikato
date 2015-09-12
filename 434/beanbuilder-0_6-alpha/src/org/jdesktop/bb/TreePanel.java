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
package org.jdesktop.bb;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import java.beans.BeanInfo;
import java.beans.BeanDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;

import javax.swing.event.TreeModelEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.bb.util.BeanInfoFactory;
import org.jdesktop.bb.util.TreeModelSupport;

import org.jdesktop.bb.model.ObjectHolder;

/**
 * Panel which encapsulates a Containment hierarchy in a scrollpane.
 * This tree only displays visual beans.
 *
 * @version  1.16 02/27/02
 * @author  Mark Davidson
 */
public class TreePanel extends JScrollPane implements TreeSelectionListener,
						      PropertyChangeListener {

    private JTree tree;
    private ContainmentTreeModel treeModel;
    private ObjectHolder objectHolder;

    /**
     * Creates a tree panel with the tree model
     */
    public TreePanel()  {
        tree = new JTree();
	tree.addTreeSelectionListener(this);
	tree.setCellRenderer(new BeanCellRenderer());

	objectHolder = ObjectHolder.getInstance();

        // Show tooltips for each item.
        ToolTipManager.sharedInstance().registerComponent(tree);

        getViewport().add(tree);
    }

    public Dimension getPreferredSize()  {
        return new Dimension(200, 200);
    }

    /**
     * Returns the tree (for listener registration).
     */
    public JTree getTree()  {
        return tree;
    }

    /**
     * Renders a node in the tree.
     */
    public class BeanCellRenderer extends DefaultTreeCellRenderer {

        // Caches the icons from the BeanInfo on a per type basis.
        Hashtable icons = new Hashtable();

        /**
         * This is called from JTree whenever it needs to get the size
         * of the component or it wants to draw it.
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                    boolean sel,
                                                    boolean expanded,
                                                    boolean leaf, int row,
                                                    boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                                        leaf, row, hasFocus );
	    Object realValue = value;
	    if (value instanceof Component) {
		// Try to see if there is any proxy translation
		realValue = objectHolder.getProxyObject((Component)value);
		if (realValue == null) {
		    realValue = value;
		}
	    }

	    Class cls = realValue.getClass();
	    Icon icon = (Icon)icons.get(cls);
	    if (icon == null)  {
		icon = BeanInfoFactory.getIcon(cls);
		if (icon != null)  {
		    icons.put(cls, icon);
		    this.setIcon(icon);
		}
	    } else {
		this.setIcon(icon);
	    }
	    
	    this.setText(cls.getName());
            return this;
        }
        
    }

    /**
     * A custom tree model which is based on the containment hiearchy.
     * A container is added as the root of the tree model. 
     * This model listens to the root container for Components added or removed.
     * <p>
     * This model class uses BeanInfo to retrieve custom attributes
     * and the Icon.
     */
    public class ContainmentTreeModel extends TreeModelSupport 
	implements ContainerListener  {

        private Object root;

        public ContainmentTreeModel(Object root)  {
            setRoot(root);
        }

	public void setRoot(Object root) {
	    this.root = root;
	}

	public Object getRoot() {
	    return root;
	}

        public Object getChild(Object parent, int index)  {
            if (parent instanceof Container)  {
                Container c = (Container)parent;
                return c.getComponent(index);
            } 
            return null;
        }

        public int getChildCount(Object parent)  {
            if (parent instanceof Container)  {
                Container c = (Container)parent;
                return c.getComponentCount();
            }
            return 0;
        }

        public int getIndexOfChild(Object parent, Object child)  {
            if (parent instanceof Container)  {
                Container c = (Container)parent;

                Component[] comps = c.getComponents();
                for (int i = 0; i < comps.length; i++) {
                    if (comps[i] == child)  {
                        return i;
                    }
                }
            }
            return -1;
        }

        /**
         * Tests to see if the node is a leaf.
         */
        public boolean isLeaf(Object node)  {
            if (BeanInfoFactory.isContainer(node) || node instanceof Container)  {
                Container container = (Container)node;
                if (container.getComponentCount() == 0 ||
		    (container != getRoot() && objectHolder.isProxyComponent(container)))
                    return true;
                else
                    return false;
	    }
            return true;
        }

        /**
         * Builds the parents of a component up to and including the root node.
         * @see javax.swing.tree.DefaultTreeModel#getPathToRoot
         */
        public Object[] getPathToRoot(Component comp)  {
            return getPathToRoot(comp, 0);
        }

        /**
         * Builds the parents of a component up to and including the root node.
         * @see javax.swing.tree.DefaultTreeModel#getPathToRoot
         */
        protected Object[] getPathToRoot(Component comp, int depth)  {
            Object[] retObjs;
	    
            if (comp == null)  {
                if (depth == 0)
                    return null;
                else
                    retObjs = new Object[depth];
            } else {
                depth++;
                if (comp == getRoot()) {
		    retObjs = new Object[depth];
                } else {
		    retObjs = getPathToRoot(comp.getParent(), depth);
		}
                retObjs[retObjs.length - depth] = comp;
            }
            return retObjs;
        }

        //
        // ContainerListener methods.
        //

        public void componentAdded(ContainerEvent evt)  {
            Container container = evt.getContainer();
            Component comp = evt.getChild();
            int[] indexes = new int[] { getIndexOfChild(container, comp) };

            fireTreeNodesInserted(this, getPathToRoot(container), 
				  indexes, new Object[]{ comp });
        }

        public void componentRemoved(ContainerEvent evt)  {
            Container container = evt.getContainer();
            Component comp = evt.getChild();

            Object[] parentPath = getPathToRoot(evt.getContainer());

            fireTreeNodesRemoved(new TreeModelEvent(this, parentPath));
        }

        public void valueForPathChanged(TreePath path, Object newValue)  { }
    }


    //
    // PropertyChangeListener method
    // 

    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        String prop = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();

	if (source == objectHolder) {
	    if (prop.equals("selectedItem")) {
		if (newValue instanceof Component) {
		    Component comp = (Component)newValue;
		    Object[] path = treeModel.getPathToRoot(comp);
		    tree.setSelectionPath(new TreePath(path));
		}
	    }
	    else if (prop.equals("root")) {
		if (treeModel != null) {
		    objectHolder.removeContainerListener(treeModel);
		}
		Object root = newValue;
		if (!(newValue instanceof Component)) {
		    root = objectHolder.getProxyComponent(newValue);
		    if (root == null) {
			root = newValue;
		    }
		}

		treeModel = new ContainmentTreeModel(root);
		tree.setModel(treeModel);
		    
		objectHolder.addContainerListener(treeModel);

		// Expand all nodes
		int row = 0;
		while(row < tree.getRowCount())  {
		    tree.expandRow(row);
		    row++;
		}
	    }
	}
    }  // end propertyChange

    /**
     * Implementation of the tree selection listener. Will set the selected
     * item.
     */
    public void valueChanged(TreeSelectionEvent evt)  {
        if (!evt.isAddedPath())
            return;

        TreePath path = evt.getPath();
        Object obj = path.getLastPathComponent();

        ObjectHolder.getInstance().setSelectedItem(obj);
    }
}
