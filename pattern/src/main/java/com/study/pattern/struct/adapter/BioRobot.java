package com.study.pattern.struct.adapter;

/**
 * @author swzhang
 * @date 2019/1/21
 * @description 仿生机器人, 实现机器人接口
 */
public class BioRobot implements Robot{

    @Override
    public void cry() {
        System.out.println("仿生机器人叫....");
    }

    @Override
    public void move() {
        System.out.println("仿生机器人慢慢移动....");
    }
}
