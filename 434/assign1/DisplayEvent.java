import java.util.EventObject;

/**
 * An event that represents a new string to display.
 * 
 * @author gb21
 */
public class DisplayEvent extends EventObject
{
    /**
     * The string to display.
     */
    private String displayString;
    
    /**
     * Represents that the string in this event is an 'information' display element.
     */
    public static final int INFORMATION = 1;
    
    private static final long serialVersionUID = 0;
    
    /**
     * Represents that the string in this event is a 'title' display element.
     */
    public static final int TITLE = 0;
    
    /**
     * The type of display element this string is to be used for.
     */
    private int type;
    
    /**
     * Creates an instance of DisplayEvent.
     * 
     * @param source The object that created this event.
     * @param displayString The string to display.
     * @param type The type of display element this string is to be used for.
     */
    public DisplayEvent(Object source, String displayString, int type)
    {
        super(source);
        
        this.displayString = displayString;
        this.type = type;
    }
    
    /**
     * Returns the string to display.
     * 
     * @return The string to display.
     */
    public String getDisplayString()
    {
        return (displayString);
    }
    
    /**
     * Returns the type of display element this string is to be used for.
     * 
     * @return The type of display element this string is to be used for.
     */
    public int getType()
    {
        return (type);
    }
}
