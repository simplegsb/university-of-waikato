import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class BezSurfTextMenu extends Canvas implements KeyListener
{
    protected BezSurfGraphics graphics_;

    protected char keyPressed_;

    public BezSurfTextMenu(int xDim, int yDim, BezSurfGraphics graphics)
    {
        graphics_ = graphics;
        keyPressed_ = 0;

        setSize(xDim, yDim);
        setBackground(Color.black);
        addKeyListener(this);
    }

    public void drawBezSurfTextMenu(int w, int h, Graphics2D g2)
    {

        g2.setColor(Color.white);
        g2.drawString("Click in this window and press any key", 10, 50);
        g2.drawString("To quite press 'Q'", 10, 80);

        if (keyPressed_ > 0)
        {
            String msg = "Key pressed '" + keyPressed_ + "'";

            g2.drawString(msg, 10, 120);
        }

    }

    public void keyPressed(KeyEvent e)
    { /* unused */}

    public void keyReleased(KeyEvent e)
    { /* unused */}

    public void keyTyped(KeyEvent e)
    {
        keyPressed_ = e.getKeyChar();
        if (keyPressed_ == 'Q')
        {
            System.exit(0);
        }
        repaint();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();

        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBezSurfTextMenu(d.width, d.height, g2);
    }

}
