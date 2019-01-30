package com.study.pattern.struct.decorator;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class Client {
    public static void main(String[] args) {
        Person man = new Man();
        ManDecoratorA manDecoratorA = new ManDecoratorA(man);
        ManDecoratorB manDecoratorB = new ManDecoratorB(man);

        manDecoratorA.eat();
        manDecoratorB.eat();
    }
}
