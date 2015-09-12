import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;


////////////////////////////////////////////////////////////////////
///////////////////////// SOCCER SIMULATOR /////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Creates a SplashScreen which appears for a limited time before the SoccerSimulator appears. The simulator consists of
 * three modes, editor, play book and theatre. Each mode has different operations associated with it.
 *
 * @author Sports Simulator Team: ???
 */
public class SoccerSimulator extends JFrame
{ 
    /**
     * Creates an instance of a SoccerSimulator.
     */
    public SoccerSimulator() 
    {
	super("Soccer Simulator 1.0");

	// Setup simulator:
	////////////////////////////////////////////////////////////////////

	myPlay = new Play(this);
	myEditorPanel = new EditorControlPanel(this);
	myTheatrePanel = new TheatreControlPanel(this);
	myFieldPanel = new FieldPanel(this);


	// Setup menus:
	////////////////////////////////////////////////////////////////////
	
	JMenuBar appMenuBar = new JMenuBar();
	appMenuBar.setBackground(Color.WHITE);
	appMenuBar.setForeground(new Color(51, 153, 0));
	appMenuBar.add(createFileMenu());
	appMenuBar.add(createSettingsMenu());
	appMenuBar.add(createHelpMenu());
	setJMenuBar(appMenuBar);


	// Setup panels:
	////////////////////////////////////////////////////////////////////

	// Setup application panel.
	Container appPanel = getContentPane();
	appPanel.setLayout(new BoxLayout(appPanel, BoxLayout.X_AXIS));

	// Contents of application panel.
	JPanel controlPanel = new JPanel(new CardLayout());
	JPanel modePanel = new ModeChooserPanel(this, controlPanel);
	modePanel.setBackground(Color.WHITE);
	appPanel.add(modePanel);
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	mainPanel.setBackground(Color.WHITE);
	appPanel.add(mainPanel);

	// Contents of main panel.
	mainPanel.add(myFieldPanel);
	mainPanel.add(controlPanel);

	// Contents of control panel.
	controlPanel.add(myEditorPanel, "editor");
	controlPanel.add(new PlayBookControlPanel(this), "play book");
	controlPanel.add(myTheatrePanel, "theatre");

	// Setup and show splash window:
	//////////////////////////////////////////////////////////////////

	SplashScreen sw = new SplashScreen("soccerLogo.jpg", 3000);


	// Setup and show simulator window:
	//////////////////////////////////////////////////////////////////

	DisplayMode screenDisp = getGraphicsConfiguration().getDevice().getDisplayMode();
	setLocation((screenDisp.getWidth()/2)-400, (screenDisp.getHeight()/2)-300);
	setSize(800, 600);
	setResizable(false);
	setVisible(true);
    }


