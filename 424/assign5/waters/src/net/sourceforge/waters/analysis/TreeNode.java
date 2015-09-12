package net.sourceforge.waters.analysis;

import java.util.ArrayList;

import net.sourceforge.waters.model.des.EventProxy;
import net.sourceforge.waters.model.des.StateProxy;

/**
 * A simple node in a tree containing two StateProxy objects.
 * 
 * @author gb21
 */
public class TreeNode
{
    /**
     * The children of this node in the tree.
     */
    private ArrayList children = new ArrayList();
    
    /**
     * The event that occured to take the two synchronous products to their current states (<code>plantState</code>
     * and <code>specState</code>).
     */
    private EventProxy event;
    
    /**
     * The parent of this node in the tree.
     */
    private TreeNode parent = null;
    
    /**
     * The state of the plant synchronous product.
     */
    private StateProxy plantState;
    
    /**
     * The state of the spec synchronous product.
     */
    private StateProxy specState;
    
    /**
     * Creates an instance of <code>TreeNode</code>.
     * 
     * @param plantState The state of the plant synchronous product.
     * @param specState The state of the spec synchronous product.
     */
    public TreeNode(StateProxy plantState, StateProxy specState, EventProxy event)
    {
        this.plantState = plantState;
        this.specState = specState;
        this.event = event;
    }
    
    /**
     * Adds <code>newChild</code> to the children of this node.
     *
     * @param newChild The child node to add.
     */
    public void addChild(TreeNode newChild)
    {
        newChild.setParent(this);
        children.add(newChild);
    }
    
    /**
     * Tests the equality of this and another object dependant on their contained states.
     * 
     * @param o The object to test for equality with this one.
     */
    public boolean equals(Object o)
    {
        TreeNode otherNode;
        
        try
        {
            otherNode = (TreeNode) o;
        }
        catch (ClassCastException e)
        {
            return (false);
        }
        
        if (hashCode() == otherNode.hashCode())
        {
            return (true);
        }
        
        return (false);
    }
    
    /**
     * Builds a hash code from the hash codes of its contained states.
     */
    public int hashCode()
    {
        if (plantState == null || specState == null)
        {
            return (0);
        }
        
        return (plantState.hashCode() + specState.hashCode());
    }
    
    /**
     * Returns the children of this node in the tree.
     * 
     * @return The children of this node in the tree.
     */
    public ArrayList getChildren()
    {
        return (children);
    }
    
    /**
     * Returns the event that occured to take the two synchronous products to their current states
     * (<code>plantState</code> and <code>specState</code>).
     * 
     * @return The event that occured to take the two synchronous products to their current states
     * (<code>plantState</code> and <code>specState</code>).
     */
    public EventProxy getEvent()
    {
        return (event);
    }
    
    /**
     * Returns the parent of this node in the tree.
     * 
     * @return The parent of this node in the tree.
     */
    public TreeNode getParent()
    {
        return (parent);
    }
    
    /**
     * Returns the state of the plant synchronous product.
     * 
     * @return The state of the plant synchronous product.
     */
    public StateProxy getPlantState()
    {
        return (plantState);
    }
    
    /**
     * Returns the state of the spec synchronous product.
     * 
     * @return The state of the spec synchronous product.
     */
    public StateProxy getSpecState()
    {
        return (specState);
    }
    
    /**
     * Sets the parent of this node in the tree.
     * 
     * @param newParent The parent of this node in the tree.
     */
    public void setParent(TreeNode newParent)
    {
        parent = newParent;
    }
}
