package imageorganizer;


import java.io.*;
import java.util.*;


/**
 * Implements various functionalities to organize and customize images in a virtual folder hierachy.
 * Operations can be implemented through the 7 menus available to the user.
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public class ImageOrganizer
{
    /**
     * The top OrganizedCategory of the virtual folder hierachy.
     */
    private static OrganizedCategory allOrganized;


    ////////////////////////////////// MAIN
    /**
     * Prints the 'Main' menu to the screen. Awaits the users option and processes it. Loops until the user
     * chooses to quit.
     */
    public static void main(String args[])
    {
	// True if the 'Quit organizer.' option has been selected.
	boolean quit = false;
	// The resulting category of a search of the whole virtual folder hierachy.
	OrganizedCategory searchCategory;

	allOrganized = new OrganizedCategory("Main");

	while(!quit)
	    {
		System.out.println("");
		System.out.println("---- IMAGE ORGANIZER ----");
		System.out.println("");
		System.out.println(">>Main:");
		System.out.println("");
		System.out.println("1: Browse organized images...");
		System.out.println("2: Search organized images...");
		System.out.println("3: Look for new images...");
		System.out.println("4: Quit organizer.");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			browseOrganizedMenu(allOrganized);
			break;
		    case(2):
			System.out.println("");
			System.out.print("Please enter the name of the category/image you wish to search for: ");
			if((searchCategory = OrganizedCategory.search(allOrganized, getUserText())) != null)
			    {
				browseOrganizedMenu(searchCategory);
			    }
			else
			    {
				System.out.println("Category/image not found.");
			    }
			break;
		    case(3):
			browseHomeMenu();
			break;
		    case(4):
			quit = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }


    ////////////////////////////////// BROWSE ORGANIZED IMAGES
    /**
     * Prints the contents of the folder currently being browsed and the 'Browse organized images' menu to
     * the screen. Awaits the users option and processes it. Loops until the user chooses to go back to the
     * 'Main' menu.
     *
     * @param folder The OrganizedCategory in the virtual folder hierachy that the browser will begin
     * browsing in.
     */
    private static void browseOrganizedMenu(OrganizedCategory folder)
    {
	// True if the 'Back to 'Main' menu...' option has been selected.
	boolean backToMain = false;
	// A browser for files beginning at the user's home directory.
	ImageBrowser browser = new ImageBrowser(folder);
	// A temporary variable for holding the option the user chooses.
	int userOption;
	// A temporary variable for holding the text the user enters.
	String userText;

	while(!backToMain)
	    {
		browser.view();

		System.out.println("");
		System.out.println(">>Main>Browse organized images:");
		System.out.println("");
		System.out.println("1: Open image...");
		System.out.println("2: Open sub category.");
		System.out.println("3: Create new sub category.");
		System.out.println("4: Open parent category.");
		System.out.println("5: Move category/image...");
		System.out.println("6: Remove category/image.");
		System.out.println("7: Rename category/image.");
		System.out.println("8: Back to 'Main' menu...");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			System.out.println("");
			System.out.print("Please enter the number of the image you wish to open: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				if(browser.get(userOption).isImage())
				    {
					openImageMenu((OrganizedImage) browser.get(userOption), userOption);
				    }
				else
				    {
					System.out.println("That was not a valid option, please try again:");
				    }
			    }
			break;
		    case(2):
			System.out.println("");
			System.out.print("Please enter the number of the sub category you wish to open: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				browser.goToSubFolder(userOption);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			break;
		    case(3):
			System.out.println("");
			System.out.print("Please enter the name of the new category you wish to add: ");
			browser.getCurrent().addEmptyCategory(getUserText());
			break;
		    case(4):
			browser.goToParentFolder();
			break;
		    case(5):
			System.out.println("");
			System.out.print("Please enter the number of the category/image you wish to move: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				moveMenu(browser.getCurrent(), userOption);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			break;
		    case(6):
			System.out.println("");
			System.out.print("Please enter the number of the category/image you wish to remove: ");
			browser.delete(getUserOption());
			System.out.println("Removed!!");
			break;
		    case(7):
			System.out.println("");
			System.out.print("Please enter the number of the category/image you wish to rename: ");
			userOption = getUserOption();
			System.out.print("Please enter the new name (do not include .jpg): ");
			userText = getUserText();
			if(browser.get(userOption) != null)
			    {
				Organized.rename(OrganizedCategory.get(browser.getCurrent(), userOption), userOption, userText);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			System.out.println("Renamed!!");
			break;
		    case(8):
			backToMain = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }


    ////////////////////////////////// OPEN IMAGE
    /**
     * Prints the 'Open image' menu to the screen. Awaits the users option and processes it. Loops until the
     * user chooses to go back to the 'Browse organized images' menu.
     *
     * @param image The image that the user wishes to perform operations on. 
     * @param index The index of the selected image in the TreeSet of its parent folder.
     */
    private static void openImageMenu(OrganizedImage image, int index)
    {
	// True if the 'Back to 'Browse organized images' menu...' option has been selected.
	boolean backToBrowseOrganized = false;

	while(!backToBrowseOrganized)
	    {
		System.out.println("");
		System.out.println(">>Main>Browse organized images>Open image:");
		System.out.println("");
		System.out.println("1: View image.");
		System.out.println("2: Image options...");
		System.out.println("3: Back to 'Browse organized images' menu...");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			image.view();
			break;
		    case(2):
			imageOptionsMenu(image, index);
			break;
		    case(3):
			backToBrowseOrganized = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }


    ////////////////////////////////// IMAGE OPTIONS
    /**
     * Prints the 'Image options' menu to the screen. Awaits the users option and processes it.
     * Loops until the  user chooses to go back to the 'Open image' menu.
     *
     * @param image The image that the user wishes to perform operations on. 
     * @param index The index of the selected image in the TreeSet of its parent folder.
     */
    private static void imageOptionsMenu(OrganizedImage image, int index)
    {
	// True if the 'Back to 'Open image' menu...' option has been selected.
	boolean backToBrowseOrganized = false;
	// A temporary variable for holding the text the user enters.
	String userText;

	while(!backToBrowseOrganized)
	    {
		System.out.println("");
		System.out.println(">>Main>Browse organized images>Open image>Image options:");
		System.out.println("");
		System.out.println("1: View date last modified.");
		System.out.println("2: View description.");
		System.out.println("3: Write new description.");
		System.out.println("4: Back to 'Open image' menu...");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			Date lastModified = new Date(image.getFile().lastModified());
			Calendar c = Calendar.getInstance();
			c.setTime(lastModified);
			System.out.println("");
			System.out.println("This file was last modified: " + c.get(Calendar.DAY_OF_MONTH) +
					   "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
			break;
		    case(2):
			System.out.println("");
			System.out.println("Description: ");
			System.out.println(image.getDesc());

			break;
		    case(3):
			System.out.println("");
			System.out.println("Write your description: (press enter to finish)");
			image.setDesc(getUserText());
			break;
		    case(4):
			backToBrowseOrganized = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }


    ////////////////////////////////// MOVE CATEGORY/IMAGE
    /**
     * Prints the 'Move category/image' menu to the screen. Awaits the users option and processes it.
     * Loops until the  user chooses to go back to the 'Browse organized images' menu.
     *
     * @param oldParent The parent category of the image to be moved in its original location.
     * @param index The index of the image to be moved in the TreeSet of its parent folder.
     */
    private static void moveMenu(OrganizedCategory oldParent, int index)
    {
	// True if the 'Cancel.' option has been selected or the category/image has been moved.
	boolean exit = false;
	// A browser for files beginning at the user's home directory.
	ImageBrowser browser = new ImageBrowser(allOrganized);
	// A temporary variable for holding the option the user chooses.
	int userOption;

	while(!exit)
	    {
		System.out.println("");
		System.out.println("Where would you like to move this category/image to?");

		browser.view();

		System.out.println("");
		System.out.println(">>Main>Browse organized images>Move category/image:");
		System.out.println("");
		System.out.println("1: Move category/image here.");
		System.out.println("2: Open sub category.");
		System.out.println("3: Open parent category.");
		System.out.println("4: Cancel.");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			// Sets the parent of the image being moved to the OrganizedCategory it will be
			// moved to.
			OrganizedCategory.get(oldParent, index).setParent(browser.getCurrent());
			// Creates a copy of the OrganizedImage in the new location.
			browser.getCurrent().getCategory().add(OrganizedCategory.get(oldParent, index));
			// Deletes the OrganizedImage in the original location.
			OrganizedCategory.delete(oldParent, index);
			System.out.println("");
			System.out.println("Moved!!");
			exit = true;
			break;
		    case(2):
			System.out.println("");
			System.out.print("Please enter the number of the sub category you wish to open: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				browser.goToSubFolder(userOption);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			break;
		    case(3):
			browser.goToParentFolder();
			break;
		    case(4):
			exit = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }


    ////////////////////////////////// LOOK FOR NEW IMAGES
    /**
     * Prints the 'Look for new images' menu to the screen. Awaits the users option and processes it.
     * Loops until the  user chooses to go back to the 'Main' menu.
     */
    private static void browseHomeMenu()
    {
	// True if the 'Back to 'Main' menu...' option has been selected.
	boolean backToMain = false;
	// A browser for files beginning at the user's home directory.
	FileBrowser browser = new FileBrowser(new File(System.getProperty("user.home")));
	// A temporary variable for holding the option the user chooses.
	int userOption;

	while(!backToMain)
	    {
		browser.view();
		
		System.out.println("");
		System.out.println(">>Main>Look for new images:");
		System.out.println("");
		System.out.println("1: Add folder/image.");
		System.out.println("2: Delete folder/image.");
		System.out.println("3: Open sub folder.");
		System.out.println("4: Open parent folder.");
		System.out.println("5: Back to 'Main' menu...");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			System.out.println("");
			System.out.print("Please enter the number of the folder/image you wish to open: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				addToOrganizedMenu(browser.get(userOption));
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			break;
		    case(2):
			System.out.println("");
			System.out.print("Please enter the number of the image file you wish to delete: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				browser.delete(userOption);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			System.out.println("");
			System.out.println("Deleted!!");
			break;
		    case(3):
			System.out.println("");
			System.out.print("Please enter the number of the sub folder you wish to open: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				browser.goToSubFolder(userOption);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			break;
		    case(4):
			browser.goToParentFolder();
			break;
		    case(5):
			backToMain = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }


    ////////////////////////////////// ADD CATEGORY/IMAGE
    /**
     * Prints the 'Add category/image' menu to the screen. Awaits the users option and processes it.
     * Loops until the  user chooses to go back to the '' menu.
     *
     * @param newOrganized The folder/file attempting to be added.
     */
    private static void addToOrganizedMenu(File newOrganized)
    {
	// True if the 'Cancel.' option has been selected or the category/image has been added.
	boolean exit = false;
	// A browser for Organized objects beginning at the 'Main' category.
	ImageBrowser browser = new ImageBrowser(allOrganized);
	// A temporary variable for holding the option the user chooses.
	int userOption;
	
	while(!exit)
	    {
		System.out.println("");
		System.out.println("Where would you like to add this category/image?");

		browser.view();
		
		System.out.println("");
		System.out.println(">>Main>Look for new images>Open image>Add image:");
		System.out.println("");
		System.out.println("1: Add here.");
		System.out.println("2: Open sub category.");
		System.out.println("3: Open parent category.");
		System.out.println("4: Cancel.");
		System.out.println("");
		System.out.print("\t>> ");
		
		switch(getUserOption())
		    {
		    case(1):
			OrganizedCategory.add(browser.getCurrent(), newOrganized);
			System.out.println("");
			System.out.println("Added!!");
			exit = true;
			break;
		    case(2):
			System.out.println("");
			System.out.print("Please enter the number of the sub category you wish to open: ");
			userOption = getUserOption();
			if(browser.get(userOption) != null)
			    {
				browser.goToSubFolder(userOption);
			    }
			else
			    {
				System.out.println("That was not a valid option, please try again:");
			    }
			break;
		    case(3):
			browser.goToParentFolder();
			break;
		    case(4):
			exit = true;
			break;
		    default:
			System.out.println("That was not a valid option, please try again:");
		    }
	    }
    }
    
    /**
     * Takes the user's option from the command prompt and returns it as an integer.
     *
     * @return The users input parsed to an integer or -1 if it cannot be parsed or read.
     */
    private static int getUserOption()
    {
	// Reads the users input.
	BufferedReader optionReader = new BufferedReader(new InputStreamReader(System.in));

	try
	    {
		return(Integer.parseInt(optionReader.readLine()));
	    }
	catch(Exception ex)
	    {
		return(-1);
	    }
    }

    /**
     * Takes the user's text input from the command prompt and returns it.
     *
     * @return The users text input as a String or null if it cannot be read.
     */
    private static String getUserText()
    {
	// Reads the users input.
	BufferedReader optionReader = new BufferedReader(new InputStreamReader(System.in));

	try
	    {
		return(optionReader.readLine());
	    }
	catch(Exception ex)
	    {
		return(null);
	    }
    }
}

    














