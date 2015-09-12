import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


////////////////////////////////////////////////////////////////////
//////////////////// FIELD MOUSE MOTION LISTENER ///////////////////
////////////////////////////////////////////////////////////////////


/**
 * Performs the simulator's editing operations depending on the type of mouse event and current editing status.
 *
 * @author Sports Simulator Team: ???
 */
public class FieldMouseMotionListener implements MouseMotionListener
{
    /**
     * Creates an instance of FieldMouseMotionListener.
     *
     * @param simulator The SoccerSimulator this FieldMouseMotionListener is associated with.
     */
    public FieldMouseMotionListener(SoccerSimulator simulator)
    {
	thisSimulator = simulator;
    }


    /**
     * The SoccerSimulator this FieldMouseMotionListener is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * Responds when the user drags the mouse on the FieldPanel. Changes the position of any selected MovingObject's turning
     * point.
     *
     * @param event The event that triggers this operation.
     */
    public void mouseDragged(MouseEvent event)
    {
	if(thisSimulator.mode != SoccerSimulator.EDITOR || !thisSimulator.getPlay().myEditor.dragging
	   || event.getButton() == MouseEvent.BUTTON3)
	    {
		return;
	    }
	
	// The position on the FieldPanel the user is dragging.
	int x = event.getX();
	int y = event.getY();

	// Change the position of the selected ball turning point.
	if(thisSimulator.getPlay().selected == Play.BALL)
	    {
		MoveVector dragVector =
		    thisSimulator.getPlay().getBall().getVector(thisSimulator.getPlay().myEditor.positionNum);
		dragVector.setStartPoint(new Point(x, y));
		if(thisSimulator.getPlay().myEditor.positionNum != 0)
		    {
			dragVector = thisSimulator.getPlay().getBall().getVector
			    (thisSimulator.getPlay().myEditor.positionNum-1);
			dragVector.setEndPoint(new Point(x, y));
		    }
	    }
	// Change the position of the selected player turning point.
	else if(thisSimulator.getPlay().selected >= 0)
	    {
		Player dragPlayer =
		    (Player) thisSimulator.getPlay().getPlayers().get(thisSimulator.getPlay().selected);
		MoveVector dragVector = dragPlayer.getVector(thisSimulator.getPlay().myEditor.positionNum);
		dragVector.setStartPoint(new Point(x, y));
		if(thisSimulator.getPlay().myEditor.positionNum != 0)
		    {
			dragVector = dragPlayer.getVector(thisSimulator.getPlay().myEditor.positionNum-1);
			dragVector.setEndPoint(new Point(x, y));
		    }
	    }
	
	thisSimulator.getFieldPanel().repaint();
    }


    /**
     * Responds when the user moves the mouse on the FieldPanel. No operations are associated with this event.
     *
     * @param event The event that triggers this operation.
     */
    public void mouseMoved(MouseEvent event) {}
}
