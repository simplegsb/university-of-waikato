/**
 * The main class to be executed in order to start the ChatClient.
 * 
 * @author gb21
 */
public class Chat
{
    /**
     * Creates the client and its associated window, linking them together.
     * 
     * @param args Two arguments need to be supplied for successful operation, the first is the host name of the
     * server process and the second is the port number of the server process.
     */
    public static void main(String[] args)
    {
        ChatClient client = new ChatClient();
        ChatWindow window = new ChatWindow(client);
        client.setWindow(window);
        
        // Setup window.
        window.setSize(400, 400);
        window.setTitle("gb21's Chat Client");
        window.setVisible(true);
        
        client.connect(args[0], args[1]);
    }
}
