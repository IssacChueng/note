package com.study.pattern.struct.proxy._dynamic_.jdk;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class Client {
    public static void main(String[] args) {
        Service service = new UserServiceImpl();
        MyInvocationHandler handler = new MyInvocationHandler(service);
        Service serviceProxy = (Service) handler.getProxy();
        serviceProxy.add();
    }
}
