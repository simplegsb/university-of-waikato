package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If the memory contains a "0" it changes the left hand sub display to "". Otherwise it changes it to "M".
 * @author Gary Buyn and Gavin Moore
 */
public class Memory extends Functionality
{
    
    public Memory(CalculatorWindow window)
    {
        super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	if(memory.compareTo("0") == 0)
        {
            currentSubDisplayL = "";
        }
        else
        {
            currentSubDisplayL = "M";
        }
        mWindow.setSubDisplay(0, currentSubDisplayL);
        readyForNum = true;
    }
}
