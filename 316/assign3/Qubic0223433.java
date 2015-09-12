import java.util.ArrayList;

/**
 * A qubic player that determines its next move using alpha-beta search.
 * 
 * @author gb21
 */
public class Qubic0223433 implements QubicInterface
{
    /**
     * The size of each dimension of the qubic grid.
     * i.e. A qubic grid is GRID_SIZE by GRID_SIZE by GRID_SIZE spaces.
     */
    private final int GRID_SIZE = 4;

    /**
     * The maximum depth to search to.
     */
    private int maxDepth;

    /**
     * A negative value lower than all possible utility values.
     */
    private final int NEG_INFINITY = -99999;

    /**
     * The player to move.
     */
    private byte playerToMove;

    /**
     * A positive value higher than all possible utility values.
     */
    private final int POS_INFINITY = 99999;

    /**
     * A space in the qubic grid that is not occupied.
     */
    private final int SPACE_EMPTY = 0;

    /**
     * Creates an instance of Qubic0223433.
     */
    public Qubic0223433()
    {
    }

    /**
     * Searches for the best successor to 'state' according to the evaluation function and returns it.
     * 
     * @param state The state to find the best successor for.
     * 
     * @return The best successor to 'state' according to the evaluation function.
     */
    private int[] alphaBetaSearch(byte[][][] state)
    {
        int[] result = maxValue(state, NEG_INFINITY, POS_INFINITY, 0);

        byte[][][] chosenSuccessor = (byte[][][]) getSuccessors(state).get(result[1]);

        return (getAction(state, chosenSuccessor));
    }

    /**
     * Copies the values of 'state' into a new set of byte arrays.
     * 
     * @param state The state to be copied.
     * 
     * @return A new set of byte arrays with the same values as 'state'.
     */
    private byte[][][] copyState(byte[][][] state)
    {
        byte[][][] stateCopy = new byte[GRID_SIZE][GRID_SIZE][GRID_SIZE];

        for (int xIndex = 0; xIndex < GRID_SIZE; xIndex++)
        {
            for (int yIndex = 0; yIndex < GRID_SIZE; yIndex++)
            {
                for (int zIndex = 0; zIndex < GRID_SIZE; zIndex++)
                {
                    stateCopy[xIndex][yIndex][zIndex] = state[xIndex][yIndex][zIndex];
                }
            }
        }

        return (stateCopy);
    }

