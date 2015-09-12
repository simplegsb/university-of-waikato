import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class BezSurfGraphics extends Canvas implements MouseListener
{
    public BezSurfGraphics(int xDim, int yDim)
    {
        setBackground(Color.black);
        addMouseListener(this);

        setSize(xDim, yDim);
    }

    public void drawBezSurfGraphics(int w, int h, Graphics2D g2)
    {
        BasicStroke thin = new BasicStroke(1);
        g2.setStroke(thin);

        g2.setColor(Color.white);
        g2.drawLine(0, 0, 200, 300);

        g2.setColor(Color.red);
        g2.drawLine(50, 0, 250, 300);

        g2.setColor(Color.white);
        g2.setPaint(Color.white);
        Ellipse2D ellipse = new Ellipse2D.Double(250, 300, 4, 4);
        g2.fill(ellipse);

        for (int i = 0; i < 255; i++)
        {
            Color grey = new Color(i, i, i);
            g2.setColor(grey);

            // Graphics2D does not have an API call for plotting a single point
            // => draw a line that starts and stops at the same point.
            g2.drawLine(400, 50 + i, 400, 50 + i);

        }

        g2.setColor(Color.white);
        g2.drawString("Click in this window to see print out mouse co-ords", 80, 10);
        g2.drawLine(0, h - 1, w - 1, h - 1);
    }

    public void mouseClicked(MouseEvent e)
    {
        System.out.println("Mouse click at: [" + e.getX() + "," + e.getY() + "]");
    }

    public void mouseEntered(MouseEvent e)
    { /* unused */}

    public void mouseExited(MouseEvent e)
    { /* unused */}

    public void mousePressed(MouseEvent e)
    { /* unused */}

    public void mouseReleased(MouseEvent e)
    { /* unused */}

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBezSurfGraphics(d.width, d.height, g2);
    }

}
