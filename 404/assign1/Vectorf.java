/**
 * A vector that stores its data in a FloatBuffer.
 * 
 * @author gb21
 */
public abstract class Vectorf extends BufferBackedObjectf
{                
    /**
     * Returns the length of this vector.
     * 
     * @return The length of this vector.
     */
    public float getLength()
    {        
        return ((float) Math.sqrt((double) getLengthSquared()));
    }
    
    /**
     * Returns the length of this vector squared.
     * 
     * @return The length of this vector squared.
     */
    public float getLengthSquared()
    {        
        return (buffer.get(0) * buffer.get(0) + buffer.get(1) * buffer.get(1) + buffer.get(2) * buffer.get(2));
    }
    
    /**
     * Negates this vector.
     */
    public void negate()
    {
        scale(-1.0f);
    }
    
    /**
     * Scales this vector by factor.
     * 
     * @param factor The factor to scale this vector by.
     */
    public abstract void scale(float factor);
}

