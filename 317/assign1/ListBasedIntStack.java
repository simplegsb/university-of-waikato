/////////////////////////////////////////////////
////////////// List Based Int Stack /////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class ListBasedIntStack
{
	public ListBasedIntStack()
	{
		size = 0;
	}
	
	private LinkedListNode top;
	
	private int size;

	public void push(int x)
	{
		LinkedListNode newTop = new LinkedListNode(x);
		newTop.setNext(top);
		top = newTop;
	}

	public int pop()
	{
		int oldTopElement = top.getElement();
		top = top.getNext();

		return (oldTopElement);
	}

	public int size()
	{
		return (size);
	}
}
