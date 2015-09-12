/**
 * Represents a state in the problem. This consists of all the positions of the tiles in the puzzle and a unique
 * variable is kept to determine the position of the blank tile (or space) for ease of use.
 * 
 * @author gb21
 */
public class State
{
    /**
     * A downward movement of the blank tile (or space).
     */
    public static final int DOWN = 0;

    /**
     * A left movement of the blank tile (or space).
     */
    public static final int LEFT = 1;

    /**
     * No movement of the blank tile (or space).
     */
    public static final int NONE = 4;

    /**
     * A right movement of the blank tile (or space).
     */
    public static final int RIGHT = 2;

    /**
     * An upward movement of the blank tile (or space).
     */
    public static final int UP = 3;

    /**
     * The position of the blank tile (or space).
     */
    private int[] blank;

    /**
     * All the positions of the tiles in the puzzle.
     */
    private int[][] values;

    /**
     * Creates an instance of State.
     * Fills new array objects with the contents of the parameterised arrays.
     * 
     * @param newValues All the positions of the tiles in the puzzle.
     * @param newBlank The position of the blank tile (or space).
     */
    public State(int newValues[][], int newBlank[])
    {
        // Copy arrays.
        blank = new int[newBlank.length];
        values = new int[Problem.SIDE_LENGTH][Problem.SIDE_LENGTH];
        System.arraycopy(newBlank, 0, blank, 0, newBlank.length);
        for (int index = 0; index < Problem.SIDE_LENGTH; index++)
        {
            System.arraycopy(newValues[index], 0, values[index], 0, newValues.length);
        }
    }

    /**
     * Creates an instance of State.
     * Fills new array objects with the contents of the arrays in the parameterised state and then performs 'move' on
     * it.
     * 
     * @param state The state to initialise this state to before performing 'move' on it.
     * @param move The move to perform on this state once initialized.
     */
    public State(State state, int move)
    {
        // Copy arrays.
        blank = new int[state.getBlank().length];
        values = new int[Problem.SIDE_LENGTH][Problem.SIDE_LENGTH];
        System.arraycopy(state.getBlank(), 0, blank, 0, state.getBlank().length);
        for (int index = 0; index < Problem.SIDE_LENGTH; index++)
        {
            System.arraycopy(state.getValues()[index], 0, values[index], 0, state.getValues().length);
        }

        // Perform move.
        if (move == DOWN)
        {
            values[blank[0]][blank[1]] = values[blank[0]][blank[1] + 1];
            blank[1] += 1;
            values[blank[0]][blank[1]] = 0;
        }
        else if (move == LEFT)
        {
            values[blank[0]][blank[1]] = values[blank[0] - 1][blank[1]];
            blank[0] -= 1;
            values[blank[0]][blank[1]] = 0;
        }
        else if (move == RIGHT)
        {
            values[blank[0]][blank[1]] = values[blank[0] + 1][blank[1]];
            blank[0] += 1;
            values[blank[0]][blank[1]] = 0;
        }
        else if (move == UP)
        {
            values[blank[0]][blank[1]] = values[blank[0]][blank[1] - 1];
            blank[1] -= 1;
            values[blank[0]][blank[1]] = 0;
        }
    }

    /**
     * Calculates the heuristic function of this state.
     * 
     * @return The result of the heuristic function of this state.
     */
    public int calcH()
    {
        //return (h1());
        //return (h2());
        return (h0());
    }

    /**
     * Returns true if this state and the state parametised are equal, false otherwise. Equality is dependant on all
     * the positions of the tiles being the same.
     * 
     * @return True if this state and the state parametised are equal, false otherwise.
     */
    public boolean equals(Object obj)
    {
        try
        {
            State otherState = (State) obj;
            int[][] otherValues = otherState.getValues();

            for (int indexa = 0; indexa < Problem.SIDE_LENGTH; indexa++)
            {
                for (int indexb = 0; indexb < Problem.SIDE_LENGTH; indexb++)
                {
                    if (values[indexb][indexa] != otherValues[indexb][indexa])
                    {
                        return (false);
                    }
                }
            }
        }
        catch (ClassCastException e)
        {
            return (false);
        }

        return (true);
    }

    /**
     * Returns the position of the blank tile (or space).
     * 
     * @return The position of the blank tile (or space).
     */
    public int[] getBlank()
    {
        return (blank);
    }

    /**
     * Returns all the positions of the tiles in the puzzle.
     * 
     * @return All the positions of the tiles in the puzzle.
     */
    public int[][] getValues()
    {
        return (values);
    }

    /**
     * A heuristic function.
     * This function's result is always 0.
     * 
     * @return This function's result.
     */
    private int h0()
    {
        return (0);
    }

    /**
     * A heuristic function.
     * This function's result is equal to the amount of tiles which are misplaced in this state.
     * 
     * @return This function's result.
     */
    private int h1()
    {
        int count = 1;
        int predictedCost = 0;

        for (int indexa = 0; indexa < Problem.SIDE_LENGTH; indexa++)
        {
            for (int indexb = 0; indexb < Problem.SIDE_LENGTH; indexb++)
            {
                if (values[indexb][indexa] != count && values[indexb][indexa] != 0)
                {
                    // Increment the result of this function.
                    predictedCost++;
                }
                count++;
            }
        }

        return (predictedCost);
    }

    /**
     * A heuristic function.
     * This function's result is equal to the amount of tiles which are misplaced in this state.
     * 
     * @return This function's result.
     */
    private int h2()
    {
        int count = 1;
        int predictedCost = 0;

        // For all tiles, check all tiles for the tile of the right value and find the manhatten distance between
        // them.
        for (int indexa = 0; indexa < Problem.SIDE_LENGTH; indexa++)
        {
            for (int indexb = 0; indexb < Problem.SIDE_LENGTH; indexb++)
            {
                for (int indexc = 0; indexc < Problem.SIDE_LENGTH; indexc++)
                {
                    for (int indexd = 0; indexd < Problem.SIDE_LENGTH; indexd++)
                    {
                        if (values[indexd][indexc] == count)
                        {
                            // Add the manhatten value of tile 'count' to the result of this function.
                            predictedCost += Math.abs(indexb - indexd) + Math.abs(indexa - indexc);
                        }
                    }
                }
                count++;
            }
        }

        return (predictedCost);
    }

    /**
     * Prints a representation of this state to standard out.
     */
    public void printState()
    {
        for (int indexa = 0; indexa < Problem.SIDE_LENGTH; indexa++)
        {
            for (int indexb = 0; indexb < Problem.SIDE_LENGTH; indexb++)
            {
                System.out.print(values[indexb][indexa] + "\t");
            }
            System.out.println("");
        }
    }
}
