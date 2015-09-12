import java.util.Random;
import java.util.TreeMap;

/////////////////////////////////////////////////
//////////////////// Sorting ////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Accessing
{
	public static void main(String[] args)
	{
		int N = 10000;
		int k = 1;
		int sum = 0;

		// The structure to use for holding the values:
		SplayTree structure = new SplayTree();
		//SkipList structure = new SkipList();
		//TreeMap structure = new TreeMap();

		for (int index = 0; index < (k * N); index += 2)
		{
			structure.insert(index);
			//structure.put(new Integer(index), new Integer(index));
		}

		long startTime = System.currentTimeMillis();

		for (int repeat = 0; repeat < 10; repeat++)
		{
			for (int index = 0; index < (k * N) / 10; index++)
			{
				SplayNode result = structure.find(structure.getRoot(), index);
				//SkipNode result = structure.find(structure.getStart(), index);
				//Object result = structure.get(new Integer(index));

				if (result.getValue() == index)
					//if (result != null)
				{
					sum += index;
				}
			}
		}

		System.out.println("Sum = " + sum);
		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
	}
}
