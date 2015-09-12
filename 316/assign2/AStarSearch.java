import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Algorithms for implementing both A* search and a bidirectional A* search.
 * 
 * @author gb21
 */
public class AStarSearch
{
    /**
     * The amount of nodes generated whilst searching for a solution to the problem.
     */
    private static int nodesGenerated;

    /**
     * Performs a bidirectional A* search for a path between the initial state and the goal state of 'problem'.
     * 
     * @param problem The problem of which the bidirectional A* search is attempting to find a solution.
     * 
     * @return The node last visited if the search was successful, null otherwise.
     */
    public static Node bidirectionalSearch(Problem problem)
    {
        List visitedStatesA = new ArrayList();
        List fringeA = new ArrayList();
        fringeA.add(new Node(null, problem.getInitialState(), State.NONE));
        List visitedStatesB = new ArrayList();
        List fringeB = new ArrayList();
        fringeB.add(new Node(null, problem.getGoalState(), State.NONE));
        nodesGenerated = 2;

        Node currentNode;
        List newStates;
        List successors;

        while (!fringeA.isEmpty() && !fringeB.isEmpty())
        {
            // A* search starting at the initial state.
            if (!fringeA.isEmpty())
            {
                currentNode = (Node) fringeA.remove(0);

                visitedStatesA.add(currentNode);

                successors = currentNode.getChildren();
                nodesGenerated += successors.size();

                newStates = insertNewStates(fringeA, successors, visitedStatesA);

                for (int index = 0; index < newStates.size(); index++)
                {
                    if (fringeB.contains((Node) newStates.get(index)))
                    {
                        return (currentNode);
                    }
                }
            }

            // A* search starting at the goal state.
            if (!fringeB.isEmpty())
            {
                currentNode = (Node) fringeB.remove(0);

                visitedStatesB.add(currentNode);

                successors = currentNode.getChildren();
                nodesGenerated += successors.size();

                newStates = insertNewStates(fringeB, successors, visitedStatesB);

                for (int index = 0; index < newStates.size(); index++)
                {
                    if (fringeA.contains((Node) newStates.get(index)))
                    {
                        return (currentNode);
                    }
                }
            }
        }
        
        return (null);
    }

    /**
     * Inserts any nodes in 'newNodes' that contain states which have not already been visited during this search into
     * 'nodes'.
     * 
     * @param nodes The list to insert any nodes containing states not previously visited.
     * @param newNodes The nodes to insert into 'nodes' if they contain states not previously visited.
     * @param visitedStates The states which have already been visited.
     * 
     * @return A list containing all nodes in 'newNodes' that were successfully inserted into 'nodes'.
     */
    private static List insertNewStates(List nodes, List newNodes, List visitedStates)
    {
        Iterator newNodeIter = newNodes.iterator();
        Node currentNode;
        List insertedNodes = new ArrayList();

        while (newNodeIter.hasNext())
        {
            currentNode = (Node) newNodeIter.next();

            if (!visitedStates.contains(currentNode))
            {
                Node.addInOrder(nodes, currentNode);
                insertedNodes.add(currentNode);
            }
        }

        return (insertedNodes);
    }

    /**
     * Generates a problem, attempts to solve it and outputs the amount of nodes generated during the search.
     * 
     * @param args Ignores any arguments.
     */
    public static void main(String[] args)
    {
        Problem problem = new Problem(14);

        System.out.println("Solving...");

        //Node goal = search(problem);
        Node goal = bidirectionalSearch(problem);

        if (goal != null)
        {
            System.out.println("Solved:");
            goal.getState().printState();
        }
        else
        {
            System.out.println("Not solved.");
        }

        System.out.println("Nodes generated: " + nodesGenerated);
    }

    /**
     * Performs an A* search for a path between the initial state and the goal state of 'problem'.
     * 
     * @param problem The problem of which the A* search is attempting to find a solution.
     * 
     * @return The node last visited if the search was successful, null otherwise.
     */
    public static Node search(Problem problem)
    {
        List visitedStates = new ArrayList();
        List fringe = new ArrayList();
        fringe.add(new Node(null, problem.getInitialState(), State.NONE));
        nodesGenerated = 1;

        Node currentNode;
        List successors;

        while (!fringe.isEmpty())
        {
            currentNode = (Node) fringe.remove(0);

            if (problem.isGoalState(currentNode.getState()))
            {
                return (currentNode);
            }

            visitedStates.add(currentNode);

            successors = currentNode.getChildren();
            nodesGenerated += successors.size();

            insertNewStates(fringe, successors, visitedStates);
        }

        return (null);
    }
}
