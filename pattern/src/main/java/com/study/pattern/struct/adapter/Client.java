package com.study.pattern.struct.adapter;

/**
 * @author swzhang
 * @date 2019/1/21
 * @description
 */
public class Client {
    public static void main(String[] args) {
        BioRobot bioRobot = new BioRobot();
        Dog dog = new Dog();

        Robot dogRobot = new DogAdapter(dog);
        System.out.println("BioRob cry......");
        bioRobot.cry();

        System.out.println("DogRob cry......");
        dogRobot.cry();

        Bird bird = new Bird();

    }
}
