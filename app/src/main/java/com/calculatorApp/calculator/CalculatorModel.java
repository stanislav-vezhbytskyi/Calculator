package com.calculatorApp.calculator;

import com.calculatorApp.calculatorLogic.Calculator;
import com.calculatorApp.calculatorLogic.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public class CalculatorModel {
    private StringBuilder inputStr = new StringBuilder();
    private HashMap<Integer, Character> symbolMap;
    private Calculator calculator;

    public CalculatorModel() {
        calculator = new Calculator();

        symbolMap = new HashMap<>();
        symbolMap.put(R.id.zero, '0');
        symbolMap.put(R.id.one, '1');
        symbolMap.put(R.id.two, '2');
        symbolMap.put(R.id.three, '3');
        symbolMap.put(R.id.four, '4');
        symbolMap.put(R.id.five, '5');
        symbolMap.put(R.id.six, '6');
        symbolMap.put(R.id.seven, '7');
        symbolMap.put(R.id.eight, '8');
        symbolMap.put(R.id.nine, '9');
        symbolMap.put(R.id.plus, '+');
        symbolMap.put(R.id.minus, '-');
        symbolMap.put(R.id.multiply, '*');
        symbolMap.put(R.id.divide, '/');
        symbolMap.put(R.id.dot, '.');
        symbolMap.put(R.id.open_bracket, '(');
        symbolMap.put(R.id.close_bracket, ')');
    }

    public void onSymbolPressed(int buttonId) {
        inputStr.append(symbolMap.get(buttonId));
    }

    public void onActionPressed(int buttonId) {
        if (buttonId == R.id.btn_clear) {
            inputStr = new StringBuilder();
        } else if (buttonId == R.id.btn_delete) {
            if (inputStr.length() > 0) {
                inputStr.deleteCharAt(inputStr.length() - 1);
            }
        } else if (buttonId == R.id.equals) {
            try {
                Parser parser = new Parser();
                ArrayList<String> formulaByElements = parser.formulaToElements(inputStr.toString());
                
                double result = calculator.calculate(formulaByElements);

                inputStr = new StringBuilder("" + (result == (int) result ? (int) result : result));
            } catch (Exception e) {
                inputStr = new StringBuilder("Invalid value");
            }
        }
    }

    public String getText() {
        return inputStr.toString();
    }

    public HashMap<Integer, Character> getSymbolMap() {
        return symbolMap;
    }
}
