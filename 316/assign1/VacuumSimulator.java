/**
 * Implements the VacuumAgent in a VacuumAgentEnvironment. This is a driver class to link the two and test the
 * performance of the agent in the environment.
 * 
 * @author gb21
 */
public class VacuumSimulator
{
    /**
     * The agent this simulation tests.
     */
    private static VacuumAgent agent;

    /**
     * Represents the 'DOWN' orientation of the agent.
     */
    private static final int DOWN = 2;

    /**
     * The environment the agent is being tested in.
     */
    private static VacuumAgentEnvironment environment;

    /**
     * Represents the 'LEFT' orientation of the agent.
     */
    private static final int LEFT = 3;

    /**
     * The current orientation of the agent.
     */
    private static int orientAgent;
    
    /**
     * Represents the 'RIGHT' orientation of the agent.
     */
    private static final int RIGHT = 1;
    
    /**
     * Represents the 'UP' orientation of the agent.
     */
    private static final int UP = 0;

    /**
     * The current position of the agent in the x direction.
     */
    private static int xAgent;

    /**
     * The current position of the agent in the y direction.
     */
    private static int yAgent;

    /**
     * Implements the test of the agent in the environment. Keeps track of the agent's position and orientation as
     * well as updating the environment when the agent performs actions which will change the status of the
     * environment.
     * 
     * @param args
     */
    public static void main(String[] args)
    {        
        environment = new VacuumAgentEnvironment(8, 15);
        agent = new VacuumAgent(10000);

        xAgent = environment.getXHome();
        yAgent = environment.getYHome();
        orientAgent = UP;
        
        int score = 0;
        boolean collisionOccured = false;

        do
        {   
            // Process actions of the agent.
            if (agent.outputActuators() == agent.FORWARD)
            {
                collisionOccured = !processForward();
            }
            else if (agent.outputActuators() == agent.RIGHT)
            {
                orientAgent = (orientAgent + 1) % 4;
            }
            else if (agent.outputActuators() == agent.LEFT)
            {
                orientAgent = (orientAgent - 1);
                if (orientAgent < 0)
                {
                    orientAgent = 3;
                }
            }
            else
            {
                environment.setStatus(xAgent, yAgent, 'c');
                
                // Increase the score for cleaning a zone.
                score += 100;
            }
            
            // Process the inputs to the agent.
            agent.inputPerspect(collisionOccured, environment.getStatus(xAgent, yAgent) == 'd',
                    environment.getStatus(xAgent, yAgent) == 'h');
            
            // Decrease the score for performing an action.
            score--;
            
            collisionOccured = false;
        }
        while (agent.outputActuators() != agent.OFF);
        
        if (environment.getStatus(xAgent, yAgent) != 'h')
        {
            // Decrease the score for not returning home.
            score -= 1000;
            
            System.out.println("Not at home!");
        }
        
        System.out.println("Finished!");
        System.out.println("Total score: " + score);
    }

    /**
     * Determines whether the agent has attempted to move into an obstacle or out of the environment. If the agent is
     * attempting a legal move its position is updated.
     * 
     * @return True if the agent has attempted an illegal move, false otherwise.
     */
    private static boolean processForward()
    {
        if (orientAgent == UP)
        {
            // If the agent is not trying to leave environment.
            if (xAgent > 0)
            {
                // If the agent is not trying to move onto an obstacle.
                if (environment.getStatus(xAgent - 1, yAgent) != 'o')
                {
                    xAgent--;
                    return (false);
                }
            }
            return (true);
        }

        if (orientAgent == DOWN)
        {
            // If the agent is not trying to leave environment.
            if (xAgent < environment.getXLength() - 1)
            {
                // If the agent is not trying to move onto an obstacle.
                if (environment.getStatus(xAgent + 1, yAgent) != 'o')
                {
                    xAgent++;
                    return (false);
                }
            }
            return (true);
        }

        if (orientAgent == LEFT)
        {
            // If the agent is not trying to leave environment.
            if (yAgent > 0)
            {
                // If the agent is not trying to move onto an obstacle.
                if (environment.getStatus(xAgent, yAgent - 1) != 'o')
                {
                    yAgent--;
                    return (false);
                }
            }
            return (true);
        }

        // If the agent is not trying to leave environment.
        if (yAgent < environment.getYLength() - 1)
        {
            // If the agent is not trying to move onto an obstacle.
            if (environment.getStatus(xAgent, yAgent + 1) != 'o')
            {
                yAgent++;
                return (false);
            }
        }
        return (true);
    }
}
