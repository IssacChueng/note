package com.study.pattern.struct.proxy._dynamic_.cglib;

/**
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class Client {
    public static void main(String[] args) {
        BookFacadeImpl bookFacade = new BookFacadeImpl();
        BookFacadeCglib cglib = new BookFacadeCglib();
        BookFacadeImpl instance = (BookFacadeImpl) cglib.getInstance(bookFacade);
        instance.addBook();

    }
}
