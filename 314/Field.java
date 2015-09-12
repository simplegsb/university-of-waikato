import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;


////////////////////////////////////////////////////////////////////
/////////////////////////////// FIELD //////////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Contains methods for drawing fields and field components (ball and players, and their MoveVectors.)
 *
 * @author Sports Simulator Team: ???
 */
public class Field
{
    /**
     * Creates an instance of Field.
     *
     * @param play The Play this Field is associated with.
     */
    public Field(Play play)
    {
	thisPlay = play;
    }


    /**
     * The Play this Field is associated with.
     */
    private Play thisPlay;


    /**
     * Draws the field with or without components depending on options in myTheatre.
     *
     * @param g The graphics to paint to.
     */
    public void drawAll(Graphics g, FieldDrawer myFieldDrawer)
    {
	if(thisPlay.getSimulator().mode == thisPlay.getSimulator().THEATRE)
	    {
	    if(thisPlay.myTheatre.showField)
		{
		    myFieldDrawer.drawCurrentField(g);
		}

	    if(thisPlay.myTheatre.showPath)
		{
		    drawMovingPaths(g);
		}

	    if(thisPlay.myTheatre.playing)
		{
		    drawMovements(g);
		}
	    else
		{
		    drawObjects(g);
		}
	    }
	else
	    {
		myFieldDrawer.drawCurrentField(g);
		drawMovingPaths(g);
		drawObjects(g);
	    }
    }
	

    /**
     * Draw the MovingObjects on this FieldPanel.
     *
     * @param g The graphics to paint to.
     */	
    public void drawObjects(Graphics g)
    {
	// Draw the ball in the field.
	if(thisPlay.ballExists())
	    {
		// For all actions of the ball.
		for(int i = 0; i < thisPlay.getBall().numOfVectors(); i ++)
		    {
			// Get current action.
			MoveVector tempVector = thisPlay.getBall().getVector(i);
			// Get start point of current action (turning point for the ball.)
			Point startPoint = tempVector.getStartPoint();
			// Draw a circle at the turning point.
			g.setColor(thisPlay.getBallColor());
			g.fillOval(startPoint.getRoundedX(), startPoint.getRoundedY(), Ball.RADIUS, Ball.RADIUS);
			// If the ball is currently selected draw a yellow outline.
			if(thisPlay.selected == Play.BALL)
			    { 
				g.setColor(Color.yellow);
				g.drawOval(startPoint.getRoundedX(), startPoint.getRoundedY(), Ball.RADIUS, Ball.RADIUS);
			    }
		    }
	    }
	// Draw players in the field.
	if (thisPlay.playersExist())
	    {
		// For every player.
		for(int i = 0; i < thisPlay.getPlayers().size(); i ++)
		    {
			Player tempPlayer = (Player) thisPlay.getPlayers().get(i);
			// For all actions of the player.
			for(int j = 0; j < tempPlayer.numOfVectors(); j ++)
			    {
				// Get current action.
				MoveVector tempVector = tempPlayer.getVector(j);
				// Get start point of current action (turning point for the player.)
				Point startPoint = tempVector.getStartPoint();
				// Draw a square at the turning point.
				g.setColor(thisPlay.getTeamColor(tempPlayer.getTeam()));
				g.fillRect(startPoint.getRoundedX(), startPoint.getRoundedY(), Player.WIDTH, Player.HEIGHT);
				// If the player has a number, draw it.
				if(tempPlayer.hasNumber())
				    {
					g.setColor(thisPlay.getNumberColor());
					g.drawString(Integer.toString(tempPlayer.getNumber()),
						     startPoint.getRoundedX(), startPoint.getRoundedY() + 17);
				    }
				// If the player is currently selected draw a yellow outline.
				if (thisPlay.selected == i)
				    { 
					g.setColor(Color.yellow);
					g.drawRect(startPoint.getRoundedX(), startPoint.getRoundedY(), Player.WIDTH,
						   Player.HEIGHT);
				    }
			    }
		    }
	    }
    }

	
    /**
     * Draw the ActionArrows of the MovingObjects on this FieldPanel.
     *
     * @param g The graphics to paint to.
     */	
    public void drawMovingPaths(Graphics g)
    {
	// The color of the ActionArrows.
	g.setColor(new Color(0, 65, 27));

	// Draw the ball's ActionArrows in the field.
	if(thisPlay.ballExists())
	    {
		// Draw a shadow of the ball at the first ball turning point (the starting point for the ball.)
		int bx = thisPlay.getBall().getVector(0).getStartPoint().getRoundedX();
		int by = thisPlay.getBall().getVector(0).getStartPoint().getRoundedY();
		g.fillOval(bx, by, Ball.RADIUS, Ball.RADIUS);

		// For all actions of the ball.
		for (int i = 0; i < thisPlay.getBall().numOfVectors(); i ++)
		    {
			// Get current action.
			MoveVector tempVector = thisPlay.getBall().getVector(i);
			// Draw a path from the start of the ActionArrow to the end of the ActionArrow.
			if(tempVector.hasDestination())
			    {
				Point startPoint = tempVector.getStartPoint();
				Point endPoint = tempVector.getEndPoint();
				drawArrow(g, startPoint.getCenterX(Ball.RADIUS), startPoint.getCenterY(Ball.RADIUS),
					  endPoint.getCenterX(Ball.RADIUS), endPoint.getCenterY(Ball.RADIUS), 29, 30);	
			    }
		    }
	    }
	// Draw the players' ActionArrows in the field.
	if (thisPlay.playersExist())
	    {
		// For every player.
		for(int i = 0; i < thisPlay.getPlayers().size(); i ++)
		    {
			Player tempPlayer = (Player) thisPlay.getPlayers().get(i);
			// Draw a shadow of the player at the first ball turning point (the starting point for the player.)
			int px = tempPlayer.getVector(0).getStartPoint().getRoundedX();
			int py = tempPlayer.getVector(0).getStartPoint().getRoundedY();	
			g.fillRect(px, py, Player.WIDTH, Player.HEIGHT);

			// For all actions of the player.
			for (int j = 0; j < tempPlayer.numOfVectors(); j ++)
			    {
				// Get current action.
				MoveVector tempVector = tempPlayer.getVector(j);
				// Draw a path from the start of the ActionArrow to the end of the ActionArrow.
				if (tempVector.hasDestination())
				    {		
					Point startPoint = tempVector.getStartPoint();
					Point endPoint = tempVector.getEndPoint();
					drawArrow(g, startPoint.getCenterX(Player.WIDTH),
						  startPoint.getCenterY(Player.HEIGHT), endPoint.getCenterX(Player.WIDTH),
						  endPoint.getCenterY(Player.HEIGHT), 29, 30);
				    }
			    }
		    }
	    }
    }


