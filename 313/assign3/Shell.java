import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

////////////////////////////////////////////////////////////////////
/////////////////////////////// SHELL //////////////////////////////
////////////////////////////////////////////////////////////////////

/**
 * 
 */
public class Shell
{
	public Shell()
	{}

	// Main:
	////////////////////////////////////////////////////////////////////

	public static void main(String[] args)
	{
		Shell shell = new Shell();

		shell.openShell();
	}

	// Fields:
	////////////////////////////////////////////////////////////////////

	/**
	 *
	 */
	private HashMap variableMap = new HashMap();

	// Functional methods:
	////////////////////////////////////////////////////////////////////

	/**
	 *
	 */
	public void openShell()
	{
		boolean exit = false;

		while (exit == false)
		{
			exit = processPrompt(prompt());
		}

		System.exit(0);
	}

	/**
	 *
	 */
	private String prompt()
	{
		System.out.print("> ");

		try
		{
			byte byteArray[] = new byte[128];
			System.in.read(byteArray);
			return (createValidString(byteArray));
		}
		catch (IOException e)
		{
			System.out.println("IOException.");
		}

		return (new String(" "));
	}

	/**
	 *
	 */
	private boolean processPrompt(String command)
	{
		int spaceIndex;
		String variable = new String();
		String key = new String();
		ArrayList parameters = new ArrayList();

		spaceIndex = command.indexOf(' ');
		if (spaceIndex != -1)
		{
			variable = command.substring(0, spaceIndex);
			command = command.substring(spaceIndex + 1);
		}
		else
		{
			variable = command;
			command = "";
		}

		spaceIndex = command.indexOf(' ');
		if (spaceIndex != -1)
		{
			key = command.substring(0, spaceIndex);
			command = command.substring(spaceIndex + 1);
		}
		else if (command.length() != 0)
		{
			key = command;
			command = "";
		}

		for (int paramIndex = 0; spaceIndex != -1; paramIndex++)
		{
			spaceIndex = command.indexOf(' ');
			if (spaceIndex != -1)
			{
				parameters.add(command.substring(0, spaceIndex));
				command = command.substring(spaceIndex + 1);
			}
			else if (command.length() != 0)
			{
				parameters.add(command);
				command = "";
			}
		}

		return (processCommand(variable, key, parameters));
	}

	/**
	 *
	 */
	private boolean processCommand(String variable, String key, ArrayList parameters)
	{
		if (variable.compareToIgnoreCase("bye") == 0)
		{
			return (true);
		}
		else if (key.compareTo("boolean") == 0)
		{
			if (!wrapBoolean(variable, parameters))
			{
				System.out.println("Invalid boolean.");
			}
		}
		else if (key.compareTo("char") == 0)
		{
			if (!wrapChar(variable, parameters))
			{
				System.out.println("Invalid char.");
			}
		}
		else if (key.compareTo("double") == 0)
		{
			if (!wrapDouble(variable, parameters))
			{
				System.out.println("Invalid double.");
			}
		}
		else if (key.compareTo("int") == 0)
		{
			if (!wrapInt(variable, parameters))
			{
				System.out.println("Invalid int.");
			}
		}
		else if (key.compareToIgnoreCase("string") == 0)
		{
			if (!wrapString(variable, parameters))
			{
				System.out.println("Invalid string.");
			}
		}
		else if (key.compareToIgnoreCase("new") == 0)
		{
			if (!constructor(variable, parameters))
			{
				System.out.println("Invalid constructor.");
			}
		}
		else if (key.compareToIgnoreCase("method") == 0)
		{
			if (!method(variable, parameters))
			{
				System.out.println("Invalid method.");
			}
		}
		else if (key.compareToIgnoreCase("show") == 0)
		{
			if (!show(variable))
			{
				System.out.println("Invalid variable.");
			}
		}
		else
		{
			System.out.println("Invalid command.");
		}

		return (false);
	}

	/**
	 *
	 */
	private boolean wrapBoolean(String variable, ArrayList parameters)
	{
		try
		{
			Object newObject = new Boolean((String) parameters.get(0));
			variableMap.put(variable, newObject);
			show(variable);

			return (true);
		}
		catch (Exception e)
		{
			return (false);
		}
	}

	/**
	 *
	 */
	private boolean wrapChar(String variable, ArrayList parameters)
	{
		try
		{
			Object newObject = new Character(((String) parameters.get(0)).charAt(0));
			variableMap.put(variable, newObject);
			show(variable);

			return (true);
		}
		catch (Exception e)
		{
			return (false);
		}
	}

	/**
	 *
	 */
	private boolean wrapDouble(String variable, ArrayList parameters)
	{
		try
		{
			Object newObject = new Double(Double.valueOf((String) parameters.get(0)).doubleValue());
			variableMap.put(variable, newObject);
			show(variable);

			return (true);
		}
		catch (Exception e)
		{
			return (false);
		}
	}

