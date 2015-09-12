
/////////////////////////////////////////////////
///////////////// Adapting Stack ////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class AdaptingStack
{
	public AdaptingStack()
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
			int newStack[] = new int[stack.length * 2];
			System.arraycopy(stack, 0, newStack, 0, size);
			stack = newStack;
		}
		stack[size++] = x;
	}

	public int pop()
	{
		if (size <= stack.length / 4)
		{
			int newStack[] = new int[stack.length / 2];
			System.arraycopy(stack, 0, newStack, 0, size);
			stack = newStack;
		}

		return (stack[size--]);
	}

	public int size()
	{
		return (size);
	}
}
