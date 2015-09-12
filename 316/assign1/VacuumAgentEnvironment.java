import java.util.Random;

/**
 * An environment compatable with a VacuumAgent.
 * 
 * @author gb21
 */
public class VacuumAgentEnvironment
{
    /**
     * Holds the states of all the zones in this environment.
     */
    private char[][] envGrid;
    
    /**
     * The position of the 'home' zone in the x direction.
     */
    private int xHome;
    /**
     * The length of the environment in the x direction.
     */
    private int xLength;
    
    /**
     * The position of the 'home' zone in the y direction.
     */
    private int yHome;
    
    /**
     * The length of the environment in the y direction.
     */
    private int yLength;

    /**
     * Creates an instant of a VacuumAgentEnvironment.
     * 
     * The environment is a two dimensional box shape with sides of length l where minSize <= l <= maxSize.
     * Each zone in the environment has one of four statuses, clean, dirty, obstacle and home.
     * 
     * @param minSide The minimum length that the sides of the environment can be.
     * @param maxSide The maximum length that the sides of the environment can be.
     */
    public VacuumAgentEnvironment(int minSide, int maxSide)
    {
        Random random = new Random(System.currentTimeMillis());
        
        // Create environment.
        xLength = random.nextInt(maxSide - minSide + 1) + minSide;
        yLength = random.nextInt(maxSide - minSide + 1) + minSide;
        envGrid = new char[xLength][yLength];
        
        // Populate environment.
        for (int xIndex = 0; xIndex < xLength; xIndex++)
        {
            for (int yIndex = 0; yIndex < yLength; yIndex++)
            {
                if (random.nextFloat() < 0.05f)
                {
                    envGrid[xIndex][yIndex] = 'd';
                }
                else if (random.nextFloat() < 0.05f)
                {
                    envGrid[xIndex][yIndex] = 'o';
                }
                else
                {
                    envGrid[xIndex][yIndex] = 'c';
                }
            }
        }
        xHome = random.nextInt(xLength);
        yHome = random.nextInt(yLength);
        envGrid[xHome][yHome] = 'h';
        
        // TESTING \\
        // Output environment grid.
        System.out.println("xLength = " + xLength);
        System.out.println("yLength = " + yLength);
        for (int yIndex = 0; yIndex < yLength; yIndex++)
        {
            for (int xIndex = 0; xIndex < xLength; xIndex++)
            {
                System.out.print(envGrid[xIndex][yIndex] + " ");
            }
            
            System.out.print('\n');
        }
    }
    
    /**
     * Returns the status of the zone at the position given.
     * 
     * @param x The position of the zone in the x direction.
     * @param y The position of the zone in the y direction.
     * 
     * @return The status of the zone at the position given.
     */
    public char getStatus(int x, int y)
    {
        return (envGrid[x][y]);
    }
    
    /**
     * Returns the position of the 'home' zone in the x direction.
     * 
     * @return The position of the 'home' zone in the x direction.
     */
    public int getXHome()
    {
        return (xHome);
    }
    
    /**
     * Returns the length of the environment in the x direction.
     * 
     * @return The length of the environment in the x direction.
     */
    public int getXLength()
    {
        return (xLength);
    }
    
    /**
     * Returns the position of the 'home' zone in the y direction.
     * 
     * @return The position of the 'home' zone in the y direction.
     */
    public int getYHome()
    {
        return (yHome);
    }
    
    /**
     * Returns the length of the environment in the y direction.
     * 
     * @return The length of the environment in the y direction.
     */
    public int getYLength()
    {
        return (yLength);
    }
    
    /**
     * Sets the status of the zone at the position given.
     * 
     * @param x The position of the zone in the x direction.
     * @param y The position of the zone in the y direction.
     * @param newStatus The status to give the zone.
     */
    public void setStatus(int x, int y, char newStatus)
    {
        envGrid[x][y] = newStatus;
    }
}