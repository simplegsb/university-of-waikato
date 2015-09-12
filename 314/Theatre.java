import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;


////////////////////////////////////////////////////////////////////
/////////////////////// THEATRE //////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Holds all necessary fields and methods for executing these operations.
 *
 * @author Sports Simulator Team: ???
 */
public class Theatre implements ActionListener
{
    /**
     * Creates an instance of a TheatreControlPanel.
     *
     * @param play
     */
    public Theatre(Play play)
    {
	thisPlay = play;

	direction = FORWARD;
	playing = false;
	showField = true;
	showPath = false;
	showNumber = true;
	loop = false;
    }


    /**
     * The Play this TheatreControlPanel is associated with.
     */
    private Play thisPlay;


    /**
     * The timer that controls the animation.
     */
    private Timer animationTimer;


    /**
     * The delay of the animation (in milliseconds.)
     */
    private int animationDelay = 50;


    /**
     * The direction of the animation.
     */
    public int direction;
    public static final int FORWARD = 0, BACKWARD = 1;


    /**
     * True if the animation is paused.
     */
    public boolean playing;


    /**
     * The field will be shown during animation if this is true.
     */
    public boolean showField;
	

    /**
     * The paths of the ball and players will be shown during animation if this is true.
     */
    public boolean showPath;
	

    /**
     * The numbers of the players will be shown during animation if this is true.
     */
    public boolean showNumber;
	

    /**
     * Animation will loop if this is true.
     */
    public boolean loop;


    /**
     * Repaints the FieldPanel when an action is performed.
     */
    public void actionPerformed(ActionEvent e)
    {
	thisPlay.getSimulator().getFieldPanel().repaint();
    }


   /** 
     * Control the speed of the animation.
     *
     * @param fps A frames-per-second figure that will be used to adjust the delay in the timer.
     */
    public void setFPS(int fps)
    {
	animationDelay = 1000/fps;
	if(animationTimer == null) 
	    {
		return;
	    }
	animationTimer.setDelay(animationDelay);
    }


    /**
     * Initialize the animation, calculate motions of all the MovingObjects.
     */	
    public void initializeAnimation()
    {
	if(thisPlay.playersExist())
	    {
		for(int i = 0; i < thisPlay.getPlayers().size(); i ++)
		    {
			Player thisPlayer = (Player) thisPlay.getPlayers().get(i);
			thisPlayer.initMovements();
		    }
	    }
	if(thisPlay.ballExists())
	    {
		thisPlay.getBall().initMovements();
	    }
    }


    /**
     * Starts the animation.
     */
    public void startAnimation()
    {
	// Start form the beginning of the animation.
	if(animationTimer == null)
	    {
		initializeAnimation();
		animationTimer = new Timer(animationDelay, this);
		animationTimer.start();
	    }
	// Continue from last animation section.
	else
	    {
		animationTimer.restart();
	    }

	playing = true;
	thisPlay.myAudio.play();
    }
    
    
    /**
     * Pauses the animation.
     */
    public void pauseAnimation()
    {
	if(animationTimer == null)
	    {
		return;
	    }

	animationTimer.stop();

	playing = false;
    }
    
    
    /**
     * Stops the animation.
     */
    public void stopAnimation()
    {
	if(animationTimer == null)
	    {
		return;
	    }

	animationTimer.stop();
	animationTimer = null;

	thisPlay.myAudio.stop();
	thisPlay.getSimulator().getFieldPanel().repaint();
    }
    
    
    /**
     * Checks if all of the MovingObjects in the animation have reached the end of their movements.
     */
    public boolean endAnimation()
    {
    	if(thisPlay.ballExists() && !thisPlay.getBall().reachTheEnd())
	    {
		return(false);
	    }
	
    	if(thisPlay.playersExist())
	    {
    		for(int i = 0; i < thisPlay.getPlayers().size(); i ++)
		    {
    			Player tempPlayer = (Player) thisPlay.getPlayers().get(i);
    			if(!tempPlayer.reachTheEnd())
			    {
				return(false);
			    }
		    }
	    }
    	return(true);
    }
}
