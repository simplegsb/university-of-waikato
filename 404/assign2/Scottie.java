import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

/**
 * Tool for performing image recognition and enhancement. This tool is specialised for use with musical staff system
 * images. It is able to filter noise in the image (smooth), identify staff lines, rotate the image to account for
 * skew and remove staff lines.
 * 
 * For information on how to use this tool, run it with no parameters or include the -help parameter.
 * 
 * @author gb21
 */
public class Scottie
{
    /**
     * The image to process.
     */
    private static PBMImage image = null;

    /**
     * The level of verbosity to output during execution.
     */
    private static int verbosity = 0;

    /**
     * Performs the options parameterized.
     * 
     * @param args The options for this image processor to perform.
     */
    public static void main(String[] args)
    {
        // Search for file name if one was given.
        String filename = null;

        for (int index = 0; index < args.length; index++)
        {
            args[index].toLowerCase();

            if (args[index].charAt(0) != '-')
            {                
                filename = args[index];
            }
            else if (args[index].equals("-verbose") || args[index].equals("-generate")
                    || args[index].equals("-window"))
            {
                index++;
            }
        }

        // Open input stream.
        if (filename != null)
        {
            try
            {
                image = PBMImage.readFromStream(new FileInputStream(filename), filename);
            }
            catch (FileNotFoundException ex)
            {
                System.err.println("Could not read PBM image from file. Reading from standard input stream...");
            }
        }
        else
        {
            image = PBMImage.readFromStream(System.in, null);
        }

        if (image == null)
        {
            System.err.println("Could not read PBM image from standard input stream.");

            printUsage();
            System.exit(0);
        }

        // Process image.
        int scale = -1;

        for (int index = 0; index < args.length; index++)
        {
            args[index].toLowerCase();

            // Help argument.
            if (args[index].equals("-help"))
            {
                printUsage();
            }

            // Verbose argument.
            else if (args[index].equals("-verbose"))
            {
                index++;

                try
                {
                    verbosity = Integer.parseInt(args[index]);
                }
                catch (NumberFormatException ex)
                {
                    printUsage();
                    System.exit(0);
                }
            }

            // Window argument.
            else if (args[index].equals("-window"))
            {
                index++;

                try
                {
                    scale = Integer.parseInt(args[index]);
                }
                catch (NumberFormatException ex)
                {
                    printUsage();
                    System.exit(0);
                }
            }

            // Generate argument.
            else if (args[index].equals("-generate"))
            {
                index++;

                // Filter noise argument.
                if (args[index].equals("filtnoise"))
                {
                    filterNoise();

                    if (verbosity > 1)
                    {
                        System.err.println("Set pixels in output image: " + setPixelCount(image));
                    }

                    //image.writeToStream(System.out);
                }

                // Detect staff lines argument.
                else if (args[index].equals("stafflines"))
                {
                    writeStafflines(detectStafflines());
                }

                // Rotate argument.
                else if (args[index].equals("rotate"))
                {
                    rotate(detectSkewAngle(detectStafflines()));

                    if (verbosity > 1)
                    {
                        System.err.println("Set pixels in output image: " + setPixelCount(image));
                    }

                    //image.writeToStream(System.out);
                }

                // Remove argument.
                else if (args[index].equals("remove"))
                {
                    remove(detectStafflines());

                    if (verbosity > 1)
                    {
                        System.err.println("Set pixels in output image: " + setPixelCount(image));
                    }

                    //image.writeToStream(System.out);
                }
            }
        }

        if (scale != -1)
        {
            image.displayInWindow(scale);
        }
    }

