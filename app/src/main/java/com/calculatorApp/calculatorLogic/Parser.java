package com.calculatorApp.calculatorLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for parsing variables into HashMap and formula to elements into ArrayList
 */
public class Parser {

    /**
     * Convert an array of lines into a HashMap representing variables and their values.
     *
     * @param lines Array of lines containing variable assignments in the format "variableName = variableValue".
     * @return HashMap<String, Double> representing variable names as keys and their corresponding values as values.
     */
    public HashMap<String, Double> convertLinesToVariableMap(String[] lines) throws Exception {
        HashMap<String, Double> variables = new HashMap<>();

        for (String line : lines) {
            String variableName;
            double variableValue;

            String[] variableNameAndValue = line.replace(" ", "").split("=");
            if (variableNameAndValue.length == 2) {
                variableName = variableNameAndValue[0];
                variableValue = Double.parseDouble(variableNameAndValue[1]);

                variables.put(variableName, variableValue);
            } else {
                throw new Exception("Error: incorrect variables");
            }
        }
        return variables;
    }

    /**
     * Breaks down a mathematical formula into individual elements such as numbers, variables, and operators.
     *
     * @param formula   The input mathematical formula as a string.
     * @return ArrayList<String> representing the individual elements extracted from the formula.
     */
    public ArrayList<String> formulaToElements(String formula) {
        String param = createParam();

        ArrayList<String> elements = new ArrayList<>();

        Pattern pattern = Pattern.compile(param);
        Matcher matcher = pattern.matcher(formula.replace(" ", ""));


        while (matcher.find()) {
            String tempElement = matcher.group();

            if (tempElement.equals("-") && isOperation(elements.get(elements.size() - 1))) {
                matcher.find();
                String nextElement = matcher.group();

                elements.add("-1");
                elements.add(Operations.MULTIPLY_TO_UNARY_MINUS.getAction());
                elements.add(nextElement);

                continue;
            }

            elements.add(tempElement);
        }
        return elements;
    }

    /**
     * Method that create special param for regex
     * @return String line param
     */
    public String createParam(){
        /*
         * Regular Expression (RegEx) to extract elements from the formula.
         * - \\s* matches any surrounding spaces.
         * - ((?:-)?\\d+(?:\\.\\d+)?|[a-zA-Z]+(?:[a-zA-Z\\d]+)?) extracts numbers (including negative numbers)
         *   and variable names. Numbers can be integers or decimals. Variable names must start with a letter
         *   and can contain alphanumeric characters.
         * - |[+\\-*^\\/]\\s* extracts the arithmetic operators (+, -, *, /, ^) and ignores surrounding spaces.
         *
         * For example, the formula "x + 2 * y - 3" would be split into ["x", "+", "2", "*", "y", "-", "3"].
         */
        String param =  "(\\d+(?:\\.\\d+)?|[a-zA-Z]+(?:[a-zA-Z\\d]+)?)|" +
                "[+\\-*^\\/]|([()])";


        //this part of the code create line like |(func1|func2|func3) and this func I keep from enum Functions
        StringBuilder functionsParam = new StringBuilder();
        functionsParam.append("|");
        functionsParam.append("(");
        for (Functions function: Functions.values()) {
            functionsParam.append(function.name().toLowerCase());
            functionsParam.append("|");
        }
        functionsParam.setCharAt(functionsParam.length()-1,')');

        return param + functionsParam;
    }

    /**
     * Method for check symbols like: '+','-','/','^','*','(' But not ')'
     * @param symbol String symbol
     * @return boolean value
     */
    private boolean isOperation(String symbol) {
        for (Operations operation : Operations.values()) {
            if (symbol.equals(operation.getAction())) {
                return true;
            }
        }
        return symbol.equals('(');//OPEN_BRACKET
    }

}

