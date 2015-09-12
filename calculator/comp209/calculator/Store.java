package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Memory'.
 * Sets the memory to the main display's current contents.
 * @author Gary Buyn and Gavin Moore
 */
public class Store extends Memory
{
    
    public Store(CalculatorWindow window)
    {
        super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	memory = currentDisplay;
        super.invoke();
    }
}
