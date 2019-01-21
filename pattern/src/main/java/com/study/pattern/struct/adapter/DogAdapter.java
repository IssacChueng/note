package com.study.pattern.struct.adapter;

/**
 * @author swzhang
 * @date 2019/1/21
 * @description 适配器
 */
public class DogAdapter implements Robot {

    public DogAdapter(Dog dog) {
        this.dog = dog;
    }

    Dog dog;

    @Override
    public void cry() {
        System.out.println("机器人模拟狗叫......");
        dog.wang();
    }

    @Override
    public void move() {
        System.out.println("机器人模拟狗跑......");
        dog.run();
    }
}
