package com.crawl;


import static com.mezhou887.proxy.ProxyPool.proxyQueue;

import com.mezhou887.proxy.entity.Proxy;

public class DelayQueueTest {
    public static void main(String[] args){
        Proxy proxy = new Proxy("123", 123, 0);
        proxy.setSuccessfulTotalTime(1000);
        proxy.setSuccessfulTimes(3);
        Proxy proxy1 = new Proxy("1234", 123, 1);
        proxy1.setSuccessfulTotalTime(1000);
        proxy1.setSuccessfulTimes(2);
        proxyQueue.add(proxy);
        proxyQueue.add(proxy1);
        if(proxy.getSuccessfulAverageTime() == 0.0d){
            System.out.println(true);
        }
        try {
            Proxy p = proxyQueue.take();
            System.out.println("出队成功:" + p.toString());
            p = proxyQueue.take();
            System.out.println("出队成功:" + p.toString());
            p = proxyQueue.take();
            System.out.println("出队成功:" + p.toString());
        } catch (InterruptedException e) {
        	logger.error(e);
        }
    }
}
