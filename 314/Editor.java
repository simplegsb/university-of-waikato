////////////////////////////////////////////////////////////////////
////////////////////////////// EDITOR //////////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Holds all necessary fields and methods for xecuting these operations.
 *
 * @author Sports Simulator Team: ???
 */
public class Editor
{
    /**
     * Creates an instance of an Editor.
     *
     * @param play The Play this Editor is associated with.
     */
    public Editor(Play play)
    {
	thisPlay = play;

	operation = NO_OP;
	dragging = false;
    }


    /**
     * The Play this Editor is associated with.
     */
    private Play thisPlay;


    /**
     * The editing operation the user is currently attempting to perform on the Play.
     */
    public int operation;
    public static final int NO_OP = 0, REMOVE = 1;


    /**
     * The offset on the x and y axis' respectively from the top left corner of the FieldPanel.
     */   
    public int offsetX , offsetY;


    /**
     * True if the mouse is currently dragging a MovingObject.
     */   
    public boolean dragging;


    /**
     * 
     */   
    public int positionNum;


    /**
     * Moves the MovingObjects in thisPlay by the distance on the axis supplied.
     *
     * @param dx The distance on the x axis to move each MovingObject.
     * @param dy The distance on the y axis to move each MovingObject.
     */
    public void moveAll(int dx, int dy)
    {
    	if(thisPlay.ballExists())
	    {
		thisPlay.getBall().shift(dx, dy);
	    }
	if(thisPlay.playersExist())
	    {
		// For every player.
		for(int i = thisPlay.getPlayers().size() - 1; i >= 0; i--)
		    {
			Player tempPlayer = (Player) thisPlay.getPlayers().get(i);
			tempPlayer.shift(dx, dy);
		    }
	    }

	thisPlay.getSimulator().getFieldPanel().repaint();
    }


    /**
     * Rotates the MovingObjects in thisPlay by the angle supplied.
     *
     * @param angle The angle to rotate each MovingObject.
     */
    public void rotateAll(double angle)
    {
    	if(thisPlay.ballExists())
	    {
		thisPlay.getBall().rotate(angle);
	    }
	if(thisPlay.playersExist())
	    {
		// For every player.
		for(int i = thisPlay.getPlayers().size() - 1; i >= 0; i--)
		    {
			Player tempPlayer = (Player) thisPlay.getPlayers().get(i);
			tempPlayer.rotate(angle);
		    }
	    }
	
	thisPlay.getSimulator().getFieldPanel().repaint();
    }
}
