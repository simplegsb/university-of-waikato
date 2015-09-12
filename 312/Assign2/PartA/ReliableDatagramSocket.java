import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Timer;
import java.util.zip.CRC32;
//import DodgyDatagramSocket.*;

/**
 * A datagram socket that incorporates reliabbility in a network connection that is lossy, corrupting and may have
 * delays.
 * 
 * @author gb21
 */
class ReliableDatagramSocket extends DodgyDatagramSocket
{        
    /**
     * The length of the header added to packets.
     */
    private static final int HEADER_LENGTH = 4;
    
    /**
     * The maximum size of a reliably sent packet (including header).
     */
    private static final int MAX_PACKET_LENGTH = Byte.MAX_VALUE;
    
    /**
     * The maximum size of a reliably sent packet (excluding header).
     */
    public static final int MAX_DATA_LENGTH = MAX_PACKET_LENGTH - HEADER_LENGTH;
    
    /**
     * The previous sequence number of packets received.
     */
    private byte previousReceivingSequenceNumber = -1;
    
    /**
     * The previous sequence number of packets sent.
     */
    private byte previousSendingSequenceNumber = -1;
    
    /**
     * The sequence number of packets received.
     */
    private byte receivingSequenceNumber = 0;
    
    /**
     * The sequence number of packets sent.
     */
    private byte sendingSequenceNumber = 0;
    
    /**
     * A tag to add to the packet that makes it an ACK packet.
     */
    private final byte TAG_ACK = 1;
    
    /**
     * A tag to add to the packet that makes it a regular packet.
     */
    private final byte TAG_NONE = 0;
    
    /**
     * Creates an instance of ReliableDatagramSocket.
     * 
     * @throws SocketException
     */
    public ReliableDatagramSocket(String newDestHostName) throws SocketException
    {
        super();
    }

    /**
     * Creates an instance of ReliableDatagramSocket at port specified.
     * 
     * @param port The port to make the socket at.
     * 
     * @throws SocketException
     */
    public ReliableDatagramSocket(int port) throws SocketException
    {
        super(port);
    }
    
    /**
     * Adds a header to the packet given with a checksum, tag, sequence number and length.
     * 
     * @param tag Defines the packet as an ACK, NAK or regular packet.
     * @param sequenceNumber The sequence number of the packet.
     * @param p The packet the header is to be added to.
     */
    private void addHeader(byte tag, byte sequenceNumber, DatagramPacket p)
    {
        byte[] packetData = p.getData();
        
        // Create new packet buffer.
        byte[] headerAndData = new byte[packetData.length + HEADER_LENGTH];
        System.arraycopy(packetData, 0, headerAndData, HEADER_LENGTH, packetData.length);
        
        // Add sequence number.
        headerAndData[1] = sequenceNumber;
        
        // Add tag.
        headerAndData[2] = tag;
        
        // Add length.
        headerAndData[3] = Byte.parseByte(Integer.toString(packetData.length + HEADER_LENGTH));
        
        // Calculate and add checksum.
        CRC32 crc = new CRC32();
        crc.update(headerAndData, 1, headerAndData.length - 1);
        headerAndData[0] = Byte.parseByte(Long.toString(crc.getValue() % Byte.MAX_VALUE));
        
        p.setData(headerAndData);
    }
    
    /**
     * Determines if the packet given has the sequence number given.
     * 
     * @param p The packet to check.
     * @param sequenceNumber The sequence number to check for.
     * 
     * @return True if the packet has the sequence number given, false otherwise.
     */
    private boolean hasSeq(DatagramPacket p, byte sequenceNumber)
    {
        byte[] packetData = p.getData();
        
        return (packetData[1] == sequenceNumber ? true : false);
    }
    
    /**
     * Determines if the packet given is an ACK packet.
     * 
     * @param p The packet to check.
     * 
     * @return True if the packet is an ACK packet, false otherwise.
     */
    private boolean isACK(DatagramPacket p)
    {
        byte[] packetData = p.getData();
        
        return (packetData[2] == TAG_ACK ? true : false);
    }
    
