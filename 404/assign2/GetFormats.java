import javax.imageio.*;
import java.util.Arrays;

/**
 * Prints the formats available for use with image IO using the current Java API and external class path supplied.
 * 
 * For addition format support include the JAI I/O API.
 * 
 * @author gb21
 */
public class GetFormats
{
    public static void main(String args[])
    {
        String readFormats[] = ImageIO.getReaderFormatNames();
        String writeFormats[] = ImageIO.getWriterFormatNames();
        
        System.out.println("Readers: " + Arrays.asList(readFormats));
        System.out.println("Writers: " + Arrays.asList(writeFormats));
    }
}