    /**
     * Determines the utility of 'state'.
     * 
     * @param state The state to evaluate.
     * 
     * @return The utility of 'state'.
     */
    private int evaluationFunction(byte[][][] state)
    {
        // Terminal check.
        if (isWonByPlayer(state, playerToMove))
        {
            return (1000);
        }
        else if (isDrawn(state))
        {
            return (500);
        }
        else if (isWonByPlayer(state, playerToMove * -1))
        {
            return (-1000);
        }

        int utility = 0;
        int thisPlayer = 0;
        int otherPlayer = 0;
        boolean blocked = false;

        // Straight line check.
        for (int index = 0; index < GRID_SIZE; index++)
        {
            for (int index2 = 0; index2 < GRID_SIZE; index2++)
            {
                for (int index3 = 0; index3 < GRID_SIZE; index3++)
                {
                    // X axis straight lines.
                    if (state[index3][index][index2] == playerToMove && otherPlayer == 0)
                    {
                        thisPlayer++;
                    }
                    else if (state[index3][index][index2] == playerToMove * -1 && thisPlayer == 0)
                    {
                        otherPlayer++;
                    }
                    else
                    {
                        blocked = true;
                    }

                    // If it is possible to win in this line,
                    // utility += the amount of squares this player holds squared if this player can win, or
                    // utility -= the amount of squares the other player holds squared if the other player can win.
                    if (!blocked)
                    {
                        utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
                    }

                    thisPlayer = 0;
                    otherPlayer = 0;
                    blocked = false;
                }

                for (int index3 = 0; index3 < GRID_SIZE; index3++)
                {
                    // Y axis straight lines.
                    if (state[index][index3][index2] == playerToMove && otherPlayer == 0)
                    {
                        thisPlayer++;
                    }
                    else if (state[index][index3][index2] == playerToMove * -1 && thisPlayer == 0)
                    {
                        otherPlayer++;
                    }
                    else
                    {
                        blocked = true;
                    }

                    // If it is possible to win in this line,
                    // utility += the amount of squares this player holds squared if this player can win, or
                    // utility -= the amount of squares the other player holds squared if the other player can win.
                    if (!blocked)
                    {
                        utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
                    }

                    thisPlayer = 0;
                    otherPlayer = 0;
                    blocked = false;
                }

                for (int index3 = 0; index3 < GRID_SIZE; index3++)
                {
                    // Z axis straight lines.
                    if (state[index][index2][index3] == playerToMove && otherPlayer == 0)
                    {
                        thisPlayer++;
                    }
                    else if (state[index][index2][index3] == playerToMove * -1 && thisPlayer == 0)
                    {
                        otherPlayer++;
                    }
                    else
                    {
                        blocked = true;
                    }

                    // If it is possible to win in this line,
                    // utility += the amount of squares this player holds squared if this player can win, or
                    // utility -= the amount of squares the other player holds squared if the other player can win.
                    if (!blocked)
                    {
                        utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
                    }

                    thisPlayer = 0;
                    otherPlayer = 0;
                    blocked = false;
                }
            }
        }

        // Plane diagonal check.
        for (int index = 0; index < GRID_SIZE; index++)
        {
            // XY plane check.
            int index2 = 0;
            int index3 = 0;

            while (index2 < GRID_SIZE)
            {
                if (state[index2][index3][index] == playerToMove && otherPlayer == 0)
                {
                    thisPlayer++;
                }
                else if (state[index2][index3][index] == playerToMove * -1 && thisPlayer == 0)
                {
                    otherPlayer++;
                }
                else
                {
                    blocked = true;
                }

                // If it is possible to win in this line,
                // utility += the amount of squares this player holds squared if this player can win, or
                // utility -= the amount of squares the other player holds squared if the other player can win.
                if (!blocked)
                {
                    utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
                }

                thisPlayer = 0;
                otherPlayer = 0;
                blocked = false;

                index2++;
                index3++;
            }

            // XZ plane check.
            index2 = 0;
            index3 = 0;

            while (index2 < GRID_SIZE)
            {
                if (state[index2][index][index3] == playerToMove && otherPlayer == 0)
                {
                    thisPlayer++;
                }
                else if (state[index2][index][index3] == playerToMove * -1 && thisPlayer == 0)
                {
                    otherPlayer++;
                }
                else
                {
                    blocked = true;
                }

                // If it is possible to win in this line,
                // utility += the amount of squares this player holds squared if this player can win, or
                // utility -= the amount of squares the other player holds squared if the other player can win.
                if (!blocked)
                {
                    utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
                }

                thisPlayer = 0;
                otherPlayer = 0;
                blocked = false;

                index2++;
                index3++;
            }

            // YZ plane check.
            index2 = 0;
            index3 = 0;

            while (index2 < GRID_SIZE)
            {
                if (state[index2][index][index3] == playerToMove && otherPlayer == 0)
                {
                    thisPlayer++;
                }
                else if (state[index2][index][index3] == playerToMove * -1 && thisPlayer == 0)
                {
                    otherPlayer++;
                }
                else
                {
                    blocked = true;
                }

                // If it is possible to win in this line,
                // utility += the amount of squares this player holds squared if this player can win, or
                // utility -= the amount of squares the other player holds squared if the other player can win.
                if (!blocked)
                {
                    utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
                }

                thisPlayer = 0;
                otherPlayer = 0;
                blocked = false;

                index2++;
                index3++;
            }
        }

        // Multi plane diagonals.

        // 1st diagonal.
        int index = 0;
        int index2 = 0;
        int index3 = 0;

        while (index < GRID_SIZE)
        {
            if (state[index][index2][index3] == playerToMove && otherPlayer == 0)
            {
                thisPlayer++;
            }
            else if (state[index][index2][index3] == playerToMove * -1 && thisPlayer == 0)
            {
                otherPlayer++;
            }
            else
            {
                blocked = true;
            }

            // If it is possible to win in this line,
            // utility += the amount of squares this player holds squared if this player can win, or
            // utility -= the amount of squares the other player holds squared if the other player can win.
            if (!blocked)
            {
                utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
            }

            thisPlayer = 0;
            otherPlayer = 0;
            blocked = false;

            index++;
            index2++;
            index3++;
        }

        // 2nd diagonal.
        index = GRID_SIZE - 1;
        index2 = 0;
        index3 = 0;

        while (index >= 0)
        {
            if (state[index][index2][index3] == playerToMove && otherPlayer == 0)
            {
                thisPlayer++;
            }
            else if (state[index][index2][index3] == playerToMove * -1 && thisPlayer == 0)
            {
                otherPlayer++;
            }
            else
            {
                blocked = true;
            }

            // If it is possible to win in this line,
            // utility += the amount of squares this player holds squared if this player can win, or
            // utility -= the amount of squares the other player holds squared if the other player can win.
            if (!blocked)
            {
                utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
            }

            thisPlayer = 0;
            otherPlayer = 0;
            blocked = false;

            index--;
            index2++;
            index3++;
        }
        
        // 3rd diagonal.
        index = 0;
        index2 = GRID_SIZE - 1;
        index3 = 0;

        while (index < GRID_SIZE)
        {
            if (state[index][index2][index3] == playerToMove && otherPlayer == 0)
            {
                thisPlayer++;
            }
            else if (state[index][index2][index3] == playerToMove * -1 && thisPlayer == 0)
            {
                otherPlayer++;
            }
            else
            {
                blocked = true;
            }

            // If it is possible to win in this line,
            // utility += the amount of squares this player holds squared if this player can win, or
            // utility -= the amount of squares the other player holds squared if the other player can win.
            if (!blocked)
            {
                utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
            }

            thisPlayer = 0;
            otherPlayer = 0;
            blocked = false;

            index++;
            index2--;
            index3++;
        }
        
        // 4th diagonal.
        index = 0;
        index2 = 0;
        index3 = GRID_SIZE - 1;

        while (index < GRID_SIZE)
        {
            if (state[index][index2][index3] == playerToMove && otherPlayer == 0)
            {
                thisPlayer++;
            }
            else if (state[index][index2][index3] == playerToMove * -1 && thisPlayer == 0)
            {
                otherPlayer++;
            }
            else
            {
                blocked = true;
            }

            // If it is possible to win in this line,
            // utility += the amount of squares this player holds squared if this player can win, or
            // utility -= the amount of squares the other player holds squared if the other player can win.
            if (!blocked)
            {
                utility += thisPlayer * thisPlayer - otherPlayer * otherPlayer;
            }

            thisPlayer = 0;
            otherPlayer = 0;
            blocked = false;

            index++;
            index2++;
            index3--;
        }

        return (utility);
    }

