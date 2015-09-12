import java.io.Serializable;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A general purpose bean used to display 2 strings in a GUI. The first is a title and the second is information.
 * 
 * @author gb21
 */
public class Displayer extends JPanel implements DisplayListener, Serializable
{
    /**
     * The information this bean displays.
     */
    private JLabel information = new JLabel("Information goes here.");
    
    private static final long serialVersionUID = 0;
    
    /**
     * The title this bean displays.
     */
    private JLabel title = new JLabel("Title goes here.");
    
    /**
     * Creates an instance of DisplayerBean.
     */
    public Displayer()
    {
        add(title);
        add(information, "South");
    }
    
    /**
     * Returns the information this bean displays.
     * 
     * @return The information this bean displays.
     */
    public String getInformation()
    {
        return (information.getText());
    }
    
    /**
     * Returns the title this bean displays.
     * 
     * @return The title this bean displays.
     */
    public String getTitle()
    {
        return (title.getText());
    }
    
    /**
     * Updates this bean to display the string given in <code>event</code> in its respective position i.e. title or
     * information label.
     */
    public void processDisplayEvent(DisplayEvent event)
    {
        if (event.getType() == DisplayEvent.INFORMATION)
        {
            information.setText(event.getDisplayString());
        }
        else if (event.getType() == DisplayEvent.TITLE)
        {
            title.setText(event.getDisplayString());
        }
        
        //repaint();
    }
    
    /**
     * Sets the information this bean displays.
     * 
     * @param newInformation The new information for this bean to display.
     */
    public void setInformation(String newInformation)
    {
        information.setText(newInformation);
    }
    
    /**
     * Sets the title this bean displays.
     * 
     * @param newTitle The new title for this bean to display.
     */
    public void setTitle(String newTitle)
    {
        title.setText(newTitle);
    }
}
