package com.study.https.ssl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * @author swzhang
 * @date 2019/2/16
 * @description
 */
public class HttpsMain {
    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("https://steamdb.info/app");
        try {
            getMethod.addRequestHeader("Host","steamdb.info");
            getMethod.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
            getMethod.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            getMethod.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            getMethod.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
            getMethod.addRequestHeader("Referer", "https://steamdb.info/");
            getMethod.addRequestHeader("DNT", "1");
            getMethod.addRequestHeader("Connection", "keep-alive");
            getMethod.addRequestHeader("Cookie", "__cfduid=d82ce581e64ddbbf20cd8922767c46be01550311951; cf_clearance=1bfa29d00b0e8205022075e51469632b243bd293-1550311967-86400-150");
            getMethod.addRequestHeader("Upgrade-Insecure-Requests", "1");
            getMethod.addRequestHeader("Cache-Control", "max-age=0");
            getMethod.addRequestHeader("TE", "Trailers");
            getMethod.addRequestHeader("Content-Charset", "UTF-8");
            httpClient.executeMethod(getMethod);
            File file = new File("C:\\Users\\vivian\\result.html");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
            }
            InputStream response = getMethod.getResponseBodyAsStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byte[] data = new byte[1024];
            FileChannel channel = fileOutputStream.getChannel();
            while (response.read(data) != -1) {
                byteBuffer.put(data);
                byteBuffer.flip();
                channel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            getMethod.releaseConnection();
        }
    }
}
