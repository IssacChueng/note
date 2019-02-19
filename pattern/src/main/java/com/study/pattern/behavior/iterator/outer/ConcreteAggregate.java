package com.study.pattern.behavior.iterator.outer;

import java.util.Iterator;

/**
 * @author swzhang
 * @date 2019/2/19
 * @description 具体聚集
 */
public class ConcreteAggregate implements Aggregate{

    private Object[] objects = null;

    private int capcity;

    private int size;

    public ConcreteAggregate(int size) {
        objects = new Object[size];
        capcity = size;
        size = 0;
    }

    @Override
    public Iterator createIterator() {
        return new ConcreteIterator(this);
    }

    public int size() {
        return size;
    }

    public Object get(int index) {
        return objects[index];
    }

    public void add(Object o) {
        if (size >= objects.length) {
            return;
        } else {
            objects[size] = o;
            size++;
        }
    }
}
