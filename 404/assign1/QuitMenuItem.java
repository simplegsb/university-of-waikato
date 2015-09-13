/**
 * Quits the application.
 * 
 * @author gb21
 */
public class QuitMenuItem extends MenuItem
{
    /**
     * Creates an instance of QuitMenuItem.
     * 
     * @param name The name of this quit menu item.
     * @param selector The key to press to select this quit menu item.
     */
    public QuitMenuItem(String name, char selector)
    {
        super(name, selector);
    }
    
    public void performAction()
    {
        System.exit(0);
    }
}

