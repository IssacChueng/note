package com.study.pattern.struct.decorator;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class ManDecoratorB extends Decorator {
    @Override
    public void eat() {
        super.eat();
        System.out.println("eat finish");
    }

    public ManDecoratorB(Person person) {
        super(person);
    }
}
