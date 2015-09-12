import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;


////////////////////////////////////////////////////////////////////
/////////////////////////// FIELD DRAWER ///////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Contains methods for drawing different fields.
 *
 * @author Sports Simulator Team: ???
 */
public class FieldDrawer
{
    /**
     * Creates an instance of a FieldDrawer. Sets the default field type to 'FULL'.
     *
     * @param panel The FieldPanel this FieldDrawer is associated with.
     */
    public FieldDrawer(FieldPanel panel)
    {
	myPanel = panel;

	fieldType = FULL;
    }


    /**
     * The FieldPanel this FieldDrawer is associated with.
     */
    private FieldPanel myPanel;


    /**
     * The type of field to draw using the drawCurrentField method.
     */
    public int fieldType;
    public final static int NONE = 0, FULL = 1, HALF = 2, LEFT_HALF = 3, RIGHT_HALF = 4;
	

    /**
     * Draw the field specified by fieldType.
     *
     * @param g The graphics to paint to.
     */	
    public void drawCurrentField(Graphics g)
    {
	if(fieldType == FULL)
	    {
		drawFullSoccerField(g);
	    }
	else if(fieldType == HALF)
	    {
		drawHalfSoccerField(g);
	    }
	else if(fieldType == LEFT_HALF)
	    {
		drawLeftHalfSoccerField(g);
	    }
	else if(fieldType == RIGHT_HALF)
	    {
		drawRightHalfSoccerField(g);
	    }
    }


    /**
     * Draw a full soccer field.
     *
     * @param g The graphics to paint to.
     */	
    public void drawFullSoccerField(Graphics g)
    {
	Graphics2D g2d = (Graphics2D) g;
	Stroke previousStroke = g2d.getStroke();
	g2d.setPaint(Color.white);
	g2d.setStroke(new BasicStroke(5.0f));

	// Draw edge.
	g2d.draw(new Rectangle2D.Double(10, 10, myPanel.getWidth()-20, myPanel.getHeight()-20));

	// Draw middle circle.
	g2d.drawOval(myPanel.getWidth()/2-myPanel.getHeight()/8, myPanel.getHeight()/2-myPanel.getHeight()/8,
		     myPanel.getHeight()/4, myPanel.getHeight()/4);

	// Draw middle line.
	g2d.draw(new Line2D.Double(myPanel.getWidth()/2, 10, myPanel.getWidth()/2, myPanel.getHeight()-10));

	GeneralPath path = new GeneralPath();

	// Draw left small rectangle.
	path.moveTo(10, myPanel.getHeight()*3/8);
	path.lineTo(myPanel.getWidth()/15, myPanel.getHeight()*3/8);
	path.lineTo(myPanel.getWidth()/15, myPanel.getHeight()*5/8);
	path.lineTo(10, myPanel.getHeight()*5/8);
	g2d.draw(path);

	// Draw left big rectangle.
	path.moveTo(10, myPanel.getHeight()/4);
	path.lineTo(myPanel.getWidth()/8, myPanel.getHeight()/4);
	path.lineTo(myPanel.getWidth()/8, myPanel.getHeight()*3/4);
	path.lineTo(10, myPanel.getHeight()*3/4);
	g2d.draw(path);

	// Draw right small rectangle.
	path.moveTo(myPanel.getWidth()-10, myPanel.getHeight()*3/8);
	path.lineTo(myPanel.getWidth()*14/15, myPanel.getHeight()*3/8);
	path.lineTo(myPanel.getWidth()*14/15, myPanel.getHeight()*5/8);
	path.lineTo(myPanel.getWidth()-10, myPanel.getHeight()*5/8);
	g2d.draw(path);

	// Draw right big rectangle.
	path.moveTo(myPanel.getWidth()-10, myPanel.getHeight()/4);
	path.lineTo(myPanel.getWidth()*7/8, myPanel.getHeight()/4);
	path.lineTo(myPanel.getWidth()*7/8, myPanel.getHeight()*3/4);
	path.lineTo(myPanel.getWidth()-10, myPanel.getHeight()*3/4);
	g2d.draw(path);

	// Draw left arc.
	g2d.drawArc(myPanel.getWidth()/8 - myPanel.getHeight()/10, myPanel.getHeight()/2 - myPanel.getHeight()/10,
		    myPanel.getHeight()/5, myPanel.getHeight()/5, 90, -180);

	// Draw right arc.
	g2d.drawArc(myPanel.getWidth()*7/8-myPanel.getHeight()/10, myPanel.getHeight()/2-myPanel.getHeight()/10,
		    myPanel.getHeight()/5, myPanel.getHeight()/5, 90, 180);

	// Restore previous stroke.
	g2d.setStroke(previousStroke);
    }


