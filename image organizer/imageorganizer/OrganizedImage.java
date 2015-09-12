package imageorganizer;


import imageviewer.ImageViewer;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


/**
 * A JPEG image in a hierachy of categories and JPEG images.
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public class OrganizedImage extends Organized
{
    public OrganizedImage(String name, File file)
    {
	super(name);
	imageFile = file;
    }


    /**
     * The image file this OrganizedImage represents.
     */
    private File imageFile;


    /**
     * A description of the image.
     */
    private String description = null;


    /**
     * Opens a window containing the picture this OrganizedImage holds.
     */
    public void view()
    {
	try
	    {
		ImageViewer viewer = new ImageViewer();

		BufferedImage image = ImageViewer.loadImage(imageFile);
		if (image == null)
		    {
			System.out.println("Unable to read image format!");
		    }
		else
		    {
			viewer.setImage(image);
			viewer.showImage();
		    }
	    }
	catch(Exception ex)
	    {
		System.out.println("That was not a valid option, please try again:");
	    }
    }


    /**
     * True if this Organized object is an OrganizedImage, false otherwise.
     *
     * @return true, this is an OrganizedImage.
     */
    public boolean isImage()
    {
	return(true);
    }


    /**
     * True if this Organized object is an OrganizedCategory, false otherwise.
     *
     * @return false, this is not an OrganizedCategory.
     */
    public boolean isCategory()
    {
	return(false);
    }


    /**
     * An accessor to the File object this OrganizedImage represents.
     *
     * @return The File object this OrganizedImage represents.
     */
    public File getFile()
    {
	return(imageFile);
    }


    /**
     * An accessor to the description of the image.
     *
     * @return The desription or a message saying there is no description as a String.
     */
    public String getDesc()
    {
	if(description == null)
	    {
		return("There is no description for this image.");
	    }
	else
	    {
		return(description);
	    }
    }


    /**
     * Changes the description of this image to desc.
     *
     * @param desc The new description.
     */
    public void setDesc(String desc)
    {
	description = desc;
    }
}
