package com.study.pattern.behavior.iterator.outer;

import java.util.Iterator;

/**
 * @author swzhang
 * @date 2019/2/19
 * @description
 */
public class Client {
    public static void main(String[] args) {
        ConcreteAggregate<Integer> aggregate = new ConcreteAggregate(10, Integer.class);
        for (int i = 0; i < 5; i++) {
            aggregate.add(1);
        }
        Iterator iterator = aggregate.createIterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
