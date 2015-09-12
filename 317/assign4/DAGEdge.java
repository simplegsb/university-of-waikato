/////////////////////////////////////////////////
//////////////////// DAGEdge ////////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class DAGEdge
{
	public DAGEdge(int newWeight, DAGVertex newStart, DAGVertex newEnd)
	{
		weight = newWeight;
		startVertex = newStart;
		endVertex = newEnd;
		
		startVertex.addOutEdge(this);
		endVertex.addInEdge(this);
	}
	
	private DAGVertex endVertex;
	
	private DAGVertex startVertex;
	
	private int weight;
	
	/**
	 * 
	 */
	public DAGVertex getEndVertex()
	{
		return endVertex;
	}

	/**
	 * 
	 */
	public DAGVertex getStartVertex()
	{
		return startVertex;
	}

	/**
	 * 
	 */
	public int getWeight()
	{
		return weight;
	}

	/**
	 * 
	 */
	public void setEndVertex(DAGVertex vertex)
	{
		endVertex = vertex;
	}

	/**
	 * 
	 */
	public void setStartVertex(DAGVertex vertex)
	{
		startVertex = vertex;
	}

	/**
	 * 
	 */
	public void setWeight(int i)
	{
		weight = i;
	}

}
