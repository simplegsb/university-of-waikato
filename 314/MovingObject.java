import java.io.Serializable;
import java.util.ArrayList;


////////////////////////////////////////////////////////////////////
/////////////////////////// MOVING OBJECT //////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * An object that can be moved within a FieldPanel using multiple MoveVectors.
 *
 * @author Sports Simulator Team: ???
 */
public class MovingObject implements Serializable
{
    /**
     * Creates an instance of a MovingObject.
     *
     * @param x The position of the MovingObject on the x axis.
     * @param y The position of the MovingObject on the y axis.
     * @param theatre The Theatre that will control the movement of this MovingObject.
     */
    public MovingObject(double x, double y, Theatre theatre)
    {
	myTheatre = theatre;

	vectorList = new ArrayList();
	MoveVector newVector = new MoveVector(x, y, myTheatre);

	vectorList.add(newVector);
    }


    /**
     * Creates an instance of a MovingObject.
     *
     * @param point The position of the MovingObject.
     * @param theatre The Theatre that will control the movement of this MovingObject.
     */
    public MovingObject(Point point, Theatre theatre)
    {
	myTheatre = theatre;

	vectorList = new ArrayList();
	MoveVector newVector = new MoveVector(point, myTheatre);

	vectorList.add(newVector);
    }


    /**
     * The Theatre that will control the movement of this MovingObject.
     */
    private Theatre myTheatre;


    /**
     * Contains all of the MoveVectors (movements) for this MovingObject.
     */
    protected ArrayList vectorList;


    /**
     * Returns the number of MoveVectors this MovingObject has.
     *
     * @return The number of MoveVectors this MovingObject has.
     */
    public int numOfVectors()
    {
	return(vectorList.size());
    }
	

    /**
     * The MoveVector the MovingObject is currently moving on.
     */
    protected MoveVector currentVector;


    /**
     * The index of the MoveVector the MovingObject is currently moving on.
     */
    protected int currentVectorIndex = 0;


    /**
     * The point the MovingObject is currently at.
     */
    protected Point currentPoint;


    /**
     * Returns the point the MovingObject is currently at.
     *
     * @return The point the MovingObject is currently at.
     */
    public Point getCurrent()
    {
	return(currentPoint);
    }


    /**
     * Returns the MoveVector at the supplied index.
     *
     * @param index The index of the MoveVector to return.
     *
     * @return The MoveVector at the supplied index.
     */
    public MoveVector getVector(int index)
    {
	return((MoveVector) vectorList.get(index));
    }
	

    /**
     * Removes the MoveVector at the supplied index.
     *
     * @param index The index of the MoveVector to remove.
     */
    public void removeVector(int index)
    {
	if(index == 0)
	    {
		MoveVector nextVector = (MoveVector) vectorList.get(index+1);
		currentPoint = new Point(nextVector.getStartPoint());
	    }
	else if(index == vectorList.size()-1)
	    {
		MoveVector preVector = (MoveVector) vectorList.get(index-1);
		preVector.removeEndPoint();
		if(vectorList.size() == 2)
		    {
			currentPoint = new Point(preVector.getStartPoint());
		    }
	    }
	else
	    {
		MoveVector preVector = (MoveVector) vectorList.get(index-1);
		MoveVector nextVector = (MoveVector) vectorList.get(index+1);
		preVector.setEndPoint(nextVector.getStartPoint());
	    }

	vectorList.remove(index);
    }


    /**
     * Adds a MoveVector to the end of the vectorList of this MovingObject.
     *
     * @param x The position of the end point of the new MoveVector on the x axis.
     * @param y The position of the end point of the new MoveVector on the y axis.
     */
    public void setDestination(double x, double y)
    {
	// Get the last MoveVector for this MovingObject.
	MoveVector lastVector = (MoveVector) vectorList.get(vectorList.size()-1);
	lastVector.setEndPoint(x, y);

	// Create a new MoveVector whose start point is the end point of the last MoveVector for this MovingObject.
	MoveVector newVector = new MoveVector(lastVector.getEndPoint(), myTheatre);
	
	vectorList.add(newVector);
    }
	

    /**
     * Adds a MoveVector to the end of the vectorList of this MovingObject.
     *
     * @param point The position of the end point of the new MoveVector.
     */
    public void setDestination(Point point)
    {
	// Get the last MoveVector for this MovingObject.
	MoveVector lastVector = (MoveVector) vectorList.get(vectorList.size()-1);
	lastVector.setEndPoint(point);

	// Create a new MoveVector whose start point is the end point of the last MoveVector for this MovingObject.
	MoveVector newVector = new MoveVector(lastVector.getEndPoint(), myTheatre);
	
	vectorList.add(newVector);
    }

	
    /**
     * Initializes the movement of this MovingObject.
     */
    public void initMovements()
    {
	if(myTheatre != null)
	    {
		if(myTheatre.direction == Theatre.FORWARD)
		    {
			currentVectorIndex = 0;
		    }
		else
		    {
			currentVectorIndex = vectorList.size()-1;
		    }
	    }
	else
	    {
		currentVectorIndex = 0;
	    }

	for(int i = 0; i < vectorList.size(); i++ )
	    {
		MoveVector curVector = (MoveVector) vectorList.get(i);
		currentPoint = new Point(curVector.getStartPoint());
		curVector.initMovement();
	    }
    }
	

