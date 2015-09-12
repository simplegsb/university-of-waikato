package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * Changes the contents of the main display to "0" and clears the center sub display.
 * @author Gary Buyn and Gavin Moore
 */
public class CE extends Functionality
{
    public CE(CalculatorWindow window)
    {
        super(window);
    }

    public void invoke()
    {
        currentDisplay = "0";
        currentSubDisplayC = "";
	mWindow.setDisplay(currentDisplay);
        mWindow.setSubDisplay(1, currentSubDisplayC);
        readyForNum = true;
    }
}
