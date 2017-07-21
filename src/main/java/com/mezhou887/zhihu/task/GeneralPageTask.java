package com.mezhou887.zhihu.task;

import org.apache.log4j.Logger;

import com.mezhou887.zhihu.entity.Page;

/**
 * GeneralPageTask
 * 下载初始化authorization字段页面
 */
public class GeneralPageTask extends ZhihuPageTask {
	private static Logger logger = Logger.getLogger(GeneralPageTask.class);
    private Page page = null;

    public GeneralPageTask(String url, boolean proxyFlag) {
        super(url, proxyFlag);
    }

    @Override
    public void retry() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        	logger.error(e);
        }
        this.run();//继续下载
    }

    @Override
    public void handle(Page page) {
        this.page = page;
    }

    public Page getPage(){
        return page;
    }
}
