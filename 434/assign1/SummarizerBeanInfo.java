import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;

/**
 * 
 * 
 * @author gb21
 */
public class SummarizerBeanInfo
{
    public BeanDescriptor getBeanDescriptor()
    {
        return (new BeanDescriptor(Summarizer.class, SummarizerCustomizer.class));
    }

    public EventSetDescriptor[] getEventSetDescriptors()
    {
        EventSetDescriptor desriptor = null;
        
        try
        {
            desriptor = new EventSetDescriptor(Summarizer.class, "display", DisplayListener.class,
                    "processDisplayEvent");
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
        }
        
        return (new EventSetDescriptor[] {desriptor});
    }
}