    /**
     * Determines the amount of skew present in the image.
     *
     * @param stafflines The staff lines to detect skew of.
     * 
     * @return The angle of skew detected in radians.
     */
    private static double detectSkewAngle(Vector<Line> stafflines)
    {
        if (verbosity > 0)
        {
            System.err.println("Detecting angle of skew...");
        }

        // Determine tha average length of the horizontal and vertical components of the staff lines
        double verticalLength = 0;
        double horizontalLength = 0;
        double linesUsed = 0;
        Line line;

        for (int index = 0; index < stafflines.size(); index++)
        {
            line = stafflines.get(index);

            // If line is not properly initialized, ignore it.
            if (line.x1 == -1 || line.x2 == -1 || line.y1 == -1 || line.y2 == -1)
            {
                if (verbosity > 2)
                {
                    System.err.println("Found incomplete staff line - ignoring.");
                }

                continue;
            }

            verticalLength += line.y1 - line.y2;
            horizontalLength += line.x2 - line.x1;

            linesUsed++;
        }

        boolean negative = false;
        if (verticalLength < 0.0)
        {
            negative = true;
        }

        verticalLength /= linesUsed;
        horizontalLength = Math.abs(horizontalLength) / linesUsed;

        // Determine the angle the lines are on.
        double hypotenuse = Math.sqrt(verticalLength * verticalLength + horizontalLength * horizontalLength);
        double skew = Math.asin(verticalLength / hypotenuse);

        if (negative)
        {
            skew *= -1;
        }

        if (verbosity > 2)
        {
            System.err.println("Skew angle of " + Math.toDegrees(skew) + " degrees detected.");
        }

        return (skew);
    }

    /**
     * Determines the positions of any staff lines in the image.
     * 
     * @return The staff lines detected.
     */
    private static Vector<Line> detectStafflines()
    {
        if (verbosity > 0)
        {
            System.err.println("Detecting staff lines...");
        }

        // Create a vertical histogram and determine the area occupied by the staff system. The first and last
        // positions found to be part of the staff system are kept for further processing.
        int[] histogram = image.getHistogram(PBMImage.VERTICAL);
        int first = -1;
        int last = -1;

        // If a frequency from the histogram is above this threshold the 'result' is true and it is possible that the
        // position that frequency denotes is within the staff system.
        int staffThreshold = image.getHeight() / 20;

        // Keeps a range of 'results' from the histogram, if the amount in the range is over a threshold then it is
        // considered that the last frequency in the range denotes a position within the staff system.
        int[] range = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Range of 10.
        int rangeThreshold = 7;
        int rangeCount = 0;

        // For each column in the image.
        for (int column = 0; column < image.getWidth(); column++)
        {
            // Update range to include new frequency.
            if (range[0] == 1)
            {
                rangeCount--;
            }

            System.arraycopy(range, 1, range, 0, range.length - 1);

            if (histogram[column] > staffThreshold)
            {
                range[range.length - 1] = 1;
                rangeCount++;
            }
            else
            {
                range[range.length - 1] = 0;
            }

            // Record if the current frequency denotes the first and/or last position in the staff system.
            if (rangeCount >= rangeThreshold)
            {
                if (first == -1)
                {
                    first = column - range.length;
                }
                last = column;
            }
        }

        // Create horizontal histograms from the two positions in the staff system and determine where the staff lines
        // are within these systems. The offset width is the width of the image to take a histogram of. The line
        // threshold works in a similar way to the staff threshold above.
        int offsetWidth = image.getWidth() / 5;
        int lineThreshold = offsetWidth / 2;
        Vector<Line> stafflines = new Vector<Line>();

        // Create a horizontal histogram from the first position in the staff system. Save positions where staff lines
        // are found.
        histogram = image.getHistogram(PBMImage.HORIZONTAL, 0, image.getHeight(), first, first + offsetWidth);
        boolean prevWasLine = false;

        for (int row = 0; row < image.getHeight(); row++)
        {
            if (histogram[row] > lineThreshold)
            {
                // If this is a continuation of a known line.
                if (prevWasLine)
                {
                    stafflines.get(stafflines.size() - 1).width++;
                }
                else
                {
                    stafflines.add(new Line(first, row, -1, -1, 1));
                    prevWasLine = true;
                }
            }
            else
            {
                prevWasLine = false;
            }
        }

        // Create a horizontal histogram from the last position in the staff system. Save positions where staff lines
        // are found.
        histogram = image.getHistogram(PBMImage.HORIZONTAL, 0, image.getHeight(), last - offsetWidth, last);
        prevWasLine = false;
        int currentLine;

        for (int row = 0; row < image.getHeight(); row++)
        {
            if (histogram[row] > lineThreshold)
            {
                // If this is not a continuation of a known line.
                if (!prevWasLine)
                {
                    currentLine = getClosestLine(stafflines, row);

                    stafflines.get(currentLine).x2 = last;
                    stafflines.get(currentLine).y2 = row;
                    prevWasLine = true;
                }
            }
            else
            {
                prevWasLine = false;
            }
        }

        return (stafflines);
    }

