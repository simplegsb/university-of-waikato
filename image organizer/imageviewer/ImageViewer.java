package imageviewer;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 * Class ImageViewer provides the ability to display an image (jpeg or gif)
 * in a Frame. <p>
 * The following code demonstrates usage.<p>
 * 
 * <code>
 * // Use ImageViewer to load an image for us <br>
 * BufferedImage myImage = ImageViewer.loadImage(new File("/home/mhall/test.jpg")); <br>
 * <br>
 *
 * // Construct an ImageViewer... <br>
 * ImageViewer iv = new ImageViewer(); <br><br>
 * 
 * // Pass our image to the ImageViewer and make it visible... <br>
 * iv.setImage(myImage); <br>
 * iv.showImage(); <br><br>
 * 
 * // Hide the frame... <br>
 * iv.hideImage(); <br>
 *
 * </code><p>
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version 1.0
 */
public class ImageViewer extends JPanel {

  // Holds the current image to be displayed
  protected BufferedImage mImage;
  
  // A frame to display the image in
  protected JFrame mDisplayFrame;

  /**
   * Static convenience method that uses ImageIO to load an image
   * <br>(Postcondition: returns null or a BufferedImage object)
   * @param file a file to load
   * <br>(Precondition: file != null)
   * @return a <code>BufferedImage</code> containing the image or null if ImageIO
   * was unable to decode the image
   * @exception IOException if some IO error occurs
   */
  public static BufferedImage loadImage(File file) throws IOException {
    return ImageIO.read(file);
  }

  /**
   * Set the image to display. Normally should be followed with a call to showImage()
   * in order to display the image.
   *
   * @param image an image to display
   */
  public void setImage(BufferedImage image) {
    mImage = image;
    if (mDisplayFrame != null) {
      if (mDisplayFrame.isVisible()) {
	showImage();
      }
    } else {
      repaint();
    }
  }

  /**
   * Get the current image
   *
   * @return a <code>BufferedImage</code> or null if there is no current image
   */
  public BufferedImage getImage() {
    return mImage;
  }

  /**
   * Pops up a window to display the current image. If the window is already open
   * then it simply displays the current image.
   */
  public void showImage() {
    if (mDisplayFrame != null) {
      repaint();
      if (mImage != null) {
	mDisplayFrame.setSize(mImage.getWidth(), mImage.getHeight());
	mDisplayFrame.setVisible(true);
      }
    } else {
      mDisplayFrame = new JFrame("Image Displayer");
      mDisplayFrame.getContentPane().setLayout(new BorderLayout());
      mDisplayFrame.getContentPane().add(this, BorderLayout.CENTER);
      mDisplayFrame.addWindowListener(new WindowAdapter() {
	  public void windowClosing(WindowEvent e) {
	    mDisplayFrame.dispose();
	    mDisplayFrame = null;
	  }
	});
      if (mImage != null) {
	mDisplayFrame.setSize(mImage.getWidth(), mImage.getHeight());
	mDisplayFrame.setVisible(true);
      }
    }
  }

  /**
   * Hides the display window
   */
  public void hideImage() {
    if (mDisplayFrame != null) {
      mDisplayFrame.setVisible(false);
    }
  }

  /**
   * Renders the image into this component
   *
   * @param g a <code>Graphics</code> value
   */
  public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (mImage != null) {
        g.drawImage(mImage,0,0,this);
      }
   }
}
