import java.io.Serializable;


////////////////////////////////////////////////////////////////////
/////////////////////////////// POINT //////////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * A planar point.
 *
 * @author Sports Simulator Team: ???
 */
public class Point implements Serializable
{
    /**
     * Creates an instance of a Point.
     *
     * @param x The position of the Point on the x axis.
     * @param y The position of the Point on the y axis.
     */
    public Point(double x, double y)
    {
	myX = x;
	myY = y;
    }
	

    /**
     * Creates an instance of a Point.
     *
     * @param point The position of the Point.
     */
    public Point(Point point)
    {
	myX = point.myX;
	myY = point.myY;
    }

	
    /**
     * The position  of this Point on the axis.
     */
    private double myX, myY; 


    /**
     * Returns the position of this Point on the x axis.
     *
     * @return The position of this Point on the x axis.
     */
    public double getX()
    {
	return(myX);
    }
    

    /**
     * Returns the position of this Point on the y axis.
     *
     * @return The position of this Point on the y axis.
     */
    public double getY()
    {
	return(myY);
    }

  	
    /**
     * Returns the position of this Point on the x axis rounded to the nearest integer.
     *
     * @return The position of this Point on the x axis rounded to the nearest integer.
     */
    public int getRoundedX()
    {
	return((int) Math.round(myX));
    }

	
    /**
     * Returns the position of this Point on the y axis rounded to the nearest integer.
     *
     * @return The position of this Point on the y axis rounded to the nearest integer.
     */
    public int getRoundedY()
    {
	return((int) Math.round(myY));
    }
  	

    /**
     * Returns the position of the center of this Point on the x axis.
     *
     * @param width The width of the point.
     *
     * @return The position of the center of this Point on the x axis.
     */
    public int getCenterX(int width)
    {
	return((int) myX+width/2);
    }


    /**
     * Returns the position of the center of this Point on the x axis.
     *
     * @param height The height of the point.
     *
     * @return The position of the center of this Point on the x axis.
     */
    public int getCenterY(int height)
    {
	return((int) myY+height/2);
    }


    /**
     * Returns a Point with position equal to the position of this Point added to the position of the Point supplied.
     * i.e. (x1,y1) + (x2,y2) = (x1+x2,y1+y2) = Point returned.
     *
     * @param point The point to add to this one.
     *
     * @return A Point with position equal to the position of this Point added to the position of the Point supplied.
     */
    public Point add(Point point)
    {
	return(new Point(myX + point.myX, myY + point.myY));
    }
    

    /**
     * Returns a Point with position equal to the position of the Point supplied subtracted from the position of this Point.
     * i.e. (x1,y1) + (x2,y2) = (x1-x2,y1-y2) = Point returned.
     *
     * @param point The point to subtract from this one.
     *
     * @return A Point with position equal to the position of the Point supplied subtracted from the position of this Point.
     */
    public Point sub(Point point)
    {
	return(new Point(myX - point.myX, myY - point.myY));
    }
    

    /**
     * Copies the position of the Point supplied to the position of this point.
     *
     * @param point The Point to copy the position from.
     */
    public void copy(Point point)
    {
	myX = point.myX;
	myY = point.myY;
    }
    

    /**
     * Moves this Point by the x and y values supplied.
     *
     * @param x The distance to move this Point on the x axis.
     * @param y The distance to move this Point on the y axis.
     */
    public void transform(double x, double y)
    {
	myX = myX + x;
	myY = myY + y;
    }
}

