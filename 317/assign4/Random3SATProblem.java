import java.util.Random;

/////////////////////////////////////////////////
////////////// Random 3SAT Problem //////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Random3SATProblem
{
	public Random3SATClause clauses[];
	
	/**
	 * Creates an instance of Random3SATProblem, a 3SAT problem with C clauses and random variables
	 * (numbered 1 to N - 1).
	 * 
	 * @param N The number of variables in the 3SAT problem.
	 * @param C The number of clauses in the 3SAT problem.
	 */
	public Random3SATProblem(int N, int C)
	{
		clauses = new Random3SATClause[C];
		Random rand = new Random(System.currentTimeMillis());
		
		for (int index = 0; index < C; index++)
		{
			clauses[index] = new Random3SATClause(N, rand);
		}
	}
}
