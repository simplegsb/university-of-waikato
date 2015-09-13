/**
 * A 4 dimensional vector that stores its data in a FloatBuffer.
 * 
 * This vector uses a 3D homogeneous coordinate system.
 * 
 * @author gb21
 */
public class Vectorf4 extends Vectorf
{
    /**
     * Returns the multiplication of <code>a</code> and <code>b</code>.
     * 
     * @param a The first vector to multiply.
     * @param b The second vector to multiply.
     * 
     * @return The multiplication of <code>a</code> and <code>b</code>.
     */
    public static Vectorf4 multiply(Vectorf4 a, Vectorf4 b)
    {
        FloatBuffer aBuf = a.getBuffer();
        FloatBuffer bBuf = b.getBuffer();

        return (new Vectorf4(aBuf.get(0) * bBuf.get(0), aBuf.get(1) * bBuf.get(1), aBuf.get(2) * bBuf.get(2), aBuf
                .get(3) * bBuf.get(3)));
    }

    /**
     * Creates an instance of a Vectorf4.
     * 
     * Values are initialized to (0.0f, 0.0f, 0.0f, 1.0f).
     */
    public Vectorf4()
    {
        buffer = createFloatBuffer(4);

        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);

        buffer.flip();
    }

    /**
     * Creates an instance of Vectorf4.
     * 
     * Values are initialized to the buffer given.
     * 
     * @param newBuffer The buffer to set as the values of this vector.
     */
    public Vectorf4(FloatBuffer newBuffer)
    {
        buffer = newBuffer;
    }

    /**
     * Creates an instance of a Vectorf4.
     * 
     * Values are initialized to those given.
     * 
     * @param v0 The first value of this vector.
     * @param v1 The second value of this vector.
     * @param v2 The third value of this vector.
     * @param v3 The fourth value of this vector.
     */
    public Vectorf4(float v0, float v1, float v2, float v3)
    {
        buffer = createFloatBuffer(4);

        buffer.put(v0);
        buffer.put(v1);
        buffer.put(v2);
        buffer.put(v3);

        buffer.flip();
    }

    /**
     * Returns the sum of vectors <code>a</code> and <code>b</code> where the sum of the two vectors (x1, y1, z1) and
     * (x2, y2, z2) is (x1 + x2, y1 + y2, z1 + z2).
     * 
     * @param a The first vector.
     * @param b The second vector.
     */
    public static Vectorf4 add(Vectorf4 a, Vectorf4 b)
    {
        FloatBuffer aBuf = (FloatBuffer) a.getBuffer();
        FloatBuffer bBuf = (FloatBuffer) b.getBuffer();

        Vectorf4 newVector = new Vectorf4();
        FloatBuffer newBuf = (FloatBuffer) newVector.getBuffer();

        newBuf.put(0, aBuf.get(0) + bBuf.get(0));
        newBuf.put(1, aBuf.get(1) + bBuf.get(1));
        newBuf.put(2, aBuf.get(2) + bBuf.get(2));

        return (newVector);
    }

    /**
     * Returns the cross product of vectors <code>a</code> and <code>b</code>.
     * 
     * @param a The first vector.
     * @param b The second vector.
     * 
     * @return The cross product of vectors <code>a</code> and <code>b</code>.
     */
    public static Vectorf4 crossProduct(Vectorf4 a, Vectorf4 b)
    {
        Vectorf4 cross = new Vectorf4();

        FloatBuffer aBuf = (FloatBuffer) a.getBuffer();
        FloatBuffer bBuf = (FloatBuffer) b.getBuffer();
        FloatBuffer cBuf = (FloatBuffer) cross.getBuffer();

        cBuf.put(0, aBuf.get(1) * bBuf.get(2) - aBuf.get(2) * bBuf.get(1));
        cBuf.put(1, aBuf.get(2) * bBuf.get(0) - aBuf.get(0) * bBuf.get(2));
        cBuf.put(2, aBuf.get(0) * bBuf.get(1) - aBuf.get(1) * bBuf.get(0));

        return (cross);
    }

    /**
     * Returns the dot product of vectors <code>a</code> and <code>b</code>.
     * 
     * @param a The first vector.
     * @param b The second vector.
     * 
     * @return The dot product of vectors <code>a</code> and <code>b</code>.
     */
    public static float dotProduct(Vectorf4 a, Vectorf4 b)
    {
        FloatBuffer aBuf = (FloatBuffer) a.getBuffer();
        FloatBuffer bBuf = (FloatBuffer) b.getBuffer();

        float dot = 0.0f;

        for (int index = 0; index < 2; index++)
        {
            dot += aBuf.get(index) * bBuf.get(index);
        }

        return (dot);
    }

    /**
     * Homogenizes this vector.
     * 
     * Scales all values equally so that the w dimension is 1.0f.
     */
    public void homogenize()
    {
        if (buffer.get(3) == 1.0f)
        {
            return;
        }
        
        buffer.put(0, buffer.get(0) / buffer.get(3));
        buffer.put(1, buffer.get(1) / buffer.get(3));
        buffer.put(2, buffer.get(2) / buffer.get(3));
        buffer.put(3, 1.0f);
    }

    /**
     * Scales this vector into a unit length vector.
     */
    public void normalize()
    {
        float sum = buffer.get(0) + buffer.get(1) + buffer.get(2);

        buffer.put(0, buffer.get(0) / sum);
        buffer.put(1, buffer.get(1) / sum);
        buffer.put(2, buffer.get(2) / sum);
    }

    public void scale(float factor)
    {
        buffer.put(0, buffer.get(0) * factor);
        buffer.put(1, buffer.get(1) * factor);
        buffer.put(2, buffer.get(2) * factor);
    }

    /**
     * Returns the subtraction of vectors <code>a</code> and <code>b</code> where the subtraction of the two vectors
     * (x1, y1, z1) and (x2, y2, z2) is (x1 - x2, y1 - y2, z1 - z2).
     * 
     * @param a The first vector.
     * @param b The second vector.
     */
    public static Vectorf4 subtract(Vectorf4 a, Vectorf4 b)
    {
        FloatBuffer aBuf = (FloatBuffer) a.getBuffer();
        FloatBuffer bBuf = (FloatBuffer) b.getBuffer();

        Vectorf4 newVector = new Vectorf4();
        FloatBuffer newBuf = (FloatBuffer) newVector.getBuffer();

        newBuf.put(0, aBuf.get(0) - bBuf.get(0));
        newBuf.put(1, aBuf.get(1) - bBuf.get(1));
        newBuf.put(2, aBuf.get(2) - bBuf.get(2));

        return (newVector);
    }
}

