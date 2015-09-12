import java.util.ArrayList;

/////////////////////////////////////////////////
/////////////////// DAGVertex ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class DAGVertex
{
	public DAGVertex(int newID)
	{
		id = newID;
	}
	
	int id;
	
	private ArrayList inEdges = new ArrayList();
	
	private ArrayList outEdges = new ArrayList();
	
	public int getID()
	{
		return (id);
	}
	
	/**
	 * 
	 */
	public ArrayList getInEdges()
	{
		return (inEdges);
	}

	/**
	 * 
	 */
	public ArrayList getOutEdges()
	{
		return outEdges;
	}

	/**
	 * 
	 */
	public void addInEdge(DAGEdge edge)
	{
		inEdges.add(edge);
	}

	/**
	 * 
	 */
	public void addOutEdge(DAGEdge edge)
	{
		outEdges.add(edge);
	}

}
