/////////////////////////////////////////////////
/////////////////// Array Heap //////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class ArrayHeap
{
	public ArrayHeap(int fullSize)
	{
		heap = new int[fullSize + 1];
		size = 1;
	}

	private int heap[];

	private int size;

	public void add(int x)
	{
		heap[size] = x;

		upheap(size);

		size++;
	}

	public int remove()
	{
		int x = heap[size - 1];
		heap[size - 1] = heap[1];
		heap[1] = x;

		size--;
		downheap(1);

		return (heap[size]);
	}

	private void upheap(int currentNode)
	{
		if (currentNode > 1)
		{
			if (heap[currentNode / 2] > heap[currentNode])
			{
				int x = heap[currentNode];
				heap[currentNode] = heap[currentNode / 2];
				heap[currentNode / 2] = x;

				upheap(currentNode / 2);
			}
		}
	}

	private void downheap(int currentNode)
	{
		if (currentNode * 2 + 1 < size)
		{
			if (heap[currentNode * 2] < heap[currentNode * 2 + 1])
			{
				if (heap[currentNode * 2] < heap[currentNode])
				{
					int x = heap[currentNode];
					heap[currentNode] = heap[currentNode * 2];
					heap[currentNode * 2] = x;
					
					downheap(currentNode * 2);
				}
			}
			else
			{
				if (heap[currentNode * 2 + 1] < heap[currentNode])
				{
					int x = heap[currentNode];
					heap[currentNode] = heap[currentNode * 2 + 1];
					heap[currentNode * 2 + 1] = x;
					
					downheap(currentNode * 2 + 1);
				}
			}
		}
		else if (currentNode * 2 < size)
		{
			if (heap[currentNode * 2] < heap[currentNode])
			{
				int x = heap[currentNode];
				heap[currentNode] = heap[currentNode * 2];
				heap[currentNode * 2] = x;
				
				downheap(currentNode * 2);
			}
		}
	}
	
	public int[] getArray()
	{
		return (heap);
	}
}
