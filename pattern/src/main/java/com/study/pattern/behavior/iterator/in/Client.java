package com.study.pattern.behavior.iterator.in;

/**
 * @author swzhang
 * @date 2019/2/26
 * @description
 */
public class Client {
    public static void main(String[] args) {
        BiaoBeiVoiceToText biaoBeiVoiceToText = new BiaoBeiVoiceToText();
        biaoBeiVoiceToText.setVoiceFile("D:\\voice\\");
        String text = "您好，没有听清楚您讲的话，我是南京鼓楼医院回访中心的，来做一下回访，您看方便么？";
        biaoBeiVoiceToText.biaoBeiTTs(text);

    }
}
