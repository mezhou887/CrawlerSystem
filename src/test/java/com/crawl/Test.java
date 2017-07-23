package com.crawl;

import org.apache.log4j.Logger;

import com.mezhou887.crawler.util.HttpClientUtil;
import com.mezhou887.proxy.ProxyPool;
import com.mezhou887.proxy.entity.Proxy;

public class Test {
	private static Logger logger = Logger.getLogger(Test.class);
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
