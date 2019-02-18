package com.study.pattern.behavior.strategy;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description ConcreteStrategy
 */
public class StudentStrategy implements PriceCalculator {
    @Override
    public double calculate(double price) {
        System.out.println("学生票8折, 票价为" + price * 0.8 + "¥");
        return price * 0.8;
    }
}
