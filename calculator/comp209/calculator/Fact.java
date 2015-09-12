package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * Overwrites the main display's current contents with the factorial of the main display's current content's.
 * @author Gary Buyn and Gavin Moore
 */
public class Fact extends Functionality
{
    
    public Fact(CalculatorWindow window)
    {
	super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	currentFactor = strToDouble(currentDisplay);
	result = 1;
	negative = false;

        if(currentFactor < 0)
	    {
		negative = true;
		currentFactor *= -1;
	    }
	for(;currentFactor > 0; currentFactor--)
	    {
		result = result * currentFactor;
	    }
	if(negative == true)
	    {
		result *= -1;
	    }

	outputAnswer(mWindow, doubleToStr(result));
    }

    /**
     * The number the number having the factorial function applied to it is currently being multiplied by.
     */
    private double currentFactor;
    
    /**
     * The result of the factorial.
     */
    private double result;

    /**
     * True if the number having the factorial function applied to it is negative.
     */
    private boolean negative;
}
