import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;


////////////////////////////////////////////////////////////////////
/////////////////////// FIELD MOUSE LISTENER ///////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Performs the simulator's editing operations depending on the type of mouse event and current editing status.
 *
 * @author Sports Simulator Team: ???
 */
public class FieldMouseListener implements MouseListener
{
    /**
     * Creates an instance of FieldMouseListener.
     *
     * @param simulator The SoccerSimulator this FieldMouseListener is associated with.
     */
    public FieldMouseListener(SoccerSimulator simulator)
    {
	thisSimulator = simulator;
    }


    /**
     * The SoccerSimulator this FieldMouseListener is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * Responds when the user clicks the mouse on the FieldPanel. Depending on the current value of 'operation' and where
     * the mouse is clicked the relevent operation is performed.
     *
     * @param event The event that triggers this operation.
     */
    public void mouseClicked(MouseEvent event)
    {	
	// Only perform operations if simulator is in EDITOR mode.
	if(thisSimulator.mode != SoccerSimulator.EDITOR)
	    {
		return;
	    }

	// The position on the FieldPanel the user clicked.
	int x = event.getX();
	int y = event.getY();
	
	// If the user clicked the left mouse button.
	if(event.getButton() == MouseEvent.BUTTON1)
	    {
		// Perform relevent operation.
		switch(thisSimulator.getPlay().myEditor.operation)
		    {
		    // Select MovingObject if clicked on, otherwise cancel current operation.
		    case 0:
			thisSimulator.getPlay().selectMovingObject(x, y);
			break;

		    // Remove MovingObject action / MovingObject.
		    case 1:
			thisSimulator.getPlay().removeMovingObject(x, y);
			break;
		    }
	    }

	// If the user clicked the right mouse button.
	else if(event.getButton() == MouseEvent.BUTTON3)
	    {
		// If the ball is selected.
		if(thisSimulator.getPlay().selected == Play.BALL)
		    {
			thisSimulator.getPlay().getBall().setDestination(new Point(x, y));
			thisSimulator.getFieldPanel().repaint();
		    }
		// If a player is selected.
		else if(thisSimulator.getPlay().selected >= 0)
		    {
			Player selectedPlayer =
			    (Player) thisSimulator.getPlay().getPlayers().get(thisSimulator.getPlay().selected);
			selectedPlayer.setDestination(new Point(x, y));
			thisSimulator.getFieldPanel().repaint();
		    }
	    }
    }
    
    /**
     * Responds when the user presses the mouse on the FieldPanel. If the user presses on a MovingObject the dragging
     * operation begins and that MovingObject will move with the mouse.
     *
     * @param event The event that triggers this operation.
     */
    public void mousePressed(MouseEvent event)
    {
	if(thisSimulator.getPlay().myEditor.operation != Editor.NO_OP || thisSimulator.mode != SoccerSimulator.EDITOR
	   || thisSimulator.getPlay().myEditor.dragging || event.getButton() == MouseEvent.BUTTON3)
	    {
		return;
	    }

	// The position on the FieldPanel the user pressed.
	int x = event.getX();
	int y = event.getY();

	thisSimulator.getPlay().initMovingObjectDrag(x, y);
    }
    

    /**
     * Responds when the user releases the mouse on the FieldPanel. Ends the dragging operation.
     *
     * @param event The event that triggers this operation.
     */ 
    public void mouseReleased(MouseEvent event)
    {
	thisSimulator.getPlay().myEditor.dragging = false;
    }
    

    /**
     * Responds when the mouse enters the FieldPanel. No operations are associated with this event.
     *
     * @param event The event that triggers this operation.
     */ 
    public void mouseEntered(MouseEvent event) {}


    /**
     * Responds when the mouse exits the FieldPanel. No operations are associated with this event.
     *
     * @param event The event that triggers this operation.
     */ 
    public void mouseExited(MouseEvent event) {}
}
