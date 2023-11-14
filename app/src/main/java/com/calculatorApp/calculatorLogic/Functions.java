package com.calculatorApp.calculatorLogic;

/**
 * Enum with basic math functions and the realization of these functions
 */
public enum Functions {
    SIN{
        @Override
        double substituteValueIntoFunction(double number){
            return Math.sin(number);
        }
    },
    COS{
        @Override
        double substituteValueIntoFunction(double number){
            return Math.cos(number);
        }
    },
    TAN{
        @Override
        double substituteValueIntoFunction(double number){
            return Math.tan(number);
        }
    },
    ATAN{
        @Override
        double substituteValueIntoFunction(double number){
            return Math.atan(number);
        }
    },
    LOG10{
        @Override
        double substituteValueIntoFunction(double number){
            if(number <= 0){
                throw new ArithmeticException("Error: logarithm is defined only for positive real numbers. " +
                        "logarithm argument must be greater than zero");
            }
            return Math.log10(number);
        }
    },
    LOG2{

        @Override
        double substituteValueIntoFunction(double number){
            if(number <= 0){
                throw new ArithmeticException("Error: logarithm is defined only for positive real numbers. " +
                        "logarithm argument must be greater than zero");
            }
            return Math.log10(number)/Math.log10(2);
        }
    },
    SQRT{
        @Override
        double substituteValueIntoFunction(double number){
            if(number < 0){
                throw new ArithmeticException("Error: you cannot determine the square root of a negative number");
            }
            return Math.sqrt(number);
        }
    };
    abstract double substituteValueIntoFunction(double number);
}
