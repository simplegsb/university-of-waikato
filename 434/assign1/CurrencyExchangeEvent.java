import java.util.EventObject;

/**
 * An event that represents a new set of exchange rate currency data.
 * 
 * @author gb21
 */
public class CurrencyExchangeEvent extends EventObject
{
    /**
     * The exchange rate currency data.
     */
    private double[] currencyData;
    
    /**
     * The names of the exchange rate currency data entries.
     */
    private String[] currencyNames;
    
    private static final long serialVersionUID = 0;
    
    /**
     * Creates an instance of CurrencyEvent.
     * 
     * @param source The object that created this event.
     * @param currencyData The exchange rate currency data.
     * @param currencyNames The names of the exchange rate currency data entries.
     */
    public CurrencyExchangeEvent(Object source, double[] currencyData, String[] currencyNames)
    {
        super(source);
        
        this.currencyNames = currencyNames;
        this.currencyData = currencyData;
    }
    
    /**
     * Returns the exchange rate currency data.
     * 
     * @return The exchange rate currency data.
     */
    public double[] getCurrencyData()
    {
        return (currencyData);
    }
    
    /**
     * Returns the exchange rate currency data at <code>index</code>.
     * 
     * @param index The index of the exchange rate currency data entry to return.
     * 
     * @return The exchange rate currency data at the <code>index</code>.
     */
    public double getCurrencyData(int index)
    {
        return (currencyData[index]);
    }
    
    /**
     * Returns the name of the exchange rate currency data at <code>index</code>.
     * 
     * @param index The index of the the name of exchange rate currency data entry to return.
     * 
     * @return The the name of exchange rate currency data at the <code>index</code>.
     */
    public String getCurrencyName(int index)
    {
        return (currencyNames[index]);
    }
    
    /**
     * Returns the names of the exchange rate currency data entries.
     * 
     * @return The names of the exchange rate currency data entries.
     */
    public String[] getCurrencyNames()
    {
        return (currencyNames);
    }
}
