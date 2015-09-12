import javax.swing.JFrame;

public class CustomizerTest
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        
        QuoteProducerCustomizer cust = new QuoteProducerCustomizer();
        
        frame.setContentPane(cust);
        frame.pack();
        frame.setVisible(true);
    }
}
