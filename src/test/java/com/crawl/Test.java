package com.crawl;

import org.apache.http.client.methods.HttpGet;

import com.mezhou887.proxy.ProxyPool;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.util.HttpClientUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static int j = 0;
    public static void main(String[] args){
        Proxy proxy = new Proxy("123456",8080, 1000);
        ProxyPool.proxyQueue.add(proxy);
        Proxy[] proxyArray = ProxyPool.proxyQueue.toArray(new Proxy[0]);
        System.out.println(proxyArray.length);
        HttpClientUtil.serializeObject(proxyArray, "proxies");
        try {
            proxyArray = (Proxy[]) HttpClientUtil.deserializeObject("proxies");
            System.out.println(proxyArray.length);
        } catch (Exception e) {
        	logger.error(e);
        }
    }
}
