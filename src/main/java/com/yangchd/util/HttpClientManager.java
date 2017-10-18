package com.yangchd.util;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yangchd.util.HttpClientTool.*;

/**
 * @author yangchd  2017-09-28
 * HttpClient工具类
 */
public class HttpClientManager {

    /**
     * 发送GET请求
     * @param url           请求url
     * @param headerMap     请求头设置
     */
    public static String doGetMethod(String url, Map<String, String> headerMap) throws IOException {
        String result;
        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            //创建
            HttpGet httpGet = new HttpGet(url);

            //添加请求头
            addHeader(httpGet, headerMap);

            //执行get请求
            httpResponse = httpClient.execute(httpGet);

            //获取httpClient请求返回值
            result = getHttpResponseResult(httpResponse);
        } finally {
            closeHttp(httpClient);
            closeHttp(httpResponse);
        }
        return result;
    }

    /**
     * 发送POST请求
     * @param url           请求url
     * @param data          请求数据，json格式
     * @param headerMap     请求头
     */
    public static String doPostMethod(String url, String data, Map<String, String> headerMap) throws IOException {
        String result;
        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            //创建post
            HttpPost httpPost = new HttpPost(url);

            //添加请求头
            addHeader(httpPost, headerMap);

            //设置body参数
            StringEntity body = new StringEntity(data);
            body.setContentType("application/json;charset=utf-8");
            httpPost.setEntity(body);

            //执行post请求
            httpResponse = httpClient.execute(httpPost);

            //获取httpClient请求返回值
            result = getHttpResponseResult(httpResponse);
        } finally {
            closeHttp(httpClient);
            closeHttp(httpResponse);
        }
        return result;
    }

    /**
     * 发送POST请求
     * @param url           请求url
     * @param data          请求参数Map<String, Object>格式
     * @param headerMap     请求头
     */
    public static String doPostMethod(String url, Map<String, Object> data, Map<String, String> headerMap) throws IOException {
        String result;
        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            //创建post
            HttpPost httpPost = new HttpPost(url);

            //添加请求头
            addHeader(httpPost, headerMap);

            ////设置body参数
            List<NameValuePair> para = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                para.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(para, "utf-8"));

            //执行post请求
            httpResponse = httpClient.execute(httpPost);

            //获取httpClient请求返回值
            result = getHttpResponseResult(httpResponse);
        } finally {
            closeHttp(httpClient);
            closeHttp(httpResponse);
        }
        return result;
    }

    /**
     * 下载方法
     * @param url           文件下载地址
     * @param savePath      文件下载路径
     */
    public static String getFileByUrl(String url, String savePath) throws IOException {
        String filename;

        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(url);
            //执行get请求
            HttpResponse response = httpClient.execute(get);

            int httpReturn200 = 200;
            if (httpReturn200 != response.getStatusLine().getStatusCode()) {
                return "文件下载错误！error code = 200 ";
            }

            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            if (null == savePath) {
                savePath = HttpClientManager.class.getResource("/").getPath();
            }
            filename = getFileName(response);

            File file = new File(savePath + filename);
            if (!file.exists()) {
                file.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(file);

            //设置缓冲区大小
            byte[] buffer = new byte[1024];
            int ch;
            while ((ch = in.read(buffer)) != -1) {
                out.write(buffer, 0, ch);
            }
            in.close();
            out.flush();
            out.close();
        } finally {
            closeHttp(httpClient);
        }
        return filename;
    }

}
