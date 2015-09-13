/**
 * A 4 by 4 matrix that stores its data in a FloatBuffer.
 * 
 * This matrix uses a 3D homogeneous coordinate system.
 * 
 * @author gb21
 */
public class Matrixf44 extends Matrixf
{
    /**
     * Returns the multiplication of <code>matrix1</code> and <code>matrix2</code>.
     * 
     * @param matrix1 The first matrix.
     * @param matrix2 The second matrix.
     * 
     * @return The multiplication of <code>matrix1</code> and <code>matrix2</code>.
     */
    public static Matrixf44 multiply(Matrixf44 matrix1, Matrixf44 matrix2)
    {
        Matrixf44 newMatrix = new Matrixf44();

        FloatBuffer m1Buf = matrix1.getBuffer();
        FloatBuffer m2Buf = matrix2.getBuffer();
        FloatBuffer newBuf = newMatrix.getBuffer();

        float sum = 0.0f;

        // For every row in the first matrix.
        for (int row = 0; row < 4; row++)
        {
            // For every column in the second matrix.
            for (int column = 0; column < 4; column++)
            {
                // For every element in the current row of the first matrix and every element in the current column of
                // the second matrix add the sum for the current element of the new matrix.
                for (int element = 0; element < 4; element++)
                {
                    sum += m1Buf.get((row * 4) + element) * m2Buf.get((element * 4) + column);
                }

                newBuf.put(sum);
                sum = 0.0f;
            }
        }

        newBuf.flip();

        return (newMatrix);
    }

    /**
     * Returns the multiplication of <code>matrix</code> and <code>vector</code>. <code>vector</code> is treated as a
     * column vector so that it can be multiplied.
     * 
     * @param matrix The matrix to be multiplied.
     * @param vector The vector to be multiplied.
     * 
     * @return The multiplication of <code>matrix</code> and <code>vector</code>.
     */
    public static Vectorf4 multiply(Matrixf44 matrix, Vectorf4 vector)
    {
        Vectorf4 newVector = new Vectorf4();

        FloatBuffer mBuf = matrix.getBuffer();
        FloatBuffer vBuf = vector.getBuffer();
        FloatBuffer newBuf = newVector.getBuffer();

        float sum = 0.0f;

        // For every row in the matrix.
        for (int row = 0; row < 4; row++)
        {
            // For every element in the vector and every element in the current row of the matrix add the sum for the
            // current element of the new vector.
            for (int element = 0; element < 4; element++)
            {
                sum += mBuf.get((row * 4) + element) * vBuf.get(element);
            }

            newBuf.put(sum);
            sum = 0.0f;
        }

        newBuf.rewind();

        return (newVector);
    }
    
    /**
     * Returns the multiplication of <code>vector</code> and <code>matrix</code>.
     * 
     * @param vector The vector to be multiplied.
     * @param matrix The matrix to be multiplied.
     * 
     * @return The multiplication of <code>vector</code> and <code>matrix</code>.
     */
    public static Vectorf4 multiply(Vectorf4 vector, Matrixf44 matrix)
    {
        Vectorf4 newVector = new Vectorf4();

        FloatBuffer vBuf = vector.getBuffer();
        FloatBuffer mBuf = matrix.getBuffer();
        FloatBuffer newBuf = newVector.getBuffer();

        float sum = 0.0f;

        // For every column in the matrix.
        for (int column = 0; column < 4; column++)
        {
            // For every element in the vector and every element in the current column of the matrix add the sum for
            // the current element of the new vector.
            for (int element = 0; element < 4; element++)
            {
                sum += vBuf.get(element) * mBuf.get((element * 4) + column);
            }

            newBuf.put(sum);
            sum = 0.0f;
        }

        newBuf.rewind();

        return (newVector);
    }

    /**
     * Creates an instance of Matrixf44.
     * 
     * Values are initialized to the identity matrix.
     */
    public Matrixf44()
    {
        buffer = createFloatBuffer(16);

        setIdentity();
    }

    /**
     * Creates an instance of Matrixf44.
     * 
     * Values are initialized to the buffer given.
     * 
     * @param newBuffer The buffer to set as the values of this matrix.
     */
    public Matrixf44(FloatBuffer newBuffer)
    {
        buffer = newBuffer;
    }

    /**
     * Sets this matrix to the identity matrix.
     */
    public void setIdentity()
    {
        buffer.put(1.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);

        buffer.flip();
    }
}

