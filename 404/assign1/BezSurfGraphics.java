import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

/**
 * THIS CLASS IS BASED ON THE CLASS OF THE SAME NAME PROVIDED ON THE COURSE WEBSITE.
 * 
 * The graphics component of a Bezier Surface Viewer.
 * 
 * Displays the bezier surface model.
 * 
 * Reacts to mouse events to select control points of the bezier surface.
 * 
 * @author gb21
 */
public class BezSurfGraphics extends Canvas implements MouseListener
{
    /**
     * The model to be rendered on this graphics area.
     */
    private BezSurfModel model = new BezSurfModel();
    
    /**
     * Creates an instance of BezSurfGraphics.
     * 
     * @param xDim The size of the x dimension of this graphics component.
     * @param yDim The size of the y dimension of this graphics component.
     */
    public BezSurfGraphics(int xDim, int yDim)
    {
        setSize(xDim, yDim);
        setBackground(Color.black);
        addMouseListener(this);
    }
    
    /**
     * Returns the model to be rendered on this graphics area.
     * 
     * @return The model to be rendered on this graphics area.
     */
    public BezSurfModel getModel()
    {
        return (model);
    }
    
    public void mouseClicked(MouseEvent e)
    {
        model.selectControlPoint(e.getX(), e.getY());
        
        repaint();
    }

    public void mouseEntered(MouseEvent e)
    {/* unused */}

    public void mouseExited(MouseEvent e)
    {/* unused */}

    public void mousePressed(MouseEvent e)
    {/* unused */}

    public void mouseReleased(MouseEvent e)
    {/* unused */}
    
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = getSize();
        g2d.setBackground(getBackground());
        g2d.clearRect(0, 0, d.width, d.height);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        model.paint(g2d);
    }
}

