package com.mezhou887.zhihu.task;


import com.mezhou887.crawler.parse.ListPageParser;
import com.mezhou887.crawler.util.Config;
import com.mezhou887.crawler.util.Md5Util;
import com.mezhou887.crawler.util.SimpleInvocationHandler;
import com.mezhou887.crawler.util.ZhiHuConstants;
import com.mezhou887.remove.ConnectionManager;
import com.mezhou887.zhihu.ZhiHuHttpClient;
import com.mezhou887.zhihu.entity.Page;
import com.mezhou887.zhihu.entity.User;
import com.mezhou887.zhihu.parse.ZhiHuUserListPageParser;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mezhou887.zhihu.ZhiHuHttpClient.parseUserCount;

/**
 * 知乎用户列表详情页task
 */
public class DetailListPageTask extends ZhihuPageTask {
    private static Logger logger = Logger.getLogger(DetailListPageTask.class);
    private static ListPageParser proxyUserListPageParser;
    
    // Thread-数据库连接
    private static Map<Thread, Connection> connectionMap = new ConcurrentHashMap<>();
    static {
        proxyUserListPageParser = getProxyUserListPageParser();
    }


    public DetailListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }

    public static Map<Thread, Connection> getConnectionMap() {
        return connectionMap;
    }

    @Override
    public void retry() {
        zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, Config.isProxy));
    }

    @Override
    public void handle(Page page) {
        if(!page.getHtml().startsWith("{\"paging\"")){
            //代理异常，未能正确返回目标请求数据，丢弃
            currentProxy = null;
            return;
        }
        List<User> list = proxyUserListPageParser.parseListPage(page);
        for(User u : list){
            logger.info("解析用户成功:" + u.toString());
            Connection cn = getConnection();
            if (zhiHuDao1.insertUser(cn, u)){
                parseUserCount.incrementAndGet();
            }
            for (int j = 0; j < u.getFollowees() / 20; j++){
                if (zhiHuHttpClient.getDetailListPageThreadPool().getQueue().size() > 1000){
                    continue;
                }
                String nextUrl = String.format(ZhiHuConstants.USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
                if (zhiHuDao1.insertUrl(cn, Md5Util.Convert2Md5(nextUrl)) || zhiHuHttpClient.getDetailListPageThreadPool().getActiveCount() == 1){
                    //防止死锁
                    HttpGet request = new HttpGet(nextUrl);
                    request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                    zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
                }
            }
            
        }
    }

    /**
     * 每个thread维护一个Connection
     * @return
     */
    private Connection getConnection(){
        Thread currentThread = Thread.currentThread();
        Connection cn = null;
        if (!connectionMap.containsKey(currentThread)){
            cn = ConnectionManager.createConnection();
            connectionMap.put(currentThread, cn);
        }  else {
            cn = connectionMap.get(currentThread);
        }
        return cn;
    }

    /**
     * 代理类
     * @return
     */
    private static ListPageParser getProxyUserListPageParser(){
        ListPageParser userListPageParser = ZhiHuUserListPageParser.getInstance();
        InvocationHandler invocationHandler = new SimpleInvocationHandler(userListPageParser);
        ListPageParser proxyUserListPageParser = (ListPageParser) Proxy.newProxyInstance(userListPageParser.getClass().getClassLoader(),
                userListPageParser.getClass().getInterfaces(), invocationHandler);
        return proxyUserListPageParser;
    }    
    
}
