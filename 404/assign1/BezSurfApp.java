import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class BezSurfApp extends JApplet
{
    public static void main(String s[])
    {
        BezSurfApp bezSurfApp = new BezSurfApp();
        bezSurfApp.init();

        JFrame frame = new JFrame("Bezier Surface Viewer");
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        frame.getContentPane().add("Center", bezSurfApp);
        frame.pack();
        frame.setResizable(false);

        frame.setVisible(true);
    }

    final int FrameXDim_ = 640;

    final int FrameYDim_ = 540;

    final int GraphicsXDim_ = FrameXDim_;

    final int GraphicsYDim_ = 340;

    final int TextMenuXDim_ = FrameXDim_;

    final int TextMenuYDim_ = (FrameYDim_ - GraphicsYDim_);

    public void init()
    {
        BezSurfGraphics graphics = new BezSurfGraphics(GraphicsXDim_, GraphicsYDim_);

        BezSurfTextMenu textmenu = new BezSurfTextMenu(TextMenuXDim_, TextMenuYDim_, graphics);

        getContentPane().add(graphics);
        getContentPane().add("South", textmenu);
    }
}
