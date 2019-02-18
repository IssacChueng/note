/**
 * 模板方法模式<br/>
 * <p>
 *     定义一个算法框架,将具体实现延迟到子类<br/>
 *     模板方法使子类可以不改变一个算法的结构即可重新定义该算法的特定步骤
 * </p>
 * <p>
 *     分为两个角色, 抽象类角色(AbstractClass)和具体实现角色(ConcreteClass)<br/>
 *     抽象类角色负责定义步骤,具体实现类决定实现步骤
 * </p>
 */
package com.study.pattern.behavior.template;