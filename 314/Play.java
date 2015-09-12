import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JOptionPane;


////////////////////////////////////////////////////////////////////
/////////////////////////////// PLAY ///////////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Holds all the elements of a play: The audio, field, ball and players.
 *
 * @author Sports Simulator Team: ???
 */
public class Play
{
    /**
     * Creates an instance of Play.
     */
    public Play(SoccerSimulator simulator)
    {
	thisSimulator = simulator;
	myEditor = new Editor(this);
	myTheatre = new Theatre(this);
	myAudio = new AudioManager();

	// Initialize colors.
	ballColor = Color.darkGray;
	team1Color = Color.black;
	team2Color = Color.red;
	numberColor = Color.white;

	// Initialize play.
	selected = NO_SEL;
	currentTeam = 1;
    }


    /**
     * The SoccerSimulator this Play is associated with.
     */
    private SoccerSimulator thisSimulator;


    /**
     * Returns the SoccerSimulator this Play is associated with.
     *
     * @return The SoccerSimulator this Play is associated with.
     */
    public SoccerSimulator getSimulator()
    {
	return(thisSimulator);
    }


    /**
     * Sets the SoccerSimulator this Play is associated with.
     *
     * @param newSimulator The SoccerSimulator this Play is associated with.
     */
    public void setSimulator(SoccerSimulator newSimulator)
    {
	thisSimulator = newSimulator;
    }


    /**
     * The Editor this Play is associated with.
     */
    public Editor myEditor;


    /**
     * The Theatre this Play is associated with.
     */
    public Theatre myTheatre;


    /**
     * The AudioManager this Play is associated with.
     */
    public AudioManager myAudio;


    /**
     * The Field this Play holds.
     */
    private Field myField = new Field(this);


    /**
     * Returns the Field this Play holds.
     *
     * @return The Field this Play holds.
     */
    public Field getField()
    {
	return(myField);
    }


    /**
     * The Ball this Play holds.
     */
    private Ball myBall;


    /**
     * Returns the Ball this Play holds.
     *
     * @return The Ball this Play holds.
     */
    public Ball getBall()
    {
	return(myBall);
    }


    /**
     * Returns true if this Play currently holds a Ball.
     *
     * @return True if this Play currently holds a Ball, false otherwise.
     */
    public boolean ballExists()
    {
    	return(myBall != null);
    }


    /**
     * Adds a Ball to this Play, if a ball already exists an error message is shown.
     */
    public void addBall()
    {
	if(!ballExists())
	    {
		myEditor.offsetX = 0;
		myEditor.offsetY = 0;
		
		Dimension fieldSize = thisSimulator.getFieldPanel().getSize();
		myBall = new Ball(new Point(fieldSize.width/2, fieldSize.height/2), myTheatre);
		selected = BALL;
	    }
	else
	    {
		JOptionPane.showMessageDialog(thisSimulator.getFieldPanel(), "Invalid option", "Ball already exists!",
					      JOptionPane.ERROR_MESSAGE);
	    }

	myEditor.operation = Editor.NO_OP;
	thisSimulator.getFieldPanel().repaint();
    }

    /**
     * The players this Play holds.
     */
    private ArrayList myPlayers = new ArrayList();


    /**
     * Returns the players this Play holds.
     *
     * @return The players this Play holds.
     */
    public ArrayList getPlayers()
    {
	return(myPlayers);
    }


    /**
     * Returns true if this Play currently holds atleast one Player.
     *
     * @return True if this Play currently holds atleast one Player, false otherwise.
     */
    public boolean playersExist()
    {
    	return(myPlayers != null || myPlayers.isEmpty());
    }


    /**
     * Adds a Player to this Play.
     */
    public void addPlayer()
    {
	myEditor.offsetX = 0;
        myEditor.offsetY = 0;
	
	Dimension fieldSize = thisSimulator.getFieldPanel().getSize();
	Player newPlayer = new Player(new Point(fieldSize.width/2, fieldSize.height/2), myTheatre, currentTeam);
	
	do
	    {
		newPlayer.setNumber(inputNumber());
	    }
	while(!validNumber(newPlayer.getNumber(), newPlayer.getTeam()));
	
	myPlayers.add(newPlayer);
	selected = myPlayers.size()-1;

	myEditor.operation = Editor.NO_OP;
	thisSimulator.getFieldPanel().repaint();
    }
    

    /**
     * The color of the Ball this Play holds.
     */
    private Color ballColor;


    /** 
     * Returns the color of the Ball this Play holds.
     *
     * @return The color of the Ball this Play holds.
     */
    public Color getBallColor()
    {
    	return(ballColor);
    }


    /** 
     * Sets the color of the Ball this Play holds.
     *
     * @param color The color of the Ball this Play holds.
     */
    public void setBallColor(Color color)
    {
    	ballColor = color;

    	thisSimulator.getFieldPanel().repaint();
    }


    /**
     * The colors of the two teams of players this Play holds.
     */
    private Color team1Color, team2Color;
    

