/////////////////////////////////////////////////
/////////////////// Skip Node ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class SkipNode
{
	/**
	 * Creates an instance of a Skip Node with value 'n'.
	 * 
	 * @param n The value of the Skip Node.
	 */
	public SkipNode(int n)
	{
		value = n;
	}
	
	private int value;
	
	private SkipNode before = null;
	
	private SkipNode after = null;
	
	private SkipNode above = null;
	
	private SkipNode below = null;
	
	public int getValue()
	{
		return (value);
	}

	public void setValue(int n)
	{
		value = n;
	}
	
	public SkipNode getBefore()
	{
		return (before);
	}

	public void setBefore(SkipNode node)
	{
		before = node;
	}
	
	public SkipNode getAfter()
	{
		return (after);
	}

	public void setAfter(SkipNode node)
	{
		after = node;
	}
	
	public SkipNode getAbove()
	{
		return (above);
	}

	public void setAbove(SkipNode node)
	{
		above = node;
	}
	
	public SkipNode getBelow()
	{
		return (below);
	}

	public void setBelow(SkipNode node)
	{
		below = node;
	}
}
