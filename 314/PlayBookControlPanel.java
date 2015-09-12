import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;


////////////////////////////////////////////////////////////////////
////////////////////// PLAY BOOK CONTROL PANEL /////////////////////
////////////////////////////////////////////////////////////////////


/**
 * The panel that holds the buttons for executing play book operations.
 *
 * @author Sports Simulator Team: ???
 */
public class PlayBookControlPanel extends JPanel
{
    /**
     * Creates an instance of a PlayBookControlPanel.
     *
     * @param simulator The SoccerSimulator this PlayBookControlPanel is associated with.
     */
    public PlayBookControlPanel(SoccerSimulator simulator)
    {
	thisSimulator = simulator;

	// Initialize color.
	setBackground(Color.WHITE);

	// Create and add the "Create New" button,
	JButton newPlayButton = new JButton("Create New");
	newPlayButton.setFocusable(false);
	newPlayButton.setToolTipText("Start from scratch.");
	newPlayButton.setPreferredSize(new Dimension(110, 25));
	newPlayButton.setBackground(Color.WHITE);
	newPlayButton.addActionListener( 
					new ActionListener()
					    {
						public void actionPerformed(ActionEvent e)
						{
						    int option = JOptionPane.showConfirmDialog
							(thisSimulator, "Do you want to save this play first?",
							 "Current play not saved", JOptionPane.YES_NO_CANCEL_OPTION);

						    if(option == JOptionPane.YES_OPTION)
							{
							    thisSimulator.savePlay();
							}

						    if(option != JOptionPane.CANCEL_OPTION)
							{
							    Play newPlay = new Play(thisSimulator);
							    thisSimulator.setPlay(newPlay);
							    thisSimulator.getFieldPanel().repaint();
							}
						}
					    });
	add(newPlayButton);

	// Create and add the "Save Play" button,
	JButton savePlayButton = new JButton("Save Play");
	savePlayButton.setFocusable(false);
	savePlayButton.setToolTipText("Save this play for future use.");
	savePlayButton.setPreferredSize(new Dimension(110, 25));
	savePlayButton.setBackground(Color.WHITE);
	savePlayButton.addActionListener( 
					 new ActionListener()
					     {
						 public void actionPerformed(ActionEvent e)
						 {
						     thisSimulator.savePlay();
						 }
					     });
	add(savePlayButton);

	// Create and add the "Load Play" button,
	JButton loadPlayButton = new JButton("Load Play");
	loadPlayButton.setFocusable(false);
	loadPlayButton.setToolTipText("Load a play.");
	loadPlayButton.setPreferredSize(new Dimension(110, 25));
	loadPlayButton.setBackground(Color.WHITE);
	loadPlayButton.addActionListener( 
					 new ActionListener()
					     {
						 public void actionPerformed(ActionEvent e)
						 {
						     int option = JOptionPane.showConfirmDialog
							 (thisSimulator, "Do you want to save this play first?",
							  "Current play not saved", JOptionPane.YES_NO_CANCEL_OPTION);

						     if(option == JOptionPane.YES_OPTION)
							 {
							     thisSimulator.savePlay();
							 }

						     if(option != JOptionPane.CANCEL_OPTION)
							 {
							     Play newPlay = thisSimulator.loadPlay();

							     if(newPlay != null)
								 {
								     newPlay.setSimulator(thisSimulator);
								     thisSimulator.setPlay(newPlay);
								     thisSimulator.getFieldPanel().repaint();
								 }
							 }
						 }
					     });
	add(loadPlayButton);
    }

	
    /**
     * The SoccerSimulator this PlayBookControlPanel is associated with.
     */
    private SoccerSimulator thisSimulator;
}
