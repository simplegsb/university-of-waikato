package imageorganizer;


import java.io.*;


/**
 * An object in a hierachy of categories and JPEG images.
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public abstract class Organized implements Comparable
{
    public Organized(String name)
    {
	mName = name;
	mParentCategory = null;
    }


    /**
     * The name of this Organized object.
     */
    private String mName;


    /**
     * The parent of this Organized object.
     */
    private OrganizedCategory mParentCategory;


    /**
     * An accessor to this Organized object's name.
     *
     * @return The name of this Organized object as a String.
     */
    public String getName()
    {
	return(mName);
    }


    /**
     * Changes this Organized object's name to newName.
     */
    private void setName(String newName)
    {
	mName = newName;
    }


    /**
     * An accessor to this Organized object's parent category.
     *
     * @return The parent of this Organized object (OrganizedCategory).
     */
    public OrganizedCategory getParent()
    {
	return(mParentCategory);
    }


    /**
     * Changes this Organized object's parent category to the newParent OrganizedCategory.
     */
    public void setParent(OrganizedCategory newParent)
    {
	mParentCategory = newParent;
    }


    /**
     * Compares this Organized object to another Organized object.
     */
    public int compareTo(Object other)
    {
	Organized otherOrganized = (Organized) other;
	return(mName.compareToIgnoreCase(otherOrganized.getName()));
    }


    /**
     * Renames the Organized object with name.
     *
     * @param newOrganized The Organized object to be renamed.
     * @param index The index of the Organized object to be renamed in the TreeSet of its original parent.
     * OrganizedCategory.
     * @param newName The String to change the Organized object's name to.
     */
    public static void rename(Organized newOrganized, int index, String newName)
    {
	// The new File object created if a OrganizedImage is being renamed.
	File newFile;
	//
	OrganizedImage newImage;

	if(newOrganized.isImage())
	    {
		newImage = (OrganizedImage) newOrganized;
		newImage.getFile().renameTo(new File(newImage.getFile().getParent(),
						   newName + ".jpg"));
		newFile = new File(newImage.getFile().getParent(), newName + ".jpg");
		newImage.getParent().add(newImage.getParent(), newFile);
		OrganizedCategory.delete(newImage.getParent(), index);
	    }
	else
	    {
		newOrganized.setName(newName);
	    }
    }


    /**
     * Shows a visual representation of this Organized object.
     */
    public abstract void view();


    /**
     * True if this Organized object is an OrganizedImage, false otherwise.
     */
    public abstract boolean isImage();


    /**
     * True if this Organized object is an OrganizedCategory, false otherwise.
     */
    public abstract boolean isCategory();
}
