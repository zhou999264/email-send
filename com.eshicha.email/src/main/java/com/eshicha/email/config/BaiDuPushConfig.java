package com.eshicha.email.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Configuration
public class BaiDuPushConfig {

    public static String url = "http://data.zz.baidu.com/urls?site=https://www.eshicha.cn&token=fjuaNtcf0z3wYUNZ";
    public static String docurl = "http://data.zz.baidu.com/urls?site=https://doc.eshicha.cn&token=fjuaNtcf0z3wYUNZ";


    public static String Post(String PostUrl, String[] Parameters) {
        if (null == PostUrl || null == Parameters || Parameters.length == 0) {
            return null;
        }
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            //建立URL之间的连接
            URLConnection conn = new URL(PostUrl).openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("Host", "data.zz.baidu.com");
            conn.setRequestProperty("User-Agent", "curl/7.12.1");
            conn.setRequestProperty("Content-Length", "83");
            conn.setRequestProperty("Content-Type", "text/plain");

            //发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //获取conn对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            String param = "";
            for (String s : Parameters) {
                param += s + "\n";
            }
            out.print(param.trim());
            //进行输出流的缓冲
            out.flush();
            //通过BufferedReader输入流来读取Url的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送post请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args){
        String[] param = {
                "https://www.eshicha.cn",
                "https://www.eshicha.cn/blog-abd.html",
                "https://www.eshicha.cn/blog-jltl.html",
                "https://www.eshicha.cn/blog-txzyfd.html",
                "https://www.eshicha.cn/blog-single.html",
                "https://www.eshicha.cn/blog-hhyx.html"
        };
        String[] docParam = {
                "https://doc.eshicha.cn",
        };

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 500; i++) {
//        for (int i = 0; i < 3000; i++) {
            final int index = i;
            fixedThreadPool.execute(() -> {
                String json = Post(url, param);//执行推送方法
                System.out.println(index+"结果是" + json);  //打印推送结果

//                String jsons = Post(docurl, docParam);//执行推送方法
//                System.out.println(index+"结果是" + jsons);  //打印推送结果

            });
        }
    }
}