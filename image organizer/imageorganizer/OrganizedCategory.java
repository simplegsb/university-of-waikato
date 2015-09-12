package imageorganizer;


import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;


/**
 * A category in a hierachy of categories and JPEG images.
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public class OrganizedCategory extends Organized
{
    public OrganizedCategory(String name)
    {
	super(name);
	category = new TreeSet();
    }


    /**
     * A treeSet holding all of the OrganizedImage and OrganizedCategory objects in this OrganizedCategory.
     */
    private SortedSet category;


    /**
     * Prints a textual representation of this OrganizedCategory.
     */
    public void view()
    {
	// The Organized object currently being checked.
	Organized currentOrganized;
	// An iterator over all the Organized object's in this OrganizedCategory.
	Iterator categoryIter;

	System.out.println("");
	System.out.println(getName() + ":");

	if(!category.isEmpty())
	    {
		// Print all OrganizedImage objects.
		System.out.println("\tImages:");
		categoryIter = category.iterator();
		for(int index = 0; categoryIter.hasNext(); index++)
		    {
			currentOrganized = (Organized) categoryIter.next();
			if(currentOrganized.isImage())
			    {
				System.out.println("\t\t" + index + ": " + currentOrganized.getName());
			    }
		    }
		
		// Print all OrganizedCategory objects.
		System.out.println("\tCategories:");
		categoryIter = category.iterator();
		for(int index = 0; categoryIter.hasNext(); index++)
		    {
			currentOrganized = (Organized) categoryIter.next();
			if(currentOrganized.isCategory())
			    {
				System.out.println("\t\t" + index + ": " + currentOrganized.getName());
			    }
		    }
	    }
	else
	    {
		System.out.println("\tThis folder is empty.");
	    }
    }


    /**
     * True if this Organized object is an OrganizedImage, false otherwise.
     *
     * @return false, this is not an OrganizedImage.
     */
    public boolean isImage()
    {
	return(false);
    }


    /**
     * True if this Organized object is an OrganizedCategory, false otherwise.
     *
     * @return true, this is an OrganizedCategory.
     */
    public boolean isCategory()
    {
	return(true);
    }


    /**
     * An accessor to this OrganizedCategory object's TreeSet.
     *
     * @return The TreeSet of this OrganizedCategory.
     */
    public SortedSet getCategory()
    {
	return(category);
    }


    /**
     * An accessor to the size of the TreeSet of this OrganizedCategory.
     *
     * @return The size of the TreeSet of this OrganizedCategory.
     */
    public int getSize()
    {
	return(category.size());
    }


    /**
     * Constructs an iterator over the TreeSet of this OrganizedCategory.
     *
     * @return An iterator over the TreeSet of this OrganizedCategory.
     */
    public Iterator getIterator()
    {
	return(category.iterator());
    }


    /**
     * Adds an OrganizedCategory with an empty TreeSet to this OrganizedCategory.
     *
     * @param name The name of the OrganizedCategory to be added.
     */
    public void addEmptyCategory(String name)
    {
	OrganizedCategory newCategory = new OrganizedCategory(name);
	newCategory.setParent(this);
	category.add(newCategory);
    }


    /**
     * Converts a File object to an Organized object and adds it to an OrganizedCategory.
     *
     * @param parent The OrganizedCategory the Organized object will be added to.
     * @param newFile The File object to be added.
     */
    public static void add(OrganizedCategory parent, File newFile)
    {
	if(newFile.isFile())
	    {
		if(imageCheck(newFile))
		    {
			OrganizedImage newOrganized = new OrganizedImage(newFile.getName(), newFile);
			newOrganized.setParent(parent);
			parent.getCategory().add(newOrganized);
		    }
	    }
	else if(newFile.isDirectory())
	    {
		addFolder(parent, newFile);
	    }
    }


    /**
     * Recursively converts a File object (directory) to an OrganizedCategory object and adds it and all
     * JPEG files it contains to an OrganizedCategory. Does not add any directory that contains no JPEG
     * files in itself or its sub directories. 
     *
     * @param parent The OrganizedCategory the Organized object will be added to.
     * @param folder The File object (directory) to be added.
     */
    public static void addFolder(OrganizedCategory parent, File folder)
    {
	// The category the directory currently being added will be represented by.
	OrganizedCategory newCategory = new OrganizedCategory(folder.getName());
	// An array containing all the File objects in the directory currently being added.
	File allFiles[] = folder.listFiles();

	if(allFiles != null)
	    {
		for(int index = 0; index < allFiles.length; index++)
		    {
			if(allFiles[index].isFile())
			    {
				
				newCategory.add(newCategory, allFiles[index]);
			    }
			else if(allFiles[index].isDirectory())
			    {
				addFolder(newCategory, allFiles[index]);
			    }
		    }
		if(!newCategory.getCategory().isEmpty())
		    {
			newCategory.setParent(parent);
			parent.getCategory().add(newCategory);
		    }
	    }
    }


    /**
     * Returns the category/image at the index passed to the method of the OrganizedCategory passed to the
     * method.
     *
     * @param category The category to get the Organized object from.
     * @param index The index of the category/image to be returned in the TreeSet of the OrganizedCategory
     * passed to the method.
     * @return The Organized object of the category/image at the index passed to the method.
     */
    public static Organized get(OrganizedCategory category, int index)
    {
	// The Organized object currently being checked.
	Organized currentOrganized;
	// An iterator over the TreeSet of the OrganizedCategory currently being browsed.
	Iterator categoryIter = category.getIterator();
	
	for(int iterIndex = 0; categoryIter.hasNext(); iterIndex++)
	    {
		currentOrganized = (Organized) categoryIter.next();
		if(iterIndex == index)
		    {
			return(currentOrganized);
		    }
	    }

	System.out.println("That was not a valid option, please try again:");
	return(null);
    }


    /**
     * Searches the Organized object hierachy within the OrganizedCategory passed to the function for an
     * Organized object with a name lexicographically equal to the String passed to the function.
     *
     * @param folder The top OrganizedCategory to begin the search in.
     * @param search The String to compare the names of the Organized objects in the hierachy with.
     * @return The OrganizedCategory with a name lexicographically equal to the String passed to the
     * function or the OrganizedCategory that holds the OrganizedImage with a name lexicographically equal
     * to the String passed to the function.
     */
    public static OrganizedCategory search(OrganizedCategory folder, String search)
    {
	// The Organized object whose name is currently being compared to the String.
	Organized current;
	// The result of searching an OrganizedCategory, null if unsuccessful.
	OrganizedCategory searchCategory;
	//
	Iterator searchIter = folder.getCategory().iterator();

	while(searchIter.hasNext())
	    {
		current = (Organized) searchIter.next();
		if(current.getName().compareTo(search) == 0)
		    {
			if(current.isImage())
			    {
				return(current.getParent());
			    }
			else
			    {
				return((OrganizedCategory) current);
			    }
		    }
		else if(current.isCategory())
		    {
			searchCategory = search((OrganizedCategory) current, search);
			if(searchCategory != null)
			    {
				return(searchCategory);
			    }
		    }
	    }
	return(null);
    }


    /**
     * Deletes the category/image found at the index in the TreeSet of the OrganizedCategory  passed to the
     * method.
     *
     * @param folder The OrganizedCategory that the Organized object to be deleted is in.
     * @param index The index of the category/image to be deleted in the TreeSet of the OrganizedCategory
     * passed to the method.
     */
    public static void delete(OrganizedCategory folder, int index)
    {	
	// The Organized object currently being checked.
	Organized currentOrganized;
	// An iterator over the TreeSet of the OrganizedCategory currently being browsed.
	Iterator categoryIter = folder.getIterator();

	for(int iterIndex = 0; categoryIter.hasNext(); iterIndex++)
	    {
		currentOrganized = (Organized) categoryIter.next();
		if(iterIndex == index)
		    {
			categoryIter.remove();
		    }
	    }
    }


    /**
     * Checks if the File object passed to it refers to a JPEG file.
     *
     * @param imageFile The File object to be checked.
     * @return True if the File object refers to a JPEG file, false otherwise.
     */
    public static boolean imageCheck(File imageFile)
    {
	//
	BufferedImage image;

	try
	    {
		image = ImageIO.read(imageFile);

		return(image == null ? false : true);
	    }
	catch(Exception ex)
	    {
		return(false);
	    }
    }
}
