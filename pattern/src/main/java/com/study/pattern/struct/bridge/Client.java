package com.study.pattern.struct.bridge;

/**
 * @author swzhang
 * @date 2019/2/13
 * @description
 */
public class Client {
    public static void main(String[] args) {
        ConcreteImplementA concreteImplementA = new ConcreteImplementA();
        Abstraction abstraction = new RefinesAbstraction(concreteImplementA);
        abstraction.operation();
    }
}