    /**
     * Returns the action taken to get from 'state' to 'successor'.
     * 
     * @param state The state the action was taken at.
     * @param successor The state created as a result of the action.
     * 
     * @return The action taken to get from 'state' to 'successor'.
     */
    private int[] getAction(byte[][][] state, byte[][][] successor)
    {
        int[] action = new int[3];

        for (int xIndex = 0; xIndex < GRID_SIZE; xIndex++)
        {
            for (int yIndex = 0; yIndex < GRID_SIZE; yIndex++)
            {
                for (int zIndex = 0; zIndex < GRID_SIZE; zIndex++)
                {
                    if (state[xIndex][yIndex][zIndex] != successor[xIndex][yIndex][zIndex])
                    {
                        action[0] = xIndex;
                        action[1] = yIndex;
                        action[2] = zIndex;

                        return (action);
                    }
                }
            }
        }

        return (null);
    }

    /**
     * Returns a list of all possible successors of state.
     * 
     * @param state The state to create the successors of.
     * 
     * @return A list of all possible successors of state.
     */
    private ArrayList getSuccessors(byte[][][] state)
    {
        ArrayList successors = new ArrayList();

        for (int xIndex = 0; xIndex < GRID_SIZE; xIndex++)
        {
            for (int yIndex = 0; yIndex < GRID_SIZE; yIndex++)
            {
                for (int zIndex = 0; zIndex < GRID_SIZE; zIndex++)
                {
                    if (state[xIndex][yIndex][zIndex] == SPACE_EMPTY)
                    {
                        byte[][][] successor = copyState(state);
                        successor[xIndex][yIndex][zIndex] = playerToMove;
                        successors.add(successor);
                    }
                }
            }
        }

        return (successors);
    }

