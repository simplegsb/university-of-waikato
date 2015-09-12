////////////////////////////////////////////////////////////////////
////////////////////////////// PLAYER //////////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * A player that can be moved within a FieldPanel using multiple MoveVectors.
 *
 * @author Sports Simulator Team: ???
 */
public class Player extends MovingObject
{
    /**
     * Creates an instance of Player.
     *
     * @param x The position of the Player on the x axis.
     * @param y The position of the Player on the y axis.
     * @param theatre The Theatre that will control the movement of this Player.
     * @param team The team this Player is in.
     */
    public Player(double x, double y, Theatre theatre, int team)
    {
	super(x, y, theatre);

	myTeam = team;
    }
	

    /**
     * Creates an instance of Player.
     *
     * @param point The position of the Player.
     * @param theatre The Theatre that will control the movement of this Player.
     * @param team The team this Player is in.
     */
    public Player(Point point, Theatre theatre, int team)
    {
	super(point, theatre);

	myTeam = team;
    }
	

    /**
     * The team this Player is in.
     */
    private int myTeam;


    /**
     * Returns the team this Player is in.
     *
     * @return The team this Player is in.
     */
    public int getTeam()
    {
	return(myTeam);
    }
	

    /**
     * Sets the team this Player is in.
     *
     * @param team The team this Player is in.
     */
    public void setTeam(int team)
    {
	myTeam = team;
    }


    /**
     * The number of this Player.
     */
    private int myNumber;


    /**
     * Returns the number of this Player.
     *
     * @return The number of this Player.
     */
    public int getNumber()
    {
	return(myNumber);
    }


    /**
     * Sets the number of this Player.
     *
     * @param team The number of this Player.
     */
    public void setNumber(int number)
    {
	myNumber = number;
    }


    /**
     * Returns true if this Player's number is not 0, false otherwise.
     *
     * @return True if this Player's number is not 0, false otherwise.
     */
    public boolean hasNumber()
    { 
	return(myNumber != 0);
    }


    /**
     * The heaight and width of the Player.
     */
    public static int WIDTH = 20;
    public static int HEIGHT = 20;
}
