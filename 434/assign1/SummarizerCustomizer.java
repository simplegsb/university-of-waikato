import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Provides the means to customize a summarizer bean.
 * 
 * @author gb21
 */
public class SummarizerCustomizer extends JPanel implements ActionListener, Customizer
{
    /**
     * The object this customizer will edit.
     */
    private Summarizer object = null;
    
    private static final long serialVersionUID = 0;
    
    /**
     * Contains the number to be set as the size of the window of the summarizer object.
     */
    private JTextField window = new JTextField(40);
    
    /**
     * Creates an instance of SummarizerCustomizer.
     */
    public SummarizerCustomizer()
    {
        window.addActionListener(this);
        
        add(new JLabel("Window Size:"));
        add(window);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        int windowSize;
        try
        {
            windowSize = Integer.parseInt(window.getText());
        }
        catch (NumberFormatException ex)
        {
            return;
        }
        
        if (windowSize <= 0)
        {
            return;
        }
        
        object.setWindowSize(windowSize);
    }
    
    public void setObject(Object object)
    {
        this.object = (Summarizer) object;
    }
}
