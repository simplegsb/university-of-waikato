/////////////////////////////////////////////////
/////////////////// Tree Node ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class TreeNode
{
	public TreeNode(Object o, int key)
	{
		myObject = o;
		myKey = key;
	}
	
	private final int LEFT = 0;
	private final int RIGHT = 1;
	
	private Object myObject;
	
	private int myKey;
	
	private int mySide;
	
	private TreeNode parent = null;
	
	private TreeNode leftChild = null;
	
	private TreeNode rightChild = null;
	
	public Object getObject()
	{
		return (myObject);
	}
	
	public void setObject(Object o)
	{
		myObject = o;
	}
	
	public int getKey()
	{
		return (myKey);
	}
	
	public void setKey(int key)
	{
		myKey = key;
	}
	
	public boolean isLeftChild()
	{
		return (mySide == LEFT ? true : false);
	}
	
	public boolean isRightChild()
	{
		return (mySide == RIGHT ? true : false);
	}
	
	public void setSide(int newSide)
	{
		mySide = newSide;
	}
	
	public TreeNode getParent()
	{
		return (parent);
	}
	
	public void setParent(TreeNode newParent)
	{
		parent = newParent;
	}
	
	public TreeNode getLeftChild()
	{
		return (leftChild);
	}
	
	public void setLeftChild(TreeNode newChild)
	{
		newChild.setSide(LEFT);
		newChild.setParent(this);
		leftChild = newChild;
	}
	
	public TreeNode getRightChild()
	{
		return (rightChild);
	}
	
	public void setRightChild(TreeNode newChild)
	{
		newChild.setSide(RIGHT);
		newChild.setParent(this);
		rightChild = newChild;
	}
}
