package imageviewer;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

/**
 * Command line test program for the ImageViewer class. Demonstrates
 * programatically how to use the ImageViewer class.
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version 1.0
 */
public class ImageViewerTest {

  /**
   * Main method for using this class.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main (String [] args) {

    try {
      ImageViewer hd = new ImageViewer();
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      while (true) {
	System.out.print("(l)oad image, (s)how window, (h)ide window: ");
	String input = br.readLine();
	if (input.length() == 0) {
	  System.exit(0);
	} else if (input.charAt(0) == 'l') {
	  System.out.print("enter file name to load: ");
	  input = br.readLine();
	  BufferedImage readI = ImageViewer.loadImage(new File(input));
	  if (readI == null) {
	    System.err.println("Unable to read image format!");
	    System.exit(1);
	  }
	  hd.setImage(readI);
	  hd.showImage();
	} else if (input.charAt(0) == 's') {
	  hd.showImage();
	} else if (input.charAt(0) == 'h') {
	  hd.hideImage();
	}
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
