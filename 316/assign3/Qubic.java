//modified: Sun 1 May: added more diagonals to isWonByPlayer()

public class Qubic
{
    // Qubic MaxPlayerClass MinPlayerClass MaxDepth
    public static void main(String[] args) throws Exception
    {

        QubicInterface max = (QubicInterface) Class.forName(args[0]).newInstance();
        QubicInterface min = (QubicInterface) Class.forName(args[1]).newInstance();
        int maxDepth = Integer.parseInt(args[2]);
        new Qubic().playGame(max, min, maxDepth);
    }

    public void playGame(QubicInterface max, QubicInterface min, int maxDepth)
    {
        byte[][][] board = new byte[4][4][4];
        for (;;)
        {
            if (move(max, (byte) 1, board, maxDepth))
            {
                return;
            }
            if (move(min, (byte) -1, board, maxDepth))
            {
                return;
            }
        }
    }

    public void showBoard(byte[][][] board)
    {
        System.out.println("------------");
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 3 - i; k++)
                    System.out.print("  ");
                for (int k = 0; k < 4; k++)
                {
                    switch (board[i][j][k])
                    {
                        case -1:
                            System.out.print("x");
                            break;
                        case 1:
                            System.out.print("o");
                            break;
                        case 0:
                            System.out.print(".");
                            break;
                    }
                }
                System.out.println("");
            }
        }
    }

    public boolean move(QubicInterface player, byte who, byte[][][] board, int maxDepth)
    {
        int[] move = player.move(board, who, maxDepth);
        board[move[0]][move[1]][move[2]] = who;
        showBoard(board);
        if (isWonByPlayer(board, who))
        {
            System.out.println("won by " + who);
            return true;
        }
        if (isDrawn(board))
        {
            System.out.println("draw");
            return true;
        }
        return false;
    }

    public boolean isDrawn(byte[][][] board)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    if (board[i][j][k] == 0)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isWonByPlayer(byte[][][] board, int player)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (((board[i][j][0] == player) && (board[i][j][1] == player) && (board[i][j][2] == player) && (board[i][j][3] == player))
                        || ((board[i][0][j] == player) && (board[i][1][j] == player) && (board[i][2][j] == player) && (board[i][3][j] == player))
                        || ((board[0][i][j] == player) && (board[1][i][j] == player) && (board[2][i][j] == player) && (board[3][i][j] == player)))
                {
                    return true;
                }
            }
        }
        for (int i = 0; i < 4; i++)
        {
            if (((board[i][i][0] == player) && (board[i][i][1] == player) && (board[i][i][2] == player) && (board[i][i][3] == player))
                    || ((board[i][0][i] == player) && (board[i][1][i] == player) && (board[i][2][i] == player) && (board[i][3][i] == player))
                    || ((board[0][i][i] == player) && (board[1][i][i] == player) && (board[2][i][i] == player) && (board[3][i][i] == player)))
            {
                return true;
            }
        }
        for (int i = 0; i < 4; i++)
        {
            if (((board[i][3][0] == player) && (board[i][2][1] == player) && (board[i][1][2] == player) && (board[i][0][3] == player))
                    || ((board[3][0][i] == player) && (board[2][1][i] == player) && (board[1][2][i] == player) && (board[0][3][i] == player))
                    || ((board[0][i][3] == player) && (board[1][i][2] == player) && (board[2][i][1] == player) && (board[3][i][0] == player)))
            {
                return true;
            }
        }
        if (((board[0][0][0] == player) && (board[1][1][1] == player) && (board[2][2][2] == player) && (board[3][3][3] == player))
                || ((board[3][0][0] == player) && (board[2][1][1] == player) && (board[1][2][2] == player) && (board[0][3][3] == player))
                || ((board[0][3][0] == player) && (board[1][2][1] == player) && (board[2][1][2] == player) && (board[3][0][3] == player))
                || ((board[3][3][0] == player) && (board[2][2][1] == player) && (board[1][1][2] == player) && (board[0][0][3] == player)))
        {
            return true;
        }

        // I seem to have missed some diagonals, here is a fix:
        // code added by Jimmy
        for (int i = 0; i < 4; i++)
        {
            if ((board[i][0][0] == player) && (board[i][1][1] == player) && (board[i][2][2] == player)
                    && (board[i][3][3] == player))
            {
                return true;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            if ((board[0][0][i] == player) && (board[1][1][i] == player) && (board[2][2][i] == player)
                    && (board[3][3][i] == player))
            {
                return true;
            }
        }
        // end code added by jimmy
        // plus one more:
        for (int i = 0; i < 4; i++)
        {
            if ((board[0][i][0] == player) && (board[1][i][1] == player) && (board[2][i][2] == player)
                    && (board[3][i][3] == player))
            {
                return true;
            }
        }
        return false;
    }
}
