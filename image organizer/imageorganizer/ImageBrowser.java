package imageorganizer;


import java.io.*;
import java.util.*;


/**
 * Browses a hierachy of Organized objects. (OrganizedCategory and OrganizedImage objects)
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public class ImageBrowser implements Browser
{
    public ImageBrowser(OrganizedCategory main)
    {
	browseCategory = main;
    }


    /**
     * The OrganizedCategory currently being browsed.
     */
    private OrganizedCategory browseCategory;


    /**
     * Sets the OrganizedCategory currently being browsed to the parent OrganizedCategory of the current
     * OrganizedCategory.
     */
    public void goToParentFolder()
    {
	if(browseCategory.getParent() != null)
	    {
		browseCategory = browseCategory.getParent();
	    }
	else
	    {
		System.out.println("That was not a valid option, please try again:");
	    }
    }


    /**
     * Sets the OrganizedCategory currently being browsed to the selected sub OrganizedCategory of the
     * current OrganizedCategory.
     *
     * @param index The index of the sub folder to move to in the TreeSet of the OrganizedCategory currently
     * being browsed.
     */
    public void goToSubFolder(int index)
    {
	// The Organized object currently being checked.
	Organized currentOrganized;
	// The OrganizedImage object with the index passed.
	OrganizedCategory foundOrganized = null;
	// An array containing all the File objects in the OrganizedCategory currently being browsed.
	Iterator categoryIter = browseCategory.getIterator();
	
	for(int iterIndex = 0; categoryIter.hasNext(); iterIndex++)
	    {
		currentOrganized = (Organized) categoryIter.next();
		if(iterIndex == index && currentOrganized.isCategory())
		    {
			foundOrganized = (OrganizedCategory) currentOrganized;
			browseCategory = foundOrganized;
		    }
	    }

	if(browseCategory != foundOrganized)
	    {
		System.out.println("That was not a valid option, please try again:");
	    }
    }


    /**
     * Prints a texual representation of the OrganizedCategory currently being browsed.
     */
    public void view()
    {
	browseCategory.view();
    }


    /**
     * Deletes the category/image found at the index in the TreeSet of the OrganizedCategory currently being
     * browsed.
     *
     * @param index The index of the category/image to be deleted in the OrganizedCategory currently being
     * browsed.
     */
    public void delete(int index)
    {	
	OrganizedCategory.delete(browseCategory, index);
    }


    /**
     * Returns the OrganizedCategory currently being browsed.
     *
     * @return The OrganizedCategory currently being browsed.
     */
    public OrganizedCategory getCurrent()
    {
	return(browseCategory);
    }


    /**
     * Returns the category/image at the index passed to the method of the OrganizedCategory currently being
     * browsed.
     *
     * @param index The index of the category/image to be returned in the OrganizedCategory currently being
     * browsed.
     * @return The Organized object of the category/image at the index passed to the method.
     */
    public Organized get(int index)
    {
	return(OrganizedCategory.get(browseCategory, index));
    }
}
