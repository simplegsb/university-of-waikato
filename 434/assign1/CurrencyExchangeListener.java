import java.util.EventListener;

/**
 * Enables classes to listen for <code>CurrencyExchangeEvent</code>s.
 * 
 * @author gb21
 */
public interface CurrencyExchangeListener extends EventListener
{
    public void processCurrencyExchangeEvent(CurrencyExchangeEvent event);
}
