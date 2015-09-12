package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If the digit being entered is the first digit i.e. it overwrites any previous contents in the main display then it adds
 * the contents of the right hand sub display to the 'equation' ListArray and clears the right hand sub display. If the
 * digit being entered is not the first it just concatenates the digit to the main display's current contents.
 * @author Gary Buyn and Gavin Moore
 */
public class Digit extends Functionality
{
    public Digit(CalculatorWindow window, String digit)
    {
        super(window);
        mDigit = digit;
    }
    
    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	// Checks if length is too great or if the display is ready to be overwritten
        if(currentDisplay.length() < 18 || readyForNum == true)
	{
	    // Checks if the display is ready to be overwritten or if digit entered is "0"
		if(readyForNum == true || currentDisplay.compareTo("0") == 0)
		    {

                        if(currentSubDisplayR != "")
                            {
                                equation.add(currentSubDisplayR);
                                currentSubDisplayR = "";
                                mWindow.setSubDisplay(2, currentSubDisplayR);
                            }

                        readyForNum = false;
                        currentDisplay = mDigit;
			mWindow.setDisplay(mDigit);

		    }
		// Prevents more than one decimal from being entered
		else if(!(currentDisplay.indexOf(".") >= 0 && mDigit.compareTo(".") == 0))
		    {
			currentDisplay = currentDisplay + mDigit;
			mWindow.setDisplay(currentDisplay);
		    }

		lastElementBracket = false;
	    }
    }
    
    /**
     * The 'digit' that has just been pressed.
     */
    public String mDigit;
}
