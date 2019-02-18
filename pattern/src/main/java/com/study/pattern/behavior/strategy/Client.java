package com.study.pattern.behavior.strategy;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class Client {
    public static void main(String[] args) {
        PriceContext priceContext = new PriceContext();
        MovieTicket liulangdiqiu = new MovieTicket();
        liulangdiqiu.setPrice(50);
        priceContext.setMovieTicket(liulangdiqiu);
        PriceCalculator priceCalculator = new StudentStrategy();
        priceContext.setPriceCalculator(priceCalculator);
        priceContext.getPrice();
        priceContext.setPriceCalculator(new ChildrenStrategy());
        priceContext.getPrice();
        priceContext.setPriceCalculator(new VIPStrategy());
        priceContext.getPrice();
    }
}
