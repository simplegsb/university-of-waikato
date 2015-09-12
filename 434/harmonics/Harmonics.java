package harmonics;
import java.awt.*;
import java.util.*;
import javax.swing.JPanel;

public class Harmonics extends JPanel  {
  public final static int NFREQUENCIES = 20;
  private BitSet frequencies;

  public Harmonics() {
    frequencies = new BitSet(NFREQUENCIES);
    Dimension d = new Dimension(301, 150);
    setMinimumSize(d);
    setPreferredSize(d);
    setMaximumSize(d);
  }

  public BitSet getFrequencies() {
    return frequencies;
  }

  public void setFrequencies(BitSet frequencies) {
    this.frequencies = frequencies;
    repaint();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // Draw rectangle around the display area
    Dimension d = getSize();
    int width = d.width;
    int height = d.height;
    g.setColor(Color.blue);
    g.drawRect(0, 0, width - 1, height - 1);

    // Draw lines for x and y axes
    int y = height/2;
    g.drawLine(0, y, width, y);
    g.drawLine(width/2, 0, width/2, height);

    // Compute data values and remember 
    // max and min values
    double max = 0;
    double min = 0;
    double deltax = 2 * Math.PI/(width - 1);
    double x = -Math.PI;
    double data[] = new double[width];
    for(int i = 0; i < width; i++) {
      double value = f(x);
      data[i] = value;
      min = (value < min) ? value : min;
      max = (value > max) ? value : max;
      x += deltax;
    }

    // Scale and translate data values
    double scale = height/(max - min);
    for(int i = 0; i < width; i++) {
      double value = data[i];
      double k = (value - min)/(max - min);
      data[i] = height * (1 - k);
    }

    // Draw curve for data values
    g.setColor(Color.black);
    for(int i = 1; i < width; i++) {
      g.drawLine(i - 1, (int)data[i - 1], i, (int)data[i]);
    }
  }

  private double f(double x) {
    double value = 0;
    for(int i = 0; i < NFREQUENCIES; i++) {
      if(frequencies.get(i)) {
        value += ((double)1/(i + 1)) * Math.sin((i+1) * x);
      }
    }
    return value;
  }
}
