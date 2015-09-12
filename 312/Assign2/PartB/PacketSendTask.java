import java.io.IOException;
import java.net.DatagramPacket;
import java.util.List;
import java.util.TimerTask;

/**
 * Sends packets to a socket when this task is run.
 * 
 * @author gb21
 */
public class PacketSendTask extends TimerTask
{
    /**
     * The packets to send.
     */
    private List packets;
    
    /**
     * The socket to send the packets to.
     */
    private ReliableDatagramSocket socket;
    
    /**
     * Creates an instance of TimeoutPacketSend.
     * 
     * @param ps The packets to send.
     * @param s The socket to send the packets to.
     */
    public PacketSendTask(List ps, ReliableDatagramSocket s)
    {
        super();
        
        packets = ps;
        socket = s;
    }
    
    /**
     * Sends all packets to the socket.
     */
    public void run()
    {        
        try
        {
            for (int index = 0; index < packets.size(); index++)
            {
                socket.unreliableSend((DatagramPacket) packets.get(index));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
