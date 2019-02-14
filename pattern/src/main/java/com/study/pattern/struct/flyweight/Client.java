package com.study.pattern.struct.flyweight;

/**
 * @author swzhang
 * @date 2019/2/14
 * @description
 */
public class Client {
    public static void main(String[] args) {
        IgoChessman black1 = IgoChessmanFactory.getIgoChessman("b");
        IgoChessman black2 = IgoChessmanFactory.getIgoChessman("b");
        IgoChessman black3 = IgoChessmanFactory.getIgoChessman("b");

        //判断两颗棋子是否相同
        System.out.println("两颗棋子是否相同: " + (black1 == black2));

        IgoChessman white1 = IgoChessmanFactory.getIgoChessman("w");
        IgoChessman white2 = IgoChessmanFactory.getIgoChessman("w");

        black1.display();
        black2.display();
        white1.display();
        white2.display();
        black3.display();
    }
}
