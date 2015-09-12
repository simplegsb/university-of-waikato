import java.util.Iterator;

/**
 * A decision tree.
 * 
 * @author gb21
 */
public class DecisionTree
{
    /**
     * The root node of this tree.
     */
    private Node root = new Node();
    
    /**
     * Returns the root node of this tree.
     * 
     * @return The root node of this tree.
     */
    public Node getRoot()
    {
        return (root);
    }
    
    /**
     * Tests the given example against this decision tree to determine its classification.
     * 
     * @param example The example to test.
     * 
     * @return True if the example is classified as true by this tree, false otherwise.
     */
    public boolean testExample(String example)
    {
        Node currentNode = root;
        Node currentChild;
        Iterator childrenIter;
        int attributeIndex;
        
        while (currentNode.getChildren().size() > 0)
        {
            attributeIndex = currentNode.getAttribute();
            childrenIter = currentNode.getChildren().iterator();
            
            // If attribute is false in this example go to false child.
            if (example.charAt(attributeIndex) == '0')
            {
                while (childrenIter.hasNext())
                {
                    currentChild = (Node) childrenIter.next();
                    
                    if (currentChild.getValue() == false)
                    {
                        currentNode = currentChild;
                    }
                }
            }
            // Otherwise go to true child.
            else
            {
                while (childrenIter.hasNext())
                {
                    currentChild = (Node) childrenIter.next();
                    
                    if (currentChild.getValue() == true)
                    {
                        currentNode = currentChild;
                    }
                }
            }
        }
        
        return (currentNode.getClassification());
    }
}
