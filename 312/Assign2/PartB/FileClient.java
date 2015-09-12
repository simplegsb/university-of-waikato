import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Sends the file specified to a process at the host name and port number specified.
 * 
 * @author gb21
 */
public class FileClient
{   
    /**
     * The port number for the socket on this client.
     */
    private static final int CLIENT_PORT_NUMBER = 31150;
    
    /**
     * Creates a socket to send a file through.
     * 
     * @param serverHostName The host name of the process to send the files to.
     * @param serverPort The port number of the process to send the files to.
     */
    public static void main(String[] args)
    {
        // Check for correct number of arguments.
        if (args.length != 3)
        {
            System.out.println("Three arguments required, usage:");
            System.out.println("FileClient <server host name> <server port #> <file name>");
            System.exit(0);
        }
        
        InetAddress serverAddress = null;
        
        // Check server name is valid.
        try
        {
            serverAddress = InetAddress.getByName(args[0]);
        }
        catch (UnknownHostException e)
        {
            System.out.println("Invalid server host name, usage:");
            System.out.println("FileClient <server host name> <server port #> <file name>");
            System.exit(0);
        }
        
        int serverPortNumber = 0;
        
        // Check port number is an integer.
        try
        {
            serverPortNumber = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Server port number must be integer, usage:");
            System.out.println("FileClient <server host name> <server port #> <file name>");
            System.exit(0);
        }
        
        BufferedInputStream inFromFile = null;
        
        // Attempt to open file.
        try
        {
           inFromFile = new BufferedInputStream(new FileInputStream(args[2]));
        }
        catch (IOException e)
        {
            System.out.println("Invalid file name, usage:");
            System.out.println("FileClient <server host name> <server port #> <file name>");
            System.exit(0);
        }
        
        ReliableDatagramSocket socket = null;
        
        // Attempt to create socket.
        try
        {
            socket = new ReliableDatagramSocket(CLIENT_PORT_NUMBER, serverAddress, serverPortNumber);
            socket.setVerbose(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        // Send file.
        try
        {          
            System.out.println("Sending file...");
            long startTime = System.currentTimeMillis();
            byte[] readBytes;
            
            while (inFromFile.available() > 0)
            {                
                // Create array for packet data.
                if (inFromFile.available() >= ReliableDatagramSocket.MAX_DATA_LENGTH)
                {
                    readBytes = new byte[ReliableDatagramSocket.MAX_DATA_LENGTH];
                }
                else
                {
                    readBytes = new byte[inFromFile.available()];
                }
                
                inFromFile.read(readBytes);
                
                socket.send(new DatagramPacket(readBytes, readBytes.length));
                
                System.out.print(".");
            }
            inFromFile.close();
            
            // Write empty packet to server to denote end of file.
            socket.send(new DatagramPacket(new byte[0], 0));
            
            // This line is commented out because it caused exceptions at send_thread.run(send_thread.java:40) of
            // which I was unable to determine the cause.
            socket.close();
            
            System.out.println("\nFile sent.");
            System.out.println("Time taken to send (ms): " + (System.currentTimeMillis() - startTime));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
