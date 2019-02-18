/**
 * 策略模式<br/>
 * <p>
 *     定义一系列算法类, 将每个算法封装起来,让它们可以互相替换.<br/>
 *     策略模式让算法独立于使用它们的客户而变化
 * </p>
 * <p>
 *     包含3个角色: 环境类(Context), 抽象策略类(Strategy), 具体策略类(ConcreteStrategy)<br/>
 *     环境类包含一个抽象策略类引用,负责调用策略类方法<br/>
 *     抽象策略类声明策略的公共方法<br/>
 *     具体策略类提供方法实现
 * </p>
 */
package com.study.pattern.behavior.strategy;