    /** 
     * Returns the colors of the two teams of players this Play holds.
     *
     * @return The colors of the two teams of players this Play holds.
     */
    public Color getTeamColor(int team)
    {
    	if(team == 1)
	    {
    		return(team1Color);
	    }
    	else
	    {
    		return(team2Color);
	    }
    }


    /** 
     * Sets the color of the two teams of players this Play holds.
     *
     * @param color The color of the two teams of players this Play holds.
     */
    public void setTeamColor(int team, Color color)
    {
    	if(team == 1)
	    {
    		team1Color = color;
	    }
    	else
	    {
    		team2Color = color;
	    }

    	thisSimulator.getFieldPanel().repaint();
    }


    /**
     * The color of the number that appears on the players this Play holds.
     */
    private Color numberColor;


    /** 
     * Returns the color of the number that appears on the players this Play holds.
     *
     * @return The color of the number that appears on the players this Play holds.
     */
    public Color getNumberColor()
    {
    	return(numberColor);
    }


    /** 
     * Sets the color of the number that appears on the players this Play holds.
     *
     * @param color The color of the number that appears on the players this Play holds.
     */
    public void setNumberColor(Color color)
    {
    	numberColor = color;

    	thisSimulator.getFieldPanel().repaint();
    }


    /**
     * The MovingObject currently selected (numbers 0 and above represent players.)
     */
    public int selected;
    public static final int NO_SEL = -2, BALL = -1;


    /**
     * The team currently selected (1 = team1, 2 = team2.)
     */
    public int currentTeam;


    /**
     * The number the user has input.
     */   
    private int userNumber;


    /**
     * Selects the MovingObject at the position supplied if one exists there.
     *
     * @param x The position to check for MovingObjects to select on the x axis.
     * @param y The position to check for MovingObjects to select on the y axis.
     */ 
    public void selectMovingObject(int x, int y)
    {
	selected = NO_SEL;
	
	// Select ball.
	if(ballExists())
	    {
		// For all actions of the ball.
		for (int i = 0; i < myBall.numOfVectors(); i ++)
		    {
			// Get current action.
			MoveVector tempVector = myBall.getVector(i);
			// Get start point of current action (turning point for the ball.)
			Point startPoint = tempVector.getStartPoint();
			// If the user has clicked on this ball turning point.
			if(x >= startPoint.getRoundedX()
			   && x < startPoint.getRoundedX() + Ball.RADIUS
			   && y >= startPoint.getRoundedY()
			   && y < startPoint.getRoundedY() + Ball.RADIUS)
			    {
				selected = BALL;
			    }
		    }
	    }
	// Select player.
	if(playersExist() && selected == NO_SEL)
	    {
		// For every player.
		for(int i = 0; i < myPlayers.size(); i ++)
		    {
			Player tempPlayer = (Player) myPlayers.get(i);
			// For all actions of the player.
			for(int j = 0; j < tempPlayer.numOfVectors(); j ++)
			    {
				// Get current action.
				MoveVector tempVector = tempPlayer.getVector(j);
				// Get start point of current action (turning point for the player.)
				Point startPoint = tempVector.getStartPoint();
				// If user has clicked on this player turning point.
				if(x >= startPoint.getRoundedX() 
				   && x < startPoint.getRoundedX() + Player.WIDTH
				   && y >= startPoint.getRoundedY() 
				   && y < startPoint.getRoundedY() + Player.HEIGHT)
				    {
					selected = i;
				    }
			    }
		    }
	    }

	myEditor.operation = Editor.NO_OP;
	thisSimulator.getFieldPanel().repaint();
    }


    /**
     * Removes the MovingObject action / MovingObject at the position supplied if one exists there.
     *
     * @param x The position to check for MovingObject actions / MovingObjects to remove on the x axis.
     * @param y The position to check for MovingObject actions / MovingObjects to remove on the y axis.
     */ 
    public void removeMovingObject(int x, int y)
    {
	// Remove ball action / ball.
	if(ballExists())
	    {
		// For all actions of the ball.
		for(int i = 0; i < myBall.numOfVectors(); i++)
		    {
			// Get current action.
			MoveVector tempVector = myBall.getVector(i);
			// Get start point of current action (turning point for the ball.)
			Point startPoint = tempVector.getStartPoint();
			// If user has clicked on this ball turning point.
			if(x >= startPoint.getRoundedX()
			   && x < startPoint.getRoundedX() + Ball.RADIUS
			   && y >= startPoint.getRoundedY()
			   && y < startPoint.getRoundedY() + Ball.RADIUS)
			    {
				if(myBall.numOfVectors() == 1)
				    {
					myBall = null;
					thisSimulator.getEditorPanel().removeButton.setSelected(false);
					break;
				    }
				else
				    {
					myBall.removeVector(i);
				    }
			    }
		    }
	    }
	// Remove player action / player.
	if(playersExist())
	    {
		// For every player.
		for(int i = 0; i < myPlayers.size(); i ++)
		    {
			Player tempPlayer = (Player) myPlayers.get(i);
			// For all actions of the player.
			for(int j = 0; j < tempPlayer.numOfVectors(); j ++)
			    {
				// Get current action.
				MoveVector tempVector = tempPlayer.getVector(j);
				// Get start point of current action (turning point for the player.)
				Point startPoint = tempVector.getStartPoint();
				// If the user has clicked on this player turning point.
				if(x >= startPoint.getRoundedX() 
				   && x < startPoint.getRoundedX() + Player.WIDTH
				   && y >= startPoint.getRoundedY() 
				   && y < startPoint.getRoundedY() + Player.HEIGHT)
				    {
					if(tempPlayer.numOfVectors() == 1)
					    {
						myPlayers.remove(i);
					    }
					else
					    {
						tempPlayer.removeVector(j);
					    }
				    }
			    }
		    }
	    }

	myEditor.operation = Editor.NO_OP;
	thisSimulator.getFieldPanel().repaint();

	thisSimulator.getEditorPanel().removeButton.setSelected(false);
    }


