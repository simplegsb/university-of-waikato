package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If the center sub display contains "INV" it overwrites the main display's current contents with the inverse log of the
 * main display's current content's. If not it overwrites the main display's current contents with the log of the main
 * display's current content's.
 * @author Gary Buyn and Gavin Moore
 */
public class Log extends Functionality
{
    
    public Log(CalculatorWindow window)
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
                outputAnswer(mWindow, doubleToStr(Math.exp(temp)));
                currentSubDisplayC = "";
                mWindow.setSubDisplay(1, currentSubDisplayC);
            }
        else
            {
                outputAnswer(mWindow, doubleToStr(Math.log(temp)));
            }

	mWindow.setDisplay(currentDisplay);
    }
    
    /**
     * A temporary variable for processing the function when the contents of the calculator's display has been changed to
     * double format.
     */
    double temp;
}
