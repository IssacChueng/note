package com.study.pattern.behavior.template;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class CurrentAccount extends Account {
    @Override
    public void calculateInterest() {
        System.out.println("计算活期账户");
    }
}
