import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.beans.Customizer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Provides the means to customize a displayer bean.
 * 
 * @author gb21
 */
public class DisplayerCustomizer extends JPanel implements ActionListener, Customizer
{
    /**
     * Contains the text to be put into the information of the displayer object.
     */
    private JTextField information = new JTextField(40);
    
    /**
     * The object this customizer will edit.
     */
    private Displayer object = null;
    
    /**
     * Contains the text to be put into the title of the displayer object.
     */
    private JTextField title = new JTextField(40);
    
    private static final long serialVersionUID = 0;
    
    /**
     * Creates an instance of DisplayerCustomizer.
     */
    public DisplayerCustomizer()
    {
        information.addActionListener(this);
        title.addActionListener(this);
        
        setLayout(new GridLayout(2, 1));
        
        JPanel titlePanel = new JPanel();
        add(titlePanel);
        
        titlePanel.add(new JLabel("Title:"));
        titlePanel.add(title);
        
        JPanel informationPanel = new JPanel();
        add(informationPanel);
        
        informationPanel.add(new JLabel("Information:"));
        informationPanel.add(information);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() == information)
        {
            object.setInformation(information.getText());
        }
        else
        {
            object.setTitle(information.getText());
        }
    }
    
    public void setObject(Object object)
    {
        this.object = (Displayer) object;
    }
}
