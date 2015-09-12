import java.util.TimerTask;

/**
 * Broadcasts currency data to any listeners of <code>source</code>.
 * 
 * @author gb21
 */
public class CurrencyExchangeTask extends TimerTask
{
    /**
     * The <code>CurrencyExchange</code> that created this task.
     */
    private CurrencyExchange source;
    
    /**
     * Creates an instance of CurrencyExchangeTask.
     * 
     * @param source The <code>CurrencyExchange</code> that created this task.
     */
    public CurrencyExchangeTask(CurrencyExchange source)
    {
        this.source = source;
    }
    
    /**
     * Broadcasts currency data to any listeners of <code>source</code>.
     */
    public void run()
    {
        source.broadcastCurrencyData();
    }
}
