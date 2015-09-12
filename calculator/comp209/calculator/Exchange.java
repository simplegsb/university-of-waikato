package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Digit'.
 * Swaps the value in the calculator's memory with the main display's current contents.
 * @author Gary Buyn and Gavin Moore
 */
public class Exchange extends Digit
{
    public Exchange(CalculatorWindow window, String digit)
    {
	super(window, digit);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	mDigit = memory;
	memory = currentDisplay;
	currentDisplay = "0";

        super.invoke();

        checkM(mWindow);
    }
}
