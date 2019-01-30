package com.study.pattern.struct.decorator;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class ManDecoratorA extends Decorator {

    public ManDecoratorA(Person person) {
        super(person);
    }

    @Override
    public void eat() {
        super.eat();
        reEat();
    }

    public void reEat() {
        System.out.println("吃中餐");
    }
}
