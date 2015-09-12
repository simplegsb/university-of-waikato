import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * 
 * @author gb21
 */
public class ChatServer
{
    /**
     * Creates a passive TCP socket and waits for client processes to connect to it.
     * 
     * @param args A single argument should be supplied specifying the port to create the passive TCP socket.
     */
    public static void main(String[] args)
    {        
        try
        {
            ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));

            System.out.println("File server has been activated...");
            System.out.println("Server is operating on:");
            System.out.println("Host name: " + InetAddress.getLocalHost());
            System.out.println("Host port: " + welcomeSocket.getLocalPort());

            while (true)
            {
                Socket connectionSocket = welcomeSocket.accept();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
