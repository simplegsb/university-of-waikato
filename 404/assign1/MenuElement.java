/**
 * An element that can be contained as an item within a <code>Menu</code>.
 * 
 * @author gb21
 */
public interface MenuElement
{
    /**
     * Returns the name of this menu element.
     * 
     * @return The name of this menu element.
     */
    public String getName();
    
    /**
     * Returns the key to press to select this menu element.
     * 
     * @return The key to press to select this menu element.
     */
    public char getSelector();
}

