package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of CE.
 * Changes the contents of the main display to "0", clears the center and right hand sub displays and resets the equation.
 * @author Gary Buyn and Gavin Moore
 */
public class AC extends CE
{
    public AC(CalculatorWindow window)
    {
        super(window);
    }
    
    public void invoke()
    {
        super.invoke();
        equation.clear();
	currentSubDisplayR = "";
        mWindow.setSubDisplay(2, currentSubDisplayR);
	lastElementBracket = false;
    }
}
