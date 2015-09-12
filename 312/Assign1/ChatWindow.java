import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

/**
 * A window for outputting messages to the screen and obtaining messages to be sent to the server.
 * 
 * @author gb21
 */
public class ChatWindow extends JFrame
{
    /**
     * For outputting messages that have been recieved.
     */
    private JTextPane chatViewer = new JTextPane();

    /**
     * For entering messages to be sent to the server process.
     */
    private JTextField messageInputField = new JTextField();
    
    /**
     * The client process this window is representing.
     */
    private ChatClient client;

    /**
     * Creates an instance of a ChatWindow.
     * 
     * Sets up the components of the window.
     * 
     * @param newClient The client this process this window is representing.
     */
    public ChatWindow(ChatClient newClient)
    {
        client = newClient;
        
        BoxLayout layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(layout);

        getContentPane().add(new JScrollPane(chatViewer));
        JPanel messageInputPanel = new JPanel();

        BoxLayout layout2 = new BoxLayout(messageInputPanel, BoxLayout.X_AXIS);
        messageInputPanel.setLayout(layout2);
        getContentPane().add(messageInputPanel);
        messageInputPanel.add(new JLabel(" Message: "));
        messageInputField.setMaximumSize(new Dimension(2000, 60));
        messageInputPanel.add(messageInputField);
        JButton send = new JButton("Send");
        messageInputPanel.add(send);
        send.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                sendMessage();
            }
        });

        getContentPane().add(new JLabel("Commands:"));
        getContentPane().add(new JLabel("#list - lists all current clients"));
        getContentPane().add(new JLabel("#privatex - sends a private message to user number x"));
        getContentPane().add(new JLabel("#quit - quits the client"));

        chatViewer.setEditable(false);
    }

    /**
     * Outputs a message to the screen.
     * 
     * @param message The message to be output to the screen.
     */
    public void postMessage(String message)
    {
        chatViewer.setText(chatViewer.getText() + message + '\n');
    }
 
    /**
     * Outputs a message to the server process.
     */
    private void sendMessage()
    {
        client.sendMessage(messageInputField.getText() + '\n');
        messageInputField.setText("");
    }
}
