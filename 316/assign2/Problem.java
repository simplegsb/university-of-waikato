import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Formulates a problem of the 15-puzzle type. The solution to the problem will be at the depth parameterized in the
 * constructor.
 * 
 * @author gb21
 */
public class Problem
{
    /**
     * The length of the side of the puzzle i.e. there will be n * n tiles in a ((n * n) - 1)-puzzle problem and the
     * SIDE_LENGTH value for that puzzle will be n.
     */
    public static final int SIDE_LENGTH = 4;
    
    /**
     * The state in which the puzzle will be in when it has been solved.
     */
    private State goalState;
    
    /**
     * The state in which the puzzle will be initially.
     */
    private State initialState;
    
    /**
     * Creates an instance of Problem.
     * 
     * Initializes the goal and initial states.
     * 
     * @param depth The depth at which a solution for this puzzle can be found.
     */
    public Problem(int depth)
    {
        int[] blank = {SIDE_LENGTH - 1, SIDE_LENGTH - 1};
        int[][] values = new int[SIDE_LENGTH][SIDE_LENGTH];
        
        // Init goal state.
        int count = 1;
        for (int indexa = 0; indexa < SIDE_LENGTH; indexa++)
        {
            for (int indexb = 0; indexb < SIDE_LENGTH; indexb++)
            {
                values[indexb][indexa] = count;
                count++;
            }
        }
        values[blank[0]][blank[1]] = 0;
        
        goalState = new State(values, blank);
        
        System.out.println("Goal state:");
        goalState.printState();
        
        // Init initial state.
        System.out.println("Generating initial state...");
        
        initialState = new State(values, blank);
        List visitedStates = new ArrayList();
        Random random = new Random(System.currentTimeMillis());
        boolean validMoveMade;
        
        // Make 'depth' movements from the goal state to initialize the initial state (avoiding repeated states).
        while (depth > 0)
        {
            visitedStates.add(initialState);
            
            // Try to create a successor to the current state (initialState) until a vlid one is found. A valid
            // successor is one that is defined (State constructor does not throw exception for invalid move
            // parameter) and one that has not already been visited whilst initializing this initial state.
            validMoveMade = false;
            while (!validMoveMade)
            {                
                try
                {
                    initialState = new State(initialState, random.nextInt(4));
                    
                    // If successor has already been visited revert to the previous state.
                    if (visitedStates.contains(initialState))
                    {
                        initialState = (State) visitedStates.get(visitedStates.size() - 1);
                    }
                    else
                    {
                        validMoveMade = true;
                    }
                }
                // Thrown if the state attempted to be created is not defined.
                catch (IndexOutOfBoundsException e)
                {}
            }
            
            depth--;
        }
        
        System.out.println("Initial state generated:");
        initialState.printState();
    }
    
    /**
     * Returns the goal state of this problem.
     * 
     * @return The goal state of this problem.
     */
    public State getGoalState()
    {
        return (goalState);
    }
    
    /**
     * Returns the initial state of this problem.
     * 
     * @return The initial state of this problem.
     */
    public State getInitialState()
    {
        return (initialState);
    }
    
    /**
     * Returns true if the state parameterized is a goal state and false otherwise.
     * 
     * @param state The state to be checked.
     * 
     * @return True if the state parameterized is a goal state and false otherwise.
     */
    public boolean isGoalState(State state)
    {
        if (goalState.equals(state))
        {
            return (true);
        }
        return (false);
    }
}
