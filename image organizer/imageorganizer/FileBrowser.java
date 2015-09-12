package imageorganizer;


import java.io.*;


/**
 * Browses a hierachy of File objects.
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public class FileBrowser implements Browser
{
    public FileBrowser(File home)
    {
	browseDir = home;
    }


    /**
     * The directory currently being browsed.
     */
    private File browseDir;


    /**
     * Sets the directory currently being browsed to the parent directory of the current directory.
     */
    public void goToParentFolder()
    {
	// The parent directory of the directory currently being browsed.
	File parentDir = browseDir.getParentFile();

	if(parentDir.isDirectory())
	    {
		browseDir = parentDir;
	    }
	else
	    {
		System.out.println("That was not a valid option, please try again:");
	    }
    }


    /**
     * Sets the directory currently being browsed to the selected sub directory of the current directory.
     *
     * @param index The index of the sub folder to move to in the directory currently being browsed.
     */
    public void goToSubFolder(int index)
    {    
	// An array containing all the File objects in the directory currently being browsed.
	File allFiles[] = browseDir.listFiles();

	if(allFiles[index].isDirectory())
		{
		    browseDir = allFiles[index];
		}
	    else
		{
		    System.out.println("That was not a valid option, please try again:");
		}
    }


    /**
     * Prints a textual representation of the directory currently being browsed.
     */
    public void view()
    {
	// An array containing all the File objects in the directory currently being browsed.
	File allFiles[] = browseDir.listFiles();

	System.out.println("");
	System.out.println(browseDir.getName() + ":");

	if(allFiles != null)
	    {
		// Print all of the files and their corresponding indexes.
		System.out.println("\tFiles:");
		for(int index = 0; index < allFiles.length; index++)
		    {
			if( allFiles[index].isFile())
			    {
				System.out.println("\t\t" + index + ": " + allFiles[index].getName());
			    }
		    }

		// Print all of the folders and their corresponding indexes.
		System.out.println("\tFolders:");
		for(int index = 0; index < allFiles.length; index++)
		    {
			if( allFiles[index].isDirectory())
			    {
				System.out.println("\t\t" + index + ": " + allFiles[index].getName());
			    }
		    }
	    }
	else
	    {
		System.out.println("\tThis folder is empty.");
	    }
    }


    /**
     * Deletes the folder/file found at the index in the directory currently being browsed passed to the
     * method.
     *
     * @param index The index of the folder/file to be deleted in the directory currently being browsed.
     */
    public void delete(int index)
    {	
	// An array containing all the File objects in the directory currently being browsed.
	File allFiles[] = browseDir.listFiles();
	File delFile;

	delFile = allFiles[index];

	if(delFile.isDirectory())
	    {
		deleteFolder(delFile);
	    }
	else if(delFile.isFile())
	    {
		delFile.delete();
	    }
    }


    /**
     * Recursively deletes all contents of a directory and then deletes the directory itself.
     *
     * @param folder The directory to be deleted.
     */
    private void deleteFolder(File folder)
    {	
	// An array containing all the File objects in the directory currently being browsed.
	File allFiles[] = folder.listFiles();

	if(allFiles != null)
		{
		    for(int num = 0; num < allFiles.length; num++)
			{
			    if(allFiles[num].isFile())
				{
				    allFiles[num].delete();
				}
			    else if(allFiles[num].isDirectory())
				{
				    deleteFolder(allFiles[num]);
				}
			}
		}
	folder.delete();
    }


    /**
     * Returns the directory currently being browsed.
     *
     * @return The directory currently being browsed.
     */
    public File getCurrent()
    {
	return(browseDir);
    }


    /**
     * Returns the folder/file at the index passed to the method of the directory currently being browsed.
     *
     * @param index The index of the folder/file to be returned in the directory currently being browsed.
     * @return The File object of the folder/file at the index passed to the method.
     */
    public File get(int index)
    {
	// An array containing all the File objects in the directory currently being browsed.
	File allFiles[] = browseDir.listFiles();
	
	try
	    {
		return(allFiles[index]);
	    }
	catch(Exception ex)
	    {
		System.out.println("That was not a valid option, please try again:");
		return(null);
	    }
    }
}

