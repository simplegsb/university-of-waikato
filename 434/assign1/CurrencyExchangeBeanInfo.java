import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.SimpleBeanInfo;

/**
 * 
 * 
 * @author gb21
 */
public class CurrencyExchangeBeanInfo extends SimpleBeanInfo
{
    public BeanDescriptor getBeanDescriptor()
    {
        return (new BeanDescriptor(CurrencyExchange.class, CurrencyExchangeCustomizer.class));
    }

    public EventSetDescriptor[] getEventSetDescriptors()
    {
        EventSetDescriptor desriptor = null;
        
        try
        {
            desriptor = new EventSetDescriptor(CurrencyExchange.class, "currencyExchange",
                    CurrencyExchangeListener.class, "processCurrencyExchangeEvent");
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
        }
        
        return (new EventSetDescriptor[] {desriptor});
    }
}
