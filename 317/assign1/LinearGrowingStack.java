/////////////////////////////////////////////////
////////////// Linear Growing Stack /////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class LinearGrowingStack
{
	public LinearGrowingStack()
	{
		stack = new int[1];
		size = 0;
	}

	private int stack[];

	private int size;
	
	private final int GROW_SIZE = 1000;

	public void push(int x)
	{
		if (size >= stack.length)
		{
			int newStack[] = new int[size + GROW_SIZE];
			System.arraycopy(stack, 0, newStack, 0, size);
			stack = newStack;
		}
		stack[size++] = x;
	}

	public int pop()
	{
		return (stack[size--]);
	}

	public int size()
	{
		return (size);
	}
}