    /**
     * Selects the MovingObject at the position supplied for dragging if one exists there.
     *
     * @param x The position to check for MovingObjects to drag on the x axis.
     * @param y The position to check for MovingObjects to drag on the y axis.
     */ 
    public void initMovingObjectDrag(int x, int y)
    {
	selected = NO_SEL;
	
	// Drag ball.
	if(ballExists())
	    {
		// For all actions of the ball.
		for(int i = 0; i < myBall.numOfVectors(); i++)
		    {
			// Get current action.
			MoveVector tempVector = myBall.getVector(i);
			// Get start point of current action (turning point for the ball.)
			Point startPoint = tempVector.getStartPoint();
			int bx = startPoint.getRoundedX();
			int by = startPoint.getRoundedY();
			// If the user has pressed on this ball turning point.
			if(x >= bx && x < bx + Ball.RADIUS && y >= by && y < by + Ball.RADIUS)
			    {
				selected = BALL;
				myEditor.dragging = true;
				myEditor.positionNum = i;
				
				// Set the distance from the corner of the shape to (x,y).
				myEditor.offsetX = x - bx;  
				myEditor.offsetY = y - by;
				
				thisSimulator.getFieldPanel().repaint();
			    }
		    }
	    }

	// Drag player.
	if(playersExist() && selected == NO_SEL)
	    {
		// For every player.
		for(int i = 0; i < myPlayers.size(); i ++)
		    {		
			Player tempPlayer = (Player) myPlayers.get(i);
			// For all actions of the player.
			for(int j = 0; j < tempPlayer.numOfVectors(); j++)
			    {
				// Get current action.
				MoveVector tempVector = tempPlayer.getVector(j);
				// Get start point of current action (turning point for the player.)
				Point startPoint = tempVector.getStartPoint();
				int px = startPoint.getRoundedX();
				int py = startPoint.getRoundedY();
				// If the user has pressed on this player turning point.
				if(x >= px && x < px + Player.WIDTH && y >= py && y < py + Player.HEIGHT)
				    {
					selected = i;
					myEditor.dragging = true;
					myEditor.positionNum = j;

					// Set the distance from the corner of the shape to (x,y).
					myEditor.offsetX = x - px;
					myEditor.offsetY = y - py;

					thisSimulator.getFieldPanel().repaint();
				    }
			    }
		    }
	    }
    }

	
    /** 
     * Creates a window where the user is asked to input a number. If the number is valid, it is returned, otherwise 0 is
     * returned.
     *
     * @return The number the user inputted.
     */
    public int inputNumber()
    {
    	String input = JOptionPane.showInputDialog(thisSimulator.getFieldPanel(), "Please enter player number",
						   "Player Number", JOptionPane.INFORMATION_MESSAGE);

    	// a player's number can be 0, which means he doesn't have a number
    	if(input == null)
	    {
		return(0);
	    }

    	try
	    {
    		userNumber = Integer.parseInt(input);	
	    }
    	catch(NumberFormatException e)
	    {
    		JOptionPane.showMessageDialog(thisSimulator.getFieldPanel(), "You must enter an integer!",
					      "Invalid Number Format", JOptionPane.ERROR_MESSAGE);
    		inputNumber();
	    }

    	return(userNumber);
    }
    

    /** 
     * Takes a number inputted by the user and checks if it is valid. Returns true if the number is valid, false otherwise.
     *
     * @param number The number inputted by the user.
     * @param team The current team.
     *
     * @return True if the number is valid, false otherwise.
     */
    public boolean validNumber(int number, int team)
    {
	if(userNumber < 0 || userNumber > 99)
	    {
		JOptionPane.showMessageDialog(thisSimulator.getFieldPanel(), "The number must between 0 to 99!",
					      "Invalid Player Number", JOptionPane.ERROR_MESSAGE);
		return(false);
	    }

    	if(userNumber == 0)
	    {
		return true;
	    }

    	for(int i = 0; i < myPlayers.size(); i ++)
	    {
		Player tempPlayer = (Player) myPlayers.get(i);
		if (team == tempPlayer.getTeam() && number == tempPlayer.getNumber())
		    {
			JOptionPane.showMessageDialog(thisSimulator.getFieldPanel(), "The number already exists!",
						      "Invalid Player Number", JOptionPane.ERROR_MESSAGE);
			return(false);
		    }
	    }
    	return(true);	
    }
}

