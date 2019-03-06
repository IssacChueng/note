package com.study.pattern.behavior.iterator.in;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class BiaoBeiVoiceToText {

    private static String url = "http://39.104.162.93:8027/tts?user_id=speech&domain=1&language=zh&speed=5";
    private static String charset = "utf-8";
    private static String uid = "speech";
    private static String lan = "zh";
    private static String voiceFile;

    public String biaoBeiTTs(String text) {
        byte[] ret = send_post(BiaoBeiVoiceToText.url, BiaoBeiVoiceToText.uid, BiaoBeiVoiceToText.lan, text,
                BiaoBeiVoiceToText.charset);

        File file = new File(voiceFile);
        String voiceAddress = file.getAbsolutePath() + File.separator + UUID.randomUUID().toString().replace("-", "");
        voiceAddress = voiceAddress + "." + "mp3";
        file = new File(voiceAddress);
        try {
            FileUtils.writeByteArrayToFile(file, ret);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return voiceAddress;
    }

    public static byte[] send_post(String url, String uid, String lan, String text, String charsets) {
        byte[] ret = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
            nvpList.add(new BasicNameValuePair("user_id", uid));
            nvpList.add(new BasicNameValuePair("lan", lan));
            nvpList.add(new BasicNameValuePair("text", text));
            nvpList.add(new BasicNameValuePair("domain", "1"));
            nvpList.add(new BasicNameValuePair("language", lan));
            HttpEntity entity = new ByteArrayEntity(
                    CipherUtil.encrypt(EntityUtils.toByteArray(new UrlEncodedFormEntity(nvpList, charset))));
            httpPost.setEntity(entity);
            HttpClient client =  HttpClientBuilder.create().build();
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity response_entity = httpResponse.getEntity();
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                String contentType = httpResponse.getEntity().getContentType().getValue();
                if ("application/json".equals(contentType)) {
                    System.out.println("tts fail");
                }
                if ("audio/mp3".equals(contentType)) {
                    System.out.println("tts success");
                    ret = CipherUtil.decrypt(EntityUtils.toByteArray(response_entity));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void setVoiceFile(String voiceFile) {
        BiaoBeiVoiceToText.voiceFile = voiceFile;
    }
}
