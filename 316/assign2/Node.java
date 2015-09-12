import java.util.ArrayList;
import java.util.List;

/**
 * A node in a search tree. Contains all needed information to uniquely destinguish the node.
 * 
 * @author gb21
 */
public class Node
{
    /**
     * Inserts 'node' into 'nodes' in order according to the 'pathCost' of the node. Nodes with a lower 'pathCost' will have
     * lower indexes than nodes with higher a 'pathCost'.
     * 
     * @param nodes The list to insert 'node' into.
     * @param node The node to insert into 'nodes'.
     * 
     * @return 0 if the insert was successful, 1 otherwise.
     */
    public static int addInOrder(List nodes, Node node)
    {
        for (int index = 0; index < nodes.size(); index++)
        {
            if (((Node) nodes.get(index)).getPathCost() > node.getPathCost())
            {
                nodes.add(index, node);
                
                return (0);
            }
        }
        
        nodes.add(node);
        return (0);
    }
    
    /**
     * The action taken to reach this node.
     */
    private int action;
    
    /**
     * The depth of this node in the search tree.
     */
    private int depth;
    
    /**
     * The parent of this node.
     */
    private Node parent;
    
    /**
     * The estimated cost of choosing this node to find a solution for this problem.
     */
    private int pathCost;
    
    /**
     * The state of the problem at the point of this node in the search tree.
     */
    private State state;
    
    /**
     * Creates an instance of Node.
     * 
     * Initiates all internal variables.
     * The initiation of the 'pathCost' is dependant on the heuristic used by the state.
     * 
     * @param newParent The parent of this node.
     * @param newState The state of the problem at the point of this node in the search tree.
     * @param newAction The action taken to reach this node.
     */
    public Node(Node newParent, State newState, int newAction)
    {
        parent = newParent;
        state = newState;
        action = newAction;
        
        if (parent != null)
        {
            depth = parent.getDepth() + 1;
            pathCost = parent.getPathCost() + state.calcH();
        }
        else
        {
            depth = 0;
            pathCost = state.calcH();
        }
    }
    
    /**
     * Returns true if this node and the node parametised are equal, false otherwise. Equality is dependant solely on
     * the equality of the states these nodes contain.
     * 
     * @return True if this node and the node parametised are equal, false otherwise.
     */
    public boolean equals(Object obj)
    {
        try
        {
            Node otherNode = (Node) obj;
            
            if (!state.equals(otherNode.getState()))
            {
                return (false);
            }
        }
        catch (ClassCastException e)
        {
            return (false);
        }
        
        return (true);
    }
    
    /**
     * Returns all possible children of this node. Children are those nodes that contain the state of the problem
     * after a valid action has been performed on the current state.
     * 
     * @return All possible children of this node.
     */
    public List getChildren()
    {
        List children = new ArrayList();
        int[] blank = state.getBlank();
        
        if (blank[1] < Problem.SIDE_LENGTH - 1)
        {
            children.add(new Node(this, new State(state, State.DOWN), State.DOWN));
        }
        if (blank[0] > 0)
        {
            children.add(new Node(this, new State(state, State.LEFT), State.LEFT));
        }
        if (blank[0] < Problem.SIDE_LENGTH - 1)
        {
            children.add(new Node(this, new State(state, State.RIGHT), State.RIGHT));
        }
        if (blank[1] > 0)
        {
            children.add(new Node(this, new State(state, State.UP), State.UP));
        }
        
        return (children);
    }
    
    /**
     * Returns the depth of this node in the search tree.
     * 
     * @return The depth of this node in the search tree.
     */
    public int getDepth()
    {
        return (depth);
    }    
    
    /**
     * Returns the parent of this node.
     * 
     * @return The parent of this node.
     */
    public Node getParent()
    {
        return (parent);
    }
    
    /**
     * Returns the estimated cost of choosing this node to find a solution for this problem.
     * 
     * @return The estimated cost of choosing this node to find a solution for this problem.
     */
    public int getPathCost()
    {
        return (pathCost);
    }
    
    /**
     * Returns the state of the problem at the point of this node in the search tree.
     * 
     * @return The state of the problem at the point of this node in the search tree.
     */
    public State getState()
    {
        return (state);
    }
}
