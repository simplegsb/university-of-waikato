/////////////////////////////////////////////////
/////////////////// Splay Node //////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class SplayNode
{
	/**
	 * Creates an instance of a Splay Node with value 'n'.
	 * 
	 * @param n The value of the Splay Node.
	 */
	public SplayNode(int n)
	{
		value = n;
	}

	/**
	 * Field 'mySide' will be equal to this field if this Splay Node is the left child of its parent.
	 */
	public static final int LEFT = 0;

	/**
	 * Field 'mySide' will be equal to this field if this Splay Node is the right child of its parent.
	 */
	public static final int RIGHT = 1;

	/**
	 * The value of this Splay Node.
	 */
	private int value;

	/**
	 * Equal to the field 'LEFT' if this Splay Node is the left child of its parent, equal to the field 'RIGHT' if this
	 * Splay Node is the right child of its parent.
	 */
	private int mySide;

	/**
	 * The parent node of this Splay Node.
	 */
	private SplayNode parent;

	/**
	 * The left child node of this Splay Node.
	 */
	private SplayNode leftChild;

	/**
	 * The right child node of this Splay Node.
	 */
	private SplayNode rightChild;

	public int getValue()
	{
		return (value);
	}

	public void setValue(int n)
	{
		value = n;
	}

	public int getSide()
	{
		return (mySide);
	}

	public void setSide(int n)
	{
		mySide = n;
	}

	public SplayNode getParent()
	{
		return (parent);
	}

	public void setParent(SplayNode node)
	{
		parent = node;
	}

	public SplayNode getLeft()
	{
		return (leftChild);
	}

	public void setLeft(SplayNode node)
	{
		leftChild = node;

		if (node != null)
		{
			node.setParent(this);
			node.setSide(LEFT);
		}
	}

	public SplayNode getRight()
	{
		return (rightChild);
	}

	public void setRight(SplayNode node)
	{
		rightChild = node;

		if (node != null)
		{
			node.setParent(this);
			node.setSide(RIGHT);
		}
	}

	public boolean isLeftChild()
	{
		return (mySide == LEFT);
	}

	public boolean isRightChild()
	{
		return (mySide == RIGHT);
	}
}