    /**
     * Determines if 'state' is a state in which the players have drawn.
     * 
     * @param state The state to be checked.
     * 
     * @return True if the players have drawn, false otherwise.
     */
    public boolean isDrawn(byte[][][] state)
    {
        for (int xIndex = 0; xIndex < GRID_SIZE; xIndex++)
        {
            for (int yIndex = 0; yIndex < GRID_SIZE; yIndex++)
            {
                for (int zIndex = 0; zIndex < GRID_SIZE; zIndex++)
                {
                    if (state[xIndex][yIndex][zIndex] == SPACE_EMPTY)
                    {
                        return (false);
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if 'state' is a terminal state.
     * 
     * @param state The state to be checked.
     * 
     * @return True if 'state' is a terminal state, false otherwise.
     */
    private boolean isTerminalState(byte[][][] state)
    {
        if (isWonByPlayer(state, playerToMove) || isDrawn(state))
        {
            return (true);
        }

        return (false);
    }

    /**
     * Determines if 'state' is a state in which 'player' has won.
     * 
     * @param state The state to be checked.
     * @param player The player to be checked
     * 
     * @return True if 'player' has won, false otherwise.
     */
    public boolean isWonByPlayer(byte[][][] state, int player)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (((state[i][j][0] == player) && (state[i][j][1] == player) && (state[i][j][2] == player) && (state[i][j][3] == player))
                        || ((state[i][0][j] == player) && (state[i][1][j] == player) && (state[i][2][j] == player) && (state[i][3][j] == player))
                        || ((state[0][i][j] == player) && (state[1][i][j] == player) && (state[2][i][j] == player) && (state[3][i][j] == player)))
                {
                    return true;
                }
            }
        }
        for (int i = 0; i < 4; i++)
        {
            if (((state[i][i][0] == player) && (state[i][i][1] == player) && (state[i][i][2] == player) && (state[i][i][3] == player))
                    || ((state[i][0][i] == player) && (state[i][1][i] == player) && (state[i][2][i] == player) && (state[i][3][i] == player))
                    || ((state[0][i][i] == player) && (state[1][i][i] == player) && (state[2][i][i] == player) && (state[3][i][i] == player)))
            {
                return true;
            }
        }
        for (int i = 0; i < 4; i++)
        {
            if (((state[i][3][0] == player) && (state[i][2][1] == player) && (state[i][1][2] == player) && (state[i][0][3] == player))
                    || ((state[3][0][i] == player) && (state[2][1][i] == player) && (state[1][2][i] == player) && (state[0][3][i] == player))
                    || ((state[0][i][3] == player) && (state[1][i][2] == player) && (state[2][i][1] == player) && (state[3][i][0] == player)))
            {
                return true;
            }
        }
        if (((state[0][0][0] == player) && (state[1][1][1] == player) && (state[2][2][2] == player) && (state[3][3][3] == player))
                || ((state[3][0][0] == player) && (state[2][1][1] == player) && (state[1][2][2] == player) && (state[0][3][3] == player))
                || ((state[0][3][0] == player) && (state[1][2][1] == player) && (state[2][1][2] == player) && (state[3][0][3] == player))
                || ((state[3][3][0] == player) && (state[2][2][1] == player) && (state[1][1][2] == player) && (state[0][0][3] == player)))
        {
            return true;
        }

        // I seem to have missed some diagonals, here is a fix:
        // code added by Jimmy
        for (int i = 0; i < 4; i++)
        {
            if ((state[i][0][0] == player) && (state[i][1][1] == player) && (state[i][2][2] == player)
                    && (state[i][3][3] == player))
            {
                return true;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            if ((state[0][0][i] == player) && (state[1][1][i] == player) && (state[2][2][i] == player)
                    && (state[3][3][i] == player))
            {
                return true;
            }
        }
        // end code added by jimmy
        // plus one more:
        for (int i = 0; i < 4; i++)
        {
            if ((state[0][i][0] == player) && (state[1][i][1] == player) && (state[2][i][2] == player)
                    && (state[3][i][3] == player))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the maximum utility of a successor of 'state'.
     * If state is a terminal state, returns the utility of 'state'.
     * If the successors of state are below the maximum depth and state is not a terminal state, returns
     * 'POS_INFINITY'.
     * 
     * @param state The state to check.
     * @param alpha The value of the best choice so far for MAX.
     * @param beta The value of the best choice so far for MIN.
     * 
     * @return The utility found.
     */
    private int[] maxValue(byte[][][] state, int alpha, int beta, int depth)
    {
        // If no successors of this state are to be searched.
        if (isTerminalState(state) || depth == maxDepth)
        {
            return (new int[] { evaluationFunction(state), -1 });
        }

        depth++;
        int utility = NEG_INFINITY;
        int maxUtility = NEG_INFINITY;
        int maxUtilityIndex = -1;
        ArrayList successors = getSuccessors(state);
        byte[][][] successor;

        // For all successors.
        for (int index = 0; index < successors.size(); index++)
        {
            successor = (byte[][][]) successors.get(index);

            utility = Math.max(utility, minValue(successor, alpha, beta, depth)[0]);

            // If utility of this successor is not below the current minimum.
            if (utility >= beta)
            {
                return (new int[] { utility, index });
            }

            alpha = Math.max(alpha, utility);

            // Update index of the successor with the highest utility.
            if (utility > maxUtility)
            {
                maxUtility = utility;
                maxUtilityIndex = index;
            }
        }

        return (new int[] { utility, maxUtilityIndex });
    }

    /**
     * Returns the minimum utility of a successor of 'state'.
     * If state is a terminal state, returns the utility of 'state'.
     * If the successors of state are below the minimum depth and state is not a terminal state, returns
     * 'NEG_INFINITY'.
     * 
     * @param state The state to check.
     * @param alpha The value of the best choice so far for MAX.
     * @param beta The value of the best choice so far for MIN.
     * 
     * @return The utility found.
     */
    private int[] minValue(byte[][][] state, int alpha, int beta, int depth)
    {
        // If no successors of this state are to be searched.
        if (isTerminalState(state) || depth == maxDepth)
        {
            return (new int[] { evaluationFunction(state), -1 });
        }

        depth++;
        int utility = POS_INFINITY;
        int minUtility = POS_INFINITY;
        int minUtilityIndex = -1;
        ArrayList successors = getSuccessors(state);
        byte[][][] successor;

        // For all successors.
        for (int index = 0; index < successors.size(); index++)
        {
            successor = (byte[][][]) successors.get(index);

            utility = Math.min(utility, maxValue(successor, alpha, beta, depth)[0]);

            // If utility of this successor is not above the current maximum.
            if (utility <= alpha)
            {
                return (new int[] { utility, index });
            }

            beta = Math.min(beta, utility);

            // Update index of the successor with the lowest utility.
            if (utility < minUtility)
            {
                minUtility = utility;
                minUtilityIndex = index;
            }
        }

        return (new int[] { utility, minUtilityIndex });
    }

    /**
     * This player makes a move that it thinks is best.
     */
    public int[] move(byte[][][] state, int playerToMove, int maxDepth)
    {
        this.playerToMove = Byte.parseByte(String.valueOf(playerToMove));
        this.maxDepth = maxDepth;

        return (alphaBetaSearch(state));
    }
}
