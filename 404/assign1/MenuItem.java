/**
 * An item designed to be held within a <code>Menu</code>.
 * 
 * @author gb21
 */
public abstract class MenuItem implements MenuElement
{
    /**
     * A group of auxilary objects. This group is provided so that subclasses that are all being used to alter a
     * single object do not have to maintain seperate references to it. If object is not present menu items should
     * make no change or throw an exception.
     */
    public static Vector auxiliaryObjects = new Vector();
    
    /**
     * The name of this menu item.
     */
    private String name = "Un-named MenuItem";
    
    /**
     * The key to press to select this menu item.
     */
    private char selector = 0;
    
    /**
     * Performs the action this menu item represents.
     */
    public abstract void performAction();
    
    /**
     * Creates an instance of MenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     */
    public MenuItem(String name, char selector)
    {
        this.name = name;
        this.selector = selector;
    }
    
    public String toString()
    {
        return (selector + " -> " + name);
    }
    
    public String getName()
    {
        return (name);
    }
    
    public char getSelector()
    {
        return (selector);
    }
}

