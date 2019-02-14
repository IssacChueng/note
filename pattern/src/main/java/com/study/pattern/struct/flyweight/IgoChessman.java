package com.study.pattern.struct.flyweight;

/**
 * @author swzhang
 * @date 2019/2/14
 * @description 享元对象抽象类
 */
public abstract class IgoChessman {
    public abstract String getColor();

    public void display() {
        System.out.println("棋子颜色" + this.getColor());
    }
}
