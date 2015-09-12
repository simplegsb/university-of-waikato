import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


////////////////////////////////////////////////////////////////////
/////////////////////////// AUDIO MANAGER //////////////////////////
////////////////////////////////////////////////////////////////////


/**
 * Has the ability to load, play and stop an audio file of .wav or .au format.
 *
 * @author Sports Simulator Team: ???
 */
public class AudioManager
{
    /**
     * Creates an instance of an AudioManager.
     */
    public AudioManager() {}


    /**
     * The clip this AudioManager is managing.
     */
    private AudioClip audioClip;


    /**
     * True if sound has been enabled.
     */
    private boolean enabled;
	

    /**
     * Returns true if sound has been enabled, false otherwise.
     *
     * @return True if sound has been enabled, false otherwise.
     */
    public boolean setEnabled()
    {
	return(enabled);
    }


    /**
     * Sets enabled to the boolean value supplied.
     *
     * @param b A boolean value to set enabled to.
     */
    public void setEnabled(boolean b)
    {
	enabled = b;
    }
	

    /**
     * Starts playing an audio clip if one has been loaded and sound is enabled.
     */
    public void play()
    {
	if(audioClip != null && enabled)
	    {
		audioClip.play();
	    }
    }
    
    
    /**
     * Stops playing an audio clip if one has been loaded and sound is enabled.
     */
    public void stop()
    {
	if(audioClip != null && enabled)
	    {
		audioClip.stop();
	    }
    }


    /**
     * Allows the user to choose an audio file (.wav or .au) to load and attaches that file to this AudioManager.
     */
    public void loadAudio()
    {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Choose an audio file...");

	// Set filter for .wav/.au source files and open file chooser.
	fileChooser.addChoosableFileFilter(new AudioFileFilter());
	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	int result = fileChooser.showOpenDialog(null);

	// // If the user clicked the Cancel button on the dialog, return.
	if(result == JFileChooser.CANCEL_OPTION)
	    {
		return;
	    }

	// Get selected file.
	File file = fileChooser.getSelectedFile();

	// Open file.
	try
	    {
		audioClip = Applet.newAudioClip(file.toURL());
	    }
	catch(IOException e)
	    {
		JOptionPane.showMessageDialog(null, "Cannot read file", "Error loading audio", JOptionPane.ERROR_MESSAGE);
	    }
    }	
}
