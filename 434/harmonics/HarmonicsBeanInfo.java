package harmonics;
import java.beans.*;

public class HarmonicsBeanInfo extends SimpleBeanInfo {

  public BeanDescriptor getBeanDescriptor() {
    return new BeanDescriptor(Harmonics.class, 
      HarmonicsCustomizer.class);
  }
}