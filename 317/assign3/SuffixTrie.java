import java.util.ArrayList;
import java.util.Iterator;

/////////////////////////////////////////////////
////////////////// Suffix Trie //////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class SuffixTrie
{
	public SuffixTrie(String t)
	{
		myString = t;

		for (int index = myString.length() - 1; index >= 0; index--)
		{
			insertString(index, root);
		}
	}

	String myString;

	TrieNode root = new TrieNode(-1, -1);

	private void insertString(int newStringStart, TrieNode root)
	{
		Iterator childIter = root.getChildren().iterator();
		boolean inserted = false;

		while (childIter.hasNext() && !inserted)
		{
			TrieNode currentChild = (TrieNode) childIter.next();
			int nodeStart = currentChild.getStart();
			int nodeEnd = currentChild.getEnd();

			int nodeIndex = nodeStart;
			int newStringIndex = newStringStart;
			while (nodeIndex <= nodeEnd
				&& newStringIndex < myString.length()
				&& myString.charAt(nodeIndex) == myString.charAt(newStringIndex))
			{
				nodeIndex++;
				newStringIndex++;
			}

			if (nodeIndex > nodeStart)
			{
				if (nodeIndex <= nodeEnd)
				{
					ArrayList currentChildren = currentChild.getChildren();
					currentChild.setChildren(null);
					currentChild.setValues(nodeStart, nodeIndex - 1);
					
					currentChild.addChild(nodeIndex, nodeEnd, currentChildren);
					currentChild.addChild(newStringIndex, myString.length() - 1, null);
				}
				else
				{
					insertString(newStringIndex, currentChild);
				}

				inserted = true;
			}
		}

		if (!inserted)
		{
			root.addChild(newStringStart, myString.length() - 1, null);
		}
	}

	public int search(String p)
	{
		return (search(p, root));
	}

	public int search(String p, TrieNode node)
	{
		Iterator childIter = node.getChildren().iterator();

		while (childIter.hasNext())
		{
			TrieNode currentChild = (TrieNode) childIter.next();
			int nodeStart = currentChild.getStart();
			int nodeEnd = currentChild.getEnd();

			boolean fail = false;
			for (int nodeIndex = nodeStart; nodeIndex <= nodeEnd; nodeIndex++)
			{
				int matchIndex = 0;
				while (nodeIndex + matchIndex <= nodeEnd && !fail)
				{
					if (matchIndex < p.length())
					{
						if (myString.charAt(nodeIndex + matchIndex) != p.charAt(matchIndex))
						{
							fail = true;
						}
						else
						{
							matchIndex++;
						}
					}
					else
					{
						return (nodeIndex);
					}
				}

				if (fail == false)
				{
					if (matchIndex == p.length())
					{
						return (nodeIndex);
					}
					else
					{
						if (search(p.substring(matchIndex), currentChild) == 0)
						{
							return (nodeIndex);
						}
					}
				}
			}

			int childSearch = search(p, currentChild);
			if (childSearch != -1)
			{
				return (childSearch);
			}
		}

		return (-1);
	}
}
