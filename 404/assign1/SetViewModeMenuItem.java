/**
 * Sets the rendering mode that the Bezier Surface Model will be viewed in.
 * 
 * @author gb21
 */
public class SetViewModeMenuItem extends MenuItem
{
    /**
     * The rendering mode that the Bezier Surface Model will be viewed in.
     */
    private int renderingMode = BezSurfModel.CONTROL_POINT;
    
    /**
     * Creates an instance of SetViewModeMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param renderingMode The rendering mode that the Bezier Surface Model will be viewed in.
     */
    public SetViewModeMenuItem(String name, char selector, int renderingMode)
    {
        super(name, selector);
        
        this.renderingMode = renderingMode;
    }
    
    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        
        if (renderingMode == BezSurfModel.CONTROL_POINT)
        {
            graphics.getModel().setRenderingMode(BezSurfModel.CONTROL_POINT);
        }
        else if (renderingMode == GridRasterizer.WIRE_FRAME)
        {
            graphics.getModel().setRenderingMode(GridRasterizer.WIRE_FRAME);
        }
        else if (renderingMode == GridRasterizer.FLAT)
        {
            graphics.getModel().setRenderingMode(GridRasterizer.FLAT);
        }
        else if(renderingMode == GridRasterizer.GOURAUD)
        {
            graphics.getModel().setRenderingMode(GridRasterizer.GOURAUD);
        }
        
        graphics.repaint();
    }
}

