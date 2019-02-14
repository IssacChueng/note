package com.study.pattern.struct.flyweight;

import java.util.Hashtable;

/**
 * @author swzhang
 * @date 2019/2/14
 * @description
 */
public class IgoChessmanFactory {

    private static IgoChessmanFactory instance = new IgoChessmanFactory();
    private static Hashtable<String, IgoChessman> hashtable;

    private IgoChessmanFactory() {
        hashtable = new Hashtable<>();
        IgoChessman black, white;

        black = new BlackIgoChessman();

        white = new WhiteIgoChessman();

        hashtable.put("w", white);
        hashtable.put("b", black);
    }

    public static IgoChessmanFactory getInstance() {
        return instance;
    }

    public static IgoChessman getIgoChessman(String color) {
        return hashtable.get(color);
    }
}
