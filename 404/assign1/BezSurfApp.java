import javax.swing.JFrame;

/**
 * THIS CLASS IS BASED ON THE CLASS OF THE SAME NAME PROVIDED ON THE COURSE WEBSITE.
 * 
 * An application that holds both the graphics and text menu components of the Bezier Surface Viewer.
 * 
 * @author gb21
 */
public class BezSurfApp extends JApplet
{
    public static void main(String args[])
    {
        JFrame frame = new JFrame("Bezier Surface Viewer");
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        frame.getContentPane().add("Center", new BezSurfApp());
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    /**
     * The size of the x dimension of the application frame.
     */
    private final int frameXDim = 640;

    /**
     * The size of the y dimension of the application frame.
     */
    private final int frameYDim = 540;

    /**
     * The size of the x dimension of the graphics component.
     */
    private final int graphicsXDim = frameXDim;

    /**
     * The size of the y dimension of the graphics component.
     */
    private final int graphicsYDim = 340;

    /**
     * The size of the x dimension of the text menu component.
     */
    private final int textMenuXDim = frameXDim;

    /**
     * The size of the y dimension of the text menu component.
     */
    private final int textMenuYDim = (frameYDim - graphicsYDim);

    /**
     * Creates an instance of BezSurfApp.
     * 
     * Adds the graphics and text menu components.
     */
    public BezSurfApp()
    {
        BezSurfGraphics graphics = new BezSurfGraphics(graphicsXDim, graphicsYDim);

        BezSurfTextMenu textmenu = new BezSurfTextMenu(textMenuXDim, textMenuYDim, graphics);

        getContentPane().add(graphics);
        getContentPane().add("South", textmenu);
    }
}

