package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If the main display's current contents is positive to changes it to negative. If it is negative it changes it to positive.
 * @author Gary Buyn and Gavin Moore
 */
public class Negation extends Functionality
{
    
    public Negation(CalculatorWindow window)
    {
	super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
        temp = strToDouble(currentDisplay);
        
        temp *= -1;
        
	currentDisplay = doubleToStr(temp);
        mWindow.setDisplay(currentDisplay);
    }
    
    /**
     * A temporary variable for processing the function when the contents of the calculator's display has been changed to
     * double format.
     */
    double temp;
}
