package com.study.pattern.struct.bridge;

/**
 * @author swzhang
 * @date 2019/2/13
 * @description 具体实现化角色
 */
public class ConcreteImplementA implements Implementor {
    @Override
    public void operationImpl() {
        System.out.println("具体操作A");
    }
}
