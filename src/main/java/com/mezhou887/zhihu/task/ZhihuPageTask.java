package com.mezhou887.zhihu.task;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import com.mezhou887.crawler.task.AbstractPageTask;
import com.mezhou887.crawler.util.HttpClientUtil;
import com.mezhou887.crawler.util.SimpleInvocationHandler;
import com.mezhou887.remove.ZhiHuDao1;
import com.mezhou887.remove.ZhiHuDao1Imp;
import com.mezhou887.zhihu.ZhiHuHttpClient;
import com.mezhou887.zhihu.entity.Page;

public abstract class ZhihuPageTask extends AbstractPageTask {
	
	protected static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();

	protected static ZhiHuDao1 zhiHuDao1;	
	static {
		zhiHuDao1 = getZhiHuDao1();
	}
	
    public ZhihuPageTask(String url, boolean proxyFlag) {
        super(url, proxyFlag);
    }
    
	public ZhihuPageTask(HttpRequestBase request, boolean proxyFlag){
		super(request, proxyFlag);
	}    

	// 子类实现重试的功能
	public abstract void retry();

	// 子类去处理page
	public abstract void handle(Page page);
	
	/**
	 * 代理类，统计方法执行时间
	 * @return
	 */
	private static ZhiHuDao1 getZhiHuDao1(){
		ZhiHuDao1 zhiHuDao1 = new ZhiHuDao1Imp();
		InvocationHandler invocationHandler = new SimpleInvocationHandler(zhiHuDao1);
		ZhiHuDao1 proxyZhiHuDao1 = (ZhiHuDao1) java.lang.reflect.Proxy.newProxyInstance(zhiHuDao1.getClass().getClassLoader(),
				zhiHuDao1.getClass().getInterfaces(), invocationHandler);
		return proxyZhiHuDao1;
	}

	@Override
    public Page getWebPage(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
            response = HttpClientUtil.getResponse(request);
            Page page = new Page();
            page.setStatusCode(response.getStatusLine().getStatusCode());
            page.setHtml(EntityUtils.toString(response.getEntity()));
            page.setUrl(request.getURI().toString());
            return page;
    }	
}
