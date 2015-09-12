import java.util.Random;

/////////////////////////////////////////////////
/////////////////// Skip List ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class SkipList
{
	/**
	 *
	 */
	public SkipList()
	{
		// Create start and end nodes.
		topStart = new SkipNode(MIN);
		topEnd = new SkipNode(MAX);

		topStart.setAfter(topEnd);
		topEnd.setBefore(topStart);

		height = 0;
	}

	private final int MIN = -99999;

	private final int MAX = 99999;

	private SkipNode topStart;

	private SkipNode topEnd;

	private Random rand = new Random(System.currentTimeMillis());

	private int height;

	public SkipNode getStart()
	{
		return (topStart);
	}

	/**
	 *
	 */
	public SkipNode find(SkipNode node, int n)
	{
		if (n == node.getValue())
		{
			return (node);
		}

		if (n > node.getValue())
		{
			return (find(node.getAfter(), n));
		}
		else
		{
			if (node.getBelow() != null)
			{
				return (find(node.getBefore().getBelow(), n));
			}
			else
			{
				return (node.getBefore());
			}
		}
	}

	public void insert(int n)
	{
		// Decide the height that this item will have randomly.
		int itemHeight = 0;
		while (rand.nextInt(2) == 0)
		{
			itemHeight++;
		}

		// Find the location to insert the new node.
		SkipNode insertLocation = find(topStart, n);
		while (insertLocation.getBelow() != null)
		{
			insertLocation = insertLocation.getBelow();
		}

		// If the height of the item being inserted is higher than that of any item previously inserted increase the
		// height of the Skip List to the height of the new item.
		while (itemHeight > height)
		{
			SkipNode newStart = new SkipNode(MIN);
			SkipNode newEnd = new SkipNode(MAX);

			newStart.setAfter(newEnd);
			newStart.setBelow(topStart);
			topStart.setAbove(newStart);
			newEnd.setBefore(newStart);
			newEnd.setBelow(topEnd);
			topEnd.setAbove(newEnd);

			topStart = newStart;
			topEnd = newEnd;

			height++;
		}

		// Insert the new node at the location and any positions above this location according to the height of this
		// item.
		SkipNode temp = null;
		for (int count = 0; count <= itemHeight; count++)
		{
			SkipNode newNode = new SkipNode(n);

			// Assign pointers for the new node.
			if (temp != null)
			{
				temp.setAbove(newNode);
			}
			newNode.setAfter(insertLocation.getAfter());
			insertLocation.getAfter().setBefore(newNode);
			insertLocation.setAfter(newNode);
			newNode.setBefore(insertLocation);
			newNode.setBelow(temp);

			temp = newNode;

			// Find location to insert next node.
			while (insertLocation.getAbove() == null && insertLocation.getBefore() != null)
			{
				insertLocation = insertLocation.getBefore();
			}
			insertLocation = insertLocation.getAbove();
		}
	}

	public void remove()
	{}
}
