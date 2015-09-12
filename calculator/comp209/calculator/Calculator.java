package comp209.calculator;

import comp209.calculatorwindow.*;
import comp209.calculator.*;

/**
 * This is a dummy calculator class that can be used as a starting point
 * for implementing the calculator functionality. Its {@link #main} method
 * contains the code needed to set up all the buttons required for the
 * assignment.
 *
 * @author Robi Malik
 */

public class Calculator {

  /**
   * A main method that creates and pops up a calculator window
   * with all the buttons required for the assignment.
   * No functionality is attached to any button.
   */
  public static void main(String[] args)
  {
    CalculatorWindow window = new CalculatorWindow("Phat Calculator!", 7, 5, 3);

    window.addButton("AC", 0, 0, new AC(window));
    window.addButton("sin", 0, 1, new Sin(window));
    window.addButton("cos", 0, 2, new Cos(window));
    window.addButton("tan", 0, 3, new Tan(window));
    window.addButton("deg", 0, 4, new Deg(window));
    window.addButton("CE", 1, 0, new CE(window));
    window.addButton("fact", 1, 1, new Fact(window));
    window.addButton("log", 1, 2, new Log(window));
    window.addButton("pow", 1, 3, new Operator(window, "pow"));
    window.addButton("pi", 1, 4, new Pi(window, "0"));
    window.addButton("INV", 2, 0, new Inverse(window));
    window.addButton("(", 2, 1, new Bracket(window, "("));
    window.addButton(")", 2, 2, new Bracket(window, ")"));
    window.addButton("1/x", 2, 3, new OneOverX(window));
    window.addButton("/", 2, 4, new Operator(window, "/"));
    window.addButton("STO", 3, 0, new Store(window));
    window.addButton("7", 3, 1, new Digit(window, "7"));
    window.addButton("8", 3, 2, new Digit(window, "8"));
    window.addButton("9", 3, 3, new Digit(window, "9"));
    window.addButton("*", 3, 4, new Operator(window, "*"));
    window.addButton("RCL", 4, 0, new Recall(window, "0"));
    window.addButton("4", 4, 1, new Digit(window, "4"));
    window.addButton("5", 4, 2, new Digit(window, "5"));
    window.addButton("6", 4, 3, new Digit(window, "6"));
    window.addButton("-", 4, 4, new Operator(window, "-"));
    window.addButton("SUM", 5, 0, new Sum(window));
    window.addButton("1", 5, 1, new Digit(window, "1"));
    window.addButton("2", 5, 2, new Digit(window, "2"));
    window.addButton("3", 5, 3, new Digit(window, "3"));
    window.addButton("+", 5, 4, new Operator(window, "+"));
    window.addButton("EXC", 6, 0, new Exchange(window, "0"));
    window.addButton("0", 6, 1, new Digit(window, "0"));
    window.addButton(".", 6, 2, new Digit(window, "."));
    window.addButton("+/-", 6, 3, new Negation(window));
    window.addButton("=", 6, 4, new Calculate(window));

    window.setDisplay("Hello");

    window.popup();
  }

}
