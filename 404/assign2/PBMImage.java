import java.awt.Canvas;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Represents a black and white raster image in PBM format.
 * 
 * It is recommended to create an instance of this class by way of the readFromStream() function.
 * 
 * @author gb21
 */
public class PBMImage
{
    /**
     * Used to identify a black pixel in this image.
     */
    public static final int BLACK = 1;

    /**
     * Represents a horizontal orientation for histograms of this image.
     */
    public static final int HORIZONTAL = 0;

    /**
     * Represents a vertical orientation for histograms of this image.
     */
    public static final int VERTICAL = 1;

    /**
     * Used to identify a white pixel in this image.
     */
    public static final int WHITE = 0;

    /**
     * Converts a file from the input stream given to create a new file with the same name but with the extension .pbm
     * as well as being in PBM format.
     * 
     * This function supports all image formats supported by the Java API. For use with PBM images (as this function
     * requires) it is necessary to include the JAI I/O API in the class path. To check what formats are supported by
     * your current API run the GetFormats class.
     * 
     * If the file given already has the extension .pbm its stream is returned.
     * 
     * @param stream A stream for the file to convert.
     * @param filename The name of the file the input stream is pointing to.
     * 
     * @return A stream for the new file created.
     */
    public static FileInputStream convertImageFile(FileInputStream stream, String filename)
    {
        // Determine the extension of the file.
        String[] splitName = filename.split("[.]");
        String suffix = splitName[splitName.length - 1].toLowerCase();
        
        if (suffix.equals("pbm"))
        {
            return (stream);
        }
        
        filename = filename.substring(0, filename.length() - suffix.length() - 1) + ".pbm";
        
        // Create a new file and write to it.
        try
        {
            File newFile = new File(filename);
            newFile.createNewFile();
            FileInputStream newFileIn = new FileInputStream(newFile);
            
            if (!ImageIO.write(ImageIO.read(stream), "pnm", newFile))
            {
                System.err.println("Image format 'pnm' not supported by Java API, ensure JAI I/O API is installed.");
                System.exit(1);
            }
            
            return (newFileIn);
        }
        catch (IOException ex)
        {
            System.exit(0);
        }
        
        return (null);
    }

    /**
     * Reads data from the input stream given and creates an image from it. If the data in the input stream is found
     * to be corrupted or an incorrect format <code>null</code> is returned.
     * 
     * If the input stream is reading from a file the filename should be provided so that any format conversion can
     * be completed. Format conversion support is not available when reading from other input sources and the filename
     * should be <code>null</code>.
     * 
     * @param stream The input stream to read the image data from.
     * @param filename The name of the file the input stream is reading from if it reading from a file.
     * 
     * @return An image read in from the input stream.
     */
    public static PBMImage readFromStream(InputStream stream, String filename)
    {
        PBMImage image = new PBMImage();
        
        // If the image is being read from a file, convert the file to pbm.
        if (stream instanceof FileInputStream && filename != null)
        {
            stream = convertImageFile((FileInputStream) stream, filename);
        }

        // Reads stream, axtracting elements of PBM format.
        try
        {            
            image.setVersion(readWord(stream));

            image.setWidth(Integer.parseInt(readWord(stream)));

            image.setHeight(Integer.parseInt(readWord(stream)));
            
            // Read pixels.
            int bytesInRow = (image.getWidth() + 7) / 8;
            byte[][] newPixels = new byte[image.getHeight()][bytesInRow];

            // Read the pixels one row at a time.
            for (int row = 0; row < image.getHeight(); row++)
            {
                byte[] currentBytes = new byte[bytesInRow];
                stream.read(currentBytes);
                newPixels[row] = currentBytes;
            }
            image.setPixels(newPixels);
        }
        catch (Exception ex)
        {
            return (null);
        }

        return (image);
    }

