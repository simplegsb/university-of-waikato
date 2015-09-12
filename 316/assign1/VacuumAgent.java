import java.util.Random;

/**
 * An artificially intelligent pure reflex agent that can navigate an environment with obstacles and clean dirt from
 * dirty zones. It will also turn off if it reaches a 'home' zone.
 * 
 * @author gb21
 */
public class VacuumAgent
{
    /**
     * The action currently chosen by the agent from the last percept input.
     */
    private int action;
    
    /**
     * The amount of moves left in this agent's life time. The agent will turn itself off when no moves are left.
     */
    private int availableMoves;
    
    /**
     * Represents the action 'FORWARD' that can be made by the agent.
     */
    public final int FORWARD = 0;
    
    /**
     * Represents the action 'LEFT' that can be made by the agent.
     */
    public final int LEFT = 2;
    
    /**
     * Represents the action 'OFF' that can be made by the agent.
     */
    public final int OFF = 4;
    /**
     * Used to decide some of the actions of the agent.
     */
    private Random random = new Random(System.currentTimeMillis());

    /**
     * Represents the action 'RIGHT' that can be made by the agent.
     */
    public final int RIGHT = 1;
    
    /**
     * Represents the action 'SUCK' that can be made by the agent.
     */
    public final int SUCK = 3;
    
    /**
     * Creates an instance of a VacuumAgent.
     * 
     * @param lifeTime The amount of moves that this agent can perform before it turns itself off.
     */
    public VacuumAgent(int lifeTime)
    {
        action = 0;
        availableMoves = lifeTime;
    }
    
    /**
     * Takes the inputs from a percept and uses them to decide which action the agent should take.
     * 
     * @param obstacleSensor True if the agent has hit an obstacle as a result of its previous action.
     * @param dirtSensor True if the zone the agent is currently in contains dirt.
     * @param homeSensor True if the agent is in the 'home' zone.
     * 
     * @return The action to be taken by the agent.
     */
    private int agentProgram(boolean obstacleSensor, boolean dirtSensor, boolean homeSensor)
    {
        availableMoves--;
        
        // Turn off when all moves are used.
        if (availableMoves == 0)
        {
            return (OFF);
        }
        
        // Always clean dirt if it is present.
        if (dirtSensor)
        {
            return (SUCK);
        }
        
        // If an obstacle has been hit change course.
        if (obstacleSensor)
        {
            if (random.nextBoolean())
            {
                return (RIGHT);
            }
            return (LEFT);
        }
        
        // If you are home and not much moves are available turn off.
        if (homeSensor && availableMoves < 1000)
        {
            return (OFF);
        }
        
        // Otherwise move randomly.
        if (random.nextInt(4) > 0)
        {
            return (FORWARD);
        }
        if (random.nextBoolean())
        {
            return (LEFT);
        }
        return (RIGHT);
    }
    
    /**
     * Returns the available moves the agent has before it will shut itself off.
     * 
     * @return The available moves the agent has before it will shut itself off.
     */
    public int getAvailableMoves()
    {
        return (availableMoves);
    }

    /**
     * Reads a persept of the environment.
     * 
     * @param obstacleSensor True if the agent has hit an obstacle as a result of its previous action.
     * @param dirtSensor True if the zone the agent is currently in contains dirt.
     * @param homeSensor True if the agent is in the 'home' zone.
     */
    public void inputPerspect(boolean obstacleSensor, boolean dirtSensor, boolean homeSensor)
    {
        action = agentProgram(obstacleSensor, dirtSensor, homeSensor);
    }
    
    /**
     * Returns the action the agent has decided to take as a result of the last percept.
     * 
     * @return The action the agent has decided to take as a result of the last percept.
     */
    public int outputActuators()
    {        
        return (action);
    }
}
