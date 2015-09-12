package comp209.calculatorwindow;

/**
 * This interface represents the behaviour of a calculator button.  It is
 * implemented by objects that provide calculator functionality to the
 * calculator window. Each button registered via the {@link
 * CalculatorWindow#addButton CalculatorWindow.addButton()} method should
 * be attached with an object implementing this interface. The interface
 * method {@link #invoke invoke()} is called from class {@link
 * CalculatorWindow} whenever the corresponding button has been pressed.
 *
 * @author Robi Malik
 */

public interface CalculatorFunction {

  /**
   * Callback method for button press.  This method is called from class
   * {@link CalculatorWindow} whenever the button associated with this
   * calculator function object has been pressed.
   */
  public void invoke();

}
