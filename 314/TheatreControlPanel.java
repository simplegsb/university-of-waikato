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
/////////////////////// THEATRE CONTROL PANEL //////////////////////
////////////////////////////////////////////////////////////////////


/**
 * The panel that holds the buttons for executing theatre operations. Also holds all necessary fields and methods for
 * executing these operations.
 *
 * @author Sports Simulator Team: ???
 */
public class TheatreControlPanel extends JPanel
{
    /**
     * Creates an instance of a TheatreControlPanel.
     *
     * @param simulator The SoccerSimulator this TheatreControlPanel is associated with.
     */
    public TheatreControlPanel(SoccerSimulator simulator)
    {
	thisSimulator = simulator;

	// Initialize color.
	setBackground(Color.WHITE);

	// Create and add the "Play" button, this puts the theatre into 'Play Mode' or 'Pause Mode' depending on current
	// mode.
	playButton = new JButton("Play");
	playButton.setFocusable(false);
	playButton.setToolTipText(">");
	playButton.setPreferredSize(new Dimension(110, 25));
	playButton.setBackground(Color.WHITE);
	playButton.addActionListener(
				     new ActionListener()
					 {
					     public void actionPerformed(ActionEvent e)
					     {    
						 if (!thisSimulator.getPlay().myTheatre.playing)
						     {
							 thisSimulator.getPlay().myTheatre.startAnimation();
							 playButton.setText("Pause");
							 playButton.setToolTipText("||");
							 stopButton.setEnabled(true);
						     }
						 else
						     {
							 thisSimulator.getPlay().myTheatre.pauseAnimation();
							 playButton.setText("Play");
							 playButton.setToolTipText(">");
						     }
					     }
					 });
	add(playButton);

	// Create and add the "Stop" button, this puts the theatre into 'Stop Mode'.
	stopButton = new JButton("Stop");
	stopButton.setFocusable(false);
	stopButton.setToolTipText("STOP");
	stopButton.setPreferredSize(new Dimension(110, 25));
	stopButton.setBackground(Color.WHITE);
	stopButton.setEnabled(false);
	stopButton.addActionListener(
				     new ActionListener()
					 {
					     public void actionPerformed(ActionEvent e)
					     {
						 thisSimulator.getPlay().myTheatre.stopAnimation();
						 resetPlayButton();
					     }
					 });
	add(stopButton);

	// Create and add the "Forward" button, this sets the theatre's direction of play to forward.
	directionButton = new JButton("Direction");
	directionButton.setFocusable(false);
	directionButton.setToolTipText("currently ->");
	directionButton.setPreferredSize(new Dimension(110, 25));
	directionButton.setBackground(Color.WHITE);
	directionButton.addActionListener(
				       new ActionListener()
					   {
					       public void actionPerformed(ActionEvent e)
					       {
						   if(thisSimulator.getPlay().myTheatre.direction == Theatre.FORWARD)
						       {
							   thisSimulator.getPlay().myTheatre.direction = Theatre.BACKWARD;
							   directionButton.setToolTipText("currently <-");
							   
						       }
						   else
						       {
							   thisSimulator.getPlay().myTheatre.direction = Theatre.FORWARD;
							   directionButton.setToolTipText("currently ->");
						       }
					       }
					   });
	add(directionButton);

	// Create and add the "Speed" slider, this sets the theatre's speed of play.
	speedSlider = new JSlider(SwingConstants.HORIZONTAL, 10, 90, 50);
	speedSlider.setFocusable(false);
	speedSlider.setToolTipText("<- slower -<>- faster ->");
	speedSlider.setPreferredSize(new Dimension(225, 25));
	speedSlider.setBackground(Color.WHITE);
	speedSlider.setMajorTickSpacing(10);
	speedSlider.setPaintTicks(true);
	speedSlider.addChangeListener(
				      new ChangeListener()
					  {
					      public void stateChanged(ChangeEvent e)
					      {
						  thisSimulator.getPlay().myTheatre.setFPS(speedSlider.getValue());
					      }
					  });
	add(speedSlider);
    }


    /**
     * The SoccerSimulator this TheatreControlPanel is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * The buttons and slider for executing theatre operations that need to be used in annonymous inner classes.
     */
    private JButton playButton, stopButton, directionButton;
    private JSlider speedSlider;


    /**
     * Resets the play button so that it is ready to perform the 'play' operation when it is pressed.
     */
    public void resetPlayButton()
    {
	playButton.setText("Play");
	playButton.setToolTipText(">");
	playButton.setEnabled(true);
	stopButton.setEnabled(false);
	thisSimulator.getPlay().myTheatre.playing = false;
    }
}
