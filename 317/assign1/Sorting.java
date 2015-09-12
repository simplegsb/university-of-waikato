import java.util.Arrays;
import java.util.Random;

/////////////////////////////////////////////////
//////////////////// Sorting ////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Sorting
{
	public static void main(String[] args)
	{
		Random randomNums = new Random();
		int array[] = new int[10000000];
		for (int index = 0; index < array.length; index++)
		{
			array[index] = randomNums.nextInt();
		}

		long startTime = System.currentTimeMillis();

		//array = insertionSort(array);
		//array = heapSort(array);
		Arrays.sort(array);

		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
	}

	static public int[] insertionSort(int array[])
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

	static public int[] heapSort(int array[])
	{
		ArrayHeap heap = new ArrayHeap(array.length);

		for (int index = 0; index < array.length; index++)
		{
			heap.add(array[index]);
		}
		for (int index = 0; index < array.length; index++)
		{
			heap.remove();
		}
		
		array = heap.getArray();

		return (array);
	}
}
