import java.io.Serializable;
import java.util.Vector;

/**
 * Stores most recently received values in a window.
 * 
 * Calculates the average, maximum and minimum values of all values in the window and creates events to send out this
 * information to any listeners.
 * 
 * @author gb21
 */
public class Summarizer implements CurrencyExchangeListener, Serializable
{
    /**
     * The average value of all values in the window.
     */
    private double average = 0.0;

    /**
     * All listeners currently registered with this source.
     */
    private Vector listeners = new Vector();

    /**
     * The maximum value of all values in the window.
     */
    private double maximum = 0.0;

    /**
     * The minimum value of all values in the window.
     */
    private double minimum = 0.0;

    private static final long serialVersionUID = 0;
    
    /**
     * The size of the window.
     */
    private int windowSize = 10;

    /**
     * The contents of the window.
     */
    private double[] receivedValues = new double[windowSize];

    /**
     * Adds a display listener to this source.
     * 
     * @param newListener The display listener to add.
     */
    public void addDisplayListener(DisplayListener newListener)
    {
        listeners.addElement(newListener);

        fireDisplayEvent(new DisplayEvent(this, "Summary of last " + windowSize + " values received:",
                DisplayEvent.TITLE));
    }

    /**
     * Adds <code>value</code> to the received values and removes the least recently received value still in the
     * received values.
     * 
     * @param value The value to add.
     */
    private void addToReceivedValues(double value)
    {
        double[] newReceivedValues = new double[windowSize];
        System.arraycopy(receivedValues, 1, newReceivedValues, 0, windowSize - 1);
        receivedValues = newReceivedValues;

        receivedValues[windowSize - 1] = value;
    }

    /**
     * Calculates new values for the average, maximum and minimum values.
     */
    private void calculateSummary()
    {
        maximum = Double.MIN_VALUE;
        minimum = Double.MAX_VALUE;
        double sum = 0.0;

        for (int index = 0; index < windowSize; index++)
        {
            sum += receivedValues[index];

            if (receivedValues[index] > maximum)
            {
                maximum = receivedValues[index];
            }

            if (receivedValues[index] < minimum)
            {
                minimum = receivedValues[index];
            }
        }

        average = sum / windowSize;
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
            temp = (Vector) listeners.clone();
        }

        for (int index = 0; index < temp.size(); index++)
        {
            ((DisplayListener) temp.get(index)).processDisplayEvent(event);
        }
    }

    /**
     * Returns the average value of all values in the window.
     * 
     * @return The average value of all values in the window.
     */
    public double getAverage()
    {
        return (average);
    }

    /**
     * Returns the maximum value of all values in the window.
     * 
     * @return The maximum value of all values in the window.
     */
    public double getMaximum()
    {
        return (maximum);
    }

    /**
     * Returns the minimum value of all values in the window.
     * 
     * @return The minimum value of all values in the window.
     */
    public double getMinimum()
    {
        return (minimum);
    }

    /**
     * Returns the size of the window.
     * 
     * @return The size of the window.
     */
    public int getWindowSize()
    {
        return (windowSize);
    }

    /**
     * Adds all new exchange rate currency data to the received values, calculates the summary and sends events to any
     * listeners.
     */
    public void processCurrencyExchangeEvent(CurrencyExchangeEvent event)
    {
        double[] newValues = event.getCurrencyData();

        for (int index = 0; index < newValues.length; index++)
        {
            addToReceivedValues(newValues[index]);
        }

        calculateSummary();

        fireDisplayEvent(new DisplayEvent(this, "Average: " + average + ", Maximum: " + maximum + ", Minimum: "
                + minimum, DisplayEvent.INFORMATION));
    }

    /**
     * Removes a display listener from this source.
     * 
     * @param newListener The display listener to remove.
     */
    public void removeDisplayListener(DisplayListener listener)
    {
        listeners.removeElement(listener);
    }

    /**
     * Sets the size of the window.
     * 
     * @param newWindowSize The new size of the window.
     */
    public void setWindowSize(int newWindowSize)
    {
        windowSize = newWindowSize;

        // Create a new array to hold values received.
        double[] newReceivedValues = new double[windowSize];
        int sizeDifference = Math.abs(windowSize - newWindowSize);
        if (windowSize > newWindowSize)
        {
            System.arraycopy(receivedValues, sizeDifference, newReceivedValues, 0, windowSize);
        }
        else
        {
            System.arraycopy(receivedValues, 0, newReceivedValues, sizeDifference, windowSize);
        }
        receivedValues = newReceivedValues;

        fireDisplayEvent(new DisplayEvent(this, "Summary of last " + windowSize + " values received:",
                DisplayEvent.TITLE));
    }

}
