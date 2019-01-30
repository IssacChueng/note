package com.study.pattern.struct.proxy._dynamic_.jdk;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class UserServiceImpl implements Service {
    @Override
    public void add() {
        System.out.println("add user");
    }
}
