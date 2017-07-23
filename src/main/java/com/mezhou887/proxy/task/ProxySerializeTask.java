package com.mezhou887.proxy.task;

import com.mezhou887.proxy.ProxyPool;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.util.Config;
import com.mezhou887.util.HttpClientUtil;
import com.mezhou887.util.ProxyUtil;
import com.mezhou887.zhihu.ZhiHuHttpClient;

import org.apache.log4j.Logger;

/**
 * 代理序列化
 */
public class ProxySerializeTask implements Runnable{
    private static Logger logger = Logger.getLogger(ProxySerializeTask.class);
    @Override
    public void run() {
        while (!ZhiHuHttpClient.isStop){ // 需重构 
            try {
                Thread.sleep(1000 * 60 * 1);
            } catch (InterruptedException e) {
            	logger.error(e);
            }
            Proxy[] proxyArray = null;
            ProxyPool.lock.readLock().lock();
            try {
                proxyArray = new Proxy[ProxyPool.proxySet.size()];
                int i = 0;
                for (Proxy p : ProxyPool.proxySet){
                    if (!ProxyUtil.isDiscardProxy(p)){
                        proxyArray[i++] = p;
                    }
                }
            } finally {
                ProxyPool.lock.readLock().unlock();
            }

            HttpClientUtil.serializeObject(proxyArray, Config.proxyPath);
            logger.info("成功序列化" + proxyArray.length + "个代理");
        }
    }
}
