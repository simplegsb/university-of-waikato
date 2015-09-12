import java.util.Random;

/////////////////////////////////////////////////
///////////////////// Stacks ////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Stacks
{
	public static void main(String[] args)
	{
		//DoublingStack stack = new DoublingStack();
		//LinearGrowingStack stack = new LinearGrowingStack();
		//AdaptingStack stack = new AdaptingStack();
		ListBasedIntStack stack = new ListBasedIntStack();
		
		long startTime = System.currentTimeMillis();
		Random randomNums = new Random(startTime);
		
		for (int push = 1; push <= 10000000; push++)
		{
			stack.push(randomNums.nextInt());
		}
		
		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
	}
}
