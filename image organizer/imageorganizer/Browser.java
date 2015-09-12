package imageorganizer;


/**
 * This interface is designed to be implemented so that the constructor takes a perameter of a type of
 * folder and then uses these methods to move from folder to folder and carry out operations on these
 * folders and their contents.
 *
 * @author Gary Buyn (0223433) and Gavin Moore (0222689)
 */
public interface Browser
{
    /**
     * Sets the folder currently being browsed to the parent folder of the current folder.
     */
    public void goToParentFolder();


    /**
     * Sets the folder currently being browsed to the selected sub folder of the current folder.
     */
    public void goToSubFolder(int index);
    

    /**
     * Prints a textual representation of the folder currently being browsed.
     */
    public void view();


    /**
     * Deletes an element within the folder currently being browsed.
     */
    public void delete(int index);
}
