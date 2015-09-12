import java.util.Random;

/////////////////////////////////////////////////
////////////////////// GSAT /////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class GSAT
{
	private static final int N = 100;
	private static final int C = 1000;
	private static final int MAX_RESTARTS = N;
	private static final int MAX_CLIMBS = 5 * N;

	/**
	 * Generates a random 3SAT problem and attempts to solve it, outputting the truth assignment that solved the
	 * solution apon success or outputting that no solution was found if this is the case.
	 */
	public static void main(String[] args)
	{
		Random3SATProblem problem = new Random3SATProblem(N, C);

		//output3SATProblem(problem);
		
		long startTime = System.currentTimeMillis();

		boolean solution[] = findSolution(problem);
		
		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));

		if (solution != null)
		{
			System.out.println("Solution found:");
			outputTruthAssignment(solution);
		}
		else
		{
			System.out.println("No solution found.");
		}
	}

	/**
	 * Attempts to find a solution to a 3SAT problem by generating random truth assignments for the variables in the
	 * problem. MAX_RESTARTS truth assignments are generated. For every randomly generated truth assignment the
	 * algorithm will find MAX_CLIMBS successors to the truth assignment.
	 * 
	 * @param problem The 3SAT problem to be solved.
	 * 
	 * @return The truth assignment that was successful in solving the problem or null if no solution was found.
	 */
	private static boolean[] findSolution(Random3SATProblem problem)
	{
		boolean truthAssignment[];
		boolean truthAssignmentResults[] = new boolean[N];
		Random rand = new Random(System.currentTimeMillis());
		
		resetResults(truthAssignmentResults);

		for (int i = 0; i < MAX_RESTARTS; i++)
		{
			truthAssignment = GenerateTruthAssignment(rand);

			for (int j = 0; j < MAX_CLIMBS; j++)
			{
				if (checkTruthAssignment(truthAssignment, truthAssignmentResults, problem))
				{
					return (truthAssignment);
				}

				generateSuccessorFor(truthAssignment, truthAssignmentResults);
				resetResults(truthAssignmentResults);
			}
		}

		return (null);
	}

	/**
	 * Generates a random truth assignment for each variable in the 3SAT problem.
	 * 
	 * @param rand A random number generator. This is passed as a parameter so that many calls to this method in
	 * quick succession will generate different results.
	 * 
	 * @return The randomly generated truth assignment.
	 */
	private static boolean[] GenerateTruthAssignment(Random rand)
	{
		boolean truthAssignment[] = new boolean[N];

		for (int index = 0; index < N; index++)
		{
			truthAssignment[index] = rand.nextBoolean();
		}

		return (truthAssignment);
	}

	/**
	 * Checks a 3SAT problem against a truth assignment to see if all of the clauses in the 3SAT problem will evaluate
	 * to true. Also keeps track of whether a variable has been included in a clause and evaluated to false - this
	 * information can be used to determine a good successor for the truth assignment currently being used.
	 * 
	 * @param truthAssignment[] The truth assignment to check against the 3SAT problem.
	 * @param truthAssignmentResults[] Any variables that have been included in a clause and evaluated to false.
	 * @param problem The 3SAT problem to check the truth assignment against.
	 * 
	 * @return True if the truth assignment solved the 3SAT problem, false otherwise.
	 */
	private static boolean checkTruthAssignment(
		boolean truthAssignment[],
		boolean truthAssignmentResults[],
		Random3SATProblem problem)
	{
		for (int clauseIndex = 0; clauseIndex < C; clauseIndex++)
		{
			for (int varIndex = 0; varIndex < 3; varIndex++)
			{
				// If the truth assignment for the variable being checked if the same as the truth assignment for it in
				// the cluase.
				if (truthAssignment[problem.clauses[clauseIndex].vars[varIndex]]
					== problem.clauses[clauseIndex].truth[varIndex])
				{
					// Check the next clause if one exists.
					varIndex = 3;
					clauseIndex++;
				}
				else
				{
					truthAssignmentResults[problem.clauses[clauseIndex].vars[varIndex]] = false;

					// If this is the last variable in the clause this clause must not evaluate to true.
					if (varIndex == 2)
					{
						return (false);
					}
				}
			}
		}

		return (true);
	}

	/**
	 * Generates a successor for a truth assignment. A successor is a truth assignment where only one variable has
	 * a different truth value to the original truth assignment. It uses the results of the attempt to solve the 3SAT
	 * problem with the truth assignment to find an appropriate variable to change.
	 * 
	 * @param truthAssignment[] The truth assignment to change.
	 * @param truthAssignmentResults[] The results of the attempt to solve the 3SAT problem.
	 */
	private static void generateSuccessorFor(boolean truthAssignment[], boolean truthAssignmentResults[])
	{
		for (int index = 0; index < N; index++)
		{
			if (truthAssignmentResults[index] == false)
			{
				if (truthAssignment[index])
				{
					truthAssignment[index] = false;
				}
				else
				{
					truthAssignment[index] = true;
				}

				break;
			}
		}
	}

	/**
	 * Outputs the values of all the variables in the clauses in the problem and the number of the variable.
	 * 
	 * @param problem The 3SAT problem to be outputted.
	 */
	private static void output3SATProblem(Random3SATProblem problem)
	{
		for (int clauseIndex = 0; clauseIndex < C; clauseIndex++)
		{
			for (int varIndex = 0; varIndex < 3; varIndex++)
			{
				System.out.print(
					problem.clauses[clauseIndex].vars[varIndex]
						+ ": "
						+ problem.clauses[clauseIndex].truth[varIndex]
						+ " ");
			}

			System.out.println("");
		}
	}

	/**
	 * Outputs the values of the variables in the 3SAT problem that the truth assignment represents. Variables are in
	 * numerical order.
	 * 
	 * @param truthAssignment[] The truth assignment to be outputted.
	 */
	private static void outputTruthAssignment(boolean truthAssignment[])
	{
		for (int index = 0; index < N; index++)
		{
			System.out.print(truthAssignment[index] + " ");
		}
	}
	
	/**
	 * Resets all of the results of an attempt to to solve the 3SAT problem with a truth assignment to true so that
	 * results can be found again.
	 * 
	 * @param truthAssignmentResults[] The results to reset.
	 */
	private static void resetResults(boolean truthAssignmentResults[])
	{
		for (int index = 0; index < truthAssignmentResults.length; index++)
		{
			truthAssignmentResults[index] = true;
		}
	}
}
