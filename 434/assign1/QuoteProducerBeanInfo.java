import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;

/**
 * 
 * 
 * @author gb21
 */
public class QuoteProducerBeanInfo
{
    public BeanDescriptor getBeanDescriptor()
    {
        return (new BeanDescriptor(QuoteProducer.class, QuoteProducerCustomizer.class));
    }

    public EventSetDescriptor[] getEventSetDescriptors()
    {
        EventSetDescriptor desriptor0 = null;
        EventSetDescriptor desriptor1 = null;
        
        try
        {
            desriptor0 = new EventSetDescriptor(QuoteProducer.class, "currencyExchange",
                    CurrencyExchangeListener.class, "processCurrencyExchangeEvent");
            desriptor1 = new EventSetDescriptor(QuoteProducer.class, "display", DisplayListener.class,
                    "processDisplayEvent");
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
        }
        
        return (new EventSetDescriptor[] {desriptor0, desriptor1});
    }
}
