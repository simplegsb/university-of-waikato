/**
 * Increments/decrements the amount of patches in the Bezier Surface Model's surface grid.
 * 
 * @author gb21
 */
public class IncrMeshMenuItem extends ReversableMenuItem
{
    /**
     * Creates an instance of IncrMeshMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param reversed True if this menu item is reversed.
     */
    public IncrMeshMenuItem(String name, char selector, boolean reversed)
    {
        super(name, selector, reversed);
    }
    
    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        
        if (isReversed())
        {
            if (graphics.getModel().getGridSize() > 5)
            {
                graphics.getModel().setGridSize(graphics.getModel().getGridSize() - 1);
            }
        }
        else
        {
            graphics.getModel().setGridSize(graphics.getModel().getGridSize() + 1);
        }
        
        graphics.repaint();
    }
}

