import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * 
 * 
 * @author gb21
 */
public class DisplayerBeanInfo extends SimpleBeanInfo
{
    public BeanDescriptor getBeanDescriptor()
    {
        return (new BeanDescriptor(Displayer.class, DisplayerCustomizer.class));
    }
}
