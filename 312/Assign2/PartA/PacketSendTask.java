import java.io.IOException;
import java.net.DatagramPacket;
import java.util.TimerTask;

/**
 * Sends a packet to a socket when this task is run.
 * 
 * @author gb21
 */
public class PacketSendTask extends TimerTask
{
    /**
     * The packet to send.
     */
    private DatagramPacket packet;
    
    /**
     * The socket to send the packet to.
     */
    private ReliableDatagramSocket socket;
    
    /**
     * Creates an instance of TimeoutPacketSend.
     * 
     * @param p The packet to send.
     * @param s The socket to send the packet to.
     */
    public PacketSendTask(DatagramPacket p, ReliableDatagramSocket s)
    {
        super();
        
        packet = p;
        socket = s;
    }
    
    /**
     * Sends the packet to the socket.
     */
    public void run()
    {
        try
        {
            socket.unreliableSend(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
