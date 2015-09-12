import java.util.Random;

public class RandomQubicPlayer implements QubicInterface
{
    Random r;

    public RandomQubicPlayer(long seed)
    {
        r = new Random(seed);
    }

    public RandomQubicPlayer()
    {
        r = new Random();
    }

    public int[] move(byte[][][] board, int who, int maxDepth)
    {
        int nOpen = 0;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    if (board[i][j][k] == 0)
                    {
                        nOpen++;
                    }
                }
            }
        }
        int which = r.nextInt(nOpen);
        // System.out.println("nopen " + nOpen + " -> " + which);
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    if (board[i][j][k] == 0)
                    {
                        if (which == 0)
                        {
                            return new int[] { i, j, k };
                        }
                        which--;
                    }
                }
            }
        }
        // should never come here
        return new int[] { -1, -1, -1 };
    }

}
