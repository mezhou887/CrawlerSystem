package com.mezhou887.proxy.task;

import com.mezhou887.proxy.ProxyPool;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.util.HttpClientUtil;
import com.mezhou887.util.ZhiHuConstants;
import com.mezhou887.zhihu.entity.Page;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 代理检测任务, 通过访问知乎首页，能否正确响应, 如果可用，则将可用代理添加到DelayQueue延时队列中
 */
public class ProxyTestTask implements Runnable {
    private final static Logger logger = Logger.getLogger(ProxyTestTask.class);
    private Proxy proxy;
    
    public ProxyTestTask(Proxy proxy) {
        this.proxy = proxy;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        HttpGet request = new HttpGet(ZhiHuConstants.INDEX_URL);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(ZhiHuConstants.TIMEOUT).
                    setConnectTimeout(ZhiHuConstants.TIMEOUT).
                    setConnectionRequestTimeout(ZhiHuConstants.TIMEOUT).
                    setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).
                    setCookieSpec(CookieSpecs.STANDARD).
                    build();
            request.setConfig(requestConfig);
            Page page = HttpClientUtil.getPage(request);
            long endTime = System.currentTimeMillis();
            String logStr = Thread.currentThread().getName() + " " + proxy.getProxyStr() + "  executing request " + page.getUrl()  + " response statusCode:" + page.getStatusCode() + "  request cost time:" + (endTime - startTime) + "ms";
            if (page == null || page.getStatusCode() != HttpStatus.SC_OK){
                logger.warn(logStr);
                return;
            }
            request.releaseConnection();
            logger.debug(proxy.toString() + "---------" + page.toString());
            logger.debug(proxy.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
            ProxyPool.proxyQueue.add(proxy);
        } catch (IOException e) {
            logger.debug("IOException:", e);
        } finally {
            if (request != null){
                request.releaseConnection();
            }
        }
    }
}
