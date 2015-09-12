import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


////////////////////////////////////////////////////////////////////
//////////////////////// MODE CHOOSER PANEL ////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * The panel containing buttons to choose the mode the simulator is in. The default mode is 'EDITOR'.
 *
 * @author Sports Simulator Team: ???
 */
public class ModeChooserPanel extends JPanel
{
    /**
     * Creates an instance of ModeChooserPanel.
     *
     * @param simulator The SoccerSimulator this ModeChooserPanel is associated with.
     * @param controlPanel The panel that PlayBookControlsPanel, EditorControlsPanel and TheatreControlsPanel can be put
     * into.
     */
    public ModeChooserPanel(SoccerSimulator simulator, JPanel controlPanel)
    {
	thisSimulator = simulator;
	myControlPanel = controlPanel;
	controlPanelLayout = (CardLayout) myControlPanel.getLayout();

	setLayout(new GridLayout(3, 1));

	// Initialize color.
	setBackground(Color.WHITE);

	// Create and add the "Play Editor" button, this opens the 'Play Editor Mode'.
	playEditorButton = new JButton("Play Editor");
	playEditorButton.setFocusable(false);
	playEditorButton.setToolTipText("Opens the play editor.");
	playEditorButton.setBackground(Color.WHITE);
	playEditorButton.addActionListener(
					   new ActionListener()
					       {
						   public void actionPerformed(ActionEvent e)
						   {
						       // Ensure animation is not playing and is in the forward direction.
						       thisSimulator.getPlay().myTheatre.stopAnimation();
						       thisSimulator.getTheatrePanel().resetPlayButton();
						       thisSimulator.getPlay().myTheatre.direction = Theatre.FORWARD;

						       // Setup panel and mode.
						       controlPanelLayout.show(myControlPanel, "editor");
						       thisSimulator.mode = SoccerSimulator.EDITOR;
						       thisSimulator.getFieldPanel().repaint();

						       playBookButton.setSelected(false);
						       playEditorButton.setSelected(true);
						       playTheatreButton.setSelected(false);
						   }
					       });
	add(playEditorButton);

	// Create and add the "Play Book" button, this opens the 'Play Book Mode'.
	playBookButton = new JButton("Play Book");
	playBookButton.setFocusable(false);
	playBookButton.setToolTipText("Opens the play book.");
	playBookButton.setBackground(Color.WHITE);
	playBookButton.addActionListener(
					 new ActionListener()
					     {
						 public void actionPerformed(ActionEvent e)
						 {
						     // Ensure animation is not playing, is in the forward direction and
						     // nothing is selected.
						     thisSimulator.getPlay().myTheatre.stopAnimation();
						     thisSimulator.getTheatrePanel().resetPlayButton();
						     thisSimulator.getPlay().myTheatre.direction = Theatre.FORWARD;
						     thisSimulator.getPlay().selected = Play.NO_SEL;

						     // Setup panel and mode.
						     controlPanelLayout.show(myControlPanel, "play book");
						     thisSimulator.mode = SoccerSimulator.PLAYBOOK;
						     thisSimulator.getFieldPanel().repaint();

						     playBookButton.setSelected(true);
						     playEditorButton.setSelected(false);
						     playTheatreButton.setSelected(false);
						 }
					     });
	add(playBookButton);
	
	// Create and add the "Play Theatre" button, this opens the 'Play Theatre Mode'.
	playTheatreButton = new JButton("Play Theatre");
	playTheatreButton.setFocusable(false);
	playTheatreButton.setToolTipText("Opens the play theatre.");
	playTheatreButton.setBackground(Color.WHITE);
	playTheatreButton.addActionListener(
					    new ActionListener()
						{
						    public void actionPerformed(ActionEvent e)
						    {
							thisSimulator.getPlay().selected = Play.NO_SEL;

							// Setup panel and mode.
							controlPanelLayout.show(myControlPanel, "theatre");
							thisSimulator.mode = SoccerSimulator.THEATRE;
							thisSimulator.getFieldPanel().repaint();

							playBookButton.setSelected(false);
							playEditorButton.setSelected(false);
							playTheatreButton.setSelected(true);
						    }
						});
	add(playTheatreButton);

	// Set default mode.
	thisSimulator.mode = SoccerSimulator.EDITOR;

	playEditorButton.setSelected(true);
    }


    /**
     * The SoccerSimulator this ModeChooserPanel is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * The panel that PlayBookControlsPanel, EditorControlsPanel and TheatreControlsPanel can be put into.
     */
    private JPanel myControlPanel;


    /**
     * The layout of the control panel.
     */
    private CardLayout controlPanelLayout;


    /**
     * The buttons for choosing modes that need to be used in annonymous inner classes.
     */
    private JButton playBookButton, playEditorButton, playTheatreButton;
}
