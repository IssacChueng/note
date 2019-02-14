package com.study.pattern.struct.bridge;

/**
 * @author swzhang
 * @date 2019/2/13
 * @description 修正抽象角色
 */
public class RefinesAbstraction extends Abstraction {
    public RefinesAbstraction(Implementor impl) {
        super(impl);
    }

    public void otherOperation() {

    }
}