    /**
     * Reads a word from the stream given. A word is defined to be an array of characters that are either letters or
     * numbers (in ASCII format). If a character is read in that is not part of a word before reading any that are it
     * will continue to read until it finds the beginning of a word. If it reads in a character that is not part of a
     * word after characters that are, it will return the word.
     * 
     * @param stream The input stream to read from.
     * 
     * @return The first word found from the current position in the stream.
     * 
     * @throws IOException if the stream cannot be read from before the end of the word could be found.
     */
    private static String readWord(InputStream stream) throws IOException
    {
        byte[] currentByte = new byte[1];
        String currentChar = null;
        String word = new String();

        // Loop until the word has finished (a whitespace character is read after the beginning of the word).
        while (word.length() == 0 || currentChar.matches("[^\\s#]"))
        {
            stream.read(currentByte);
            currentChar = new String(currentByte);

            // If the current character is a non-whitespace character and is not the comment character.
            if (currentChar.matches("[^\\s#]"))
            {
                word = word.concat(currentChar);
            }
            // If the current character is the comment character.
            else if (currentChar.matches("#"))
            {
                // Read bytes until the end of the comment.
                do
                {
                    stream.read(currentByte);
                    currentChar = new String(currentByte);
                    
                    System.err.print(currentChar);
                }
                while (!currentChar.matches("\n"));
            }
        }

        return (word);
    }

    /**
     * The height of this image in pixels.
     */
    private int height = 0;

    /**
     * An array of bytes containing all the pixels of this image.
     */
    private byte[][] pixels = null;

    /**
     * The version of this image, sometimes known as the 'magic number'.
     */
    private String version = null;

    /**
     * The width of this image in pixels.
     */
    private int width = 0;

    public PBMImage()
    {}

    /**
     * Assigns the pixels of this image to be the pixels given.
     * 
     * CAUTION: This constructor does not initialize the width or height of this image. Some methods may not perform
     * as expected before these have been initialized.
     * 
     * @param pixels The pixels to assign to this image.
     */
    public PBMImage(byte[][] pixels)
    {
        this.pixels = pixels;
    }

    /**
     * Creates a window with the image this object represents drawn within it.
     * 
     * @param scale The scale to display this image at, a scale of x equates to a scale of 1:x.
     */
    public void displayInWindow(int scale)
    {
        JFrame frame = new JFrame();
        Canvas canvas = new PBMCanvas(this, scale);

        frame.add(canvas);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Returns the height of this image in pixels.
     * 
     * @return The height of this image in pixels.
     */
    public int getHeight()
    {
        return (height);
    }

    /**
     * Produces a histogram of the frequency of black pixels in each row/column depending on the orientation given.
     * 
     * @param orientation The orientation to calculate frequencies for.
     * 
     * @return The frequency histogram of this image.
     */
    public int[] getHistogram(int orientation)
    {
        if (orientation == HORIZONTAL)
        {
            return (getHistogram(orientation, 0, height, 0, width));
        }
        else if (orientation == VERTICAL)
        {
            return (getHistogram(orientation, 0, width, 0, height));
        }

        return (null);
    }

    /**
     * Produces a histogram of the frequency of black pixels in each row/column depending on the orientation given.
     * 
     * Frequencies are only calculated in the range of rows/colmns defined by the ranges (<code>rangeStart</code>
     * inclusive and <code>rangeEnd</code> exclusive) and only for the area of the current row/column defined by the
     * offsets (<code>offsetStart</code> inclusive and <code>offsetEnd</code> exclusive).
     * 
     * If the range or the offset area is less than 1 then <code>null</code> is returned.
     * 
     * @param orientation The orientation to calculate frequencies for.
     * @param rangeStart The start of the range to calculate frequencies for.
     * @param rangeEnd The end of the range to calculate frequencies for.
     * @param offsetStart The start of the offset area to include.
     * @param offsetEnd The end of the offset area to include.
     * 
     * @return The frequency histogram of the range and offset area given in this image.
     */
    public int[] getHistogram(int orientation, int rangeStart, int rangeEnd, int offsetStart, int offsetEnd)
    {
        if (rangeEnd - rangeStart < 1)
        {
            return (null);
        }

        if (offsetEnd - offsetStart < 1)
        {
            return (null);
        }

        int[] histogram = null;
        int count = 0;

        // Horizontal histogram.
        if (orientation == HORIZONTAL)
        {
            histogram = new int[rangeEnd - rangeStart];

            // For each row within the range given.
            for (int row = rangeStart; row < rangeEnd; row++)
            {
                count = 0;

                // Count the black pixels on the row within the offset area given.
                for (int column = offsetStart; column < offsetEnd; column++)
                {
                    if (getPixel(column, row) == BLACK)
                    {
                        count++;
                    }
                }

                histogram[row - rangeStart] = count;
            }
        }

        // Vertical histogram.
        else if (orientation == VERTICAL)
        {
            histogram = new int[rangeEnd - rangeStart];

            // For each column within the range given.
            for (int column = rangeStart; column < rangeEnd; column++)
            {
                count = 0;

                // Count the black pixels on the column within the offset area given.
                for (int row = offsetStart; row < offsetEnd; row++)
                {
                    if (getPixel(column, row) == BLACK)
                    {
                        count++;
                    }
                }

                histogram[column - rangeStart] = count;
            }
        }

        return (histogram);
    }

    /**
     * Returns the color of the pixel at the given coordinates.
     * 
     * @param x The x coordinate to get the pixel color of.
     * @param y The y coordinate to get the pixel color of.
     * 
     * @return The color of the pixel at the given coordinates or -1 if these coordinates are invalid.
     */
    public int getPixel(int x, int y)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
        {
            return (-1);
        }

        byte byteOfPixels = pixels[y][x / 8];
        int posInByte = x % 8;

        // Create mask.
        byte mask = 1;
        mask <<= (7 - posInByte);

        byteOfPixels &= mask;

        if (byteOfPixels == 0)
        {
            return (WHITE);
        }

        return (BLACK);
    }

