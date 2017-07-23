package com.mezhou887.proxy;

import com.mezhou887.crawler.httpclient.AbstractHttpClient;
import com.mezhou887.crawler.httpclient.IHttpClient;
import com.mezhou887.crawler.util.Config;
import com.mezhou887.crawler.util.HttpClientUtil;
import com.mezhou887.crawler.util.SimpleThreadPoolExecutor;
import com.mezhou887.crawler.util.ThreadPoolMonitor;
import com.mezhou887.crawler.util.ZhiHuConstants;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.proxy.task.ProxyPageTask;
import com.mezhou887.proxy.task.ProxySerializeTask;

import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProxyHttpClient extends AbstractHttpClient implements IHttpClient{
    private static final Logger logger = Logger.getLogger(ProxyHttpClient.class);

    // 单例模式
    // 定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile ProxyHttpClient instance;
    public static ProxyHttpClient getInstance(){
        if (instance == null){
            synchronized (ProxyHttpClient.class){
                if (instance == null){
                    instance = new ProxyHttpClient();
                }
            }
        }
        return instance;
    }
    
    // 代理测试线程池
    private ThreadPoolExecutor proxyTestThreadExecutor;
    
    // 代理网站下载线程池
    private ThreadPoolExecutor proxyDownloadThreadExecutor;
    
    public ProxyHttpClient(){
        initThreadPool();
        initProxy();
    }
    
    // 初始化线程池
    private void initThreadPool(){
        proxyTestThreadExecutor = new SimpleThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy(), "proxyTestThreadExecutor");
        proxyDownloadThreadExecutor = new SimpleThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), "proxyDownloadThreadExecutor");

        new Thread(new ThreadPoolMonitor(proxyTestThreadExecutor, "ProxyTestThreadPool")).start();
        new Thread(new ThreadPoolMonitor(proxyDownloadThreadExecutor, "ProxyDownloadThreadExecutor")).start();
    }

    // 初始化proxy
    private void initProxy(){
        try {
        	Proxy[] proxyArray = (Proxy[]) HttpClientUtil.deserializeObject(Config.proxyPath);
            int usableProxyCount = 0;
            for (Proxy p : proxyArray){
                if (p == null){
                    continue;
                }
                p.setTimeInterval(ZhiHuConstants.TIME_INTERVAL);
                p.setFailureTimes(0);
                p.setSuccessfulTimes(0);
                long nowTime = System.currentTimeMillis();

                //上次成功离现在少于一小时
                if (nowTime - p.getLastSuccessfulTime() < 1000 * 60 *60){
                    ProxyPool.proxyQueue.add(p);
                    ProxyPool.proxySet.add(p);
                    usableProxyCount++;
                }
            }
            logger.info("反序列化proxy成功，" + proxyArray.length + "个代理,可用代理" + usableProxyCount + "个");
        } catch (Exception e) {
            logger.error("反序列化proxy失败",e);
        }
    }

    // 抓取代理
    public void startCrawl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (String url : ProxyPool.proxyMap.keySet()){
                        // 首次本机直接下载代理页面
                        proxyDownloadThreadExecutor.execute(new ProxyPageTask(url, false));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        	logger.error(e);
                        }
                    }
                    try {
                        Thread.sleep(1000 * 60 * 60);
                    } catch (InterruptedException e) {
                    	logger.error(e);
                    }
                }
            }
        }).start();
        
        new Thread(new ProxySerializeTask()).start();
    }
    public ThreadPoolExecutor getProxyTestThreadExecutor() {
        return proxyTestThreadExecutor;
    }

    public ThreadPoolExecutor getProxyDownloadThreadExecutor() {
        return proxyDownloadThreadExecutor;
    }

    @Override
	public void startCrawl(String url) {
		
	}
}