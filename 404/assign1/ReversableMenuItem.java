/**
 * A menu item that can be reversed. A reversed item should create the inverse action of a non-reversed item.
 * 
 * @author gb21
 */
public abstract class ReversableMenuItem extends MenuItem
{    
    /**
     * True if this menu item is reversed.
     */
    private boolean reversed = false;
    
    /**
     * Creates an instance of ReversableMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param reversed True if this menu item is reversed.
     */
    public ReversableMenuItem(String name, char selector, boolean reversed)
    {
        super(name, selector);
        
        this.reversed = reversed;
    }
    
    /**
     * Returns true if this menu item is reversed, false otherwise.
     * 
     * @return True if this menu item is reversed, false otherwise.
     */
    public boolean isReversed()
    {
        return (reversed);
    }
    
    /**
     * Sets the reversed status of this menu item.
     * 
     * @param newReversed The new reversed status of this menu item.
     */
    public void setReversed(boolean newReversed)
    {
        reversed = newReversed;
    }
}

