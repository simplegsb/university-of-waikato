/////////////////////////////////////////////////
//////////////// Linked List Node ///////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class LinkedListNode
{	
	public LinkedListNode(int x)
	{
		element = x;
	}
	
	private int element;
	
	private LinkedListNode next;
	
	public int getElement()
	{
		return(element);
	}
	
	public LinkedListNode getNext()
	{
		return(next);
	}
	
	public void setNext(LinkedListNode node)
	{
		next = node;
	}
}
