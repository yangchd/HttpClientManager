package com.simpleutil;

import java.io.IOException;

/**
 * Created by yangchd on 2017/9/28.
 * http使用样例
 */
public class Demo {
    public static void main(String[] args) {
        try {
            HttpClientUtil.doGetMethod("https://www.baidu.com",null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
