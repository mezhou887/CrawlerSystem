package com.mezhou887.zhihu.task;


import com.jayway.jsonpath.JsonPath;
import com.mezhou887.util.Config;
import com.mezhou887.util.ZhiHuConstants;
import com.mezhou887.zhihu.entity.Page;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.util.List;
/**
 * 知乎用户关注列表页task
 * 下载成功解析出用户token，去重,构造用户详情url，获，添加到DetailPageDownloadThreadPool
 */
public class ListPageTask extends ZhihuPageTask {
	
	 private static Logger logger = Logger.getLogger(ListPageTask.class);

    public ListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }

    @Override
    public void retry() {
        zhiHuHttpClient.getListPageThreadPool().execute(new ListPageTask(request, Config.isProxy));
    }

    @Override
    public void handle(Page page) {
        // "我关注的人"列表页
        List<String> urlTokenList = JsonPath.parse(page.getHtml()).read("$.data..url_token");
        for (String s : urlTokenList){
            if (s == null){
                continue;
            }
            handleUserToken(s);
        }
    }
    
    private void handleUserToken(String userToken){
        String url = ZhiHuConstants.INDEX_URL + "/people/" + userToken + "/following"; //范例: https://www.zhihu.com/people/wo-yan-chen-mo/following
        boolean existUserFlag = zhiHuDao1.isExistUser(userToken);
        while (zhiHuHttpClient.getDetailPageThreadPool().getQueue().size() > 1000){ //为什么要大于1000
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            	logger.error(e);  // log
            }
        }
        if(!existUserFlag || zhiHuHttpClient.getDetailPageThreadPool().getActiveCount() == 0){
            // 防止互相等待，导致死锁
            zhiHuHttpClient.getDetailPageThreadPool().execute(new DetailPageTask(url, Config.isProxy));

        }
    }
}
