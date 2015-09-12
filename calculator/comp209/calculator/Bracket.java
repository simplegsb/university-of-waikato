package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * Adds brackets to the list as well as any necessary operators or digits
 * @author Gary Buyn and Gavin Moore
 */
public class Bracket extends Functionality
{
    public Bracket(CalculatorWindow window, String thisBracket)
    {
        super(window);
	mThisBracket = thisBracket;
    }   
    
    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	if(readyForNum == true)
	    {
		if(currentSubDisplayR != "")
		    {
			equation.add(currentSubDisplayR);
			currentSubDisplayR = "";
			mWindow.setSubDisplay(2, currentSubDisplayR);
		    }
	    }
	else if((readyForNum == false && (lastElementBracket != true) && !equation.isEmpty())
		|| (equation.isEmpty() && currentDisplay.compareTo("0") != 0))
            {
                equation.add(currentDisplay);
		readyForNum = true;
            }

	equation.add(mThisBracket);

	lastElementBracket = true;
    }

    /**
     * The bracket this button is associated with.
     */
    private String mThisBracket;
}
