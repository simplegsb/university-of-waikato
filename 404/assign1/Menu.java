import java.util.ArrayList;
import java.util.List;

/**
 * A menu that can contain items that implement the <code>MenuElement</code> interface. This menu also has a name to
 * identify it and a character that can be pressed to select it.
 * 
 * @author gb21
 */
public class Menu implements MenuElement
{
    /**
     * The items this menu consists of.
     */
    private List items = new ArrayList();

    /**
     * The name of this menu.
     */
    private String name = "Un-named Menu";

    /**
     * The key to press to select this menu.
     */
    private char selector = 0;
    
    /**
     * Creates an instance of Menu.
     * 
     * @param name The name of this menu.
     * @param selector The key to press to select this menu.
     */
    public Menu(String name, char selector)
    {
        this.name = name;
        this.selector = selector;
    }
    
    /**
     * Adds <code>item</code> to this menu.
     * 
     * @param item The item being added to this menu.
     */
    public void addItem(MenuElement item)
    {
        items.add(item);
    }
    
    /**
     * Returns the items this menu consists of.
     * 
     * @return The items this menu consists of.
     */
    public List getItems()
    {
        return (items);
    }
    
    public String getName()
    {
        return (name);
    }
    
    public char getSelector()
    {
        return (selector);
    }
    
    public String toString()
    {
        return (selector + " -> " + name);
    }
}

