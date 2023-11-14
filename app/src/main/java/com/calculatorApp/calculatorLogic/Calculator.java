package com.calculatorApp.calculatorLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple calculator class that supports basic arithmetic operations and mathematical functions.
 * The Calculator class allows users to evaluate mathematical expressions provided as ArrayList of strings,
 * with support for substitution of variables and handling of functions. It performs calculations using
 * the specified formula and variable values.
 * <p>
 * This class provides methods to set the formula and variables, as well as calculate results. It supports
 * addition, subtraction, multiplication, division, exponentiation, and trigonometric functions like sine,
 * cosine, and tangent.
 */
public class Calculator {
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSE_BRACKET = ")";

    /**
     * Calculates the result of an expression based on the given parsed ArrayList<String> formula
     * and variables stored in the HashMap<String, Double>.
     *
     * @param formula   Parsed ArrayList<String> containing the expression components.
     * @param variables Parsed variables stored in a HashMap<String, Double> where String represents the
     *                  variable name and Double represents its value.
     * @return Double result of the calculation.
     */
    public Double calculate(ArrayList<String> formula, HashMap<String, Double> variables) {

        // Create a copy of the formula to work with it without altering the original.
        ArrayList<String> currentEquation = new ArrayList<>(formula);

        // Substitute variable values into the expression.
        substitutingVariablesIntoExpression(currentEquation, variables);

        // Check for unknown variables in the equation.
        if (hasUnknownVariables(currentEquation)) {
            throw new ArithmeticException("Error: unknown variables");
        }

        double result = mainCalculate(currentEquation, 0, currentEquation.size());
        if(currentEquation.size() > 1){
            throw new ArithmeticException("Error: incorrect equation");
        }

        return result;
    }

    /**
     * Calculates the result of an expression based on the given parsed ArrayList<String> formula
     *
     * @param formula Parsed ArrayList<String> containing the expression components.
     * @return Double result of the calculation.
     */
    public Double calculate(ArrayList<String> formula) {

        // Create a copy of the formula to work with it without altering the original.
        ArrayList<String> currentEquation = new ArrayList<>(formula);

        double result = mainCalculate(currentEquation, 0, currentEquation.size());
        if(currentEquation.size() > 1){
            throw new ArithmeticException("Error: incorrect equation");
        }

        return result;
    }


    /**
     * recursive method in depth for count all expiration.
     *
     * @param currentEquation Parsed ArrayList<String> containing the expression components.
     * @param from            the index of element from which we start calculate
     * @param to              the index of element to which we start calculate
     * @return double result of calculate
     */
    private Double mainCalculate(ArrayList<String> currentEquation, int from, int to) {

        openBrackets(currentEquation, from, to);

        evaluateEquation(currentEquation, from, to);

        return Double.parseDouble(currentEquation.get(0));
    }

    /**
     * Performs calculations for functions within the equation range.
     *
     * @param currentEquation The equation components.
     * @param from            The starting index for calculation.
     * @param to              The ending index for calculation.
     */
    private void evaluateEquation(ArrayList<String> currentEquation, int from, int to) {
        for (Functions function : Functions.values()) {
            for (int i = from; i < to && i < currentEquation.size() - 1; i++) {
                if (currentEquation.get(i).equals(function.name().toLowerCase())) {
                    double result = functionAction(i, currentEquation);
                    currentEquation.remove(i + 1);
                    currentEquation.set(i, String.valueOf(result));

                    i--;
                    to--;
                }
            }
        }
        performMathOperations(currentEquation,from, to);
    }

    /**
     * This method performs various math operations on the given equation.
     *
     * @param currentEquation The equation to perform operations on.
     * @param from The starting index for operations.
     * @param to The ending index for operations.
     */
    private void performMathOperations(ArrayList<String> currentEquation, int from, int to){
        // first operation is substitute all unary minuses
        for (int i = from; i < to && i < currentEquation.size() - 1; i++) {
            if (getIndexCloseBracket(currentEquation, from) <= i && getIndexCloseBracket(currentEquation, from) != -1) {
                continue;
            }
            if (currentEquation.get(i).equals(Operations.MULTIPLY_TO_UNARY_MINUS.getAction())) {
                double result = numberAction(i, Operations.MULTIPLY_TO_UNARY_MINUS.getAction(), currentEquation);

                currentEquation.remove(i + 1);
                currentEquation.remove(i);
                currentEquation.set(i - 1, String.valueOf(result));

                to -= 2;
            }
        }
        //second operation is power and this operation differ with other operation because here
        //because here the calculation goes from right to left
        for (int i = (to - 1 < currentEquation.size())?to-1:currentEquation.size()-1; i >= from; i--) {
            if (getIndexCloseBracket(currentEquation, from) <= i && getIndexCloseBracket(currentEquation, from) != -1) {
                continue;
            }
            if (currentEquation.get(i).equals(Operations.POWER.getAction())) {
                double result = numberAction(i, Operations.POWER.getAction(), currentEquation);

                currentEquation.remove(i + 1);
                currentEquation.remove(i);
                currentEquation.set(i - 1, String.valueOf(result));

                to -= 2;
            }
        }
        //and simple left ot right calculation
        Operations[] operations = Operations.values();
        for (int j = 2; j < operations.length; j++) {
            for (int i = from; i < to && i < currentEquation.size() - 1; i++) {
                if (getIndexCloseBracket(currentEquation, from) <= i && getIndexCloseBracket(currentEquation, from) != -1) {
                    continue;
                }
                if (currentEquation.get(i).equals(operations[j].getAction())) {
                    double result = numberAction(i, operations[j].getAction(), currentEquation);

                    currentEquation.remove(i + 1);
                    currentEquation.remove(i);
                    currentEquation.set(i - 1, String.valueOf(result));

                    to -= 2;
                    i -= 2;
                }
            }
        }
    }

