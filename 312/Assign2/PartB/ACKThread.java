import java.io.IOException;

/**
 * 
 * 
 * @author gb21
 */
public class ACKThread extends Thread
{
    /**
     * 
     */
    private ReliableDatagramSocket socket;
    
    /**
     * 
     */
    public ACKThread(ReliableDatagramSocket s)
    {
        socket = s;
    }
    
    /**
     * 
     */
    public void run()
    {
        try
        {
            while (!socket.waitForACK())
            {}
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