    /**
     * Determines if the packet given is corrupt.
     * 
     * @param p The packet to check.
     * 
     * @return True if the packet is corrupt, false otherwise.
     */
    private boolean isCorrupt(DatagramPacket p)
    {
        byte[] packetData = p.getData();
        
        try
        {
            // Calculate checksum.
            CRC32 crc = new CRC32();
            crc.update(packetData, 1, packetData[3] - 1);
            byte checkSum = Byte.parseByte(Long.toString(crc.getValue() % Byte.MAX_VALUE));
            
            return (packetData[0] == checkSum ? false : true);
        }
        // If the length value has been corrupted.
        catch (IndexOutOfBoundsException e)
        {
            return (true);
        }
    }

    /**
     * Waits for a packet to be received and checks that it is not corrupt and that it is the packet wanted by the
     * receiving process. Acknowledges the packet if it was correct, acknowledges the previous packet if it wasn't.
     */
    public void receive(DatagramPacket p) throws IOException
    {
        boolean isCorrectPacket = false;
        while (!isCorrectPacket)
        {
            p.setData(new byte[MAX_PACKET_LENGTH]);
            super.receive(p);
        
            // If packet received is packet wanted.
            if (!isCorrupt(p) && hasSeq(p, receivingSequenceNumber))
            {
                isCorrectPacket = true;
                
                // Create and send reply for this packet.
                DatagramPacket reply = new DatagramPacket(new byte[0], 0, p.getAddress(), p.getPort());
                addHeader(TAG_ACK, receivingSequenceNumber, reply);
                super.send(reply);
            }
            else
            {                
                // Create and send reply for previous packet.
                DatagramPacket reply = new DatagramPacket(new byte[0], 0, p.getAddress(), p.getPort());
                addHeader(TAG_ACK, previousReceivingSequenceNumber, reply);
                super.send(reply);
            }
        }
        
        // Trim data.
        byte[] trimmedData = new byte[(p.getData())[3]];
        System.arraycopy(p.getData(), 0, trimmedData, 0, trimmedData.length);
        p.setData(trimmedData);
        
        removeHeader(p);
        
        // Increment sequence counter.
        previousReceivingSequenceNumber = receivingSequenceNumber++;
        receivingSequenceNumber %= Byte.MAX_VALUE;
    }
    
    /**
     * Removes the header from the packet.
     * 
     * @param p The packet the header is to be removed from.
     */
    private void removeHeader(DatagramPacket p)
    {
        byte[] packetData = p.getData();
        byte[] newPacketData = new byte[packetData.length - HEADER_LENGTH];
        
        System.arraycopy(packetData, HEADER_LENGTH, newPacketData, 0, newPacketData.length);
        
        p.setData(newPacketData);
    }
    
    /**
     * Resets the sequence numbers of this socket. This makes it ready to send to/receive from a new socket.
     */
    public void reset()
    {
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        receivingSequenceNumber = 0;
        sendingSequenceNumber = 0;
    }

    /**
     * Adds a header to p and sends it, will repeat sending at a constant interval until the process p is being sent to
     * acknowledges it with the correct sequence number.
     */
    public void send(DatagramPacket p) throws IOException
    {
        // Create a reply packet.
        DatagramPacket reply = new DatagramPacket(new byte[HEADER_LENGTH], HEADER_LENGTH);
        addHeader(TAG_NONE, sendingSequenceNumber, p);
        
        // Create a timer for repeatidly sending the packet.
        // The strategy of sending is to flood the receiver until a packet is acknowledged.
        Timer timeoutTimer = new Timer();
        timeoutTimer.scheduleAtFixedRate(new PacketSendTask(p, this), 0, 100);
        
        boolean isACKed = false;
        while (!isACKed)
        {        
            super.receive(reply);
            
            // If the packet received acknowledges the packet sent.
            if (!isCorrupt(reply) && isACK(reply) && hasSeq(reply, sendingSequenceNumber))
            {
                isACKed = true;
                timeoutTimer.cancel();
            }
        }
        
        // Increment sequence counter.
        previousSendingSequenceNumber = sendingSequenceNumber++;
        sendingSequenceNumber %= Byte.MAX_VALUE;
    }
    
    /**
     * Sends a packet by the same method that DodgyDatagramPacket would.
     * 
     * @param p The packet to be sent.
     * 
     * @throws IOException
     */
    public void unreliableSend(DatagramPacket p) throws IOException
    {
        super.send(p);
    }
}
