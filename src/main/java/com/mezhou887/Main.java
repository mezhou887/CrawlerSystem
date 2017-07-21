package com.mezhou887;

import com.mezhou887.proxy.ProxyHttpClient;
import com.mezhou887.zhihu.ZhiHuHttpClient;

/**
 * 爬虫入口
 */
public class Main {
    public static void main(String args []){
        ProxyHttpClient.getInstance().startCrawl();
//        ZhiHuHttpClient.getInstance().startCrawl();
    }
}
