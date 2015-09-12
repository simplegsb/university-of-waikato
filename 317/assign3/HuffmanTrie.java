import java.util.ArrayList;
import java.util.Iterator;

/////////////////////////////////////////////////
////////////////// Huffman Trie /////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class HuffmanTrie
{
	public HuffmanTrie(String t, String alphabet)
	{
		int frequencies[] = generateFrequencies(t, alphabet);
		ArrayList auxList = new ArrayList();

		for (int index = 0; index < alphabet.length(); index++)
		{
			Tree currentTree = new Tree(new TreeNode(alphabet.substring(index, index + 1), frequencies[index]));
			auxList.add(currentTree);
		}

		while (auxList.size() > 1)
		{
			Tree tree1 = removeMin(auxList);
			Tree tree2 = removeMin(auxList);

			Tree newTree = join(tree1, tree2);

			auxList.add(newTree);
		}

		myTree = (Tree) auxList.remove(0);
	}

	private Tree myTree;

	public int calcCompressedSize()
	{
		return (calcCompressedSize(0, myTree.getRoot()));
	}

	public int calcCompressedSize(int depth, TreeNode root)
	{
		if (root == null)
		{
			return (0);
		}

		if (root.getLeftChild() == null && root.getRightChild() == null)
		{
			return (root.getKey() * depth);
		}

		return (
			calcCompressedSize(depth + 1, root.getLeftChild()) + calcCompressedSize(depth + 1, root.getRightChild()));
	}

	private int[] generateFrequencies(String t, String alphabet)
	{
		int frequencies[] = new int[alphabet.length()];
		int count = 0;

		for (int alphaIndex = 0; alphaIndex < alphabet.length(); alphaIndex++)
		{
			for (int tIndex = 0; tIndex < t.length(); tIndex++)
			{
				if (t.charAt(tIndex) == alphabet.charAt(alphaIndex))
				{
					count++;
				}
			}

			frequencies[alphaIndex] = count;
			count = 0;
		}

		return (frequencies);
	}

	private Tree join(Tree a, Tree b)
	{
		Tree newTree = new Tree(new TreeNode(null, a.getRoot().getKey() + b.getRoot().getKey()));

		newTree.insert(a.getRoot(), a.getRoot().getKey());
		newTree.insert(b.getRoot(), b.getRoot().getKey());

		return (newTree);
	}

	private Tree removeMin(ArrayList list)
	{
		Iterator listIter = list.iterator();
		Tree smallestTree = null;

		while (listIter.hasNext())
		{
			Tree currentTree = (Tree) listIter.next();

			if (smallestTree == null)
			{
				smallestTree = currentTree;
			}
			else if (currentTree.getRoot().getKey() < smallestTree.getRoot().getKey())
			{
				smallestTree = currentTree;
			}
		}

		list.remove(list.indexOf(smallestTree));

		return (smallestTree);
	}
}