    /**
     * Returns the index of the closest line in the vector of lines given (using the first point of the line) on the y
     * axis to the y value given.
     * 
     * @param stafflines The vector of staff lines to check.
     * @param y The value of y to find the closest line to.
     * 
     * @return The index of the closest line to the y value given on the y axis.
     */
    private static int getClosestLine(Vector<Line> stafflines, int y)
    {
        int closest = 0;
        int smallestDifference = Math.abs(stafflines.get(0).y1 - y);

        for (int index = 1; index < stafflines.size(); index++)
        {
            if (Math.abs(stafflines.get(index).y1 - y) < smallestDifference)
            {
                closest = index;
                smallestDifference = Math.abs(stafflines.get(index).y1 - y);
            }
        }

        return (closest);
    }

    /**
     * Writes an image to the standard out stream created from the staff lines given.
     * 
     * @param stafflines The staff lines to create an image from.
     */
    private static void writeStafflines(Vector<Line> stafflines)
    {
        if (verbosity > 0)
        {
            System.err.println("Writing staff lines image...");
        }

        // Create and initialize a new image.
        PBMImage stafflineImage = new PBMImage();
        stafflineImage.setWidth(image.getWidth());
        stafflineImage.setHeight(image.getHeight());
        stafflineImage.setVersion("P4");

        byte[][] pixels = new byte[image.getHeight()][(image.getWidth() + 7) / 8];
        for (int row = 0; row < image.getHeight(); row++)
        {
            for (int column = 0; column < (image.getWidth() + 7) / 8; column++)
            {
                pixels[row][column] = 0;
            }
        }
        stafflineImage.setPixels(pixels);

        // Draw the lines mathematically to the new image.
        Line line;

        // For every line.
        for (int index = 0; index < stafflines.size(); index++)
        {
            line = stafflines.get(index);

            // If line is not properly initialized, ignore it.
            if (line.x1 == -1 || line.x2 == -1 || line.y1 == -1 || line.y2 == -1)
            {
                if (verbosity > 2)
                {
                    System.err.println("Found incomplete staff line - ignoring.");
                }

                continue;
            }

            // Perform Midpoint Line Algorithm Version 3 (from lecture handout).
            int deltaX = line.x2 - line.x1;
            int deltaY = line.y2 - line.y1;

            int delta = 2 * deltaY - deltaX;
            int incrE = 2 * deltaY;
            int incrNE = 2 * (deltaY - deltaX);

            int y = line.y1;
            for (int x = line.x1; x < line.x2; x++)
            {
                for (int width = 0; width < line.width; width++)
                {
                    stafflineImage.setPixel(x, y + width, PBMImage.BLACK);
                }

                if (delta < 0)
                {
                    delta += incrE;
                }
                else
                {
                    delta += incrNE;
                    y++;
                }
            }
        }

        if (verbosity > 1)
        {
            System.err.println("Set pixels in output image: " + setPixelCount(stafflineImage));
        }

        //stafflineImage.writeToStream(System.out);
    }

    /**
     * Filters the image to reduce the level of noise.
     */
    private static void filterNoise()
    {
        if (verbosity > 0)
        {
            System.err.println("Filtering noise...");
        }

        // Create and initialize a new image.
        PBMImage filteredImage = new PBMImage();
        filteredImage.setWidth(image.getWidth());
        filteredImage.setHeight(image.getHeight());
        filteredImage.setVersion("P4");

        byte[][] pixels = new byte[image.getHeight()][(image.getWidth() + 7) / 8];
        for (int row = 0; row < image.getHeight(); row++)
        {
            for (int column = 0; column < (image.getWidth() + 7) / 8; column++)
            {
                pixels[row][column] = image.getPixels()[row][column];
            }
        }
        filteredImage.setPixels(pixels);

        int threshold = 3;

        if (verbosity > 2)
        {
            System.err.println("Applying noise filter with threshold of " + threshold + "...");
        }

        // For every pixel.
        for (int row = 0; row < image.getHeight(); row++)
        {
            for (int column = 0; column < image.getWidth(); column++)
            {
                // If filter is true change color of pixel.
                if (noiseFilter(column, row, threshold) == true)
                {
                    if (image.getPixel(column, row) == PBMImage.BLACK)
                    {
                        image.setPixel(column, row, PBMImage.WHITE);
                    }
                    else if (image.getPixel(column, row) == PBMImage.WHITE)
                    {
                        image.setPixel(column, row, PBMImage.BLACK);
                    }
                }
            }
        }
    }

