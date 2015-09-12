import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;


////////////////////////////////////////////////////////////////////
//////////////////////////// FIELD PANEL ///////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * The panel that the field and MovingObjects will be drawn to.
 *
 * @author Sports Simulator Team: ???
 */
public class FieldPanel extends JPanel
{
    /**
     * Creates an instance of FieldPanel.
     *
     * @param simulator The SoccerSimulator this FieldPanel is associated with.
     */
    public FieldPanel(SoccerSimulator simulator)
    {
	thisSimulator = simulator;
	myFieldDrawer = new FieldDrawer(this);

	setPreferredSize(new Dimension(800, 600));

	// Initialize color.
	setBackground(new Color(51, 153, 0));

	// Add mouse listeners.
	myMouseListener = new FieldMouseListener(thisSimulator);
	addMouseListener(myMouseListener);
	myMouseMotionListener = new FieldMouseMotionListener(thisSimulator);
	addMouseMotionListener(myMouseMotionListener);
    }


    /**
     * The SoccerSimulator this FieldPanel is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * The FieldDrawer this FieldPanel is associated with.
     */
    private FieldDrawer myFieldDrawer;


    /**
     * Returns the FieldDrawer this FieldPanel is associated with.
     */
    public FieldDrawer getFieldDrawer()
    {
	return(myFieldDrawer);
    }


    /**
     * The listeners this FieldPanel is associated with.
     */
    private FieldMouseListener myMouseListener;
    private FieldMouseMotionListener myMouseMotionListener;


    /**
     * Paints the field with or without components depending on options in myTheatre.
     *
     * @param g The graphics to paint to.
     */
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	g.setFont(new Font("Courier", Font.BOLD, 14));

	thisSimulator.getPlay().getField().drawAll(g, myFieldDrawer);
    }
}
