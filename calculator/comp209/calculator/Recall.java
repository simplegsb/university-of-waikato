package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Digit'.
 * Display's the number in memory in the main display.
 * @author Gary Buyn and Gavin Moore
 */
public class Recall extends Digit
{
    
    public Recall(CalculatorWindow window, String digit)
    {
        super(window, digit);
    }
    
    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	currentDisplay = "0";
	mDigit = memory;

        super.invoke();
    }
}
