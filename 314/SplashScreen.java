import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.DisplayMode;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


////////////////////////////////////////////////////////////////////
/////////////////////////// SPLASH SCREEN //////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * A window that contains only the image with the file name supplied. The window appears for the amount of time in
 * milliseconds supplied.
 *
 * @author Sports Simulator Team: ???
 */
public class SplashScreen extends JFrame
{
    /**
     * Creates an instance of a SplashScreen.
     *
     * @param image The image that will be shown on the SplashScreen.
     * @param showTime The amount of time the SplashScreen will be shown (in milliseconds.)
     */
    public SplashScreen(String image, int showTime)
    {
	logo = new ImageIcon(image);

	Container splashPanel = getContentPane();
	splashPanel.setLayout(new BorderLayout());
	Border bd1 = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	Border bd2 = BorderFactory.createEtchedBorder();
	Border bd3 = BorderFactory.createCompoundBorder(bd1, bd2);
	((JPanel) splashPanel).setBorder(bd3);

	splashPanel.add("Center", new JLabel(logo, JLabel.CENTER));

	setUndecorated(true);
	showFor(showTime);
    }
    

    /**
     * The logo to show on the SplashScreen.
     */
    private ImageIcon logo;


    /**
     * Displays the SplashScreen in the center of the screen for the amount of time supplied.
     *
     * @param showTime The amount of time the SplashScreen will be shown (in milliseconds.)
     */
    public void showFor(int showTime)
    {
	setSize(logo.getIconWidth(), logo.getIconHeight());

	DisplayMode screenDisp = getGraphicsConfiguration().getDevice().getDisplayMode();
	setLocation((screenDisp.getWidth()/2)-getSize().width/2, (screenDisp.getHeight()/2)-getSize().height/2);

	setVisible(true);
	try
	    {
		Thread.sleep(showTime);
	    } 
	catch(InterruptedException e) {}
	setVisible(false);
    }	
}

