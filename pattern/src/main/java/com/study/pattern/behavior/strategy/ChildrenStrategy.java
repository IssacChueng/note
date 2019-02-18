package com.study.pattern.behavior.strategy;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class ChildrenStrategy implements PriceCalculator {
    @Override
    public double calculate(double price) {
        System.out.println("儿童票半价, 票价为 " + price * 0.5 + "¥");
        return price * 0.5;
    }
}
