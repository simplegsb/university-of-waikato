import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manages all of the messages coming from a given client and relays them to the appropriate clients.
 * 
 * @author gb21
 */
public class ChatSession extends Thread
{
    /**
     * The socket of the client this ChatSession is managing.
     */
    private Socket clientSocket;

    /**
     * A collection of the sockets of all clients currently connected to the server process.
     */
    private ArrayList allClientSockets;

    /**
     * Creates an instance of a ChatSession.
     * 
     * @param newClientSocket The socket of the client this ChatSession is managing.
     * @param newClientSockets A collection of the sockets of all clients currently connected to the server process.
     */
    public ChatSession(Socket newClientSocket, ArrayList newClientSockets)
    {
        clientSocket = newClientSocket;
        allClientSockets = newClientSockets;
        allClientSockets.add(clientSocket);
    }

    /**
     * Listens for any messages coming from the client process this ChatSession is managing and processes them,
     * relaying them to the approriate client(s).
     */
    public void run()
    {
        System.out.println("Session started...");

        try
        {
            // Setup streams.
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            outToClient.writeBytes("You are now connected to the chat server...\n");

            String message = inFromClient.readLine();
            while (message.compareTo("#quit") != 0)
            {
                if (message.compareTo("#list") == 0)
                {
                    list();
                }
                else if (message.startsWith("#private"))
                {
                    privateMessage(message.substring(8));
                }
                else
                {
                    publicMessage(message);
                }

                message = inFromClient.readLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        allClientSockets.remove(clientSocket);

        System.out.println("Session terminated.");
    }

    /**
     * Relays a list of all clients currently connected to the server process to the client this ChatSession is
     * managing.
     */
    private void list()
    {
        try
        {
            Iterator clientSocketIter = allClientSockets.iterator();
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            outToClient.writeBytes("All clients:\n");

            for (int index = 0; clientSocketIter.hasNext(); index++)
            {
                Socket currentClient = (Socket) clientSocketIter.next();
                outToClient.writeBytes(index + ") Host name: " + currentClient.getInetAddress() + " , " + "Port: "
                        + currentClient.getPort() + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Relays a message from the client this ChatSession is managing to all clients currently connected to the server
     * process.
     * 
     * @param message The message to relay.
     */
    private void publicMessage(String message)
    {
        try
        {
            Iterator clientSocketIter = allClientSockets.iterator();
            DataOutputStream outToClient;

            while (clientSocketIter.hasNext())
            {
                outToClient = new DataOutputStream(((Socket) clientSocketIter.next()).getOutputStream());
                outToClient.writeBytes(message + '\n');
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Relays a message from the client this ChatSession is managing to only one client. The client to relay the
     * message to is determined by the first character in the message (should be a number). This is limited because
     * only clients 0 - 9 can be messaged in this manor.
     * 
     * @param message The message to relay.
     */
    private void privateMessage(String message)
    {
        try
        {
            try
            {
                DataOutputStream outToClient = new DataOutputStream(((Socket) allClientSockets.get(Integer
                        .parseInt(message.substring(0, 1)))).getOutputStream());
                outToClient.writeBytes("private: " + message.substring(1) + '\n');
            }
            catch (NumberFormatException e)
            {
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                outToClient.writeBytes("Invalid command.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
