/**
 * A two dimensional line, denoted by its two end points and a width (integers).
 * 
 * @author gb21
 */
public class Line
{
    /**
     * The x value of the first point of the line.
     */
    public int x1 = -1;
    
    /**
     * The x value of the second point of the line.
     */
    public int x2 = -1;
    
    /**
     * The y value of the first point of the line.
     */
    public int y1 = -1;
    
    /**
     * The y value of the second point of the line.
     */
    public int y2 = -1;
    
    /**
     * The width of the line.
     */
    public int width = -1;
    
    public Line()
    {}
    
    public Line(int x1, int y1, int x2, int y2, int width)
    {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.width = width;
    }
    
    public String toString()
    {
        return (new String("Line starts at: (" + x1 + ", " + y1 + "), ends at (" + x2 + ", " + y2 + ") and has width " + width + "."));
    }
}
