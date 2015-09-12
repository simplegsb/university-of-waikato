package comp209.calculatorwindow;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * <P>A calculator button.</P>
 *
 * <P>Objects of this class represent the buttons of the calculator window.
 * These are Swing buttons (class {@link javax.swing.JButton}), which
 * simply pass on their button press events to the {@link
 * CalculatorFunction#invoke invoke()} method of an associated {@link
 * CalculatorFunction} object.</P>
 *
 * In addition, this class provides the functionality to choose different
 * button colours based on the names given to buttons on creation.
 *
 * @author Robi Malik
 */

class CalculatorButton extends JButton implements ActionListener
{
  //##########################################################################
  //# Constructors
  /**
   * Create a new calculator button.
   * This constructor determines the button colour from the given button
   * name.
   * @param  name        The name of the button.
   * @param  function    The calculator function associated with the button.
   *                     If non-null, the {@link CalculatorFunction#invoke
   *                     invoke()} method of this object will be called
   *                     whenever the button receives a mouse click event.
   * @param  keyListener The key listener to be used for registering and
   *                     handling key press events. This listener is registered
   *                     as a key listener for the new button, and depending
   *                     on the button's name, some key strokes may be
   *                     registered with it.
   */
  CalculatorButton(String name,
		   CalculatorFunction function,
		   CalculatorKeyListener keyListener)
  {
    super(name);
    mFunction = function;
    Border border1 = BorderFactory.createBevelBorder
      (BevelBorder.RAISED, SHADOWLIGHT, SHADOWDARK);
    Border border2 = BorderFactory.createEmptyBorder
      (INSETS, INSETS, INSETS, INSETS);
    Border border = BorderFactory.createCompoundBorder(border1, border2);
    setBorder(border);
    setFont(LABELFONT);
    setFocusPainted(false);
    setColors(name);
    addActionListener(this);
    keyListener.registerKeys(name, this);
    addKeyListener(keyListener);
  }

  //##########################################################################
  //# Interface java.awt.event.ActionListener
  /**
   * Callback method for button press event.
   * This method is called by the AWT when this button has been pressed by
   * some mouse or keyboard event. It invokes the {@link
   * CalculatorFunction#invoke invoke()} method of the button's calculator
   * function {@link #mFunction}.
   */
  public void actionPerformed(ActionEvent event)
  {
    if (mFunction != null) {
      mFunction.invoke();
    }
  }

  //##########################################################################
  //# Private Methods
  /**
   * Set the colours for this button based on the button name.
   * This method inspects the given name, and tries to choose a suitable
   * set of colours for a button with that name. Known names are given
   * special colours, so the users can immediately see that they have
   * chosen the right names.
   * @param  name     The name given to the button.
   */
  private void setColors(String name)
  {
    Map map = getColorMap();
    String lower = name.toLowerCase();
    ButtonColors setter = (ButtonColors) map.get(lower);
    if (setter != null) {
      setter.setColors(this);
    }
  }

  /**
   * Get the colour map.
   * This method returns a map which is used to look up button colours
   * based on button names. The map is stored in the static data member
   * {@link #sColorMap} and is initialised by this method as needed.
   * @see #setColors
   */
  private static Map getColorMap()
  {
    if (sColorMap == null) {
      sColorMap = new HashMap(64);
      sColorMap.put("ac", COLORSONOFF);
      sColorMap.put("ce", COLORSOPERATION);
      sColorMap.put("inv", COLORSOPERATION);
      sColorMap.put("sin", COLORSNORMAL);
      sColorMap.put("cos", COLORSNORMAL);
      sColorMap.put("tan", COLORSNORMAL);
      sColorMap.put("deg", COLORSNORMAL);
      sColorMap.put("fact", COLORSNORMAL);
      sColorMap.put("log", COLORSNORMAL);
      sColorMap.put("pow", COLORSNORMAL);
      sColorMap.put("(", COLORSNORMAL);
      sColorMap.put(")", COLORSNORMAL);
      sColorMap.put("1/x", COLORSNORMAL);
      sColorMap.put("pi", COLORSNORMAL);
      sColorMap.put("sto", COLORSNORMAL);
      sColorMap.put("rcl", COLORSNORMAL);
      sColorMap.put("sum", COLORSNORMAL);
      sColorMap.put("exc", COLORSNORMAL);
      sColorMap.put("+", COLORSOPERATION);
      sColorMap.put("-", COLORSOPERATION);
      sColorMap.put("*", COLORSOPERATION);
      sColorMap.put("/", COLORSOPERATION);
      sColorMap.put("=", COLORSOPERATION);
      sColorMap.put(".", COLORSDIGIT);
      sColorMap.put("+/-", COLORSDIGIT);
      for (int digit = 0; digit <= 9; digit++) {
	String digitName = Integer.toString(digit);
	sColorMap.put(digitName, COLORSDIGIT);
      }
    }
    return sColorMap;
  }    


