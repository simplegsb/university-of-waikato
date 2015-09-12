package comp209.calculator;

import comp209.calculatorwindow.*;
import java.util.*;

/**
 * Is an extending subclass of 'AC'.
 * Evaluates all pending operations with an infix notation. Uses bedmas (brackets, exponents, division, multiplication,
 * addition, subtraction) to do so.
 * @author Gary Buyn and Gavin Moore
 */
public class Calculate extends AC
{
    public Calculate(CalculatorWindow window)
    {
        super(window);
    }

    /**
     * Called when the button this functionality is associated with is pressed.
     */
    public void invoke()
    {
	// Put the last element of the equation (currently in the main display) in 'equation' if needed and evaluate it.
	if(readyForNum == false && lastElementBracket == false)
	    {
		equation.add(currentDisplay);
	    }

	answer = evaluate(new ArrayList(equation));

	super.invoke();
        
	outputAnswer(mWindow, answer);
    }

    /**
     * Lists all elements of the 'list' ArrayList seperated by spaces.
     * @param list The ArrayList to output the elements of.
     */
    public void listElements(ArrayList list)
    {
	/**
	 * The iterator over the 'list' ArrayList.
	 */
	ListIterator listIter = list.listIterator();

	while(listIter.hasNext())
	    {
		System.out.print(listIter.next() + " ");
	    }
	System.out.print("\n");
    }

    /**
     * Evaluates the equation in the 'list' ArrayList, uses bedmas to do so. Returns the result (as double).
     * @param list The ArrayList to evaluate.
     * @return The result of all operations in the 'list' ArrayList.
     */
    private String evaluate(ArrayList list)
    {
	/**
	 * The result of all operations in the 'list' ArrayList.
	 */
	String result;

	list = searchForBrackets(list);
	// The 'secondOp' for this call to 'searchFor2Op' is "noOp" because I only want to search for one operator.
	list = searchFor2Op(list, "pow", "noOp");
	list = searchFor2Op(list, "/", "*");
	list = searchFor2Op(list, "+", "-");

	if(list.size() == 1)
	    {
		result = (String) list.get(0);
	    }
	else
	    {
		result = "Error: syntax";
	    }

	return(result);
    }
    /**
     * Searches every element of the 'list' ArrayList for brackets, if both an opening and closing bracket are found in the
     * correct order it evaluates all elements within the brackets and returns a changed ArrayList with the brackets and
     * their contents replaced by a single element containing the result of all the operations within the brackets.
     * @param list The ArrayList to search for the bracketss in.
     * @return The new ArrayList with all operations in the brackets evaluated.
     */
    private ArrayList searchForBrackets(ArrayList list)
    {
	/**
	 * The iterator over the 'list' ArrayList.
	 */
	ListIterator listIter = list.listIterator();

	/**
	 * The current element being tested to see if it is a bracket.
	 */
	String current;

	/**
	 * The index of an opening bracket if found.
	 */
	int openIndex = 0;

	/**
	 * The index of a closing bracket if found.
	 */
	int closeIndex = 0;

	/**
	 * Increments when opening bracket found and decrements when closing bracket is found to test if they have the same amount
	 */
	int bracCount = 0;

	while(listIter.hasNext() && bracCount >= 0)
	    {
		current = (String) listIter.next();

		if(current.compareTo("(") == 0)
		    {
			if(bracCount == 0)
			    {
				openIndex = listIter.previousIndex();
			    }

			bracCount++;
		    }

		if(current.compareTo(")") == 0)
		    {
			if(bracCount == 1)
			    {
				closeIndex = listIter.previousIndex();

				ArrayList inBrackets = new ArrayList();
				for(int index = (openIndex + 1); index <= (closeIndex - 1); index++)
				    {
					inBrackets.add(list.get(index));
				    }

				if(inBrackets.size() <= 2)
				    {
					list.remove(openIndex);
					list.remove(closeIndex - 1);
				    }
				else
				    {
					replaceRegionWithResult
					    (list, strToDouble(evaluate(inBrackets)), openIndex, (closeIndex - (openIndex - 1)));
				    }

				listIter = list.listIterator();
			    }

			bracCount--;
		    }
	    }

	if(bracCount != 0)
	    {
		list.clear();
		list.add("Error: ()");

		return(list);
	    }

	return(list);
    }

