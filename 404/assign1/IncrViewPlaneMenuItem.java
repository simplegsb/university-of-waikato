/**
 * Increments/decrements the size of the View Plane.
 * 
 * @author gb21
 */
public class IncrViewPlaneMenuItem extends ReversableMenuItem
{
    /**
     * Creates an instance of IncrViewPlaneMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param reversed True if this menu item is reversed.
     */
    public IncrViewPlaneMenuItem(String name, char selector, boolean reversed)
    {
        super(name, selector, reversed);
    }
    
    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        
        if (isReversed())
        {
            if (graphics.getModel().getViewPlaneSize() > 0.2f)
            {
                graphics.getModel().setViewPlaneSize(graphics.getModel().getViewPlaneSize() - 0.1f);
            }
        }
        else
        {
            graphics.getModel().setViewPlaneSize(graphics.getModel().getViewPlaneSize() + 0.1f);
        }
        
        graphics.repaint();
    }
}

