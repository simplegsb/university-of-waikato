package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If the center sub display contains "INV" it changes it to "". If not it changes it to "INV".
 * @author Gary Buyn and Gavin Moore
 */
public class Inverse extends Functionality
{
    public Inverse(CalculatorWindow window)
    {
        super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
        if(currentSubDisplayC.compareTo("INV") == 0)
	    {
		currentSubDisplayC = "";
	    }
	else
	    {
		currentSubDisplayC = "INV";
	    }
	mWindow.setSubDisplay(1, currentSubDisplayC);
    }
}
