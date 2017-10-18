package com.yangchd.util;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

/**
 * @author yangchd  2017/10/18.
 */
class HttpClientTool {

    /**
     * 根据下载路径返回值获取文件名称
     *
     * @param response
     */
    static String getFileName(HttpResponse response) {
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
     */
    static String getHttpResponseResult(CloseableHttpResponse httpResponse) throws IOException {
        String result;
        HttpEntity entity = httpResponse.getEntity();
        if (null != entity) {
            //处理返回值
            result = EntityUtils.toString(entity);
        } else {
            result = "未找到Http请求的返回值！";
        }
        return result;
    }

    /**
     * 添加请求头方法
     */
    static void addHeader(HttpGet httpGet, Map<String, String> headerMap) {
        if (headerMap != null) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }
    static void addHeader(HttpPost httpPost, Map<String, String> headerMap) {
        if (headerMap != null) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 关闭httpClient
     */
    static void closeHttp(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void closeHttp(CloseableHttpResponse httpResponse) {
        if (httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
