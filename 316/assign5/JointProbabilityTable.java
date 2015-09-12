import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A table that represents the joint probability distribution for a list of variables.
 * 
 * @author gb21
 */
public class JointProbabilityTable
{
    /**
     * A count of how many evidence examples have been added to this probability table.
     */
    private float evidenceCount = 0.0f;
    
    /**
     * The name of the relation this table represents.
     */
    private String name;
    
    /**
     * The probability table represented as nested lists.
     */
    private List table;
    
    /**
     * The variables the table represents the joint probability of.
     */
    private List variables;
    
    /**
     * Creates an instance of JointProbabilityTable.
     * 
     * @param variables The variables the table represents the joint probability of.
     * @param relation The name of the relation this table represents.
     */
    public JointProbabilityTable(List variables, String relation)
    {
        this.variables = variables;
        table = buildTable(variables);
        name = relation;
    }
    
    /**
     * Builds a table to hold all combinations of values of all <code>variables</code>. All probabilities are initialised
     * to 0.0f.
     * 
     * @param The variables to build the table for.
     * 
     * @return A joint probability table suitable for holding all <code>variables</code>.
     */
    private List buildTable(List variables) throws IllegalArgumentException
    {
        if (variables.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        List table = new ArrayList();
        
        Iterator valueIter = ((List) variables.get(0)).iterator();
        String currentValue;
        while (valueIter.hasNext())
        {
            currentValue = (String) valueIter.next();
            
            if (variables.size() == 1)
            {
                table.add(new Float(0.0f));
            }
            else
            {
                List remainingVariables = new ArrayList();
                remainingVariables.addAll(variables);
                remainingVariables.remove(0);
                
                table.add(buildTable(remainingVariables));
            }
        }
        
        return (table);
    }
    
    /**
     * Adjusts table to account for the inclusion of a new example.
     * 
     * @param values The values that represent the example.
     */
    public void includeEvidence(String[] values) throws IllegalArgumentException
    {
        // If evidence is incomplete.
        if (values.length != variables.size())
        {
            throw new IllegalArgumentException();
        }
        
        List currentTable = table;
        
        for (int index = 0; index < values.length; index++)
        {            
            Iterator valueIter = ((List) variables.get(index)).iterator();
            int trueIndex = -1;
            for (int currentValueIndex = 0; valueIter.hasNext(); currentValueIndex++)
            {
                if (values[index].compareTo((String) valueIter.next()) == 0)
                {
                    updateProbabilities(currentTable, currentValueIndex);
                    
                    trueIndex = currentValueIndex;
                }
                else
                {
                    updateProbabilities(currentTable, -2);
                }
            }
            
            if (index != values.length - 1)
            {
                currentTable = (List) currentTable.get(trueIndex);
            }
        }
        
        evidenceCount++;
    }
    
    /**
     * Finds the distribution across the last variable in <code>variables</code>.
     * 
     * RETURNS DISTRIBUTION OVER play IN EXAMPLE
     * COULD NOT GET THIS TO WORK.
     * 
     * @param values
     * 
     * @return
     */
    public float[] getDistribution(String[] values) throws IllegalArgumentException
    {
        float[] dist = new float[2];
        dist[1] = 0.0f;
        
        // If query is incomplete.
        if (values.length != variables.size())
        {
            throw new IllegalArgumentException();
        }
        
        List currentTable = table;
        
        for (int index = 0; index < values.length; index++)
        {            
            Iterator valueIter = ((List) variables.get(index)).iterator();
            int trueIndex = -1;
            for (int currentValueIndex = 0; valueIter.hasNext(); currentValueIndex++)
            {
                if (values[index].compareTo((String) valueIter.next()) == 0)
                {
                    trueIndex = currentValueIndex;
                }
            }
            
            if (index != values.length - 1)
            {
                currentTable = (List) currentTable.get(trueIndex);
            }
            else
            {
                Iterator probIter = currentTable.iterator();
                for (int i = 0; probIter.hasNext(); i++)
                {
                    if (i == trueIndex)
                    {
                        dist[0] = ((Float) probIter.next()).floatValue();
                    }
                    else
                    {
                        dist[1] += ((Float) probIter.next()).floatValue();
                    }
                }
            }
        }
        
        return (dist);
    }
    
    /**
     * Returns the name of the relation this table represents.
     * 
     * @return The name of the relation this table represents.
     */
    public String getName()
    {
        return (name);
    }
    
    /**
     * Updates all probability values in <code>table</code>, increasing those that are in the list within this list at
     * <code>index</code> and decreasing those in other lists.
     * 
     * If this function is called with <code>index = -1</code> all probabilities will be increased.
     * If this function is called with <code>index = -1</code> all probabilities will be decreased.
     * 
     * @param table The table to update probabilities in.
     * @param index The index of the list to increase probabilities in.
     */
    private void updateProbabilities(List table, int index)
    {
        Iterator tableIter = table.iterator();
        List currentTable;
        Object object;
        for (int currentIndex = 0; tableIter.hasNext(); currentIndex++)
        {
            object = tableIter.next();
            
            if (currentIndex == index || currentIndex == -1)
            {
                if (object instanceof Float)
                {
                    float prob = ((Float) object).floatValue();
                    ((Float) object).valueOf(((evidenceCount * prob) + 1)/(evidenceCount + 1));
                }
                else
                {
                    updateProbabilities((List) object, -1);
                }
            }
            else
            {
                if (object instanceof Float)
                {
                    float prob = ((Float) object).floatValue();
                    ((Float) object).valueOf((evidenceCount * prob)/(evidenceCount + 1));
                }
                else
                {
                    updateProbabilities((List) object, -2);
                }
            }
        }
    }
}