	/**
	 *
	 */
	private boolean wrapInt(String variable, ArrayList parameters)
	{
		try
		{
			Object newObject = new Integer(Integer.valueOf((String) parameters.get(0)).intValue());
			variableMap.put(variable, newObject);
			show(variable);

			return (true);
		}
		catch (Exception e)
		{
			return (false);
		}
	}

	/**
	 *
	 */
	private boolean wrapString(String variable, ArrayList parameters)
	{
		try
		{
			Object newObject = new String((String) parameters.get(0));
			variableMap.put(variable, newObject);
			show(variable);

			return (true);
		}
		catch (Exception e)
		{
			return (false);
		}
	}

	/**
	 *
	 */
	private boolean constructor(String variable, ArrayList parameters)
	{
		try
		{
			Class thisClass = Class.forName((String) parameters.get(0));
			parameters.remove(0);
			Constructor myCons[] = thisClass.getConstructors();

			// For constructor with no parameters.
			if (parameters.isEmpty())
			{
				Object newObject = Class.forName(thisClass.getName()).newInstance();
				variableMap.put(variable, newObject);
				show(variable);

				return (true);
			}

			// For constructors with parameters.
			for (int conIndex = 0; conIndex < myCons.length; conIndex++)
			{
				Class[] paramClasses = myCons[conIndex].getParameterTypes();

				if (parameters.size() == paramClasses.length)
				{
					Object newObject = myCons[conIndex].newInstance(getVariableArray(parameters));
					variableMap.put(variable, newObject);
					show(variable);

					return (true);
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("ClassNotFoundException.");
			return (false);
		}
		catch (IllegalAccessException e)
		{
			System.out.println("IllegalAccessException.");
			return (false);
		}
		catch (InstantiationException e)
		{
			System.out.println("InstantiationException.");
			return (false);
		}
		catch (InvocationTargetException e)
		{
			System.out.println("InvocationTargetException.");
			return (false);
		}

		return (false);
	}

	/**
	 *
	 */
	private boolean method(String variable, ArrayList parameters)
	{
		Object objectWithMethod = variableMap.get((String) parameters.get(0));
		parameters.remove(0);
		if (objectWithMethod == null)
		{
			System.out.println("A variable was not found.");
			return (false);
		}
		Class objClass = objectWithMethod.getClass();
		Method objMethods[] = objClass.getMethods();

		String methodName = (String) parameters.get(0);
		parameters.remove(0);

		try
		{
			for (int methodIndex = 0; methodIndex < objMethods.length; methodIndex++)
			{
				if (objMethods[methodIndex].getName().compareTo(methodName) == 0)
				{
					System.out.println("Found match...");

					Object paramObjects[] = getVariableArray(parameters);
					Class methodParamClasses[] = objMethods[methodIndex].getParameterTypes();

					if (objectWithMethod != null && paramObjects != null)
					{
						if (paramObjects.length == methodParamClasses.length)
						{
							Object newObject =
								objMethods[methodIndex].invoke(objectWithMethod, paramObjects);

							if (variableMap.put(variable, newObject) != null)
							{
								show(variable);
							}
							else
							{
								System.out.println(variable + " = null");
							}

							System.out.println("Good match!");
							return (true);
						}
					}
				}
			}
		}
		catch (IllegalAccessException e)
		{
			System.out.println("IllegalAccessException.");
			return (false);
		}
		catch (InvocationTargetException e)
		{
			System.out.println("InvocationTargetException.");
			return (false);
		}

		return (false);
	}

	/**
	 *
	 */
	private boolean show(String variable)
	{
		try
		{
			Object object = variableMap.get(variable);

			System.out.print(variable + " = " + object.getClass().getName());

			Field[] objFields = object.getClass().getDeclaredFields();
			if (objFields.length > 0)
			{
				System.out.print("[");

				for (int index = 0; index < objFields.length; index++)
				{
					if (index < objFields.length - 1)
					{
						System.out.print(objFields[index].getName() + ",");
					}
					else
					{
						System.out.print(objFields[index].getName());
					}
				}

				System.out.print("]\n");
			}
		}
		catch (Exception e)
		{
			return (false);
		}

		return (true);
	}

	// Utility methods:
	////////////////////////////////////////////////////////////////////

	/**
	 *
	 */
	private String createValidString(byte byteArray[])
	{
		String validString = new String(byteArray);

		for (int index = 0; index < 128; index++)
		{
			if (!Character.isLetterOrDigit(validString.charAt(index))
				&& !Character.isSpaceChar(validString.charAt(index))
				&& validString.charAt(index) != '.')
			{
				return (validString.substring(0, index));
			}
		}

		return (" ");
	}

	/**
	 *
	 */
	private Object[] getVariableArray(ArrayList parameters)
	{
		ArrayList variableObjects = new ArrayList();

		for (int index = 0; index < parameters.size(); index++)
		{
			Object thisVariable = variableMap.get(parameters.get(index));

			if (thisVariable != null)
			{
				variableObjects.add(thisVariable);
			}
			else
			{
				System.out.println("A variable was not found.");
				return (null);
			}
		}

		return (variableObjects.toArray());
	}
}
