package com.study.pattern.behavior.observer;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class Client {
    public static void main(String[] args) {
        Player zsw = new ConcretePlayer();
        ((ConcretePlayer) zsw).setName("zsw");
        Player zzz = new ConcretePlayer();
        ((ConcretePlayer) zzz).setName("zzz");
        AllyControlCenter allyControlCenter= new ConcreteAllyControlCenter();
        allyControlCenter.setName("controlCenter");
        allyControlCenter.join(zsw);
        allyControlCenter.join(zzz);
        ((ConcretePlayer) zsw).setAllyControlCenter(allyControlCenter);
        ((ConcretePlayer) zzz).setAllyControlCenter(allyControlCenter);
        zsw.beAttacked();
    }
}
