/**
 * Increments/decrements the distance between the Center Of Projection and the Bezier Surface Model.
 * 
 * @author gb21
 */
public class IncrCOPMenuItem extends ReversableMenuItem
{
    /**
     * Creates an instance of IncrCOPMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param reversed True if this menu item is reversed.
     */
    public IncrCOPMenuItem(String name, char selector, boolean reversed)
    {
        super(name, selector, reversed);
    }
    
    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        
        if (isReversed())
        {
            if (graphics.getModel().getCOPDistance() > 100.0f)
            {
                graphics.getModel().setCOPDistance(graphics.getModel().getCOPDistance() - 100.0f);
            }
        }
        else
        {
            graphics.getModel().setCOPDistance(graphics.getModel().getCOPDistance() + 100.0f);
        }
        
        graphics.repaint();
    }
}

