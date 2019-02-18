package com.study.pattern.behavior.template;

/**
 * @author swzhang
 * @date 2019/2/18
 * @description
 */
public class Client {
    public static void main(String[] args) {
        Account currentAccount = new CurrentAccount();
        currentAccount.handle("ze", "pa");
        currentAccount = new SavingAccount();
        currentAccount.handle("ze", "pa");
    }
}
