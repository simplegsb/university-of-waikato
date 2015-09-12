import java.io.File;
import javax.swing.filechooser.FileFilter;


////////////////////////////////////////////////////////////////////
///////////////////////// AUDIO FILE FILTER ////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Filters files to only include .wav, .au and directories.
 *
 * @author Sports Simulator Team: ???
 */
public class AudioFileFilter extends FileFilter
{
    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f)
    {
	return(f.getName().toLowerCase().endsWith(".wav") || f.getName().toLowerCase().endsWith(".au") || f.isDirectory());
    }
    

    /**
     * The description of this filter.
     */
    public String getDescription()
    {
	return("audio clip files ( *.wav, *.au )");
    }
}
