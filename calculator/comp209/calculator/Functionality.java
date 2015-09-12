package comp209.calculator;

import comp209.calculatorwindow.*;
import java.util.*;

/**
 * The class which all button functionalities are subclasses of. Contains all the static methods and data members needed by numerous functionalities.
 * @author Gary Buyn and Gavin Moore
 */
public abstract class Functionality implements CalculatorFunction
{
    public Functionality(CalculatorWindow window)
    {
        mWindow = window;
    }
    
    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public abstract void invoke();
    
    /**
     * Takes a double number format and converts it to the string equivalent. If the string equivalent ends with ".0" then
     * this is removed from the end.
     * @param number The double to be converted.
     * @return The string equivalent of the double passed to the function.
     */
    public static String doubleToStr(double number)
    {
	/**
	 * A temporary string to hold the string equivalent of the double passed to the function.
	 */
	String temp;

	temp = String.valueOf(number);
	temp = zeroDecimal(temp);
        
	return(temp);
    }

    /**
     * Takes a string and converts it to the double number format equivalent.
     * @param number The string to be converted.
     * @return The double equivalent of the string passed to the function.
     */
    public static double strToDouble(String number)
    {
	/**
	 * A temporary Double to hold the double number format equivalent of the String passed to the function.
	 */
	Double temp;

	temp = new Double(number);
        
	return(temp.doubleValue());
    }

    /**
     * Takes a string and converts it to the integer number format equivalent.
     * @param number The string to be converted.
     * @return The integer equivalent of the string passed to the function.
     */
    public static int strToInt(String number)
    {
	/**
	 * A temporary Float to hold the float number format equivalent of the String passed to the function.
	 */
	Float tempFloat;

	/**
	 * A temporary integer to hold the equivalent of the String passed to the function rounded to the nearest whole
	 * number.
	 */
	int tempInt;

	tempFloat = new Float(number);
	tempInt = Math.round(tempFloat.floatValue());

	return(tempInt);
    }

    /**
     * Checks if the given string ends with ".0", if it does it removes it from the end.
     * @param number A string to be checked for the ".0" suffix.
     * @return The adjusted (if required) string.
     */
    private static String zeroDecimal(String number)
    {
	if(number.endsWith(".0"))
	    {
		number = number.substring(0, (number.length() - 2));
	    }
        
	return(number);
    }
    
    /**
     * Checks if the length of the given string is longer than 18 characters. If it is it shortens it to 18 characters.
     * @param number A string to be checked for length greater than 18 characters.
     * @return The adjusted (if required) string.
     */
    public static String longAnswer(String number)
    {
	/**
	 * The length of the 'E<number>' at the end of standard form numbers.
	 */
        int baseLength = 0;

	/**
	 * Contains the 'E<number>' part of the standard form number.
	 */
	 String temp;
        
        if(number.length() > DISPLAYSIZE)
            {
		// If the number is standard form.
                if(number.indexOf("E") < 1)
		    {
			number = number.substring(0, DISPLAYSIZE);
		    }
		else
                    {
			temp = number.substring(number.indexOf("E"));

			number = number.substring(0, (DISPLAYSIZE - temp.length())) + temp;
                    }
	    }
        
        return(number);
    }

    /**
     * Checks if a non-zero number is already stored in memory.
     * @param mWindow The calculator window to output 'currentSubDisplay' to.
     */
    public static void checkM(CalculatorWindow mWindow)
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
    }

    /**
     * Checks for errors or numbers longer than the display and then outputs the number to the display.
     * @param mWindow The calculator window to output 'currentDisplay' to.
     * @param answer The number to check and output.
     */
    public static void outputAnswer(CalculatorWindow mWindow, String answer)
    {
	if(answer.compareTo("Infinity") == 0)
            {
                mWindow.setDisplay("Error: x/0");
                currentDisplay = "0";
            }
        else if(answer.compareTo("NaN") == 0)
	    {
		mWindow.setDisplay("Error: NaN");
                currentDisplay = "0";
	    }
	else
            {
		currentDisplay = answer;
                mWindow.setDisplay(longAnswer(currentDisplay));
            }
    }

    /**
     * The calculator window this button functionality is associated with.
     */
    public CalculatorWindow mWindow;
    
    /**
     * The current contents of the main display of the calculator.
     */
    public static String currentDisplay = "0";
    
    /**
     * The current contents of the left hand sub display of the calculator.
     */
    public static String currentSubDisplayL = "";

    /**
     * The current contents of the center sub display of the calculator.
     */
    public static String currentSubDisplayC = "";
    
    /**
     * The current contents of the right hand sub display of the calculator.
     */
    public static String currentSubDisplayR = "";
    
    /**
     * Contains all of the elements of the equation being entered, digits, operators and brackets.
     */
    public static ArrayList equation = new ArrayList();

    /**
     * The value currently stored in the memory of the calculator.
     */
    public static String memory = "";

    /**
     * True if the calculator is ready to recieve a new number for the main display i.e if the next digit
     * to be entered will overwrite the current contents of the main display.
     */
    public static boolean readyForNum = true;

    /**
     * True if the last element entered to the list was a bracket.
     */
    public static boolean lastElementBracket = false;

    /**
     * The length of the main display (characters).
     */
    public static final int DISPLAYSIZE = 18;
}
