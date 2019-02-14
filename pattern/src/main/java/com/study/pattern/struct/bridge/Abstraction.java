package com.study.pattern.struct.bridge;

/**
 * @author swzhang
 * @date 2019/2/13
 * @description 抽象角色, 提供对外方法operation
 */
public abstract class Abstraction {
    protected Implementor impl;

    public Abstraction(Implementor impl) {
        this.impl = impl;
    }

    public void operation() {
        impl.operationImpl();
    }
}
