import java.io.File;
import javax.swing.filechooser.FileFilter;


////////////////////////////////////////////////////////////////////
////////////////////////// SS FILE FILTER //////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Filters files to only include .ss and directories.
 *
 * @author Sports Simulator Team: ???
 */
public class SSFileFilter extends FileFilter
{
    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f)
    {
	return(f.getName().toLowerCase().endsWith(".ss") || f.isDirectory());
    }
    

    /**
     * The description of this filter.
     */
    public String getDescription()
    {
	return("sports simulator files (*.ss)");
    }
}
