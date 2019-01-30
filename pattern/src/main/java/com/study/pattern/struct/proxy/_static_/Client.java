package com.study.pattern.struct.proxy._static_;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class Client {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        Proxy proxy = new Proxy(realSubject);
        proxy.request();
    }
}
