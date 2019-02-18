package com.study.pattern.behavior.strategy;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description 作为Context
 */
public class PriceContext {
    private MovieTicket movieTicket;

    private PriceCalculator priceCalculator;


    public void setMovieTicket(MovieTicket movieTicket) {
        this.movieTicket = movieTicket;
    }

    public void setPriceCalculator(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }

    public void getPrice() {
        priceCalculator.calculate(movieTicket.getPrice());
    }
}
