import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Provides the means to customize a quote producer bean.
 * 
 * @author gb21
 */
public class QuoteProducerCustomizer extends JPanel implements ActionListener, Customizer
{
    /**
     * Contains the number to be set as the size of the window of the summarizer object.
     */
    private JComboBox names = new JComboBox();
    
    /**
     * The object this customizer will edit.
     */
    private QuoteProducer object = null;
    
    private static final long serialVersionUID = 0;
    
    /**
     * Creates an instance of QuoteProducerCustomizer.
     */
    public QuoteProducerCustomizer()
    {
        names.addActionListener(this);
        
        add(new JLabel("Currency:"));
        add(names);
    }
    
    public void actionPerformed(ActionEvent event)
    {        
        object.setCurrencyName((String) names.getSelectedItem());
    }
    
    public void setObject(Object object)
    {
        this.object = (QuoteProducer) object;
        
        String[] temp = object.getCurrencyNames();
        
        for (int index = 0; index < temp.length; index++)
        {
            names.addItem(temp[index]);
        }
    }
}
