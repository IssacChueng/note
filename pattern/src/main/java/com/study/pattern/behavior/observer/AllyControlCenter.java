package com.study.pattern.behavior.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description 被观察者
 */
public abstract class AllyControlCenter {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Player> observers = new ArrayList<>();

    public List<Player> getObservers() {
        return observers;
    }

    public void join(Player player) {
        observers.add(player);

        System.out.println(player.name() + "加入战队");
    }

    public void quit(Player player) {
        observers.removeIf(playerInner -> {
            if (playerInner.name().equals(player.name())) {
                return true;
            } else {
                return false;
            }
        });

        System.out.println(player.name() + "退出战队");
    }

    public abstract void notifyPlayer(Player player);
}
