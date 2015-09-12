import java.util.ArrayList;

/**
 * A node in a DecisionTree.
 * 
 * @author gb21
 */
public class Node
{
    /**
     * The attribute that this node has made a decision with.
     */
    private int attribute = -1;
    
    /**
     * The children of this node.
     */
    private ArrayList children = new ArrayList();
    
    /**
     * The classification of this node.
     */
    private boolean classification = true;
    
    /**
     * The value of the attribute of this node's parent that this node represents.
     */
    private boolean value = true;
    
    /**
     * Adds a node to the children of this node.
     * 
     * @param child The node to add to the children of this node.
     */
    public void addChild(Node child)
    {
        children.add(child);
    }
    
    /**
     * Returns the attribute that this node has made a decision with.
     * 
     * @return The attribute that this node has made a decision with.
     */
    public int getAttribute()
    {
        return (attribute);
    }
    
    /**
     * Returns the children of this node.
     * 
     * @return The children of this node.
     */
    public ArrayList getChildren()
    {
        return (children);
    }
    
    /**
     * Returns the classification of this node.
     * 
     * @return The classification of this node.
     */
    public boolean getClassification()
    {
        return (classification);
    }
    
    /**
     * Returns the value of the attribute of this node's parent that this node represents.
     * 
     * @return The value of the attribute of this node's parent that this node represents.
     */
    public boolean getValue()
    {
        return (value);
    }
    
    /**
     * Sets the attribute that this node has made a decision with.
     * 
     * @param attribute The attribute that this node has made a decision with.
     */
    public void setAttribute(int attribute)
    {
        this.attribute = attribute;
    }
    
    /**
     * Sets the classification of this node.
     * 
     * @param classification The classification of this node.
     */
    public void setClassification(boolean classification)
    {
        this.classification = classification;
    }
    
    /**
     * Sets the value of the attribute of this node's parent that this node represents.
     * 
     * @param value The value of the attribute of this node's parent that this node represents.
     */
    public void setValue(boolean value)
    {
        this.value = value;
    }
}