  //##########################################################################
  //# Local Classes
  /**
   * A set of button colours.
   * This abstract class represents a possible set of colours of calculator
   * buttons. It only has the ability to define the foreground and
   * background colour of a calculator button.
   */
  private static abstract class ButtonColors {
    /**
     * Set the colours of a calculator button.
     * This method assigns the foreground and background colour associated
     * with this colour set to a calculator button.
     * @param  button The button to be coloured.
     */
    abstract void setColors(CalculatorButton button);
  }

  /**
   * The set of normal button colours.
   * Normal button colours are used for all general buttons, such as the
   * unary functions, the parentheses and the memory buttons.
   */
  private static class ButtonColorsNormal extends ButtonColors {
    /**
     * Assign normal colours to a calculator button.
     */
    void setColors(CalculatorButton button)
    {
      button.setBackground(NORMALBACKGROUND);
      button.setForeground(NORMALFOREGROUND);
    }
  }

  /**
   * The set of digit button colours.
   * Digit button colours are used for the digit buttons, including the
   * decimal point and the "+/-" button.
   */
  private static class ButtonColorsDigit extends ButtonColors {
    /**
     * Assign digit colours to a calculator button.
     */
    void setColors(CalculatorButton button)
    {
      button.setBackground(DIGITBACKGROUND);
      button.setForeground(DIGITFOREGROUND);
    }
  }

  /**
   * The set of operation button colours.
   * Operation button colours are used for the binary operation buttons
   * "+", "-", "*", etc.
   */
  private static class ButtonColorsOperation extends ButtonColors {
    /**
     * Assign operation colours to a calculator button.
     */
    void setColors(CalculatorButton button)
    {
      button.setBackground(OPERATIONBACKGROUND);
      button.setForeground(NORMALFOREGROUND);
    }
  }

  /**
   * The set of on-off button colours.
   * On-off button colours are reserved for the power-on button "AC".
   */
  private static class ButtonColorsOnOff extends ButtonColors {
    /**
     * Assign on-off colours to a calculator button.
     */
    void setColors(CalculatorButton button)
    {
      button.setBackground(ONOFFBACKGROUND);
      button.setForeground(NORMALFOREGROUND);
    }
  }


  //##########################################################################
  //# Data Members
  /**
   * The calculator function associated with this button.
   * If non-null, the {@link CalculatorFunction#invoke invoke()} method of
   * this object is called whenever the button receives a mouse click
   * event.
   */
  private CalculatorFunction mFunction;


  //##########################################################################
  //# Class Variables
  /**
   * The colour map.
   * This map is used to look up button colours based on button names. It
   * maps strings (class {@link java.lang.String}) to objects of class
   * ButtonColors. This map is a static variable that is initialised when
   * the {@link #getColorMap} method is first called, and not changed any
   * more after that.
   */
  private static Map sColorMap;

  //##########################################################################
  //# Class Constants
  /**
   * A representative of the normal colour set.
   * This object is inserted into the colour map {@link #sColorMap} for all
   * entries that require normal colours.
   */
  private static final ButtonColors COLORSNORMAL =
    new ButtonColorsNormal();
  /**
   * A representative of the digit colour set.
   * This object is inserted into the colour map {@link #sColorMap} for all
   * entries that require digit colours.
   */
  private static final ButtonColors COLORSDIGIT =
    new ButtonColorsDigit();
  /**
   * A representative of the operation colour set.
   * This object is inserted into the colour map {@link #sColorMap} for all
   * entries that require operation colours.
   */
  private static final ButtonColors COLORSOPERATION =
    new ButtonColorsOperation();
  /**
   * A representative of the on-off colour set.
   * This object is inserted into the colour map {@link #sColorMap} for all
   * entries that require on-off colours.
   */
  private static final ButtonColors COLORSONOFF =
    new ButtonColorsOnOff();

  /**
   * The background colour for all normal buttons.
   */
  private static final Color NORMALBACKGROUND = new Color(48, 48, 48);
  /**
   * The background colour for all digit buttons.
   */
  private static final Color DIGITBACKGROUND = Color.WHITE;
  /**
   * The background colour for all operation buttons.
   */
  private static final Color OPERATIONBACKGROUND = new Color(64, 64, 255);
  /**
   * The background colour for all on-off buttons.
   */
  private static final Color ONOFFBACKGROUND = new Color(192, 0, 0);
  /**
   * The foreground colour for all normal, operation, and on-off buttons.
   */
  private static final Color NORMALFOREGROUND = Color.WHITE;
  /**
   * The foreground colour for all digit buttons.
   */
  private static final Color DIGITFOREGROUND = NORMALBACKGROUND;
  /**
   * The light shadow colour used for the border of all buttons.
   */
  private static final Color SHADOWLIGHT = new Color(224, 192, 224);
  /**
   * The dark shadow colour used for the border of all buttons.
   */
  private static final Color SHADOWDARK = Color.BLACK;

  /**
   * The amount of extra space added to the inner side of the border of
   * each button.
   */
  private static final int INSETS = 2;

  /**
   * The font used for all button names.
   */
  private static final Font LABELFONT = new Font("SansSerif", Font.BOLD, 16);

}
