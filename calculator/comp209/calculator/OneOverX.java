package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * Overwrites the main display's current contents with 1/(the main display's current content's).
 * @author Gary Buyn and Gavin Moore
 */
public class OneOverX extends Functionality
{
    
    public OneOverX(CalculatorWindow window)
    {
	super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	temp = strToDouble(currentDisplay);

        if(temp != 0)
            {
                outputAnswer(mWindow, doubleToStr(1/temp));
            }
        else
            {
		mWindow.setDisplay("Error: x/0");
		currentDisplay = "0";
            }
    }

    /**
     * A temporary variable for processing the function when the contents of the calculator's display has been changed to
     * double format.
     */
    private double temp;
}
