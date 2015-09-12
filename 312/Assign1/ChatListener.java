import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.SocketException;

/**
 * This class is to be implemented by a ChatClient as a seperate thread that listens out for any incoming messages
 * from the ChatSession's currently operating.
 * 
 * @author gb21
 */
public class ChatListener extends Thread
{    
    /**
     * The ChatClient to send incoming messages to.
     */
    private ChatClient client;
    
    /**
     * Creates an instance of ChatListener.
     * 
     * @param newClient The client to associate this listener with.
     */
    public ChatListener(ChatClient newClient)
    {
        client = newClient;
    }
    
    /**
     * Continues to listen to the input stream of the port for any incoming messages. Any messages found are output
     * to the client's window.
     */
    public void run()
    {
        try
        {
            // Setup streams.
            BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            String message;
            
            while (true)
            {                
                try
                {
                    message = inFromServer.readLine();
                }
                catch (SocketException e)
                {
                    break;
                }
                
                client.getWindow().postMessage(message);
            }    
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