    /**
     * Draw half of a soccer field.
     *
     * @param g The graphics to paint to.
     */	
    public void drawHalfSoccerField(Graphics g)
    {
	Graphics2D g2d = (Graphics2D) g;
	Stroke previousStroke = g2d.getStroke();
	g2d.setPaint(Color.white);
	g2d.setStroke(new BasicStroke(5.0f));

	// Draw edge.
	g2d.draw(new Rectangle2D.Double(10, 10, myPanel.getWidth()-20,  myPanel.getHeight()-20));

	// Draw middle circle.
	g2d.drawArc(myPanel.getWidth()/2-myPanel.getHeight()/6-10, myPanel.getHeight()*5/6-10, myPanel.getHeight()/3,
		    myPanel.getHeight()/3, 0, 180);

	// Draw small rectangle.
	g2d.draw(new Rectangle2D.Double(220, 10, 705/3, 468/10));

	// Draw big rectangle.
	g2d.draw(new Rectangle2D.Double(145, 10, 390, 468/4));

	// Draw half circle.
	g2d.drawArc(285,75, 468/4, 468/4, 0, -180 );

	// Restore previous stroke.
	g2d.setStroke(previousStroke);
    }


    /**
     * Draw the left side of half of a soccer field.
     *
     * @param g The graphics to paint to.
     */	
    public void drawLeftHalfSoccerField(Graphics g)
    {
	Graphics2D g2d = (Graphics2D) g;
	Stroke previousStroke = g2d.getStroke();
	g2d.setPaint(Color.white);
	g2d.setStroke(new BasicStroke(5.0f));

	// Draw edge.
	g2d.draw( new Rectangle2D.Double(10, 10, myPanel.getWidth()-20,  myPanel.getHeight()-20));	

	// Draw small rectangle.
	g2d.draw( new Rectangle2D.Double(70, 10, 705/2-40, 468/3-40));

	// Draw big rectangle.
	g2d.drawLine(10, 468*2/3-30, 550, 468*2/3-30);
	g2d.drawLine(550, 468*2/3-30, 550, 10);

	// Draw half circle.
	g2d.drawArc(150, 205, 468/3, 468/3, 0, -180);
	
	// Restore previous stroke.
	g2d.setStroke(previousStroke);
    }


    /**
     * Draw the right side of half of a soccer field.
     *
     * @param g The graphics to paint to.
     */	
    public void drawRightHalfSoccerField(Graphics g)
    {
	Graphics2D g2d = (Graphics2D) g;
	Stroke previousStroke = g2d.getStroke();
	g2d.setPaint(Color.white);
	g2d.setStroke(new BasicStroke(5.0f));

	// Draw edge.
	g2d.draw( new Rectangle2D.Double(10, 10, myPanel.getWidth() - 20,  myPanel.getHeight() - 20 ) );

	// Draw small rectangle.
	g2d.draw( new Rectangle2D.Double(300, 10, 705/2-40,  468/3-40));
	
	// Draw big rectangle.
	g2d.drawLine(150,468*2/3-30,695,468*2/3-30);
	g2d.drawLine(150,468*2/3-30,150,10);
	
	// Draw half circle.
	g2d.drawArc( 380,205, 468/3, 468/3, 0, -180 );
	
	// Restore previous stroke.
	g2d.setStroke(previousStroke);
    }
}
