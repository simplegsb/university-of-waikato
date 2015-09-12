import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

/**
 * The client process with which users and communicate with each-other via the server process.
 * 
 * @author gb21
 */
public class ChatClient
{
    /**
     * The socket connecting this client process to the server process.
     */
    private Socket socket;

    /**
     * An output stream to the server process.
     */
    private DataOutputStream outToServer;
    
    /**
     * The window to use for outputting messages incoming to this ChatClient.
     */
    private ChatWindow window;

    /**
     * Creates an instance of a ChatClient.
     */
    public ChatClient()
    {
        
    }
    
    /**
     * Creates a socket to communicate with the server process. Implements a ChatListener to listen for any incoming
     * messages from the server process.
     * 
     * Continues to listen for any outgoing messages and sends them to the server process if they are found.
     * 
     * Closes the socket and ends this process when the user enters "#quit" into the ChatClient.
     * 
     * @param serverHostName The host name of the server process.
     * @param serverPort The port number of the server process.
     */
    public void connect(String serverHostName, String serverPort)
    {
        // Attempt to make connection to a server process.
        try
        {
            socket = new Socket(serverHostName, Integer.parseInt(serverPort));
        }
        catch (IOException e)
        {
            System.out.println("Connection failed, invalid perameters.");
        }
        
        try
        {
            // Setup streams.
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            outToServer = new DataOutputStream(socket.getOutputStream());

            // Setup listener.
            ChatListener myListener = new ChatListener(this);
            myListener.start();

            window.postMessage("Welcome to the chat client...");

            // Wait for the listener to finish (occurs when connection is closed).
            try
            {
                myListener.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            window.dispose();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server process.
     * 
     * @param message The message to send to the server process.
     */
    public void sendMessage(String message)
    {
        try
        {
            outToServer.writeBytes(message);
            
            if (message.compareTo("#quit\n") == 0)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the window to use for outputting messages incoming to this ChatClient.
     * 
     * @param newWindow The window to use for outputting messages incoming to this ChatClient.
     */
    public void setWindow(ChatWindow newWindow)
    {
        window = newWindow;
    }
    
    /**
     * Returns the window to use for outputting messages incoming to this ChatClient.
     * 
     * @return The window to use for outputting messages incoming to this ChatClient.
     */
    public ChatWindow getWindow()
    {
        return (window);
    }
    
    /**
     * Returns the socket connecting this client process to the server process.
     * 
     * @return The socket connecting this client process to the server process.
     */
    public Socket getSocket()
    {
        return (socket);
    }
}
