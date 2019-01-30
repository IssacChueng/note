package com.study.pattern.struct.facade;

/**
 * 门面类,对外提供看病方法,处理调用逻辑,使客户端不需要主动调用内部逻辑
 * @author swzhang
 * @date 2019/1/30
 * @description
 */
public class Facade {
    public void goToHospital() {
        System.out.println("去医院看病:");
        new ModuleA().handleA();
        new ModuleB().handleB();
        new ModuleC().handleC();
    }
}
