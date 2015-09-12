/////////////////////////////////////////////////
///////////////// Doubling Stack ////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class DoublingStack
{
	public DoublingStack()
	{
		stack = new int[1];
		size = 0;
	}

	private int stack[];

	private int size;

	public void push(int x)
	{
		if (size >= stack.length)
		{
			int newStack[] = new int[size * 2];
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
