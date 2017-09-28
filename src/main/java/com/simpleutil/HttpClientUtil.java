package com.simpleutil;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

/**
 * 用来发送httpclient请求的工具类
 *
 * @author yangchd 2017-09-28
 */
public class HttpClientUtil {

    /**
     * GET
     */
    public static String doGetMethod(String url, Map<String, String> headerMap) throws IOException {
        String result;
        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //创建post
            HttpGet get = new HttpGet(url);

            //添加请求头
            addHeader(get, headerMap);

            //执行get请求
            CloseableHttpResponse httpResponse = httpClient.execute(get);

            //获取httpClient请求返回值
            result = getHttpResponseResult(httpResponse);
        } finally {
            closeHttp(httpClient);
        }
        return result;
    }

    /**
     * POST
     * json格式参数
     */
    public static String doPostMethod(String url, String data, Map<String, String> headerMap) throws IOException {
        String result;
        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
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
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            //获取httpClient请求返回值
            result = getHttpResponseResult(httpResponse);
        } finally {
            closeHttp(httpClient);
        }
        return result;
    }

    /**
     * POST
     * Map格式的参数
     */
    public static String doPostMethod(String url, Map<String, Object> data, Map<String, String> headerMap) throws IOException {
        String result;
        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
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
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            //获取httpClient请求返回值
            result = getHttpResponseResult(httpResponse);
        } finally {
            closeHttp(httpClient);
        }
        return result;
    }


    /**
     * 根据下载地址下载文件
     */
    public static String getFileByUrl(String url, String savePath) throws Exception {
        String filename;

        //创建默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(url);
            //执行get请求
            HttpResponse response = httpClient.execute(get);

            if (200 != response.getStatusLine().getStatusCode()) {
                throw new Exception("文件下载地址错误！error code = " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            if (null == savePath) {
                savePath = HttpClientUtil.class.getResource("/").getPath();
            }
            filename = getFileName(response);

            File file = new File(savePath + filename);
            if (!file.exists()) file.mkdirs();

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

    private static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (null != contentHeader) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (null != param) {
                    try {
                        filename = new String(param.getValue().getBytes("ISO-8859-1"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        filename = param.getValue();
                    }
                }
            }
        }
        return filename;
    }

    /**
     * 打印返回值
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private static String getHttpResponseResult(CloseableHttpResponse httpResponse) throws IOException {
        String result;
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                //处理返回值
                result = EntityUtils.toString(entity);
            } else {
                result = "未找到Http请求的返回值！";
            }
        } finally {
            closeHttp(httpResponse);
        }
        return result;
    }

    /**
     * 添加请求头方法
     */
    private static void addHeader(HttpGet httpGet, Map<String, String> headerMap) {
        if (headerMap != null) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addHeader(HttpPost httpPost, Map<String, String> headerMap) {
        if (headerMap != null) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 关闭httpClient
     *
     * @param httpClient
     * @throws IOException
     */
    private static void closeHttp(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeHttp(CloseableHttpResponse httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