    /**
     * Draw the  of the MovingObjects on this FieldPanel.
     *
     * @param g The graphics to paint to.
     */
    public void drawMovements(Graphics g)
    {
	// if all the objects have reached the end points
	if(thisPlay.myTheatre.endAnimation())
	    {
		if(thisPlay.myTheatre.loop)
		    {
			thisPlay.myTheatre.stopAnimation();
			thisPlay.myTheatre.startAnimation();
		    }
		else
		    {
			thisPlay.myTheatre.stopAnimation();
			thisPlay.getSimulator().getTheatrePanel().resetPlayButton();
			thisPlay.getSimulator().getFieldPanel().repaint();
			return;
		    }
	    }

	// Draw the ball in the field.
	if(thisPlay.ballExists())
	    {
		if(thisPlay.myTheatre.direction == Theatre.BACKWARD)
		    {
			thisPlay.getBall().moveBackward();
		    }
		else
		    {
			thisPlay.getBall().moveForward();
		    }

		g.setColor(thisPlay.getBallColor());
		g.fillOval(thisPlay.getBall().getCurrent().getRoundedX(), thisPlay.getBall().getCurrent().getRoundedY(),
			   Ball.RADIUS, Ball.RADIUS);
	    }
	// Draw players in the field.
	if(thisPlay.playersExist())
	    {
		// For every player.
		for(int i = 0; i < thisPlay.getPlayers().size(); i ++)
		    {
			Player tempPlayer = (Player) thisPlay.getPlayers().get(i);
			if(thisPlay.myTheatre.direction == Theatre.BACKWARD)
			    {
				tempPlayer.moveBackward();
			    }
			else
			    {
				tempPlayer.moveForward();
			    }

			g.setColor(thisPlay.getTeamColor(tempPlayer.getTeam()));
			g.fillRect(tempPlayer.getCurrent().getRoundedX(),
				   tempPlayer.getCurrent().getRoundedY(),
				   Player.WIDTH, Player.HEIGHT);

			// If the player has a number, draw it.
			if(thisPlay.myTheatre.showNumber && tempPlayer.hasNumber())
			    {
				g.setColor(thisPlay.getNumberColor());
				g.drawString(Integer.toString(tempPlayer.getNumber()),
					     tempPlayer.getCurrent().getRoundedX(),
					     tempPlayer.getCurrent().getRoundedY() + 17 );
			    }
		    }
	    }
    }
	

    /**
     * Draw an arrow at the end of an ActionArrow.
     *
     * @param g The graphics to paint to.
     * @param x1 Position of the base of this arrow on the x axis.
     * @param y1 Position of the base of this arrow on the y axis.
     * @param x2 Position of the point of this arrow on the x axis.
     * @param y2 Position of the point of this arrow on the y axis.
     * @param K Blade height.
     * @param N Blade length.
     */	
    void drawArrow(Graphics g, int x1, int y1, int x2, int y2, double K, double N)
    {
	double L = Math.sqrt(N * N - K * K);
	int x = x2 - x1;
	int y = y2 - y1;
	double A = Math.sqrt(x * x + y * y);
	
	int x3 = (int) (((A - K) / A) * x - (L / A) * y);
	int y3 = (int) (((A - K) / A) * y + (L / A) * x);
	int x5 = (int) (((A - K) / A) * x + (L / A) * y);
	int y5 = (int) (((A - K) / A) * y - (L / A) * x);
	
	int x4 = x1 + x3;
	int y4 = y1 + y3;
	int x6 = x1 + x5;
	int y6 = y1 + y5;
	
	float dashes[] = {10};
	Graphics2D g2d = (Graphics2D) g;
	Stroke previousStroke = g2d.getStroke();
	g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0));	
	g.drawLine(x1,y1,x2,y2); // line
	g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
	g.drawLine(x2,y2,x4,y4); // blade left
	g.drawLine(x2,y2,x6,y6); // blade right

	// Restore to previous stroke.
	g2d.setStroke( previousStroke );	
    }  
}