    /**
     * Checks a pixel to see if it should be changed to reduce noise in the image.
     * 
     * @param x The position of the pixel on the x axis.
     * @param y The position of the pixel on the y axis.
     * @param threshold The amount of same color pixels that need to be present in the nine pixel square of which the
     * current pixel is the center. If less than this amount of pixels are present true is returned.
     * 
     * @return True if the pixel should be changed, false otherwise.
     */
    private static boolean noiseFilter(int x, int y, int threshold)
    {
        // Count the black pixels in the nine pixel square of which the given pixel is the center.
        int pixel;
        int count = 0;

        for (int row = y - 1; row <= y + 1; row++)
        {
            for (int column = x - 1; column <= x + 1; column++)
            {
                pixel = image.getPixel(column, row);

                if (pixel == PBMImage.BLACK)
                {
                    count++;
                }
            }
        }

        if (image.getPixel(x, y) == PBMImage.BLACK && count < threshold)
        {
            return (true);
        }
        else if (image.getPixel(x, y) == PBMImage.WHITE && count > 9 - threshold)
        {
            return (true);
        }

        return (false);
    }

    /**
     * Prints information on the usage of this program to the standard error stream.
     */
    private static void printUsage()
    {
        System.err.println();
        System.err.println("SCOTTIE usage:");
        System.err.println("scottie -help -verbose <level> -window <scale> -generate <operation> <filename>");
        System.err.println();
        System.err.println("-help, -verbose and -window are optional arguments.");
        System.err.println("-verbose must be followed by <level>, (an integer in the range 0-3).");
        System.err.println("-window must be followed by <scale>, (an integer - scale x represents 1:x).");
        System.err.println("<scale> is the scale to display the final image at.");
        System.err.println("-generate <operation> determines an operation to perform.");
        System.err.println("<operation> can be: filtnoise|stafflines|rotate|remove");
        System.err.println();
        System.err.println("<filename> is the file name to read the PBM image from.");
        System.err.println("If the file name is not supplied it will be read from the standard input stream.");
        System.err.println();
        System.err.println("The resulting image will be output to the standard output stream.");
        System.err.println("Any verbose output is output to the standard error stream.");
        System.err.println();
    }

    /**
     * Rmoves the staff lines from the image whilst attempting not to damage any other symbols in the image.
     * 
     * @param stafflines The staff lines to remove from the image.
     */
    private static void remove(Vector<Line> stafflines)
    {
        if (verbosity > 0)
        {
            System.err.println("Removing staff lines...");
        }

        // Create and initialize a new image.
        PBMImage removedImage = new PBMImage();
        removedImage.setWidth(image.getWidth());
        removedImage.setHeight(image.getHeight());
        removedImage.setVersion("P4");

        byte[][] pixels = new byte[image.getHeight()][(image.getWidth() + 7) / 8];
        for (int row = 0; row < image.getHeight(); row++)
        {
            for (int column = 0; column < (image.getWidth() + 7) / 8; column++)
            {
                pixels[row][column] = image.getPixels()[row][column];
            }
        }
        removedImage.setPixels(pixels);

        // Set all pixels on the staff lines that pass the filter test to white.
        Line line;
        int threshold = 6;

        if (verbosity > 2)
        {
            System.err.println("Applying staff filter with threshold of " + threshold + "...");
        }

        for (int index = 0; index < stafflines.size(); index++)
        {
            line = stafflines.get(index);

            // If line is not properly initialized, ignore it.
            if (line.x1 == -1 || line.x2 == -1 || line.y1 == -1 || line.y2 == -1)
            {
                if (verbosity > 2)
                {
                    System.err.println("Found incomplete staff line - ignoring.");
                }

                continue;
            }

            // Perform Midpoint Line Algorithm Version 3 (from lecture handout).
            int deltaX = line.x2 - line.x1;
            int deltaY = line.y2 - line.y1;

            int delta = 2 * deltaY - deltaX;
            int incrE = 2 * deltaY;
            int incrNE = 2 * (deltaY - deltaX);

            int y = line.y1;
            for (int x = line.x1; x < line.x2; x++)
            {
                if (stafflineFilter(x, y, line.width, threshold) == true)
                {
                    for (int width = -1; width < line.width + 1; width++)
                    {
                        removedImage.setPixel(x, y + width, PBMImage.WHITE);
                    }
                }

                if (delta < 0)
                {
                    delta += incrE;
                }
                else
                {
                    delta += incrNE;
                    y++;
                }
            }
        }

        image = removedImage;
    }

