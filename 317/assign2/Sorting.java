import java.util.Random;

/////////////////////////////////////////////////
/////////////////// Quick Sort //////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Sorting
{
	private static final int THRESHOLD = 50;

	private static Random rand = new Random(System.currentTimeMillis());

	public static void main(String[] args)
	{
		int N = 10000;
		int k = 8;

		int array[] = new int[k * N];

		for (int index = 0; index < array.length; index++)
		{
			//array[index] = rand.nextInt();
			//array[index] = index;
			array[index] = (array.length - 1) - index;
		}

		long startTime = System.currentTimeMillis();

		array = quickSort(array);

		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
	}

	private static int[] quickSort(int[] array)
	{
		if (array.length < 2)
		{
			return (array);
		}

		// Finding the pivot.
		// Last element:
		//int pivotIndex = array.length - 1;

		// Median of 3:
		//int pivotIndex = medianOfThree(array);

		// Random:
		int pivotIndex = rand.nextInt(array.length);

		for (int index = 0; index < array.length; index++)
		{
			if (index < pivotIndex)
			{
				while (array[index] > array[pivotIndex])
				{
					int current = array[index];

					for (int currentIndex = index; currentIndex < (array.length - 1); currentIndex++)
					{
						array[currentIndex] = array[currentIndex + 1];
					}

					array[array.length - 1] = current;
					pivotIndex--;
				}
			}
			else if (index > pivotIndex)
			{
				while (array[index] < array[pivotIndex])
				{
					int current = array[index];

					for (int currentIndex = index; currentIndex > pivotIndex; currentIndex--)
					{
						array[currentIndex] = array[currentIndex - 1];
					}

					array[pivotIndex] = current;
					pivotIndex++;
				}
			}
		}

		//array = basicRec(array, pivotIndex);
		array = selectRec(array, pivotIndex);

		return (array);
	}

	private static int medianOfThree(int[] array)
	{
		int first = 0;
		int middle = array.length / 2;
		int last = array.length - 1;

		if ((array[first] >= array[middle] && array[first] <= array[last])
			|| (array[first] <= array[middle] && array[first] >= array[last]))
		{
			return (first);
		}
		else if (
			(array[middle] >= array[first] && array[middle] <= array[last])
				|| (array[middle] <= array[first] && array[middle] >= array[last]))
		{
			return (middle);
		}
		else
		{
			return (last);
		}
	}

	private static int[] insertionSort(int array[])
	{
		for (int sortedSize = 1; sortedSize < array.length; sortedSize++)
		{
			int index = 0;
			int current = array[sortedSize];

			while (array[index] <= current && index < sortedSize)
			{
				index++;
			}

			int index2 = sortedSize;

			while (index2 > index)
			{
				array[index2] = array[index2 - 1];
				index2--;
			}

			array[index] = current;
		}

		return (array);
	}

	private static int[] basicRec(int[] array, int pivotIndex)
	{
		int l[] = new int[pivotIndex];
		System.arraycopy(array, 0, l, 0, l.length);
		l = quickSort(l);

		int g[] = new int[(array.length - 1) - pivotIndex];
		System.arraycopy(array, (pivotIndex + 1), g, 0, g.length);
		g = quickSort(g);

		System.arraycopy(l, 0, array, 0, l.length);
		System.arraycopy(g, 0, array, (pivotIndex + 1), g.length);

		return (array);
	}

	private static int[] selectRec(int[] array, int pivotIndex)
	{
		int l[] = new int[pivotIndex];
		System.arraycopy(array, 0, l, 0, l.length);
		if (l.length > THRESHOLD)
		{
			l = quickSort(l);
		}
		else
		{
			l = insertionSort(l);
		}

		int g[] = new int[(array.length - 1) - pivotIndex];
		System.arraycopy(array, (pivotIndex + 1), g, 0, g.length);
		if (g.length > THRESHOLD)
		{
			g = quickSort(g);
		}
		else
		{
			g = insertionSort(g);
		}

		System.arraycopy(l, 0, array, 0, l.length);
		System.arraycopy(g, 0, array, (pivotIndex + 1), g.length);

		return (array);
	}
}
