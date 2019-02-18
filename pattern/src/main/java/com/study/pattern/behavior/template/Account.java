package com.study.pattern.behavior.template;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description 抽象类
 */
public abstract class Account {

    /**
     * 验证步骤
     * @param account
     * @param password
     * @return
     */
    public boolean validate(String account, String password) {
        System.out.printf("账号为%s", account);
        System.out.printf("密码为%s", password);
        return true;
    }

    public abstract void calculateInterest();

    public void display() {
        System.out.println("显示利息");
    }


    /**
     * 模板方法
     */
    public void handle(String account, String password) {
        validate(account, password);
        calculateInterest();
        display();
    }
}
