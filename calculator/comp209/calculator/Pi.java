package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Digit'.
 * Display's the closest double to pi in the main display.
 * @author Gary Buyn and Gavin Moore
 */
public class Pi extends Digit
{
    
    public Pi(CalculatorWindow window, String digit)
    {
	super(window, digit);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	currentDisplay = "0";
	mDigit = doubleToStr(Math.PI);

	super.invoke();
    }
}
