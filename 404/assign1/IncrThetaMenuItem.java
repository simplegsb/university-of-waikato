/**
 * Increments/decrements the angle of theta that the Center Of Projection is located at.
 * 
 * @author gb21
 */
public class IncrThetaMenuItem extends ReversableMenuItem
{
    /**
     * Creates an instance of IncrThetaMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param reversed True if this menu item is reversed.
     */
    public IncrThetaMenuItem(String name, char selector, boolean reversed)
    {
        super(name, selector, reversed);
    }
    
    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        
        if (isReversed())
        {
            graphics.getModel().rotateTheta((float) Math.PI / 20.0f * -1);
        }
        else
        {
            graphics.getModel().rotateTheta((float) Math.PI / 20.0f);
        }
        
        graphics.repaint();
    }
}

