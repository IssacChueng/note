package com.study.pattern.creator.prototype;

/**
 * 客户端,作为原型模式中的调用方
 * @author swzhang
 * @date 2019/1/19
 * @description
 */
public class Client {

    public static void main(String[] args) throws CloneNotSupportedException {
        Resume resume = new Resume();
        resume.setBirthiday("2018");
        resume.setCompany("xxx");

        Resume c = (Resume) resume.clone();

        System.out.println("---------------- prototype -----------");
        System.out.println(resume);
        System.out.println("---------------- cloned    -----------");
        System.out.println(c);

        /**
         * 克隆后不是同一个对象
         */
        System.out.println("resume == c ?");
        System.out.println(resume == c);

        System.out.println("resume.class == c.class?");
        System.out.println(resume.getClass() == c.getClass());
    }
}
