import java.util.ArrayList;

/////////////////////////////////////////////////
////////////////////// DAG //////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class DAG
{
	public DAG()
	{}

	private ArrayList edges = new ArrayList();

	private ArrayList vertices = new ArrayList();

	/**
	 * Returns all edges in the DAG.
	 * 
	 * @return All edges in the DAG.
	 */
	public ArrayList getEdges()
	{
		return edges;
	}

	/**
	 * Returns all vertices in the DAG.
	 * 
	 * @return All vertices in the DAG.
	 */
	public ArrayList getVertices()
	{
		return vertices;
	}

	/**
	 * Adds an edge to the DAG.
	 * 
	 * @param weight The weight of the edge.
	 * @param start The vertex this edge starts at.
	 * @param end The vertex this edge ends at.
	 */
	public void addEdge(int weight, DAGVertex start, DAGVertex end)
	{
		edges.add(new DAGEdge(weight, start, end));
	}

	/**
	 * Adds a vertex to the DAG.
	 * 
	 * @param id The number of this vertex.
	 */
	public void addVertex(int id)
	{
		vertices.add(new DAGVertex(id));
	}
	
	/**
	 * Returns the opposite vertex from the given vertex over the given edge.
	 * 
	 * @param vertex The vertex to find the opposite of.
	 * @param edge The edge to traverse over to find the opposite vertex.
	 */
	public DAGVertex opposite(DAGVertex vertex, DAGEdge edge)
	{
		if (edge.getStartVertex() == vertex)
		{
			return (edge.getEndVertex());
		}
		else if (edge.getEndVertex() == vertex)
		{
			return (edge.getStartVertex());
		}
		
		return (null);
	}
}
