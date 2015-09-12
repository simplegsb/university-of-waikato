import java.beans.Customizer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Provides the means to customize a currency exchange bean.
 * 
 * @author gb21
 */
public class CurrencyExchangeCustomizer extends JPanel implements Customizer
{
    /**
     * When this is checked the currency exchange object is enabled. When this is not checked, it is not enabled.
     */
    private JCheckBox checkbox = new JCheckBox();
    
    /**
     * The object this customizer will edit.
     */
    private CurrencyExchange object = null;
    
    private static final long serialVersionUID = 0;
    
    /**
     * Creates an instance of CurrencyExchangeCustomizer.
     */
    public CurrencyExchangeCustomizer()
    {
        add(new JLabel("Check to enable currency exchange:"));
        add(checkbox);
    }
    
    public void setObject(Object object)
    {
        this.object = (CurrencyExchange) object;
    }
}
