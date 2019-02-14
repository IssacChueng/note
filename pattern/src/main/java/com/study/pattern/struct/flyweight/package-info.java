/**
 * 享元模式: 未解决系统中细粒度的相同对象太多而造成资源浪费产生的解决方案, 这个模式整合了单例模式和工厂模式(有点像Spring吧);<br/>
 * 模式的整体结构由享元对象,享元池构成,当系统需要对象时,从享元池中取出,如果没有,则创建新对象返回,并向享元池中添加.<br/>
 * 享元对象可以被系统中多个地方共同使用,关键在于其区分了内部状态和外部状态.<br/>
 *
 * <p>
 *     内部状态: 可以被共享的状态,通常作为成员变量;<br/>
 *     外部状态: 不能被共享,通常由外部注入进享元对象,并且不会设置到成员变量;
 * </p>
 *
 * 模式包含四种对象: 抽象享元类(Flyweight), 具体享元类(ConcreteFlyweight), 具体非共享享元类(UnsharedConcreteFlyweight), 享元工厂类(FlyweightFactory)<br/>
 * <p>
 *     抽象享元类, 定义享元类的公共方法, 对外提供对象内部数据(共享状态),设置外部数据方法(外部状态)
 * </p>
 * <p>
 *     具体享元类, 具体实现享元类, 保存内部状态,提供使用外部状态的实现
 * </p>
 * <p>
 *     非共享具体享元类, 不能被共享,可直接实例化
 * </p>
 * <p>
 *     享元工厂类, 用于创建并管理享元对象, 实现享元池(一般是一个KV键值对)
 * </p>
 */
package com.study.pattern.struct.flyweight;