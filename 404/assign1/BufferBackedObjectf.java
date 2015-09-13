import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * An object which holds its data in a FloatBuffer.
 * 
 * @author gb21
 */
public abstract class BufferBackedObjectf
{
    /**
     * The buffer that contains the data for this object.
     */
	protected FloatBuffer buffer = null;
    
    /**
     * Returns a new float buffer of the size given.
     * 
     * @param size The size of the float buffer to create.
     * 
     * @return A new float buffer of the size given.
     */
    public static FloatBuffer createFloatBuffer(int size)
    {
        return (ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer());
    }

    /**
     * Returns the buffer that contains the data for this object.
     * 
     * @return The buffer that contains the data for this object.
     */
	public FloatBuffer getBuffer()
	{
		return (buffer);
	}
    
    /**
     * Returns a copy of the buffer that contains the data for this object.
     * 
     * @return A copy of the buffer that contains the data for this object.
     */
    public Buffer getBufferCopy()
    {
        FloatBuffer bufferCopy = createFloatBuffer(buffer.capacity());
        bufferCopy.put(buffer);
        
        buffer.flip();
        bufferCopy.flip();
        
        return (bufferCopy);
    }
	
    /**
     * Sets the buffer that contains the data for this object.
     * 
     * @param newBuffer The buffer that contains the new data for this object.
     */
	public void setBuffer(FloatBuffer newBuffer)
	{
		buffer = newBuffer;
	}
}

