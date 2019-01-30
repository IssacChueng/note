package com.study.pattern.struct.decorator;

/**
 * 装饰类,定义装饰模式,指向一个委托类
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public abstract class Decorator implements Person {

    private Person person;

    public Decorator(Person person) {
        this.person = person;
    }

    @Override
    public void eat() {
        this.person.eat();
    }
}
