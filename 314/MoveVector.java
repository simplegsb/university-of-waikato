import java.io.Serializable;


////////////////////////////////////////////////////////////////////
//////////////////////////// MOVE VECTOR ///////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Represents a path for a MovingObject to move along.
 *
 * @author Sports Simulator Team: ???
 */

public class MoveVector implements Serializable
{
    /**
     * Creates an instance of a MoveVector.
     *
     * @param x The position of the start point of the MoveVector on the x axis.
     * @param y The position of the start point of the MoveVector on the y axis.
     * @param theatre The Theatre that will control the movement of this MoveVector.
     */
    public MoveVector(double x, double y, Theatre theatre)
    {
	startPoint = new Point(x, y);

	myTheatre = theatre;
    }


    /**
     * Creates an instance of a MoveVector.
     *
     * @param point The position of the start point of the MoveVector.
     * @param theatre The Theatre that will control the movement of this MoveVector.
     */
    public MoveVector(Point point, Theatre theatre)
    {
	startPoint = new Point(point);

	myTheatre = theatre;
    }


    /**
     * The Theatre that will control the movement along this MoveVector.
     */
    private Theatre myTheatre;


    /**
     * Start, end, current and motion points of the MoveVector.
     */
    protected Point startPoint, endPoint, curPoint, motion;


    /**
     * The amount of moves to perform when moving from one end of the MoveVector to the other.
     */
    protected static final float MOVE_TIMES = 100;


    /**
     * The current move time.
     */
    protected float curMoveTime;
	
    
    /**
     * Returns the position of the start point of the MoveVector.
     *
     * @return The position of the start point of the MoveVector.
     */
    public Point getStartPoint()
    {
	return(startPoint);	
    }


    /**
     * Sets the position of the start point of the MoveVector.
     *
     * @param x The position of the start point of the MoveVector on the x axis.
     * @param y The position of the start point of the MoveVector on the y axis.
     */
    public void setStartPoint(double x, double y)
    {
	startPoint = new Point(x, y);
    }
    

    /**
     * Sets the position of the start point of the MoveVector.
     *
     * @param point The position of the start point of the MoveVector.
     */
    public void setStartPoint(Point point)
    {
	startPoint = new Point(point);
    }


    /**
     * Returns the position of the end point of the MoveVector.
     *
     * @return The position of the end point of the MoveVector.
     */
    public Point getEndPoint()
    {
	return(endPoint);
    }


    /**
     * Sets the position of the end point of the MoveVector.
     *
     * @param x The position of the end point of the MoveVector on the x axis.
     * @param y The position of the end point of the MoveVector on the y axis.
     */
    public void setEndPoint(double x, double y)
    {
	endPoint = new Point(x, y);
    }
	

    /**
     * Sets the position of the end point of the MoveVector.
     *
     * @param point The position of the end point of the MoveVector.
     */
    public void setEndPoint(Point point)
    {
	endPoint = new Point(point);
    }


    /**
     * Returns true if the MoveVector has an end point, false otherwise.
     *
     * @return True if the MoveVector has an end point, false otherwise.
     */
    public boolean hasEndPoint()
    { 
	return(endPoint != null);
    }
    
    
    /**
     * Removes the end point of the MoveVector.
     */
    public void removeEndPoint()
    {
	endPoint = null;
	motion = null;
    }
	

    /**
     * Returns the current point.
     *
     * @return The current point.
     */
    public Point getCurPoint()
    {
	return(curPoint);
    }


    /**
     * Sets current move time.
     *
     * @param time Time of the movement.
     */
    public void setCurMoveTime(int time)
    {
	curMoveTime = time;
    }
    

    /**
     * Computes and creates a new motion point.
     */
    public void setMotion()
    {
	if(endPoint != null)
	    {
		motion = new Point((endPoint.getX()-startPoint.getX())/(double) MOVE_TIMES, 
				   (endPoint.getY()-startPoint.getY())/(double) MOVE_TIMES);
	    }
    }

    /**
     * Initializes the movement on this MoveVector.
     */
    public void initMovement()
    {
	if(myTheatre != null)
	    {
		if(myTheatre.direction == Theatre.FORWARD || endPoint == null)
		    {
			curPoint = new Point(startPoint);
			curMoveTime = 0;
		    }
		else
		    {
			curPoint = new Point(endPoint);
			curMoveTime = MOVE_TIMES;
		    }
	    }
	else
	    {
		curPoint = new Point(startPoint);
		curMoveTime = 0;
	    }

	setMotion();
    }

    
    /**
     * Move forward, one move for each call.
     */
    public boolean forward()
    {
	if(endPoint == null)
	    {
		return(false);
	    }
	else if(curMoveTime == MOVE_TIMES)
	    {
		return(false);
	    }

	curPoint = curPoint.add(motion);
	curMoveTime++;
	return(true);
    }
    

    /**
     * Move backward, one move for each call.
     */
    public boolean backward()
    {
	if(curMoveTime == 0)
	    {
		return(false);
	    }

	curPoint = curPoint.sub(motion);
	curMoveTime--;		
	return(true);
    }
    

    /**
     * Returns true if this MoveVector has an end point.
     *
     * @return True if this MoveVector has an end point.
     */
    public boolean hasDestination()
    { 
	return(endPoint != null);
    }  
}
