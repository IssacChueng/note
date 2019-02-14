package com.study.pattern.struct.bridge;

/**
 * @author swzhang
 * @date 2019/2/13
 * @description
 */
public class ConcreteImplementB implements Implementor {
    @Override
    public void operationImpl() {
        System.out.println("具体实现B");
    }
}
