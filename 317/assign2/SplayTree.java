/////////////////////////////////////////////////
/////////////////// Splay Tree //////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class SplayTree
{
	/**
	 * Constructs an instance of a Splay Tree.
	 */
	public SplayTree()
	{}

	/**
	 * The root node of the Splay Tree.
	 */
	private SplayNode root;

	public SplayNode getRoot()
	{
		return (root);
	}

	/**
	 * Recursively finds the node with the value equal to the value being searched for or an external node if one is
	 * reached before a match can be found.
	 * 
	 * @param root The root of the tree to search in.
	 * @param n The value to search for.
	 * 
	 * @return The node with a value equal to the value being searched for or the external node reached.
	 */
	public SplayNode find(SplayNode root, int n)
	{
		// If the value being searched for is the same as the root value return the root.
		if (n == root.getValue())
		{
			return (root);
		}

		if (n <= root.getValue())
		{
			if (root.getLeft() != null)
			{
				return (find(root.getLeft(), n));
			}
			else
			{
				return (root);
			}
		}
		else
		{
			if (root.getRight() != null)
			{
				return (find(root.getRight(), n));
			}
			else
			{
				return (root);
			}
		}
	}

	/**
	 * Inserts a node into the Splay Tree with the value 'n'. The node is splayed after insertion.
	 * 
	 * @param n The value of the node to be inserted.
	 */
	public void insert(int n)
	{
		// The new node to be inserted.
		SplayNode newNode = new SplayNode(n);

		if (root == null)
		{
			root = newNode;
		}
		else
		{
			SplayNode insertLocation = find(root, n);

			if (n <= insertLocation.getValue())
			{
				insertLocation.setLeft(newNode);
			}
			else
			{
				insertLocation.setRight(newNode);
			}

			splay(newNode);
		}
	}

	/**
	 * Removes the node with the value 'n'. 
	 * 
	 * @param n The value of the node to be removed.
	 */
	public void remove(int n)
	{}

	/**
	 * Splays 'node' repetidly until 'node' becomes the root of the Splay Tree.
	 * 
	 * @param node The node to be splayed.
	 */
	private void splay(SplayNode node)
	{
		// The parent and grandparent of the node being splayed.
		SplayNode parent, grandParent;

		if (node != null)
		{
			// While the node being splayed is not the root.
			while ((parent = node.getParent()) != null)
			{
				// If the node being splayed is the child of the root.
				if ((grandParent = parent.getParent()) == null)
				{
					if (node.isLeftChild())
					{
						rotateRight(parent);
					}
					else
					{
						rotateLeft(parent);
					}
				}
				else
				{
					if (parent.isLeftChild())
					{
						if (node.isLeftChild())
						{
							// Zig Zig
							rotateRight(grandParent);
							rotateRight(parent);
						}
						else
						{
							// Zig Zag
							rotateLeft(parent);
							rotateRight(grandParent);
						}
					}
					else
					{
						if (node.isLeftChild())
						{
							// Zig Zag
							rotateRight(parent);
							rotateLeft(grandParent);
						}
						else
						{
							// Zig Zig
							rotateLeft(grandParent);
							rotateLeft(parent);
						}
					}
				}
			}
		}
	}

	public void rotateLeft(SplayNode node)
	{
		SplayNode xParent = node.getParent();
		SplayNode x = node;
		SplayNode y = node.getRight();

		if (xParent != null)
		{
			if (x.isLeftChild())
			{
				xParent.setLeft(y);
			}
			else
			{
				xParent.setRight(y);
			}
		}
		else
		{
			root = y;
			y.setParent(null);
		}

		SplayNode t2 = y.getLeft();
		y.setLeft(x);
		x.setRight(t2);
	}

	public void rotateRight(SplayNode node)
	{
		SplayNode yParent = node.getParent();
		SplayNode y = node;
		SplayNode x = node.getLeft();

		if (yParent != null)
		{
			if (y.isLeftChild())
			{
				yParent.setLeft(x);
			}
			else
			{
				yParent.setRight(x);
			}
		}
		else
		{
			root = x;
			x.setParent(null);
		}

		SplayNode t2 = x.getRight();
		x.setRight(y);
		y.setLeft(t2);
	}
}
