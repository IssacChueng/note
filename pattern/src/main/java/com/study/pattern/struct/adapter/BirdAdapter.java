package com.study.pattern.struct.adapter;

/**
 * @author swzhang
 * @date 2019/1/21
 * @description
 */
public class BirdAdapter implements Robot {

    private Bird bird;

    public BirdAdapter(Bird bird) {
        this.bird = bird;
    }

    @Override
    public void cry() {
        System.out.println("机器人模拟鸟鸣");
        bird.jiji();
    }

    @Override
    public void move() {
        System.out.println("机器人模拟飞行");
        bird.fly();
    }
}
