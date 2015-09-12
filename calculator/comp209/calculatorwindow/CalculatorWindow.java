package comp209.calculatorwindow;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Class CalculatorWindow represents a configurable desktop calculator window.
 *
 * <P>It manages a rectangular grid of buttons and a calculator's display.
 * It methods enable users to create the window and define the calculator's
 * buttons. Once the button layout has been set up, the window can be
 * opened.</P>
 *
 * <P>More precisely, the following code may be used to create and run
 * a calculator:</P>
 *
 * <P><CODE>//</CODE> Create window ...<BR>
 * <CODE>CalculatorWindow calc =
 *   new {@link #CalculatorWindow(int,int) CalculatorWindow}(</CODE>...<CODE>);
 *   </CODE><BR>
 * <CODE>//</CODE> Define buttons ...<BR>
 * <CODE>calc.{@link #addButton addButton}(</CODE>...<CODE>);</CODE><BR>
 * ...<BR> 
 * <CODE>//</CODE> Pop up window ...<BR>
 * <CODE>calc.{@link #popup};</CODE></P>
 *
 * <P>When the window is opened, it will manage all mouse events, reporting
 * all button presses to the user. The display can be controlled via the
 * {@link #setDisplay setDisplay()} method. The window also implements a
 * default close behaviour which causes the application to exit when the
 * window is closed.</P>
 *
 * @author Robi Malik
 */

public class CalculatorWindow extends JFrame
{

  //##########################################################################
  //# Constructors
  /**
   * Create a new calculator window with a default title.
   * @param  rows         The number of rows in the calculator's grid of
   *                      buttons.
   * @param  cols         The number of columns in the calculator's grid of
   *                      buttons.
   */
  public CalculatorWindow(int rows, int cols)
  {
    this("COMP209 Calculator", rows, cols);
  }

  /**
   * Create a new calculator window with a given title.
   * @param  title        The title to be used for the calculator window.
   * @param  rows         The number of rows in the calculator's grid of
   *                      buttons.
   * @param  cols         The number of columns in the calculator's grid of
   *                      buttons.
   */
  public CalculatorWindow(String title, int rows, int cols)
  {
    this(title, rows, cols, 0);
  }

  /**
   * Create a new calculator window with a given title and subdisplays.
   * Subdisplays are additional display cells using a very small font
   * that are placed below the main number display. Using them is completely
   * optional as far as the assignment concerned, but you may want to
   * use them in order to display additional status or debug information.
   * @param  title        The title to be used for the calculator window.
   * @param  rows         The number of rows in the calculator's grid of
   *                      buttons.
   * @param  cols         The number of columns in the calculator's grid of
   *                      buttons.
   * @param  subDisplays  The number of additional display cells placed
   *                      below the main display.
   */
  public CalculatorWindow(String title, int rows, int cols, int subDisplays)
  {
    super(title);
    mKeyListener = new CalculatorKeyListener();

    JComponent allDisplays = null;
    mMainDisplay = new JLabel();
    mMainDisplay.setFont(MAINDISPLAYFONT);
    mMainDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
    setDisplay("");
    if (subDisplays == 0) {
      allDisplays = mMainDisplay;
    } else {
      GridBagConstraints constraints = new GridBagConstraints();
      GridBagLayout displayLayout = new GridBagLayout();
      allDisplays = new JPanel(displayLayout);
      constraints.gridx = 0;
      constraints.gridy = 0;
      constraints.gridwidth = GridBagConstraints.REMAINDER;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.EAST;
      constraints.weightx = 1;
      constraints.weighty = 1;
      constraints.insets = new Insets(0, 2, 0, 2);
      displayLayout.setConstraints(mMainDisplay, constraints);
      allDisplays.add(mMainDisplay);
      mSubDisplays = new JLabel[subDisplays];
      constraints.gridy = 1;
      constraints.gridwidth = 1;
      for (int i = 0; i < subDisplays; i++) {
	mSubDisplays[i] = new JLabel();
	mSubDisplays[i].setFont(SUBDISPLAYFONT);
	constraints.gridx = i;
	if (i == 0) {
	  mSubDisplays[i].setHorizontalAlignment(SwingConstants.LEFT);
	  constraints.anchor = GridBagConstraints.WEST;
	} else if (i == subDisplays - 1) {
	  mSubDisplays[i].setHorizontalAlignment(SwingConstants.RIGHT);
	  constraints.anchor = GridBagConstraints.EAST;
	} else {
	  mSubDisplays[i].setHorizontalAlignment(SwingConstants.CENTER);
	  constraints.anchor = GridBagConstraints.CENTER;
	}
	displayLayout.setConstraints(mSubDisplays[i], constraints);
	allDisplays.add(mSubDisplays[i]);
	setSubDisplay(i, "");
      }
    }
    allDisplays.setBackground(LABELBACKGROUND);
    allDisplays.setOpaque(true);
    Border displayBorder1 = BorderFactory.createMatteBorder
      (INSETS, 0, INSETS, 0, BACKGROUND);
    Border displayBorder2 = BorderFactory.createLoweredBevelBorder();
    Border displayBorder = BorderFactory.createCompoundBorder
      (displayBorder1, displayBorder2);
    allDisplays.setBorder(displayBorder);

    LayoutManager buttonLayout = new GridLayout(rows, cols, INSETS, INSETS);
    mButtonPanel = new JPanel(buttonLayout);
    mButtonPanel.setOpaque(false);
    mButtonArray = new CalculatorButton[rows][cols];

    LayoutManager mainLayout = new BorderLayout(INSETS, INSETS);
    JPanel calculator = new JPanel(mainLayout);
    calculator.add(allDisplays, BorderLayout.NORTH);
    calculator.add(mButtonPanel, BorderLayout.CENTER);
    Border outerBorder1 = BorderFactory.createBevelBorder
      (BevelBorder.RAISED,
       SHADOWLIGHT2, SHADOWLIGHT1, SHADOWDARK2, SHADOWDARK1);
    Border outerBorder2 = BorderFactory.createEmptyBorder
      (INSETS, INSETS, INSETS, INSETS);
    Border outerBorder = BorderFactory.createCompoundBorder
      (outerBorder1, outerBorder2);
    calculator.setBorder(outerBorder);
    calculator.setBackground(BACKGROUND);

    Container pane = getContentPane();
    pane.add(calculator);
    pack();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


  //##########################################################################
  //# Creating Buttons
  /**
   * Add a new button to this calculator.
   * This method creates a new button and adds it to the calculator's
   * grid of buttons.
   * @param  name         The name of the button.
   *                      This method knows the button names given in the
   *                      assignment, and uses special colours to render
   *                      buttons with the known names.
   * @param  row          The number of the row in which the button is created.
   *                      Rows are counted from the top, starting at&nbsp;0.
   * @param  col          The number of the column in which the button is
   *                      created. Columns are counted from the left,
   *                      starting at&nbsp;0.
   * @param  function     The function object to be associated with the button.
   *                      Whenever the button is pressed, the function's
   *                      {@link CalculatorFunction#invoke() invoke()} method
   *                      will be called.
   * @throws IllegalStateException if this method is called after the
   *                      calculator has been displayed using the
   *                      {@link #popup} method.
   * @throws IllegalArgumentException if the given row or column number
   *                      is outside of the valid range given by the size
   *                      of the button grid as specified when creating the
   *                      calculator window.
   */
  public void addButton(String name, int row, int col,
			CalculatorFunction function)
  {
    if (mButtonArray == null) {
      throw new IllegalStateException
	("Trying to create a button after opening calculator window!");
    } else if (row < 0 || row >= mButtonArray.length ||
	       col < 0 || col >= mButtonArray[row].length) {
      throw new IllegalArgumentException
	("Button position " + row + "/" + col + " out of range!");
    } else {
      mButtonArray[row][col] =
	new CalculatorButton(name, function, mKeyListener);
    }
  }


  //##########################################################################
  //# Opening the Window
  /**
   * Display the calculator window.
   * This method pops up and renders the calculator window.
   * The window will react to mouse clicks by invoking the function
   * objects associated with the buttons.
   * Once this method is called, it is no longer possible to create new
   * buttons using the {@link #addButton addButton()} method.
   */
  public void popup()
  {
    final int rows = getNumRows();
    final int cols = getNumCols();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
	JComponent comp = mButtonArray[row][col];
	if (comp == null) {
	  comp = new JLabel();
	}
	mButtonPanel.add(comp);
      }
    }
    mButtonArray = null;
    pack();
    setVisible(true);
  }


  //##########################################################################
  //# Manipulating the Display
  /**
   * Change the display.
   * This method is used to change the calculator's display.
   * @param  display      A string to be displayed. The display normally
   *                      cannot hold more than eighteen characters, therefore
   *                      it is not a good idea to use this method to display
   *                      longer strings.
   */
  public void setDisplay(String display)
  {
    if (display.length() == 0) {
      mMainDisplay.setText("0");
      mMainDisplay.setForeground(LABELBACKGROUND);
    } else {
      mMainDisplay.setText(display);
      mMainDisplay.setForeground(LABELCOLOR);
    }
  }


  /**
   * Change the text of a subdisplay.
   * Subdisplays are additional display cells using a very small font
   * that are placed below the main number display. Using them is completely
   * optional as far as the assignment concerned, but you may want to
   * use them in order to display additional status or debug information.
   * @param  index        The number of the subdisplay to be modified.
   * @param  display      The string to be displayed in the subdisplay.
   * @throws ArrayIndexOutOfBoundsException to indicate that the given
   *                      given display index is outside of the range of
   *                      available displayed as specified by the constructor.
   */
  public void setSubDisplay(int index, String display)
  {
    if (mSubDisplays == null) {
      throw new ArrayIndexOutOfBoundsException
	("No subdisplays have been declared!");
    } else if (display.length() == 0) {
      mSubDisplays[index].setText("(");
      mSubDisplays[index].setForeground(LABELBACKGROUND);
    } else {
      mSubDisplays[index].setText(display);
      mSubDisplays[index].setForeground(LABELCOLOR);
    }
  }
      

  //##########################################################################
  //# Auxiliary Methods
  /**
   * Get the number of rows of buttons in this calculator window.
   */
  private int getNumRows()
  {
    return mButtonArray.length;
  }

  /**
   * Get the number of columns of buttons in this calculator window.
   */
  private int getNumCols()
  {
    try {
      return mButtonArray[0].length;
    } catch (ArrayIndexOutOfBoundsException exception) {
      return 0;
    }
  }


  //##########################################################################
  //# Data Members
  /**
   * The display.
   * The calculator's display is implemented as a label
   * which is constantly assigned new text as the display changes.
   */
  private JLabel mMainDisplay;

  /**
   * The subdisplays.
   * This is an array of labels which are constantly assigned new text
   * as the display changes. The array may also be <CODE>null</CODE>,
   * indicating that the user has not requested any subdisplays.
   * @see #setSubDisplay
   */
  private JLabel[] mSubDisplays;

  /**
   * The button panel.
   * The button panel covers the bottom part of the calculator window.
   * Its layout is defined in the constructor according to the number
   * of rows and columns requested. The actual buttons are created and
   * added to the panel in method {@link #popup}.
   */
  private JPanel mButtonPanel;

  /**
   * The array of buttons.
   * This array is assigned all the buttons requested by calls to the
   * {@link #addButton addButton()} method. The {@link #addButton
   * addButton()} method creates the buttons and stores them in this array,
   * from where they are copied into the button panel {@link #mButtonPanel}
   * when the {@link #popup} method is called. After the call to {@link
   * #popup}, the button array is cleared, i.e. reset to <CODE>null</CODE>,
   * in order to prevent further calls to {@link #addButton addButton()}.
   */
  private CalculatorButton mButtonArray[][];

  /**
   * The key listener.
   * This listener is shared by all the buttons, and handles all
   * keyboard events associated with some button.
   */
  private CalculatorKeyListener mKeyListener;


  //##########################################################################
  //# Class Constants
  /**
   * The distance between the window border and the calculator display
   * or the edge of the button panel.
   */
  private static final int INSETS = 5;
  /**
   * The background colour of the calculator window.
   */
  private static final Color BACKGROUND = new Color(80, 0, 80);
  /**
   * The colour of the outer portion of the light shadow for the
   * calculator display's border.
   */
  private static final Color SHADOWLIGHT1 = new Color(152, 96, 152);
  /**
   * The colour of the inner portion of the light shadow for the
   * calculator display's border.
   */
  private static final Color SHADOWLIGHT2 = new Color(224, 192, 224);
  /**
   * The colour of the outer portion of the dark shadow for the
   * calculator display's border.
   */
  private static final Color SHADOWDARK1 = new Color(40, 0, 40);
  /**
   * The colour of the inner portion of the dark shadow for the
   * calculator display's border.
   */
  private static final Color SHADOWDARK2 = Color.BLACK;
  /**
   * The text colour of the display.
   */
  private static final Color LABELCOLOR = Color.BLACK;
  /**
   * The background colour of the display.
   */
  private static final Color LABELBACKGROUND = new Color(255, 255, 192);
  /**
   * The font used for the main display.
   */
  private static final Font MAINDISPLAYFONT =
    new Font("SansSerif", Font.BOLD, 20);
  /**
   * The font used for the subdisplays.
   */
  private static final Font SUBDISPLAYFONT =
    new Font("SansSerif", Font.PLAIN, 10);

}
