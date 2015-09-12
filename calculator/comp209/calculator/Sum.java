package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Memory'.
 * Sets the memory to (the current value in memory)+(main display's current contents).
 * @author Gary Buyn and Gavin Moore
 */
public class Sum extends Memory
{
    
    public Sum(CalculatorWindow window)
    {
	super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	sum = strToDouble(currentDisplay) + strToDouble(memory);
        memory = doubleToStr(sum);
        super.invoke();
    }
    
    /**
     * The sum of (the current value in memory)+(main display's current contents).
     */
    private double sum;
}
