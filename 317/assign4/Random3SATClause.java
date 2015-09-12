import java.util.Random;

/////////////////////////////////////////////////
/////////////// Random 3SAT Clause //////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Random3SATClause
{
	public int vars[] = new int[3];

	public boolean truth[] = new boolean[3];

	/**
	 * Creates an instance of Random3SATClause, a clause in a 3SAT problem using random variables
	 * (numbered 0 to N - 1) and random truth values for those variables.
	 * 
	 * @param N The number of variables in the 3SAT problem.
	 * @param rand A random number generator. This is passed as a parameter so that many calls to this method in
	 * quick succession will generate different results.
	 */
	public Random3SATClause(int N, Random rand)
	{
		int currentVar = -1;
		boolean varUsed;

		for (int index = 0; index < 3; index++)
		{
			do
			{
				varUsed = false;
				
				// Choose the variable to use.
				do
				{
					currentVar = rand.nextInt() % N;
				}
				while (currentVar < 0);
				
				// If the variable has already been used in this clause.
				for (int setVar = 0; setVar < index && !varUsed; setVar++)
				{
					if (currentVar == vars[setVar])
					{
						varUsed = true;
					}
				}
			}
			while (varUsed);
			
			vars[index] = currentVar;
			truth[index] = rand.nextBoolean();
		}
	}
}
