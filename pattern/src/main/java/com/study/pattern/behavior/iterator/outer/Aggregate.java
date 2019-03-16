package com.study.pattern.behavior.iterator.outer;

import java.util.Iterator;

/**
 * @author swzhang
 * @date 2019/2/19
 * @description 聚集接口, 宽接口
 */
public interface Aggregate<T> {
    Iterator<T> createIterator();
}
