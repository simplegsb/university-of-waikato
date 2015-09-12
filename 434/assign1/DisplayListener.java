import java.util.EventListener;

/**
 * Enables classes to listen for <code>DisplayEvent</code>s.
 * 
 * @author gb21
 */
public interface DisplayListener extends EventListener
{
    public void processDisplayEvent(DisplayEvent event);
}
