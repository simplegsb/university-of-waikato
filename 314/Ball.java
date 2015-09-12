////////////////////////////////////////////////////////////////////
/////////////////////////////// BALL ///////////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * A Ball that can be moved within a FieldPanel using multiple MoveVectors.
 *
 * @author Sports Simulator Team: ???
 */
public class Ball extends MovingObject
{
    /**
     * Creates an instance of a Ball.
     *
     * @param x The position of the Ball on the x axis.
     * @param y The position of the Ball on the y axis.
     * @param theatre The Theatre that will control the movement of this Ball.
     */
    public Ball(double x, double y, Theatre theatre)
    {
	super(x, y, theatre);
    }
    

    /**
     * Creates an instance of a Ball.
     *
     * @param point The position of the Ball.
     * @param theatre The Theatre that will control the movement of this Ball.
     */
    public Ball(Point point, Theatre theatre)
    {
	super(point, theatre);
    }


    /**
     * The radius of the Ball.
     */
    public static final int RADIUS = 15;
}
