import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Waits for a client process to send a file to it and writes the file to disk.
 * 
 * @author gb21
 */
public class FileServer
{
    /**
     * Creates a UDP socket and waits for client processes to send files to it.
     * 
     * @param args A single argument should be supplied specifying the port to create the UDP socket on.
     */
    public static void main(String[] args)
    {
        // Check for correct number of arguments.
        if (args.length != 1)
        {
            System.out.println("One argument required, usage:");
            System.out.println("FileServer <server port #>");
            System.exit(0);
        }

        int portNumber = 0;

        // Check port number is an integer.
        try
        {
            portNumber = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Server port number must be integer, usage:");
            System.out.println("FileServer <server port #>");
            System.exit(0);
        }

        ReliableDatagramSocket socket = null;

        // Attempt to create socket.
        try
        {
            socket = new ReliableDatagramSocket(portNumber);
            socket.setVerbose(false);
        }
        catch (IOException e)
        {
            System.out.println("Connection failed, invalid server port, usage:");
            System.out.println("FileServer <server port #>");
            System.exit(0);
        }

        // Output server status.
        try
        {
            System.out.println("File server has been activated...");
            System.out.println("Server is operating on:");
            System.out.println("Host name: " + InetAddress.getLocalHost().getHostName());
            System.out.println("Host port: " + portNumber);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        writeFiles(socket);
    }
    
    /**
     * Writes files received through <code>socket</code> to disk in the working directory. 
     * 
     * @param socket The socket to receive files through.
     */
    private static void writeFiles(ReliableDatagramSocket socket)
    {
        int fileNumber = 0;
        
        while (true)
        {
            try
            {                
                System.out.println("Ready to receive file...");
                
                // Wait for first packet to be received.
                DatagramPacket packet = new DatagramPacket(new byte[0], 0);
                socket.receive(packet);
                byte[] writeBytes = packet.getData();
                
                // Initialize file for writing.
                File file = new File("file" + fileNumber + ".txt");
                file.createNewFile();
                BufferedOutputStream outToFile = new BufferedOutputStream(new FileOutputStream(file));
                
                System.out.println("Receiving file...");

                boolean finished = false;
                while (!finished)
                {
                    // If an empty packet was received finish file write.
                    if (writeBytes.length == 0)
                    {
                        finished = true;
                    }
                    // Otherwise write packet data to file and wait for next packet.
                    else
                    {
                        outToFile.write(writeBytes);
                        System.out.print(".");
                        
                        socket.receive(packet);
                        writeBytes = packet.getData();
                    }
                }
                
                System.out.println("\nFile received.");

                outToFile.close();
                fileNumber++;
                socket.reset();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
