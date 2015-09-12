import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/////////////////////////////////////////////////
//////////////////// Get Gold ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class GetGold
{
	final static int INFINITY = 99999;

	/**
	 * Reads text in from the file dictated by the name passed to the program. Analyses the text to determine values
	 * for the maze such as the enty point, exit point and values for each corridor.
	 * Then determines the path through the maze which will result in the aquizition of the most gold.
	 * Outputs this path (a list of vertices) and the amount of gold collected.
	 */
	public static void main(String[] args)
	{
		DAG myDAG = new DAG();
		ArrayList corridors = new ArrayList();
		int enter = -1, exit = -1;
		int goldCollected;

		// Reads the file passed to the program and determines the values for the entry, exit and corridors.
		try
		{
			BufferedReader bufReader = new BufferedReader(new FileReader(new File(args[0])));
			String line;
			String keywords[];

			while ((line = bufReader.readLine()) != null)
			{
				keywords = line.split(" ");

				if (keywords[0].compareTo("ENTER") == 0)
				{
					enter = Integer.valueOf(keywords[1]).intValue();
				}
				else if (keywords[0].compareTo("EXIT") == 0)
				{
					exit = Integer.valueOf(keywords[1]).intValue();
				}
				else if (keywords[0].compareTo("CORRIDOR") == 0)
				{
					corridors.add(
						new int[] {
							Integer.valueOf(keywords[1]).intValue(),
							Integer.valueOf(keywords[2]).intValue(),
							Integer.valueOf(keywords[3]).intValue()});
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("File reading error.");
		}

		addVertices(myDAG, corridors);

		if (addEdges(myDAG, corridors) == -1)
		{
			System.out.println("Invalid maze format.");
			System.exit(1);
		}

		DAGVertex origins[] = new DAGVertex[myDAG.getVertices().size()];

		goldCollected = DagDistances(myDAG, origins, enter, exit);

		if (goldCollected != 0)
		{
			outputPath(myDAG, origins, enter, exit);
			System.out.println("Gold collected: " + goldCollected);
		}
		else
		{
			System.out.println("Invalid maze format.");
		}
	}

	/**
	 * Uses the basic DagDistances algorithm for finding the longest path with an addition to record the path taken to
	 * find this path.
	 * 
	 * PSEUDO CODE:
	 * 
	 * Algorithm DagDistances (G, s)
	 *   for all v elements in G.vertices()
	 *     setDistance(V, 0)
	 *   perform topological sort of G.vertices()
	 *   for u = 1 to n do
	 *     for each e element of G.outEdges(u)
	 *       z = G.opposite(u, e)
	 *       r = getDistance(u) + weight(e)
	 *       if r > getDistance(z)
	 *         setDistance(z, r)
	 *         setOrigin(z, u)
	 * 
	 * @param myDAG The DAG to find the longest path within.
	 * @param origins[] A record of the vertex in the longest path immediately before the vertex in question (which is
	 * stored at the index equal to the vertex number), used for recording the path taken to reach any given vertex.
	 */
	private static int DagDistances(DAG myDAG, DAGVertex origins[], int s, int t)
	{
		ArrayList vertices = myDAG.getVertices();
		int n = vertices.size();
		int distances[] = new int[n];

		// Temporary local variables.
		Iterator iter;
		DAGVertex currentVertex;
		DAGVertex currentVertexOpp;
		DAGEdge currentEdge;

		// Initialize all of the distances of all the vertices from vertex s.
		iter = vertices.iterator();
		while (iter.hasNext())
		{
			currentVertex = (DAGVertex) iter.next();
			distances[currentVertex.getID()] = 0;
		}

		Iterator sortedVerticesIter = topologicalSort(myDAG, s).iterator();
		if (sortedVerticesIter == null)
		{
			return (0);
		}

		// For every vertex (in topological order).
		while (sortedVerticesIter.hasNext())
		{
			currentVertex = (DAGVertex) sortedVerticesIter.next();
			iter = currentVertex.getOutEdges().iterator();
			int r;

			// For every outgoing edge from the current vertex check the distances to the opposite vertex.
			while (iter.hasNext())
			{
				currentEdge = (DAGEdge) iter.next();
				currentVertexOpp = myDAG.opposite(currentVertex, currentEdge);
				r = distances[currentVertex.getID()] + currentEdge.getWeight();

				// If a longer total distance is found using the edge from the current vertex to the opposite vertex
				// replace the value for the distance to the opposite vertex with the new value and set the origin of
				// the opposite vertex to the current vertex (to record the path taken).
				if (r > distances[currentVertexOpp.getID()])
				{
					distances[currentVertexOpp.getID()] = r;
					origins[currentVertexOpp.getID()] = currentVertex;
				}
			}
		}

		return (distances[t]);
	}

	/**
	 * Using an array for each corridor which stipulates the beginning and vertices as well as the gold in the
	 * corridor this method adds all vertices needed to create the fully traversable DAG.
	 * 
	 * @param myDAG The DAG to add the vertices to.
	 * @param corridors All of the corridor arrays.
	 */
	private static void addVertices(DAG myDAG, ArrayList corridors)
	{
		Iterator corridorIter;
		Iterator verticesIter;
		DAGVertex currentVertex;

		// Check both start and end vertices for every corridor.
		for (int corrIndex = 0; corrIndex < 2; corrIndex++)
		{
			corridorIter = corridors.iterator();

			// For every corridor.
			while (corridorIter.hasNext())
			{
				verticesIter = myDAG.getVertices().iterator();
				int corridorInfo[] = (int[]) corridorIter.next();
				boolean vertexFound = false;

				// Check that the vertex has not already been added.
				while (verticesIter.hasNext() && vertexFound == false)
				{
					currentVertex = (DAGVertex) verticesIter.next();
					if (currentVertex.getID() == corridorInfo[corrIndex])
					{
						vertexFound = true;
					}
				}

				if (vertexFound == false)
				{
					myDAG.addVertex(corridorInfo[corrIndex]);
				}
			}
		}
	}

	/**
	 * Using an array for each corridor which stipulates the beginning and vertices as well as the gold in the
	 * corridor this method adds all edges to the DAG.
	 * 
	 * @param myDAG The DAG to add the vertices to.
	 * @param corridors All of the corridor arrays.
	 * 
	 * @return -1 if an error occured whilst attempting to add edges (needed vertices were not in the DAG) or 0 if
	 * the method completed successfully.
	 */
	private static int addEdges(DAG myDAG, ArrayList corridors)
	{
		Iterator corridorIter = corridors.iterator();
		Iterator verticesIter;

		// For every corridor.
		while (corridorIter.hasNext())
		{
			int corridorInfo[] = (int[]) corridorIter.next();
			verticesIter = myDAG.getVertices().iterator();

			// Find the vertices required to add this edge.
			DAGVertex startVertex = null, endVertex = null, currentVertex;
			while (verticesIter.hasNext() && (startVertex == null || endVertex == null))
			{
				currentVertex = (DAGVertex) verticesIter.next();
				if (currentVertex.getID() == corridorInfo[0])
				{
					startVertex = currentVertex;
				}
				if (currentVertex.getID() == corridorInfo[1])
				{
					endVertex = currentVertex;
				}
			}

			if (startVertex == null || endVertex == null)
			{
				return (-1);
			}

			myDAG.addEdge(corridorInfo[2], startVertex, endVertex);
		}

		return (0);
	}

	/**
	 * Sorts the vertices of the given DAG into topological order.
	 * 
	 * @param myDAG The DAG to sort vertices within.
	 * @param s The vertex to start the sort at.
	 * 
	 * @return The sorted vertices.
	 */
	private static ArrayList topologicalSort(DAG myDAG, int s)
	{
		ArrayList vertices = myDAG.getVertices();
		int n = vertices.size();
		int inDegrees[] = new int[n];

		ArrayList aux = new ArrayList();
		ArrayList sortedVertices = new ArrayList();

		Iterator iter;
		DAGVertex currentVertex;
		DAGVertex currentVertexOpp;

		iter = vertices.iterator();
		while (iter.hasNext())
		{
			currentVertex = (DAGVertex) iter.next();
			inDegrees[currentVertex.getID()] = currentVertex.getInEdges().size();

			if (currentVertex.getID() == s)
			{
				aux.add(0, currentVertex);
			}
		}

		int index = 1;
		while (aux.size() != 0)
		{
			currentVertex = (DAGVertex) aux.remove(0);
			sortedVertices.add(currentVertex);
			index++;

			iter = currentVertex.getOutEdges().iterator();
			while (iter.hasNext())
			{
				currentVertexOpp = myDAG.opposite(currentVertex, (DAGEdge) iter.next());
				inDegrees[currentVertexOpp.getID()] -= 1;

				if (inDegrees[currentVertexOpp.getID()] == 0)
				{
					aux.add(0, currentVertexOpp);
				}
			}
		}

		if (index > n)
		{
			return (sortedVertices);
		}
		else
		{
			System.out.println("Cycles found...");
			return (null);
		}
	}

	/**
	 * Outputs the longest path using the origins recorded during the search for the longest path.
	 * 
	 * @param myDAG The DAG that the path was found within.
	 * @param origins[] The origins recorded during the search for the longest path.
	 * @param s The vertex the path starts at.
	 * @param t The vertex the path ends at.
	 */
	private static void outputPath(DAG myDAG, DAGVertex origins[], int s, int t)
	{
		Iterator verticesIter = myDAG.getVertices().iterator();
		DAGVertex currentVertex = null;
		while (verticesIter.hasNext())
		{
			currentVertex = (DAGVertex) verticesIter.next();
			if (currentVertex.getID() == t)
			{
				break;
			}
		}

		Iterator pathIter = findPath(origins, s, currentVertex).iterator();
		System.out.println("Path taken:");
		while (pathIter.hasNext())
		{
			System.out.print(((DAGVertex) pathIter.next()).getID() + " ");
		}
		System.out.println("");
	}

	/**
	 * Derives the path taken using the origins recorded during the search for the longest path.
	 * 
	 * @param origins[] The origins recorded during the search for the longest path.
	 * @param s The vertex the path starts at.
	 * @param t The vertex the path ends at.
	 * 
	 * @return The path found.
	 */
	private static ArrayList findPath(DAGVertex origins[], int s, DAGVertex t)
	{
		ArrayList path = new ArrayList();
		int index = t.getID();

		path.add(0, t);
		while (index > s)
		{
			path.add(0, origins[index]);
			index = origins[index].getID();
		}

		return (path);
	}
}