    /**
     * Returns an array of bytes containing all the pixels of this image.
     * 
     * @return An array of bytes containing all the pixels of this image.
     */
    public byte[][] getPixels()
    {
        return (pixels);
    }

    /**
     * Returns the version of this image, sometimes known as the 'magic number'.
     * 
     * @return The version of this image, sometimes known as the 'magic number'.
     */
    public String getVersion()
    {
        return (version);
    }

    /**
     * Returns the width of this image in pixels.
     * 
     * @return The width of this image in pixels.
     */
    public int getWidth()
    {
        return (width);
    }

    /**
     * Sets the height of this image in pixels.
     * 
     * @param newHeight The new height of this image.
     */
    public void setHeight(int newHeight)
    {
        height = newHeight;
    }

    /**
     * Sets the color of the pixel at the given coordinates.
     * 
     * @param x The x coordinate to set the pixel color of.
     * @param y The y coordinate to set the pixel color of.
     * @param color The color to set the pixel.
     */
    public void setPixel(int x, int y, int color)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
        {
            return;
        }

        byte byteOfPixels = pixels[y][x / 8];
        int posInByte = x % 8;

        // Create mask.
        byte mask = 1;
        mask <<= (7 - posInByte);

        byteOfPixels &= ~mask;

        if (color == BLACK)
        {
            byteOfPixels |= mask;
        }

        pixels[y][x / 8] = byteOfPixels;
    }

    /**
     * Sets an array of bytes containing all the pixels of this image.
     * 
     * CAUTION: This method does not initialize the width or height of this image. Some methods may not perform as
     * expected before these have been initialized.
     * 
     * @param newPixels The new pixels of this image.
     */
    public void setPixels(byte[][] newPixels)
    {
        pixels = newPixels;
    }

    /**
     * Sets the version of this image, sometimes known as the 'magic number'.
     * 
     * @param newVersion The new version of this image.
     */
    public void setVersion(String newVersion)
    {
        version = newVersion;
    }

    /**
     * Sets the width of this image in pixels.
     * 
     * @param newWidth The new width of this image.
     */
    public void setWidth(int newWidth)
    {
        width = newWidth;
    }

    /**
     * Writes this image to the output stream given.
     * 
     * @param stream The output stream to write the image data to.
     */
    public void writeToStream(OutputStream stream)
    {
        if (!version.equals("P4"))
        {
            return;
        }

        try
        {
            stream.write((version + "\n").getBytes());
            stream.write(("# Created by scottie, a program by gb21, on " +
                    new Date(System.currentTimeMillis()).toString() + "\n").getBytes());
            stream.write((String.valueOf(width) + " " + String.valueOf(height) + "\n").getBytes());

            for (int row = 0; row < height; row++)
            {
                stream.write(pixels[row]);
            }
        }
        catch (IOException ex)
        {
            System.err.println("Could not write PBM image to stream.");
            return;
        }
    }
}