    /**
     * Create an instance of the Soccer Simulator and show it.
     */
    public static void main(String[] args)
    {
	try
	    {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch(Exception e)
	    {
		e.printStackTrace();
	    }
	
	SoccerSimulator application = new SoccerSimulator();
	application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }


    /**
     * The play this simulator is currently using.
     */
    private Play myPlay;


    /**
     * Returns the play this simulator is currently using.
     *
     * @return The play this simulator is currently using.
     */
    public Play getPlay()
    {
	return(myPlay);
    }


    /**
     * Sets the play this simulator is currently using.
     *
     * @param play The play this simulator is currently using.
     */
    public void setPlay(Play newPlay)
    {
	myPlay = newPlay;
    }


    /**
     * The EditorControlPanel in this simulator.
     */
    private EditorControlPanel myEditorPanel;


    /**
     * Returns the EditorControlPanel in this simulator.
     *
     * @return The EditorControlPanel in this simulator.
     */
    public EditorControlPanel getEditorPanel()
    {
	return(myEditorPanel);
    }


    /**
     * Sets the EditorControlPanel in this simulator.
     *
     * @param newEditorPanel The EditorControlPanel in this simulator.
     */
    public void setEditor(EditorControlPanel newEditorPanel)
    {
	myEditorPanel = newEditorPanel;
    }


    /**
     * The  in this simulator.
     */
    private TheatreControlPanel myTheatrePanel;


    /**
     * Returns the TheatreControlPanel in this simulator.
     *
     * @return The TheatreControlPanel in this simulator.
     */
    public TheatreControlPanel getTheatrePanel()
    {
	return(myTheatrePanel);
    }


    /**
     * Sets the TheatreControlPanel in this simulator.
     *
     * @param newTheatrePanel The TheatreControlPanel in this simulator.
     */
    public void setTheatrePanel(TheatreControlPanel newTheatrePanel)
    {
	myTheatrePanel = newTheatrePanel;
    }


    /**
     * The FieldPanel in this simulator.
     */
    private FieldPanel myFieldPanel;


    /**
     * Returns the FieldPanel in this simulator.
     *
     * @return The FieldPanel in this simulator.
     */
    public FieldPanel getFieldPanel()
    {
	return(myFieldPanel);
    }


    /**
     * Sets the FieldPanel in this simulator.
     *
     * @param newFieldPanel The FieldPanel in this simulator.
     */
    public void setFieldPanel(FieldPanel newFieldPanel)
    {
	myFieldPanel = newFieldPanel;
    }


    /**
     * The mode the SoccerSimulator is currently in.
     */
    public int mode;
    public static final int PLAYBOOK = 0, EDITOR = 1, THEATRE = 2;


    /**
     * The check boxes used in the 'Animation' menu.
     */
    private JCheckBoxMenuItem showPathItem, showNumberItem, loopItem;


    /**
     * Create the "File" menu, holds 'file orientated' menu items.
     *
     * @return The "File" menu.
     */
    private JMenu createFileMenu()
    {
	JMenu fileMenu = new JMenu("File");
	fileMenu.setMnemonic('F');
	fileMenu.setBackground(Color.WHITE);
		
	// Create and add the "Exit" menu item, exits the application.
	JMenuItem exitItem = new JMenuItem( "Exit");
	exitItem.setMnemonic('E');
	exitItem.addActionListener(
				   new ActionListener()
				       {
					   public void actionPerformed(ActionEvent e)
					   {
					       System.exit(0);
					   }
				       });
	fileMenu.add(exitItem);
	
	return(fileMenu);
    }
    

    /**
     * Create the "Settings" menu, holds 'settings orientated' menu items.
     *
     * @return The "Settings" menu.
     */
    private JMenu createSettingsMenu()
    {
	JMenu settingsMenu = new JMenu("Settings");
	settingsMenu.setMnemonic('S');
	settingsMenu.setBackground(Color.WHITE);
		
	// Create and add the "Color" menu, holds 'color orientated' menu items.
	JMenu colorMenu = new JMenu("Color");
	colorMenu.setMnemonic('C');
	settingsMenu.add(colorMenu);
	
	{
	    // Create and add the "Field Color" menu item, sets the color of the field.
	    JMenuItem fieldColorItem = new JMenuItem("Field Color");
	    fieldColorItem.addActionListener(
					  new ActionListener()
					      {
						  public void actionPerformed(ActionEvent e)
						  {
						      Color color = chooseColor();
						      if(color != null)
							  {
							      myFieldPanel.setBackground(color);
							  }
						  }
					      });
	    colorMenu.add(fieldColorItem);
		
	    // Create and add the "BallColor" menu item, sets the color of the ball.
	    JMenuItem ballColorItem = new JMenuItem("Ball Color");
	    ballColorItem.addActionListener(
					    new ActionListener()
						{
						    public void actionPerformed(ActionEvent e)
						    {
							Color color = chooseColor();
							if (color != null)
							    {
								myPlay.setBallColor(color);
							    }
						    }
						});
	    colorMenu.add(ballColorItem);
		
	    // Create and add the "Team1 Color" menu item, sets the color of team 1.
	    JMenuItem team1ColorItem = new JMenuItem("Team 1 Color");
	    team1ColorItem.addActionListener(
					     new ActionListener()
						 {
						     public void actionPerformed(ActionEvent e)
						     {
							 Color color = chooseColor();
							 if(color != null)
							     {
								 myPlay.setTeamColor(1, color);
							     }
						     }
						 });
	    colorMenu.add(team1ColorItem);
		
	    // Create and add the "Team2 Color" menu item, sets the color of team 2.
	    JMenuItem team2ColorItem = new JMenuItem("Team 2 Color");
	    team2ColorItem.addActionListener(
					     new ActionListener()
						 {
						     public void actionPerformed(ActionEvent e)
						     {
							 Color color = chooseColor();
							 if (color != null)
							     {
								 myPlay.setTeamColor(2, color);
							     }
						     }
						 });
	    colorMenu.add(team2ColorItem);
	}

	settingsMenu.addSeparator();
		
	// Create and add the "Field" menu, holds 'field orientated' menu items.
	JMenu fieldMenu = new JMenu("Field");
	fieldMenu.setMnemonic('i');
	settingsMenu.add(fieldMenu);
	
	{
	    // Create and add the "Field" button group, holds 'field orientated' buttons.
	    ButtonGroup fieldGroup = new ButtonGroup();
	    
	    {
		// Create and add the "No Field" menu item, sets the current field to no field.
		JRadioButtonMenuItem noFieldItem = new JRadioButtonMenuItem("No Field");
		noFieldItem.addActionListener(
					      new ActionListener()
						  {
						      public void actionPerformed(ActionEvent e)
						      {
							  myFieldPanel.getFieldDrawer().fieldType = FieldDrawer.NONE;
							  myFieldPanel.repaint();
						      }
						  });
		fieldGroup.add(noFieldItem);
		fieldMenu.add(noFieldItem);

		// Create and add the "Full Field" menu item, sets the current field to a full field.
		JRadioButtonMenuItem fullFieldItem = new JRadioButtonMenuItem("Full Field");
		fullFieldItem.setSelected(true);
		fullFieldItem.addActionListener(
						new ActionListener()
						    {
							public void actionPerformed(ActionEvent e)
							{
							    myFieldPanel.getFieldDrawer().fieldType = FieldDrawer.FULL;
							    myFieldPanel.repaint();
							}
						    });
		fieldGroup.add(fullFieldItem);
		fieldMenu.add(fullFieldItem);

		// Create and add the "Half Field" menu item, sets the current field to a half field.
		JRadioButtonMenuItem halfFieldItem = new JRadioButtonMenuItem("Half Field");
		halfFieldItem.addActionListener(
						new ActionListener()
						    {
							public void actionPerformed(ActionEvent e)
							{
							    myFieldPanel.getFieldDrawer().fieldType = FieldDrawer.HALF;
							    myFieldPanel.repaint();
							}
						    });
		fieldGroup.add(halfFieldItem);
		fieldMenu.add(halfFieldItem);

		// Create and add the "Left Side of Half Field" menu item, sets the current field to the left side of a half
		// field.
		JRadioButtonMenuItem leftHalfFieldItem = new JRadioButtonMenuItem("Left Side of Half Field");
		leftHalfFieldItem.addActionListener(
						    new ActionListener()
							{
							    public void actionPerformed(ActionEvent e)
							    {
								myFieldPanel.getFieldDrawer().fieldType =
								    FieldDrawer.LEFT_HALF;
								myFieldPanel.repaint();
							    }
							});
		fieldGroup.add(leftHalfFieldItem);
		fieldMenu.add(leftHalfFieldItem);

		// Create and add the "Right Side of Half Field" menu item, sets the current field to the right side of a
		// half field.
		JRadioButtonMenuItem rightHalfFieldItem = new JRadioButtonMenuItem("Right Side of Half Field");
		rightHalfFieldItem.addActionListener(
						     new ActionListener()
							 {
							     public void actionPerformed(ActionEvent e)
							     {
								 myFieldPanel.getFieldDrawer().fieldType =
								     FieldDrawer.RIGHT_HALF;
								 myFieldPanel.repaint();
							     }
							 });
		fieldGroup.add(rightHalfFieldItem);
		fieldMenu.add(rightHalfFieldItem);
	    }
	}

	settingsMenu.addSeparator();
		
	// Create and add the "Animation" menu, holds 'animation orientated' menu items.
	JMenu animationMenu = new JMenu("Animation");
	animationMenu.setMnemonic('A');
	settingsMenu.add(animationMenu);

	{		
	    // Create and add the "Loop" menu item, if set the animation will repeat until stopped.
	    loopItem = new JCheckBoxMenuItem("Loop");
	    loopItem.setSelected(false);
	    loopItem.addActionListener(
				       new ActionListener()
					   {
					       public void actionPerformed(ActionEvent e)
					       {
						   if(loopItem.isSelected())
						       {
							   myPlay.myTheatre.loop = true;
						       }
						   else
						       {
							   myPlay.myTheatre.loop = false;
						       }
					       }
					   });
	    animationMenu.add(loopItem);

	    // Create and add the "Show Players' and Ball paths" menu item, if set the moving object's paths will be shown
	    // during animation.
	    showPathItem = new JCheckBoxMenuItem("Show Players' and Ball paths");
	    showPathItem.addActionListener(
					   new ActionListener()
					       {
						   public void actionPerformed(ActionEvent e)
						   {
						       if(showPathItem.isSelected())
							   {
							       myPlay.myTheatre.showPath = true;
							   }
						       else
							   {
							       myPlay.myTheatre.showPath = false;
							   }
						       myFieldPanel.repaint();
						   }
					       });
	    animationMenu.add(showPathItem);
		
	    // Create and add the "Show Players' numbers" menu item, if set the players' numbers will be shown during
	    // animation.
	    showNumberItem = new JCheckBoxMenuItem("Show Players' numbers");
	    showNumberItem.setSelected(true);
	    showNumberItem.addActionListener(
					     new ActionListener()
						 {
						     public void actionPerformed(ActionEvent e)
						     {
							 if(showNumberItem.isSelected())
							     {
								 myPlay.myTheatre.showNumber = true;
							     }
							 else
							     {
								 myPlay.myTheatre.showNumber = false;
							     }
							 myFieldPanel.repaint();
						     }
						 });
	    animationMenu.add(showNumberItem);
	}

	settingsMenu.addSeparator();

	// Create and add the "Sound" menu, holds 'sound orientated' menu items.
	JMenu soundMenu = new JMenu("Sound");
	soundMenu.setMnemonic('S');
	settingsMenu.add(soundMenu);
	
	{
	    // Create and add the "Sound" button group, holds 'sound orientated' buttons.
	    ButtonGroup soundGroup = new ButtonGroup();
	    
	    {
		// Create and add the "On" menu item, turns sound on.
		JRadioButtonMenuItem soundOnItem = new JRadioButtonMenuItem("On");
		soundOnItem.setSelected(true);
		soundOnItem.addActionListener(
					      new ActionListener()
						  {
						      public void actionPerformed(ActionEvent e)
						      {
							  myPlay.myAudio.setEnabled(true);
						      }
						  });
		soundGroup.add(soundOnItem);
		soundMenu.add(soundOnItem);
		
		// Create and add the "Off" menu item, turns sound off.
		JRadioButtonMenuItem soundOffItem = new JRadioButtonMenuItem("Off");
		soundOffItem.setSelected(true);
		soundOffItem.addActionListener(
					       new ActionListener()
						   {
						       public void actionPerformed(ActionEvent e)
						       {
							   myPlay.myAudio.setEnabled(false);
						       }
						   });
		soundGroup.add(soundOffItem);
		soundMenu.add(soundOffItem);
	    }
	}
		
	return(settingsMenu);
    }
    
    
    /**
     * Create the "Help" menu, holds 'help orientated' menu items.
     *
     * @return The "Help" menu.
     */
    private JMenu createHelpMenu()
    {
	JMenu helpMenu = new JMenu("Help");
	helpMenu.setMnemonic('H');
	helpMenu.setBackground(Color.WHITE);
		
	// Create and add the "" menu item, .
	JMenuItem aboutItem = new JMenuItem("About");
	aboutItem.setMnemonic('b');
	aboutItem.addActionListener(
				    new ActionListener()
					{
					    public void actionPerformed(ActionEvent e)
					    {
						JOptionPane.showMessageDialog(myFieldPanel, "Soccer Simulator 1.0",
									      "About", JOptionPane.INFORMATION_MESSAGE);
					    }
					});
	helpMenu.add( aboutItem );

	return(helpMenu);
    }

     
    /**
     * Opens a 'JColorChooser' and returns the color chosen by the user.
     *
     * @return The color chosen by the user.
     */ 
    public Color chooseColor()
    {
	Color color = Color.WHITE;
	color = JColorChooser.showDialog(this, "Choose a color:", color);
	return(color);
    }


    /**
     * Allows the user to choose a place to save the Play this simulator is currently using.
     */
    public void savePlay()
    {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Save your play...");

	// Set filter for SS source files.
	fileChooser.addChoosableFileFilter(new SSFileFilter());
	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

	// Set to a default name for save.
	fileChooser.setSelectedFile(new File("new_play.ss"));
	int result = fileChooser.showSaveDialog(this);

	// If the user clicked the Cancel button on the dialog, return.
	if(result == JFileChooser.CANCEL_OPTION)
	    {
		return;
	    }

	// Get selected file.
	File fileName = fileChooser.getSelectedFile();

	// Open file.
	try
	    {
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
		
		try
		    {
			if(myPlay != null)
			    {
				output.writeObject(myPlay);
			    }
			output.close();
		    }
		catch(IOException e)
		    {
			JOptionPane.showMessageDialog(this, "Cannot write file", "Error saving play",
						      JOptionPane.ERROR_MESSAGE);
		    }
	    }
	catch(IOException e)
	    {
		JOptionPane.showMessageDialog(this, "Error saving play, please try a new directory or file name",
					      "Error saving play", JOptionPane.ERROR_MESSAGE);
	    }
    }


    /**
     * Allows the user to choose a sports simulator file (.ss) to load and attaches that file to this SoccerSimulator.
     *
     * @return The Play chosen by the user (or null.)
     */
    public Play loadPlay()
    {
	Play newPlay = null;

	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Choose a play...");

	// Set filter for .ss source files and open file chooser.
	fileChooser.addChoosableFileFilter(new SSFileFilter());
	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	int result = fileChooser.showOpenDialog(this);

	// If the user clicked the Cancel button on the dialog, return.
	if(result == JFileChooser.CANCEL_OPTION)
	    {
		return(newPlay);
	    }

	// Get selected file.
	File fileName = fileChooser.getSelectedFile();

	// Open file.
	try
	    {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));

		try
		    {
			Object obj = input.readObject();
			newPlay = (Play) obj;
			input.close();
		    }
		catch(EOFException e)
		    {
			JOptionPane.showMessageDialog(this, "Cannot read file", "Error loading play",
						      JOptionPane.ERROR_MESSAGE );
		    }
		catch(ClassNotFoundException e)
		    {
			JOptionPane.showMessageDialog(this, "Cannot read file", "Error loading play",
						      JOptionPane.ERROR_MESSAGE );
		    }
		catch(IOException e)
		    {
			JOptionPane.showMessageDialog(this, "Cannot read file", "Error loading play",
						      JOptionPane.ERROR_MESSAGE );
		    }
	    }
	// Process exceptions from opening file.
	catch(IOException e)
	    {
		JOptionPane.showMessageDialog(this,  "Cannot open file",
					      "Error loading play", JOptionPane.ERROR_MESSAGE);
	    }

	return(newPlay);
    }
}
