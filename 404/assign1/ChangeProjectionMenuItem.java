/**
 * Sets the projection mode that the Bezier Surface Model will be viewed in.
 * 
 * @author gb21
 */
public class ChangeProjectionMenuItem extends MenuItem
{    
    /**
     * Creates an instance of SetProjectionMenuItem.
     * 
     * @param name The name of this quit menu item.
     * @param selector The key to press to select this quit menu item.
     */
    public ChangeProjectionMenuItem(String name, char selector)
    {
        super(name, selector);
    }
    
    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        
        graphics.getModel().toggleProjectionMode();
        
        graphics.repaint();
    }
}

