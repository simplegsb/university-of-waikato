package comp209.calculatorwindow;

import java.awt.event.*;
import java.util.*;


/**
 * <P>A key listener for calculator buttons.</P>
 *
 * <P>This implementation of the {@link java.awt.event.KeyListener} interface
 * is used to handle all keyboard events for the calculator window. It is
 * associated with all the buttons, in order to make sure that keyboard
 * events are handled, independently of which button currently possesses
 * the keyboard focus.</P>
 *
 * <P>This key listener class also has the ability to determine which
 * key events are to be associated with which button. The choice is based
 * on the button name in method {@link #registerKeys registerKeys()}
 * which is invoked by the constructor of each button.
 */

class CalculatorKeyListener implements KeyListener
{
  //##########################################################################
  //# Constructors
  /**
   * Create a new calculator key listener.
   */
  CalculatorKeyListener()
  {
    mKeyMap = new HashMap(64);
  }

  /**
   * Create a new calculator key listener.
   * @param  mapSize     The initial size to be used for the listener's
   *                     table of key codes.
   */
  CalculatorKeyListener(int mapSize)
  {
    mKeyMap = new HashMap(mapSize);
  }


  //##########################################################################
  //# Defining Key Events
  /**
   * Register key codes for a button.
   * This method analyses a given button name, and figures out which
   * keyboard event might be associated with such a button. For example,
   * a button named "+" will be associated with the "+" key on the keyboard.
   * @param  name        The label used by a button.
   * @param  button      The calculator button using the label.
   */
  void registerKeys(String name, CalculatorButton button)
  {
    if (name.length() != 1) {
      return;
    }
    char charac = name.charAt(0);
    addKey(charac, button);
    if (charac == '=') {
      addKey(10, button);
    }
  }

  /**
   * Register a key code.
   * This method associates a key to a button by the key code.
   * @param  charac      The character representing to key to be checked.
   * @param  button      The calculator button to be associated with the event.
   */
  void addKey(int charac, CalculatorButton button)
  {
    Integer key = new Integer(charac);
    mKeyMap.put(key, button);
  }


  //##########################################################################
  //# Interface java.awt.event.KeyListener
  /**
   * The key-typed handler.
   * This handler is invoked when a key has been completely typed.
   * It checks whether there is a calculator button associated with the
   * key typed, and id so, simulates a button click event on that button.
   */
  public void keyTyped(KeyEvent event)
  {
    Integer key = new Integer(event.getKeyChar());
    CalculatorButton button = (CalculatorButton) mKeyMap.get(key);
    if (button != null) {
      button.doClick(150);
    }
  }

  /**
   * The key-pressed handler. Does nothing.
   */
  public void keyPressed(KeyEvent event)
  {
  }

  /**
   * The key-released handler. Does nothing.
   */
  public void keyReleased(KeyEvent event)
  {
  }


  //##########################################################################
  //# Data Members
  /**
   * A map, mapping key codes (encoded as {@link java.lang.Integer} objects)
   * to the buttons ({@link CalculatorButton} objects) to be invoked.
   */
  private Map mKeyMap;
}
