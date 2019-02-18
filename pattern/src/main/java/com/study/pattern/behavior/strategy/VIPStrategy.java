package com.study.pattern.behavior.strategy;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class VIPStrategy implements PriceCalculator {
    @Override
    public double calculate(double price) {
        System.out.println("VIP8折,票价 " + price * 0.8 + "¥, 获得 " + 100 + "积分");
        return price * 0.8;
    }
}
