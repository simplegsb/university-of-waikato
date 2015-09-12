import java.io.Serializable;
import java.util.Vector;

/**
 * Listens for exchange rate currency data and creates events to show the change in the value of only 1 chosen currency
 * to any listeners.
 * 
 * Only changes that are greater than the threshold will create events.
 * 
 * @author gb21
 */
public class QuoteProducer implements CurrencyExchangeListener, Serializable
{
    /**
     * All currency exchange listeners currently registered with this source.
     */
    private Vector currencyExchangeListeners = new Vector();
    
    /**
     * The name of the chosen currency.
     */
    private String currencyName = null;
    
    /**
     * The names of the currencies this bean can choose to process.
     */
    private String[] currencyNames = null;

    /**
     * The current value of the chosen currency.
     */
    private double currencyValue = 0.0;
    
    /**
     * All display listeners currently registered with this source.
     */
    private Vector displayListeners = new Vector();
    
    private static final long serialVersionUID = 0;
    
    /**
     * The threshold of which changes in currency must be higher for a <code>CurrencyExchangeEvent</code> to be
     * created.
     */
    private double threshold = 0.0;

    /**
     * Adds a currency exchange listener to this source.
     * 
     * @param newListener The currency exchange listener to add.
     */
    public void addCurrencyExchangeListener(CurrencyExchangeListener newListener)
    {
        currencyExchangeListeners.addElement(newListener);
    }
    
    /**
     * Adds a display listener to this source.
     * 
     * @param newListener The display listener to add.
     */
    public void addDisplayListener(DisplayListener newListener)
    {
        displayListeners.addElement(newListener);
        
        fireDisplayEvent(new DisplayEvent(this, "Exchange rate status for " + currencyName + ":", DisplayEvent.TITLE));
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
            temp = (Vector) currencyExchangeListeners.clone();
        }

        for (int index = 0; index < temp.size(); index++)
        {
            ((CurrencyExchangeListener) temp.get(index)).processCurrencyExchangeEvent(event);
        }
    }
    
    /**
     * Sends <code>event</code> to any listeners.
     * 
     * @param event The event to send to any listeners.
     */
    public void fireDisplayEvent(DisplayEvent event)
    {
        Vector temp;

        synchronized (this)
        {
            temp = (Vector) displayListeners.clone();
        }

        for (int index = 0; index < temp.size(); index++)
        {
            ((DisplayListener) temp.get(index)).processDisplayEvent(event);
        }
    }
    
    /**
     * Returns the name of the chosen currency.
     * 
     * @return The name of the chosen currency.
     */
    public String getCurrencyName()
    {
        return (currencyName);
    }
    
    /**
     * Returns the threshold of which changes in currency must be higher for a <code>CurrencyExchangeEvent</code> to
     * be created.
     * 
     * @return The threshold of which changes in currency must be higher for a <code>CurrencyExchangeEvent</code> to
     * be created.
     */
    public double getThreshold()
    {
        return (threshold);
    }

    /**
     * Isolates the value of the chosen currency (if available in <code>event</code>) and broadcasts the change
     * between this value and <code>currentValue</code> to any listeners.
     * 
     * If the change is less than <code>threshold</code> no event will be sent to listeners of
     * <code>CurrencyExchangeEvent</code>s.
     * 
     * @param event 
     */
    public void processCurrencyExchangeEvent(CurrencyExchangeEvent event)
    {
        String[] names = event.getCurrencyNames();
        boolean currencyFound = false;

        for (int index = 0; index < names.length && !currencyFound; index++)
        {
            if (names[index].equals(currencyName))
            {
                double newValue = event.getCurrencyData(index);

                // Broadcast to DisplayListeners.
                String information;
                if (newValue > currencyValue)
                {
                    information = "Increase of " + (newValue - currencyValue) + " from " + currencyValue + " to "
                            + newValue;
                }
                else if (newValue < currencyValue)
                {
                    information = "Decrease of " + (currencyValue - newValue) + " from " + currencyValue + " to "
                            + newValue;
                }
                else
                {
                    information = "No change, stays at " + currencyValue;
                }
                fireDisplayEvent(new DisplayEvent(this, information, DisplayEvent.INFORMATION));

                // Broadcast to CurrencyExchangeListeners.
                if (Math.abs(currencyValue - newValue) >= threshold)
                {
                    fireCurrencyExchangeEvent(new CurrencyExchangeEvent(this, new double[] {newValue},
                            new String[] {currencyName}));
                }

                currencyFound = true;
                currencyValue = newValue;
            }
        }
    }
    
    /**
     * Removes a currency exchange listener from this source.
     * 
     * @param newListener The currency exchange listener to remove.
     */
    public void removeCurrencyExchangeListener(CurrencyExchangeListener listener)
    {
        currencyExchangeListeners.removeElement(listener);
    }
    
    /**
     * Removes a display listener from this source.
     * 
     * @param newListener The display listener to remove.
     */
    public void removeDisplayListener(DisplayListener listener)
    {
        displayListeners.removeElement(listener);
    }
    
    /**
     * Sets the name of the chosen currency.
     * 
     * @param newCurrencyName The name of the chosen currency.
     */
    public void setCurrencyName(String newCurrencyName)
    {
        currencyName = newCurrencyName;
        
        fireDisplayEvent(new DisplayEvent(this, "Exchange rate status for " + currencyName + ":", DisplayEvent.TITLE));
    }
    
    /**
     * Sets the threshold of which changes in currency must be higher for a <code>CurrencyExchangeEvent</code> to be
     * created.
     * 
     * @param newThresholdThe threshold of which changes in currency must be higher for a
     * <code>CurrencyExchangeEvent</code> to be created.
     */
    public void setThreshold(double newThreshold)
    {
        threshold = newThreshold;
    }
}
