import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import java.util.Timer;

/**
 * Represents a source of exchange rate currency data. If this bean is enabled it will create events at a regular
 * interval to send new exchange rate currency data to any listeners.
 * 
 * @author gb21
 */
public class CurrencyExchange implements Serializable
{
    /**
     * All listeners currently registered with this source.
     */
    private Vector listeners = new Vector();
    
    /**
     * The names of all the exchange rate currency data presented in <code>filename</code>.
     */
    private String[] currencyNames = null;

    /**
     * The reader of the file <code>filename</code>.
     */
    private BufferedReader currencyReader = null;

    /**
     * This bean will create events if this variable has the value <code>true</code>.
     */
    private boolean enabled = false;

    /**
     * The name of the file to read exchange rate currency data from.
     */
    private String filename = "currencyData.txt";
    
    private static final long serialVersionUID = 0;
    
    /**
     * Responsible for scheduling the broadcasts of currency data to any listeners.
     */
    private Timer timer = new Timer();

    /**
     * Creates an instance of CurrencyExchangeBean.
     */
    public CurrencyExchange()
    {
        try
        {
            currencyReader = new BufferedReader(new FileReader(new File(filename)));
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Cannot find file: " + filename);
            ex.printStackTrace();
        }

        try
        {
            currencyNames = currencyReader.readLine().split(",");
        }
        catch (IOException ex)
        {
            System.err.println(filename + " is not a valid format.");
            ex.printStackTrace();
        }
        
        timer.schedule(new CurrencyExchangeTask(this), 0, 1000);
    }
    
    /**
     * Adds a listener to this source.
     * 
     * @param newListener The listener to add.
     */
    public void addCurrencyExchangeListener(CurrencyExchangeListener newListener)
    {
        listeners.addElement(newListener);
    }
    
    /**
     * Broadcasts a new set of currency data and corresponding names to any listeners.
     * 
     * If no more currency data is available to be sent no events will be sent.
     */
    public void broadcastCurrencyData()
    {
        if (enabled)
        {
            double[] currencyData = getCurrencyData();
        
            if (currencyData.length > 0)
            {
                fireCurrencyExchangeEvent(new CurrencyExchangeEvent(this, currencyData, currencyNames));
            }
            else
            {
                timer.cancel();
            }
        }
    }

    /**
     * Sends <code>event</code> to any listeners.
     * 
     * @param event The event to send to any listeners.
     */
    public void fireCurrencyExchangeEvent(CurrencyExchangeEvent event)
    {
        Vector temp;

        synchronized (this)
        {
            temp = (Vector) listeners.clone();
        }

        for (int index = 0; index < temp.size(); index++)
        {
            ((CurrencyExchangeListener) temp.get(index)).processCurrencyExchangeEvent(event);
        }
    }

    /**
     * Returns the next line of <code>filename</code> formatted as an array of double values.
     * 
     * @return The next line of <code>filename</code> formatted as an array of double values.
     */
    private double[] getCurrencyData()
    {
        String[] currencyDataStrings = null;
        double[] currencyData = null;

        try
        {
            currencyDataStrings = currencyReader.readLine().split(",");
            currencyData = new double[currencyDataStrings.length];

            for (int index = 0; index < currencyDataStrings.length; index++)
            {
                currencyData[index] = Double.parseDouble(currencyDataStrings[index]);
            }
        }
        catch (Exception ex)
        {
            System.err.println(filename + " is not a valid format.");
            ex.printStackTrace();
        }

        return (currencyData);
    }

    /**
     * Returns true if this bean is enabled, false otherwise.
     * 
     * This bean will only create events if it is enabled.
     * 
     * @return True if this bean is enabled, false otherwise.
     */
    public boolean isEnabled()
    {
        return (enabled);
    }

    /**
     * Removes a listener from this source.
     * 
     * @param newListener The listener to remove.
     */
    public void removeCurrencyExchangeListener(CurrencyExchangeListener listener)
    {
        listeners.removeElement(listener);
    }

    /**
     * Enables or disables this bean.
     * 
     * @param newEnabled The new enabled status of this bean.
     */
    public void setEnabled(boolean newEnabled)
    {
        enabled = newEnabled;
    }
}
