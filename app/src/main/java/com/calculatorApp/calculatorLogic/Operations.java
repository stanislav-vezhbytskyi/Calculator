package com.calculatorApp.calculatorLogic;

/**
 * Enum with basic math operations and the realization of these operations
 */
public enum Operations {
    //special symbol multiply for unary minus(priority 1)
    MULTIPLY_TO_UNARY_MINUS("&"){
        @Override
        double calculate(double a, double b) {
            return a * b;
        }
    },
    POWER("^") {
        @Override
        double calculate(double a, double b) {
            return Math.pow(a, b);
        }
    }, DIVIDE("/") {
        @Override
        double calculate(double a, double b) {
            if(b == 0){
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        }
    }, MULTIPLY("*") {
        @Override
        double calculate(double a, double b) {
            return a * b;
        }
    },
    SUBTRACT("-") {
        @Override
        double calculate(double a, double b) {
            return a - b;
        }
    },
    ADD("+") {
        @Override
        double calculate(double a, double b) {
            return a + b;
        }
    };

    abstract double calculate(double a, double b);

    //String value like "+","-","*" etc.
    private String action;

    Operations(String action) {
        this.action = action;
    }
    String getAction(){
        return this.action;
    }
}
