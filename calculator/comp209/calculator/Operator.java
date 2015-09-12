package comp209.calculator;

import comp209.calculatorwindow.*;

/**
 * Is an extending subclass of 'Functionality'.
 * If a new number has been entered i.e. a number has been entered since the last operator was added to the 'equation'
 * ListArray, it will add the number to the 'equation' ListArray. If this operator is already in the right hand sub display
 * it will remove it, otherwise it will overwrite whatever is there with this operator.
 * @author Gary Buyn and Gavin Moore
 */
public class Operator extends Functionality
{
    public Operator(CalculatorWindow window, String operator)
    {
        super(window);
        mOperator = operator;
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
        if(readyForNum == false || equation.isEmpty())
            {
                equation.add(currentDisplay);
                readyForNum = true;
            }
        
        if(currentSubDisplayR.compareTo(mOperator) == 0)
	    {
		currentSubDisplayR = "";
	    }
	else
	    {
		currentSubDisplayR = mOperator;
		lastElementBracket = false;
	    }
	mWindow.setSubDisplay(2, currentSubDisplayR);
    }

    /**
     * The operator this button is referring to.
     */
    private String mOperator;
}