    /**
     * Moves this MovingObject in the forward direction.
     */
    public void moveForward()
    {
	if(currentVectorIndex == vectorList.size())
	    {
		return;
	    }
	else if(currentVectorIndex < 0) 
	    {
		currentVectorIndex++;
	    }

	// Get current vector.
	currentVector = (MoveVector) vectorList.get(currentVectorIndex);
	if(!currentVector.forward())
	    {
		currentVectorIndex++; // move to the next vector in the list
	    }
	else
	    {
		currentPoint = currentVector.getCurPoint();
	    }
    }
	

    /**
     * Moves this MovingObject in the backward direction.
     */
    public void moveBackward()
    {
	if(currentVectorIndex == vectorList.size())
	    {
		currentVectorIndex--;
	    }
	else if(currentVectorIndex < 0) 
	    {
		return;
	    }

	// Get current vector.
	currentVector = (MoveVector) vectorList.get(currentVectorIndex);
	if(!currentVector.backward())
	    {
		currentVectorIndex--; // move to the next vector in the list
	    }
	else
	    {
		currentPoint = currentVector.getCurPoint();
	    }
    }


    /**
     * Translates all MoveVectors in this MovingObject by the x and y distances supplied.
     *
     * @param dx The distance to translate all MoveVectors in this MovingObject by on the x axis.
     * @param dy The distance to translate all MoveVectors in this MovingObject by on the y axis.
     */
    public void shift(int dx, int dy)
    {
	for(int i = 0; i < vectorList.size(); i++)
	    {
		MoveVector tempVector = (MoveVector) vectorList.get(i);

		// Get the start point of the vector.
		tempVector.getStartPoint().transform(dx, dy);
		if(tempVector.hasEndPoint())
		    {
			tempVector.getEndPoint().transform(dx, dy);
		    }
	    }
    }
	

    /**
     * Rotates all MoveVectors in this MovingObject about the start point of the first MoveVector by the angle supplied.
     *
     * @param angle The angle to rotate all MoveVectors in this MovingObject about the start point of the first MoveVector
     * by.
     */
    public void rotate(double angle)
    {
	MoveVector firstVector = (MoveVector) vectorList.get(0);
	Point firstPoint = firstVector.getStartPoint();

	for(int i = 0; i < vectorList.size(); i++)
	    {
		MoveVector tempVector = (MoveVector) vectorList.get(i);

		if(tempVector.hasEndPoint())
		    {
			tempVector.getEndPoint().copy(rotateLine(firstPoint, tempVector.getEndPoint(), angle));
			MoveVector nextVector = (MoveVector) vectorList.get(i+1);
			nextVector.getStartPoint().copy(tempVector.getEndPoint());
		    }
	    }
    }
	

    /**
     * Rotates the end point supplied about the origin point supplied by the angle supplied.
     *
     * @param originPoint The origin point to rotate the end point about.
     * @param endPoint The end point to rotate.
     * @param angle The angle to rotate the end point supplied about the origin point supplied by the angle supplied.
     */
    private Point rotateLine(Point originPoint, Point endPoint, double angle)
    {
	double x1 = (originPoint.getX()+(endPoint.getX()-originPoint.getX())*Math.cos(angle)
		     -(endPoint.getY()-originPoint.getY())*Math.sin(angle));
	double y1 = (originPoint.getY()+(endPoint.getX()-originPoint.getX())*Math.sin(angle)
		     +(endPoint.getY()-originPoint.getY())*Math.cos(angle));

	return(new Point(x1,y1));
    }


    /**
     * Returns true if the MovingObject has moved through all of its MoveVectors, false otherwise.
     *
     * @return True if the MovingObject has moved through all of its MoveVectors, false otherwise.
     */
    public boolean reachTheEnd()
    {
	if(myTheatre != null)
	    {
		if(myTheatre.direction == Theatre.FORWARD)
		    {
			if(currentVectorIndex == vectorList.size())
			    {
				return(true);
			    }
		    }
		else
		    {
			if(currentVectorIndex == -1)
			    {
				return(true);
			    }
		    }
	    }
	else
	    {
		if(currentVectorIndex == vectorList.size())
		    {
			return(true);
		    }
	    }

	return(false);
    }
}
