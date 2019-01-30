package com.study.pattern.struct.proxy._static_;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class Proxy implements Subject {

    public Proxy(Subject subject) {
        this.subject = subject;
    }

    private Subject subject;

    @Override
    public void request() {
        System.out.println("accept");
        this.subject.request();
        System.out.println("response");
    }
}
