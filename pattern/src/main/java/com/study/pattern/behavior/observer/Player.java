package com.study.pattern.behavior.observer;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description 抽象观察者
 */
public interface Player {

    String name();

    void help(Player player);

    void beAttacked();
}
