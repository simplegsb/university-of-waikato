import java.awt.Color;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


////////////////////////////////////////////////////////////////////
///////////////////// MOVE / ROTATE ALL WINDOW /////////////////////
////////////////////////////////////////////////////////////////////


/**
 * A window containing buttons for moving all MovingObjects in a Play up, down, left, right and rotate.
 *
 * @author Sports Simulator Team: ???
 */
public class MoveRotateAllWindow extends JFrame
{ 
    /**
     * Creates an instance of a MoveRotateAllWindow.
     *
     * @param simulator The SoccerSimulator this MoveRotateAllWindow is associated with.
     */
    public MoveRotateAllWindow(SoccerSimulator simulator)
    {
	super("Move/Rotate");

	thisSimulator = simulator;

	// Setup panels:
	////////////////////////////////////////////////////////////////////

	Container appPanel = getContentPane();
	appPanel.setLayout(new GridLayout(3, 3));
	appPanel.setBackground(new Color(51, 153, 0));

	appPanel.add(new JLabel());

	// Create and add the "Move Up" button, this moves all players and the ball upward.
	JButton moveUpButton = new JButton(new ImageIcon("up.gif"));
	moveUpButton.setFocusable(false);
	moveUpButton.setToolTipText("Move all players and the ball upward.");
	moveUpButton.setBackground(new Color(51, 153, 0));
	moveUpButton.addActionListener( 
				       new ActionListener()
					   {
					       public void actionPerformed(ActionEvent e)
					       {
						   thisSimulator.getPlay().myEditor.moveAll(0, -5);
					       }
					   });
	appPanel.add(moveUpButton);

	appPanel.add(new JLabel());

	// Create and add the "Move Left" button, this moves all players and the ball to the left.
	JButton moveLeftButton = new JButton(new ImageIcon("left.gif"));
	moveLeftButton.setFocusable(false);
	moveLeftButton.setToolTipText("Move all players and the ball to the left.");
	moveLeftButton.setBackground(new Color(51, 153, 0));
	moveLeftButton.addActionListener( 
					 new ActionListener()
					     {
						 public void actionPerformed(ActionEvent e)
						 {
						     thisSimulator.getPlay().myEditor.moveAll(-5, 0);
						 }
					     });
	appPanel.add(moveLeftButton);
	
	// Create and add the "Rotate" button, this rotates all players and the ball.
	JButton rotateButton = new JButton(new ImageIcon("rotate.gif"));
	rotateButton.setFocusable(false);
	rotateButton.setToolTipText("Rotate all players and the ball.");
	rotateButton.setBackground(new Color(51, 153, 0));
	rotateButton.addActionListener( 
				       new ActionListener()
					   {
					       public void actionPerformed(ActionEvent e)
					       {
						   thisSimulator.getPlay().myEditor.rotateAll(Math.PI*0.5);
					       }
					   });
	appPanel.add(rotateButton);
	
	// Create and add the "Move Right" button, this moves all players and the ball to the right.
	JButton moveRightButton = new JButton(new ImageIcon("right.gif"));
	moveRightButton.setFocusable(false);
	moveRightButton.setToolTipText("Move all players and the ball to the right.");
	moveRightButton.setBackground(new Color(51, 153, 0));
	moveRightButton.addActionListener( 
					  new ActionListener()
					      {
						  public void actionPerformed(ActionEvent e)
						  {
						      thisSimulator.getPlay().myEditor.moveAll(5, 0);
						  }
					      });
	appPanel.add(moveRightButton);

	appPanel.add(new JLabel());

	// Create and add the "Move Down" button, this moves all players and the ball downward.
	JButton moveDownButton = new JButton(new ImageIcon("down.gif"));
	moveDownButton.setFocusable(false);
	moveDownButton.setToolTipText("Move all players and the ball downward.");
	moveDownButton.setBackground(new Color(51, 153, 0));
	moveDownButton.addActionListener( 
					 new ActionListener()
					     {
						 public void actionPerformed(ActionEvent e)
						 {
						     thisSimulator.getPlay().myEditor.moveAll(0, 5);
						 }
					     });
	appPanel.add(moveDownButton);


	// Setup frame:
	//////////////////////////////////////////////////////////////////

	DisplayMode screenDisp = getGraphicsConfiguration().getDevice().getDisplayMode();
	setLocation((screenDisp.getWidth()/2), (screenDisp.getHeight()/2));
	setSize(200, 200);
	setResizable(false);
	setVisible(true);
    }


    /**
     * The SoccerSimulator this MoveRotateAllWindow is associated with.
     */
    private SoccerSimulator thisSimulator;
}