    /**
     * Rotates the image clockwise by the angle given.
     * 
     * @param angle The angle to rotate the image in radians.
     */
    private static void rotate(double angle)
    {
        if (verbosity > 0)
        {
            System.err.println("Rotating...");
        }

        // Create and initialize a new image.
        PBMImage rotatedImage = new PBMImage();
        rotatedImage.setWidth(image.getWidth());
        rotatedImage.setHeight(image.getHeight());
        rotatedImage.setVersion("P4");

        byte[][] pixels = new byte[image.getHeight()][(image.getWidth() + 7) / 8];
        for (int row = 0; row < image.getHeight(); row++)
        {
            for (int column = 0; column < (image.getWidth() + 7) / 8; column++)
            {
                pixels[row][column] = 0;
            }
        }
        rotatedImage.setPixels(pixels);

        // Find rotated positions for black pixels and set in new image.
        int xRotated;
        int yRotated;

        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                if (image.getPixel(x, y) == PBMImage.BLACK)
                {
                    xRotated = (int) Math.round(x * Math.cos(angle) - y * Math.sin(angle));
                    yRotated = (int) Math.round(x * Math.sin(angle) + y * Math.cos(angle));

                    rotatedImage.setPixel(xRotated, yRotated, PBMImage.BLACK);
                }
            }
        }

        image = rotatedImage;
    }

    /**
     * Counts all black pixels in the image given.
     * 
     * @param image The image to count black pixels in.
     * 
     * @return The number of black pixels in the image given.
     */
    private static int setPixelCount(PBMImage image)
    {
        int count = 0;

        for (int row = 0; row < image.getHeight(); row++)
        {
            for (int column = 0; column < image.getWidth(); column++)
            {
                if (image.getPixel(column, row) == PBMImage.BLACK)
                {
                    count++;
                }
            }
        }

        return (count);
    }

    /**
     * Checks a pixel on a staff line to see if it should be set to white. The position of the pixel passed should be
     * that of the highest pixel on the staff line (i.e. lowest y value for the value of x passed) and a result of
     * true denotes that all pixels for that value of x should be set to white.
     * 
     * @param x The position of the pixel on the x axis.
     * @param y The position of the pixel on the y axis.
     * @param width The width of the staff line the pixel being checked is on.
     * @param threshold The amount of black pixels that need to be present above and below the staffline for the
     * filter to return false.
     * 
     * @return True if the pixel should be set to white, false otherwise.
     */
    private static boolean stafflineFilter(int x, int y, int width, int threshold)
    {
        int count = 0;

        // Count the number of black pixels above the staff line.
        for (int column = x - 2; column <= x + 2; column++)
        {
            if (image.getPixel(column, y - 2) == PBMImage.BLACK)
            {
                count++;
            }
        }

        if (image.getPixel(x, y - 1) == PBMImage.BLACK)
        {
            count++;
        }

        // Count the number of black pixels below the staff line.
        for (int column = x - 2; column <= x + 2; column++)
        {
            if (image.getPixel(column, y + width + 1) == PBMImage.BLACK)
            {
                count++;
            }
        }

        if (image.getPixel(x, y + width) == PBMImage.BLACK)
        {
            count++;
        }

        if (count < threshold)
        {
            return (true);
        }

        return (false);
    }
}
