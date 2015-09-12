import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


////////////////////////////////////////////////////////////////////
/////////////////////// EDITOR CONTROL PANEL ///////////////////////
////////////////////////////////////////////////////////////////////


/**
 * The panel that holds the buttons for executing editing operations.
 *
 * @author Sports Simulator Team: ???
 */
public class EditorControlPanel extends JPanel
{
    /**
     * Creates an instance of an EditorControlPanel.
     *
     * @param simulator The SoccerSimulator this EditorControlPanel is associated with.
     */
    public EditorControlPanel(SoccerSimulator simulator)
    {
	thisSimulator = simulator;

	// Initialize color.
	setBackground(Color.WHITE);

	// Create and add the "Add Team1" button, this puts the field into ADD_PLAYER mode for team1.
	JButton addTeam1PlayerButton = new JButton("Add Team1");
	addTeam1PlayerButton.setFocusable(false);
	addTeam1PlayerButton.setToolTipText("Choose to add a player for team 1 on the field.");
	addTeam1PlayerButton.setPreferredSize(new Dimension(110, 25));
	addTeam1PlayerButton.setBackground(Color.WHITE);
	addTeam1PlayerButton.addActionListener( 
					       new ActionListener()
						   {
						       public void actionPerformed(ActionEvent e)
						       {
							   thisSimulator.getPlay().currentTeam = 1;
							   thisSimulator.getPlay().addPlayer();
							   thisSimulator.getPlay().myEditor.operation = Editor.NO_OP;

							   removeButton.setSelected(false);
						       }
						   });
	add(addTeam1PlayerButton);

	// Create and add the "Add Team2" button, this puts the field into ADD_PLAYER mode for team2.
	JButton addTeam2PlayerButton = new JButton("Add Team2");
	addTeam2PlayerButton.setFocusable(false);
	addTeam2PlayerButton.setToolTipText("Choose to add a player for team 2 on the field.");
	addTeam2PlayerButton.setPreferredSize(new Dimension(110, 25));
	addTeam2PlayerButton.setBackground(Color.WHITE);
	addTeam2PlayerButton.addActionListener( 
					       new ActionListener()
						   {
						       public void actionPerformed(ActionEvent e)
						       {
							   thisSimulator.getPlay().currentTeam = 2;
							   thisSimulator.getPlay().addPlayer();
							   thisSimulator.getPlay().myEditor.operation = Editor.NO_OP;

							   removeButton.setSelected(false);
						       }
						   });
	add(addTeam2PlayerButton);

	// Create and add the "Add Ball" button, this puts the field into ADD_BALL mode.
	JButton addBallButton = new JButton("Add Ball");
	addBallButton.setFocusable(false);
	addBallButton.setToolTipText("Choose to add a ball on the field.");
	addBallButton.setPreferredSize(new Dimension(110, 25));
	addBallButton.setBackground(Color.WHITE);
	addBallButton.addActionListener( 
					  new ActionListener()
					      {
						  public void actionPerformed(ActionEvent e)
						  {
						      thisSimulator.getPlay().addBall();
						      thisSimulator.getPlay().myEditor.operation = Editor.NO_OP;

						      removeButton.setSelected(false);
						  }
					      });
	add(addBallButton);

	// Create and add the "Remove" button, this puts the field into REMOVE mode.
	removeButton = new JButton("Remove");
	removeButton.setFocusable(false);
	removeButton.setToolTipText("Choose to remove a ball or player from the field.");
	removeButton.setPreferredSize(new Dimension(110, 25));
	removeButton.setBackground(Color.WHITE);
	removeButton.addActionListener( 
				       new ActionListener()
					   {
					       public void actionPerformed(ActionEvent e)
					       {
						   thisSimulator.getPlay().myEditor.operation = Editor.REMOVE;

						   removeButton.setSelected(true);
					       }
					   });
	add(removeButton);

	// Create and add the "Move/Rotate All" button, opens the move/rotate all window.
	JButton moveRotateAllButton = new JButton("Move/Rotate All");
	moveRotateAllButton.setFocusable(false);
	moveRotateAllButton.setToolTipText("Choose to open the move/rotate all window.");
	moveRotateAllButton.setPreferredSize(new Dimension(110, 25));
	moveRotateAllButton.setBackground(Color.WHITE);
	moveRotateAllButton.addActionListener( 
				       new ActionListener()
					   {
					       public void actionPerformed(ActionEvent e)
					       {
						   MoveRotateAllWindow moveRotateWindow =
						       new MoveRotateAllWindow(thisSimulator);
						   thisSimulator.getPlay().myEditor.operation = Editor.NO_OP;

						   removeButton.setSelected(false);
					       }
					   });
	add(moveRotateAllButton);

	// Create and add the "Add Sound" button, attaches an audio file to the play.
	JButton addSoundButton = new JButton("Add Sound");
	addSoundButton.setFocusable(false);
	addSoundButton.setToolTipText("Choose to attach an audio file to this play.");
	addSoundButton.setPreferredSize(new Dimension(110, 25));
	addSoundButton.setBackground(Color.WHITE);
	addSoundButton.addActionListener( 
					 new ActionListener()
					     {
						 public void actionPerformed(ActionEvent e)
						 {
						     thisSimulator.getPlay().myAudio.loadAudio();
						     thisSimulator.getPlay().myEditor.operation = Editor.NO_OP;
						    
						     removeButton.setSelected(false);
						 }
					  });
	add(addSoundButton);
    }


    /**
     * The SoccerSimulator this EditorControlPanel is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * The buttons for executing editing operations that need to be used in annonymous inner classes.
     */
    public JButton removeButton;
}
