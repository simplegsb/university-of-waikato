/**
 * Increments/decrements the position of the selected point on the Bezier Surface Model's control point grid on a
 * specified axis.
 * 
 * @author gb21
 */
public class IncrAxisMenuItem extends ReversableMenuItem
{
    /**
     * The axis to increment/decrement.
     */
    private int axis = X;

    /**
     * The x axis. (Use in constructor).
     */
    public static final int X = 0;

    /**
     * The y axis. (Use in constructor).
     */
    public static final int Y = 1;

    /**
     * The z axis. (Use in constructor).
     */
    public static final int Z = 2;

    /**
     * Creates an instance of IncrAxisMenuItem.
     * 
     * @param name The name of this menu item.
     * @param selector The key to press to select this menu item.
     * @param reversed True if this menu item is reversed.
     * @param axis The axis to increment/decrement.
     */
    public IncrAxisMenuItem(String name, char selector, boolean reversed, int axis)
    {
        super(name, selector, reversed);

        this.axis = axis;
    }

    public void performAction()
    {
        BezSurfGraphics graphics = (BezSurfGraphics) auxiliaryObjects.get(0);
        float distance = 10.0f;

        if (isReversed())
        {
            distance *= -1;
        }

        if (axis == X)
        {
            graphics.getModel().translateControlPoint(new Vectorf4(distance, 0.0f, 0.0f, 1.0f),
                    graphics.getModel().getSelectedPoint());
        }
        else if (axis == Y)
        {
            graphics.getModel().translateControlPoint(new Vectorf4(0.0f, distance, 0.0f, 1.0f),
                    graphics.getModel().getSelectedPoint());
        }
        else if (axis == Z)
        {
            graphics.getModel().translateControlPoint(new Vectorf4(0.0f, 0.0f, distance, 1.0f),
                    graphics.getModel().getSelectedPoint());
        }

        graphics.repaint();
    }
}

