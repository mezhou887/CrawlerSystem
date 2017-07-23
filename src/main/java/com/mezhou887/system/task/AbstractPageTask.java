package com.mezhou887.system.task;

import com.mezhou887.util.ZhiHuConstants;
import com.mezhou887.util.HttpClientUtil;
import com.mezhou887.util.ProxyUtil;
import com.mezhou887.proxy.ProxyPool;
import com.mezhou887.proxy.entity.Direct;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.zhihu.entity.Page;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 下载网页并解析，具体解析由子类实现
 * 若使用代理，从ProxyPool中取
 * @see ProxyPool
 */
public abstract class AbstractPageTask implements Runnable {
	private static Logger logger = Logger.getLogger(AbstractPageTask.class);
	protected String url;
	protected HttpRequestBase request;
	protected boolean proxyFlag;//是否通过代理下载
	protected Proxy currentProxy;//当前线程使用的代理

	public AbstractPageTask(){ }
	
	public AbstractPageTask(String url, boolean proxyFlag){
		this.url = url;
		this.proxyFlag = proxyFlag;
	}
	
	public AbstractPageTask(HttpRequestBase request, boolean proxyFlag){
		this.request = request;
		this.proxyFlag = proxyFlag;
	}
	
	public void run(){
		HttpGet tempRequest = null;
		try {
			if(url != null) {
				tempRequest = new HttpGet(url);
			} else if (request != null) {
				tempRequest = (HttpGet) request;
			}
			
			if(proxyFlag) {
				currentProxy = ProxyPool.proxyQueue.take();
				if(!(currentProxy instanceof Direct)){
					HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
					tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
				}
			}
			long requestStartTime = System.currentTimeMillis();
			Page page = getWebPage(tempRequest);
			
			long requestEndTime = System.currentTimeMillis();
			page.setProxy(currentProxy);
			int status = page.getStatusCode();
			String logStr = Thread.currentThread().getName() + " " + currentProxy + "  executing request " + page.getUrl()  + " response statusCode:" + status + "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
			if(status == HttpStatus.SC_OK){
				if (page.getHtml().contains("zhihu") && !page.getHtml().contains("安全验证")){ // 判断这里应该由子类来完成
					logger.debug(logStr);
					currentProxy.setSuccessfulTimes(currentProxy.getSuccessfulTimes() + 1);
					currentProxy.setSuccessfulTotalTime(currentProxy.getSuccessfulTotalTime() + (requestEndTime - requestStartTime));
					double aTime = (currentProxy.getSuccessfulTotalTime() + 0.0) / currentProxy.getSuccessfulTimes();
					currentProxy.setSuccessfulAverageTime(aTime);
					currentProxy.setLastSuccessfulTime(System.currentTimeMillis());
					handle(page);
				}else {
					if(currentProxy != null){ 
						logger.error("proxy exception:" + currentProxy.toString());						
					}
				}

			} else if(status == HttpStatus.SC_NOT_FOUND || status == HttpStatus.SC_UNAUTHORIZED || status == HttpStatus.SC_GONE){
				logger.warn(logStr);
			}
			else {
				logger.error(logStr);
				Thread.sleep(100);
				retry();
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		} catch (IOException e) {
            if(currentProxy != null){
                currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
            }
            retry();
		} finally {
			if (request != null){
				request.releaseConnection();
			}
			if (tempRequest != null){
				tempRequest.releaseConnection();
			}
			if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
				currentProxy.setTimeInterval(ZhiHuConstants.TIME_INTERVAL);
				ProxyPool.proxyQueue.add(currentProxy);
			}
		}
	}

	public abstract Page getWebPage(HttpRequestBase request) throws IOException;

	// 子类实现重试的功能
	public abstract void retry();

	// 子类去处理page
	public abstract void handle(Page page);
	
}
