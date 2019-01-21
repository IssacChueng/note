/**
 * 原型设计模式
 * 用原型实例指定创建对象的种类,并通过复制这些原型获取新对象
 * 注意: 区分浅拷贝和深拷贝
 * @see <a href="https://www.cnblogs.com/chenssy/p/3313339.html">原型设计模式</a>
 * <br/>
 * <bold>优点:</bold>
 * <br/>
 * &nbsp;&nbsp;1.如果创建的对象比较复杂,用原型模式可以简化对象的创建过程, 同时也可以提高效率(这里应该是对客户端来说,是需要调用一下clone方法,不需要知道怎么去克隆)
 * <br/>
 * &nbsp;&nbsp;2.可以使用深克隆保持对象的状态(个人理解就是克隆后的对象跟原型对象没有关联,修改对象中的引用类型不会影响到其他对象)
 * <br/>
 * &nbsp;&nbsp;3.提供了简化的创建结构(只要实现{@linkplain java.lang.Object#clone() 方法})
 * <br/><br/>
 * <bold>缺点</bold>
 * <br/>
 * &nbsp;&nbsp;1.在实现深克隆的时候可能需要比较复杂的代码(这个一般都会有复杂代码吧,而且有复杂代码的情况下有没有更好的方法)
 * <br/>
 * &nbsp;&nbsp;2.当已有的类修改了之后,需要对${@linkplain java.lang.Object#clone()}方法进行修改,违背开闭原则 (如果原有的类已经改掉了,还有什么不可违背的)
 */
package com.study.pattern.creator.prototype;