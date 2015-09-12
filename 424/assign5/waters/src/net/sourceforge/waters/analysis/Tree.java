package net.sourceforge.waters.analysis;

import java.util.Iterator;

/**
 * A simple tree structure.
 * 
 * @author gb21
 */
public class Tree
{
    /**
     * The root node of this tree.
     */
    private TreeNode root;
    
    /**
     * Creates an instance of <code>SearchTree</code>.
     * 
     * @param root The root node of this tree.
     */
    public Tree(TreeNode root)
    {
        this.root = root;
    }
    
    /**
     * Returns the root node of this tree.
     * 
     * @return The root node of this tree.
     */
    public TreeNode getRoot()
    {
        return (root);
    }
}
