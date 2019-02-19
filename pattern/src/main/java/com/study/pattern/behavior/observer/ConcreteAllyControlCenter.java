package com.study.pattern.behavior.observer;

import java.util.List;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class ConcreteAllyControlCenter extends AllyControlCenter {
    @Override
    public void notifyPlayer(Player player) {
        System.out.println(player.name() + " 正在遭受攻击, 全体出击支援");
        List<Player> players = getObservers();
        for (Player partner: players) {
            if (!player.name().equals(partner.name())) {
                partner.help(player);
            }
        }
    }
}
