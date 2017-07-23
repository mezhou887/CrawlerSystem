package com.mezhou887.zhihu;

import com.mezhou887.zhihu.task.DetailListPageTask;
import com.mezhou887.zhihu.task.DetailPageTask;
import com.mezhou887.zhihu.task.GeneralPageTask;
import com.mezhou887.crawler.httpclient.AbstractHttpClient;
import com.mezhou887.crawler.httpclient.IHttpClient;
import com.mezhou887.crawler.util.Config;
import com.mezhou887.crawler.util.SimpleThreadPoolExecutor;
import com.mezhou887.crawler.util.ThreadPoolMonitor;
import com.mezhou887.crawler.util.ZhiHuConstants;
import com.mezhou887.proxy.ProxyHttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZhiHuHttpClient extends AbstractHttpClient implements IHttpClient {
	
    private static Logger logger = Logger.getLogger(ZhiHuHttpClient.class);
    // 统计用户数量
    public static AtomicInteger parseUserCount = new AtomicInteger(0);
    private static long startTime = System.currentTimeMillis();
    public static volatile boolean isStop = false;

    // request　header 获取列表页时，必须带上
    private static String authorization;    
    
    private static volatile ZhiHuHttpClient instance;
    public static ZhiHuHttpClient getInstance(){
        if (instance == null){
            synchronized (ZhiHuHttpClient.class){
                if (instance == null){
                    instance = new ZhiHuHttpClient();
                }
            }
        }
        return instance;
    }
    
    // 详情页下载线程池
    private ThreadPoolExecutor detailPageThreadPool;
    // 列表页下载线程池
    private ThreadPoolExecutor listPageThreadPool;
    // 详情列表页下载线程池
    private ThreadPoolExecutor detailListPageThreadPool;


    private ZhiHuHttpClient() {
        intiThreadPool();
    }
    
    /**
     * 初始化线程池
     */
    private void intiThreadPool(){
        detailPageThreadPool = new SimpleThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), "detailPageThreadPool");
        listPageThreadPool = new SimpleThreadPoolExecutor(50, 80, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(5000), new ThreadPoolExecutor.DiscardPolicy(), "listPageThreadPool");
        new Thread(new ThreadPoolMonitor(detailPageThreadPool, "DetailPageDownloadThreadPool")).start();
        new Thread(new ThreadPoolMonitor(listPageThreadPool, "ListPageDownloadThreadPool")).start();
        
        detailListPageThreadPool = new SimpleThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(2000), new ThreadPoolExecutor.DiscardPolicy(), "detailListPageThreadPool");
        new Thread(new ThreadPoolMonitor(detailListPageThreadPool, "DetailListPageThreadPool")).start();

    }
    
    public void startCrawl(String url){
        detailPageThreadPool.execute(new DetailPageTask(url, Config.isProxy));
        manageHttpClient();
    }

    @Override
    public void startCrawl() {
        authorization = initAuthorization();

        String startToken = Config.startUserToken;
        String startUrl = String.format(ZhiHuConstants.USER_FOLLOWEES_URL, startToken, 0);
        HttpGet request = new HttpGet(startUrl);
        request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
        detailListPageThreadPool.execute(new DetailListPageTask(request, Config.isProxy));
        manageHttpClient();
    }

    /**
     * 初始化authorization
     * @return
     */
    private String initAuthorization(){
        logger.info("初始化authoriztion中...");
        String content = null;

//            content = HttpClientUtil.getWebPage(Config.startURL);
        GeneralPageTask generalPageTask = new GeneralPageTask(Config.startURL, true);
        generalPageTask.run();
        content = generalPageTask.getPage().getHtml();

        Pattern pattern = Pattern.compile("https://static\\.zhihu\\.com/heifetz/main\\.app\\.([0-9]|[a-z])*\\.js");
        Matcher matcher = pattern.matcher(content);
        String jsSrc = null;
        if (matcher.find()){
            jsSrc = matcher.group(0);
        } else {
            throw new RuntimeException("not find javascript url");
        }
        String jsContent = null;
//        try {
//            jsContent = HttpClientUtil.getWebPage(jsSrc);
//        } catch (IOException e) {
//            logger.error(e);
//        }
        GeneralPageTask jsPageTask = new GeneralPageTask(jsSrc, true);
        jsPageTask.run();
        jsContent = jsPageTask.getPage().getHtml();

        pattern = Pattern.compile("CLIENT_ALIAS=\"(([0-9]|[a-z])*)\"");
        matcher = pattern.matcher(jsContent);
        if (matcher.find()){
            String authorization = matcher.group(1);
            logger.info("初始化authoriztion完成");
            return authorization;
        }
        throw new RuntimeException("not get authorization");
    }
    public static String getAuthorization(){
        return authorization;
    }
    /**
     * 管理知乎客户端
     * 关闭整个爬虫
     */
    public void manageHttpClient(){
        while (true) {
            /**
             * 下载网页数
             */
            long downloadPageCount = detailListPageThreadPool.getTaskCount();

            if (downloadPageCount >= Config.downloadPageCount &&
                    !detailListPageThreadPool.isShutdown()) {
                isStop = true;
                ThreadPoolMonitor.isStopMonitor = true;
                detailListPageThreadPool.shutdown();
            }
            if(detailListPageThreadPool.isTerminated()){
                //关闭数据库连接
                Map<Thread, Connection> map = DetailListPageTask.getConnectionMap();
                for(Connection cn : map.values()){
                    try {
                        if (cn != null && !cn.isClosed()){
                            cn.close();
                        }
                    } catch (SQLException e) {
                    	logger.error(e);
                    }
                }
                //关闭代理检测线程池
                ProxyHttpClient.getInstance().getProxyTestThreadExecutor().shutdownNow();
                //关闭代理下载页线程池
                ProxyHttpClient.getInstance().getProxyDownloadThreadExecutor().shutdownNow();

                break;
            }
            double costTime = (System.currentTimeMillis() - startTime) / 1000.0;//单位s
            logger.debug("抓取速率：" + parseUserCount.get() / costTime + "个/s");
//            logger.info("downloadFailureProxyPageSet size:" + ProxyHttpClient.downloadFailureProxyPageSet.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            	logger.error(e);
            }
        }
    }
    public ThreadPoolExecutor getDetailPageThreadPool() {
        return detailPageThreadPool;
    }

    public ThreadPoolExecutor getListPageThreadPool() {
        return listPageThreadPool;
    }
    public ThreadPoolExecutor getDetailListPageThreadPool() {
        return detailListPageThreadPool;
    }

}
