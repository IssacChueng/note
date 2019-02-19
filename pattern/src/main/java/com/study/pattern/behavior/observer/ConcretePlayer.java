package com.study.pattern.behavior.observer;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description 具体观察者
 */
public class ConcretePlayer implements Player{
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    private AllyControlCenter allyControlCenter;

    public void setAllyControlCenter(AllyControlCenter allyControlCenter) {
        this.allyControlCenter = allyControlCenter;
    }

    public String name() {
        return this.name;
    }

    @Override
    public void help(Player player) {
        System.out.println(player.name() + " 坚持住,马上过来");
    }

    @Override
    public void beAttacked() {
        System.out.println("我是" + this.name + ", 正在被攻击, 请求支援!");
        allyControlCenter.notifyPlayer(this);
    }


}
