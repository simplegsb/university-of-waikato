import java.util.ArrayList;

/////////////////////////////////////////////////
/////////////////// Trie Node ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class TrieNode
{
	public TrieNode(int start, int end)
	{
		startIndex = start;
		endIndex = end;
	}

	private int startIndex;
	
	private int endIndex;

	private TrieNode parent = null;

	private ArrayList children = new ArrayList();

	public int getStart()
	{
		return (startIndex);
	}
	
	public int getEnd()
	{
		return (endIndex);
	}

	public void setValues(int start, int end)
	{
		startIndex = start;
		endIndex = end;
	}

	public TrieNode getParent()
	{
		return (parent);
	}

	public void setParent(TrieNode newParent)
	{
		parent = newParent;
	}

	public ArrayList getChildren()
	{
		return (children);
	}

	public void setChildren(ArrayList newChildren)
	{
		if (newChildren != null)
		{
			children = newChildren;
		}
		else
		{
			children = new ArrayList();
		}
	}

	public TrieNode addChild(int start, int end, ArrayList newChildsChildren)
	{
		TrieNode newChild = new TrieNode(start, end);
		newChild.setParent(this);
		newChild.setChildren(newChildsChildren);
		children.add(newChild);

		return (newChild);
	}
}