    /**
     * Searches every element of the 'list' ArrayList for one of the two given operators, if one is found it is
     * processed and the operator's equation (digit, operator, digit) will be replaced with the result in the 'list'
     * ArrayList. This only occurs if the equation is valid (of the form above). If it is not valid the evaluation of the
     * equation is aborted.
     * @param list The ArrayList to search for the operators in.
     * @param firstOp The first operator to search for.
     * @param secondOp The second operator to search for.
     * @return The changed ArrayList with all operations of the types passed evaluated.
     */
    private ArrayList searchFor2Op(ArrayList list, String firstOp, String secondOp)
    {
	/**
	 * The iterator over the 'list' ArrayList.
	 */
	ListIterator listIter = list.listIterator();

	/**
	 * The result of the equation any operator found will perform.
	 */
	double result;

	// Keep checking for the given operators until all elements of the equation have been checked.
        while(listIter.hasNext())
	    { 
		getElements(list, listIter);

		if(currentElement.compareTo(firstOp) == 0)
		    {
			// If it is not a valid equation
			if((prevElement.compareTo("cannotUseAsOp") == 0) || (nextElement.compareTo("cannotUseAsOp") == 0))
			    {
				list.clear();
				list.add("Error: syntax");
				listIter = list.listIterator();
			    }
			else
			    {
				replaceRegionWithResult(list, processOp(), (listIter.previousIndex() - 1), 3);
				listIter = list.listIterator();
			    }
		    }
		else if(currentElement.compareTo(secondOp) == 0)
		    {
			// If it is not a valid equation
			if((prevElement.compareTo("cannotUseAsOp") == 0) || (nextElement.compareTo("cannotUseAsOp") == 0))
			    {
				list.clear();
				list.add("Error: syntax");
				listIter = list.listIterator();
			    }
			else
			    {
				replaceRegionWithResult(list, processOp(), (listIter.previousIndex() - 1), 3);
				listIter = list.listIterator();
			    }
		    }
	    }

	return(list);
    }

    /**
     * Retrieves the value of the element being tested for an operator and the values of the elements before and after this
     * element in the 'list' ArrayList.
     * @param listIter The iterator over the 'list' ArrayList.
     */
    private void getElements(ArrayList list, ListIterator listIter)
    {  
	// Check to see if there is a previous element to 'get' from the equation. If there is then get it, otherwise
	// give the 'prevElement' variable the string "cannotUseAsOp".
	if(!listIter.hasPrevious())
	    {
		prevElement = "cannotUseAsOp";
	    }
	else
	    {
		prevElement = (String) list.get(listIter.previousIndex());
		if(prevElement.indexOf("E") == 0)
		    {
			prevElement = "cannotUseAsOp";
		    }
	    }
	
	if(listIter.hasNext())
	    {
		currentElement = (String) listIter.next();
	    }
	
	// Check to see if there is a next element to 'get' from the equation. If there is then get it, otherwise
	// give the 'nextElement' variable the string "cannotUseAsOp".
	if(!listIter.hasNext())
	    {
		nextElement = "cannotUseAsOp";
	    }
	else
	    {
		nextElement = (String) list.get(listIter.nextIndex());
		if(nextElement.indexOf("E") == 0)
		    {
			nextElement = "cannotUseAsOp";
		    }
	    }
    }

    /**
     * Processes the operator's equation and return's the result.
     * @return The result of the operator's equation.
     */
    private double processOp()
    {
	/**
	 * The result of the equation the operator performed.
	 */
	double result = 0;

	if(currentElement.compareTo("pow") == 0)
	    {
		
		result = Math.pow(strToDouble(prevElement), strToDouble(nextElement));
	    }
	else if(currentElement.compareTo("*") == 0)
	    {
		result = strToDouble(prevElement) * strToDouble(nextElement);
	    }
	else if(currentElement.compareTo("/") == 0)
	    {
		result = strToDouble(prevElement) / strToDouble(nextElement);
	    }
	else if(currentElement.compareTo("+") == 0)
	    {
		result = strToDouble(prevElement) + strToDouble(nextElement);
	    }
	else if(currentElement.compareTo("-") == 0)
	    {
		result = strToDouble(prevElement) - strToDouble(nextElement);
	    }

	return(result);
    }

    /**
     * Removes a range defined by the starting index and the length and then adds the 'result' in its place.
     * @param list The list to replace the region in.
     * @param result The result to add after removing the range.
     * @param startIndex The index to start removing from.
     * @param length the number of elements to remove.
     */
    private void replaceRegionWithResult(ArrayList list, double result, int startIndex, int length)
    {
	for(int removed = 0; removed < length; removed++)
	    {
		list.remove(startIndex);
	    }
	list.add(startIndex, doubleToStr(result));
    }

    /**
     * The contents of the element before the current element.
     */
    private String prevElement;

    /**
     * The contents of the current element.
     */
    private String currentElement;

    /**
     * The contents of the element after the current element.
     */
    private String nextElement;

    /**
     * The result of all operations in the 'equation' ArrayList.
     */
    private String answer;
}
