package com.study.pattern.struct.proxy._static_;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("request handling");
    }
}
