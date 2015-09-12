package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If the center sub display contains "INV" it overwrites the main display's current contents with the arc cosine of the
 * main display's current content's. If not it overwrites the main display's current contents with the cosine of the main
 * display's current content's.
 * @author Gary Buyn and Gavin Moore
 */
public class Cos extends Functionality
{
    public Cos(CalculatorWindow window)
    {
	super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	temp = strToDouble(currentDisplay);
	// Checks for the inverse function
        if(currentSubDisplayC.compareTo("INV") == 0)
        {
            outputAnswer(mWindow, doubleToStr(Math.acos(temp)));
            currentSubDisplayC = "";
            mWindow.setSubDisplay(1, currentSubDisplayC);
        }
        else
        {
            outputAnswer(mWindow, doubleToStr(Math.cos(temp)));
        }
	mWindow.setDisplay(currentDisplay);
    }

    /**
     * A temporary variable for processing the function when the contents of the calculator's display has been changed to
     * double format.
     */
    private double temp;
}
