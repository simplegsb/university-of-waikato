/////////////////////////////////////////////////
////////////////////// Tree /////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Tree
{
	public Tree(TreeNode newNode)
	{
		root = newNode;
	}

	private TreeNode root;

	public void insert(Object o, int key)
	{
		TreeNode newNode = new TreeNode(o, key);

		insert(newNode, root);
	}

	private void insert(TreeNode newNode, TreeNode root)
	{
		if (root == null)
		{
			root = newNode;
		}
		else
		{
			if (newNode.getKey() <= root.getKey())
			{
				if (root.getLeftChild() == null)
				{
					root.setLeftChild(newNode);
				}
				else
				{
					insert(newNode, root.getLeftChild());
				}
			}
			else
			{
				if (root.getRightChild() == null)
				{
					root.setRightChild(newNode);
				}
				else
				{
					insert(newNode, root.getRightChild());
				}
			}
		}
	}

	public TreeNode getRoot()
	{
		return (root);
	}
}