    /**
     * Processes open brackets and their corresponding closed brackets within the equation range.
     *
     * @param currentEquation The equation components.
     * @param from            The starting index for processing.
     * @param to              The ending index for processing.
     */
    private void openBrackets(ArrayList<String> currentEquation, int from, int to) {
        for (int i = from; i < to - 1 && i < currentEquation.size() - 1; i++) {
            if (currentEquation.get(i).equals(OPEN_BRACKET)) {
                int indexClosedBracket = getIndexCloseBracket(currentEquation, i);

                mainCalculate(currentEquation, i + 1, indexClosedBracket - 1);

                currentEquation.remove(i + 2);
                currentEquation.remove(i);
            }
        }
    }

    /**
     * Finds the index of the corresponding closing bracket within the equation range.
     *
     * @param currentEquation The equation components.
     * @param from            The starting index for the search.
     * @return The index of the closing bracket.
     * @throws InputMismatchException If no corresponding closing bracket is found.
     */
    private int getIndexCloseBracket(ArrayList<String> currentEquation, int from) {
        int numberOfBracketsInside = 0;
        for (int i = from + 1; i < currentEquation.size(); i++) {
            if (currentEquation.get(i).equals(OPEN_BRACKET)) {
                numberOfBracketsInside++;
                continue;
            }
            if (currentEquation.get(i).equals(CLOSE_BRACKET)) {
                if (numberOfBracketsInside > 0) {
                    numberOfBracketsInside--;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Performs arithmetic operations on numerical components within the equation.
     *
     * @param index           The index of the operation component.
     * @param operation       The operation to be performed.
     * @param currentEquation The equation components.
     * @return The result of the arithmetic operation.
     */
    public Double numberAction(int index, String operation, ArrayList<String> currentEquation) {
        double a = Double.parseDouble(currentEquation.get(index - 1));
        double b = Double.parseDouble(currentEquation.get(index + 1));

        for (Operations tempOperation : Operations.values()) {
            if (tempOperation.getAction().equals(operation)) {
                return tempOperation.calculate(a, b);
            }
        }
        return null;
    }

    /**
     * Performs trigonometric function operations on components within the equation.
     *
     * @param index           The index of the function component.
     * @param currentEquation The equation components.
     * @return The result of the trigonometric function.
     */
    public Double functionAction(int index, ArrayList<String> currentEquation) {
        for (Functions function : Functions.values()) {
            if (currentEquation.get(index).equals(function.name().toLowerCase())) {
                double value = Double.parseDouble(currentEquation.get(index + 1));
                return function.substituteValueIntoFunction(value);
            }
        }
        throw new RuntimeException("unknown value");
    }

    // Helper method to substitute variable values into the expression.
    private void substitutingVariablesIntoExpression(ArrayList<String> currentEquation,
                                                     HashMap<String, Double> variables) {
        for (int i = 0; i < currentEquation.size(); i++) {
            if (variables.containsKey(currentEquation.get(i))) {
                currentEquation.set(i, String.valueOf(variables.get(currentEquation.get(i))));
            } else {
                String variableWithoutBinaryMinus = currentEquation.get(i).replaceFirst(Operations.SUBTRACT.getAction(),
                        "");
                if (variables.containsKey(variableWithoutBinaryMinus)) {
                    currentEquation.set(i, String.valueOf(-1 * variables.get(variableWithoutBinaryMinus)));
                }
            }
        }
    }

    /**
     * Checks for the presence of unknown variables in the equation.
     *
     * @param currentEquation The equation components.
     * @return True if unknown variables are found, otherwise false.
     */
    private boolean hasUnknownVariables(ArrayList<String> currentEquation) {
        String param = "[a-zA-Z]+";
        Pattern pattern = Pattern.compile(param);

        for (String element : currentEquation) {
            Matcher matcher = pattern.matcher(element);

            if (matcher.find()) {
                if (!isFunction(matcher.group())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checking the value for a function
     * @param value String element from equation
     * @return ture - this is function, false - this is not function
     */
    private boolean isFunction(String value) {
        for (Functions functions : Functions.values()) {
            if (value.equals(functions.name().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
