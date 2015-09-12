import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A canvas that displays a PBM image as well as displaying analysis information used to refine staff line recognition.
 * 
 * Images can be scaled so that they fit on the screen. When doing this for a scale of 2, every second pixel will be
 * drawn.
 * 
 * @author gb21
 */
public class PBMCanvas extends Canvas
{
    /**
     * The image to display.
     */
    private PBMImage image = null;

    /**
     * The scale to display the image at, a scale of x equates to a scale of 1:x.
     */
    private int scale = 1;

    /**
     * Creates an instance of PBMCanvas.
     * 
     * @param image The image to display.
     * @param scale The scale to display the image at.
     */
    public PBMCanvas(PBMImage image, int scale)
    {
        this.image = image;
        this.scale = scale;

        setSize(image.getWidth() / scale, image.getHeight() / scale * 3);
        setBackground(Color.white);
    }

    public void paint(Graphics g)
    {
        // Explaniation of complex parts of this function can be found in the similar function detectStafflines() in
        // class Scottie.
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(getBackground());
        g2d.clearRect(0, 0, image.getWidth() / scale, image.getHeight() / scale * 3);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);

        // Draw PBM pixels.
        for (int row = 0; row < image.getHeight(); row += scale)
        {
            for (int column = 0; column < image.getWidth(); column += scale)
            {
                if (image.getPixel(column, row) == PBMImage.BLACK)
                {
                    g2d.drawLine(column / scale, row / scale, column / scale, row / scale);
                }
            }
        }

        // Draw vertical histogram.
        int lastRow = image.getHeight() / scale * 2 - 1;
        int[] histogram = image.getHistogram(PBMImage.VERTICAL);
        int first = -1;
        int last = -1;
        
        int staffThreshold = image.getHeight() / 20;
        
        int[] range = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Range of 10.
        int rangeThreshold = 7;
        int rangeCount = 0;

        for (int column = 0; column < image.getWidth(); column += scale)
        {
            g2d.setColor(Color.black);
            g2d.drawLine(column / scale, lastRow, column / scale, lastRow - (histogram[column / scale] - 1) / scale);

            // Update staff range.
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
            
            if (histogram[column] > staffThreshold)
            {
                // Color important areas red.
                g2d.setColor(Color.red);
                g2d.drawLine(column / scale, lastRow - 50, column / scale, lastRow - 40);

                if (rangeCount >= rangeThreshold)
                {
                    if (first == -1)
                    {
                        first = column - range.length;
                    }
                    last = column;
                }
            }
        }

        // Color important areas blue.
        int offsetWidth = image.getWidth() / 5;
        
        g2d.setColor(Color.blue);
        g2d.drawLine(first / scale, lastRow - 60, first / scale, lastRow - 50);
        g2d.drawLine((first + offsetWidth) / scale, lastRow - 60, (first + offsetWidth) / scale, lastRow - 50);
        g2d.drawLine(last / scale, lastRow - 60, last / scale, lastRow - 50);
        g2d.drawLine((last - offsetWidth) / scale, lastRow - 60, (last - offsetWidth) / scale, lastRow - 50);

        // Draw first horizontal histogram.
        int firstRow = image.getHeight() / scale * 2;
        int lineThreshold = offsetWidth / 2;
        
        histogram = image.getHistogram(PBMImage.HORIZONTAL, 0, image.getHeight(), first, first + offsetWidth);
        
        for (int row = 0; row < image.getHeight(); row += scale)
        {
            g2d.setColor(Color.black);
            g2d.drawLine(first / scale, row / scale + firstRow, (first + histogram[row] - 1) / scale,
                    row / scale + firstRow);
            
            // Color important areas blue.
            if (histogram[row] - 1 > lineThreshold)
            {
                g2d.setColor(Color.blue);
                g2d.drawLine(first / scale, row / scale + firstRow, (first / scale) + 100,
                        row / scale + firstRow);
            }
        }
        
        // Draw second horizontal histogram.
        histogram = image.getHistogram(PBMImage.HORIZONTAL, 0, image.getHeight(), last - offsetWidth, last);

        for (int row = 0; row < image.getHeight(); row += scale)
        {            
            g2d.setColor(Color.black);
            g2d.drawLine((last - offsetWidth) / scale, row / scale + firstRow,
                    ((last - offsetWidth) + histogram[row] - 1) / scale, row / scale + firstRow);
            
            // Color important areas blue.
            if (histogram[row] - 1 > lineThreshold)
            {
                g2d.setColor(Color.blue);
                g2d.drawLine((last - offsetWidth) / scale, row / scale + firstRow,
                        ((last - offsetWidth) / scale) + 100, row / scale + firstRow);
            }
        }
    }
}
