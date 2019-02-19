package com.study.pattern.behavior.iterator.outer;

import java.util.Iterator;

/**
 * @author swzhang
 * @date 2019/2/19
 * @description
 */
public class ConcreteIterator implements Iterator {

    private ConcreteAggregate aggregate;

    private int index;

    private int size;

    public ConcreteIterator(ConcreteAggregate aggregate) {
        this.aggregate = aggregate;
        index = -1;
        size = aggregate.size();
    }

    @Override
    public boolean hasNext() {
        return ((size - index) > 1);
    }

    @Override
    public Object next() {
        if (hasNext()) {
            return aggregate.get(++index);
        } else {
            return null;
        }
    }
}
