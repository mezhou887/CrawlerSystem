package com.mezhou887.crawler.httpclient;

import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.mezhou887.util.HttpClientUtil;
import com.mezhou887.zhihu.entity.Page;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractHttpClient {
    private Logger logger = Logger.getLogger(AbstractHttpClient.class);
    
    public InputStream getWebPageInputStream(String url){
        try {
            CloseableHttpResponse response = HttpClientUtil.getResponse(url);
            return response.getEntity().getContent();
        } catch (IOException e) {
        	logger.error(e);
        }
        return null;
    }
    
    public Page getWebPage(String url) throws IOException {
        return getWebPage(url, "UTF-8");
    }
    
    public Page getWebPage(String url, String charset) throws IOException {
        Page page = new Page();
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse(url);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(url);
        try {
            if(page.getStatusCode() == HttpStatus.SC_OK){
                page.setHtml(EntityUtils.toString(response.getEntity(), charset));
            }
        } catch (IOException e) {
        	logger.error(e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
            	logger.error(e);
            }
        }
        return page;
    }
    
    public Page getWebPage(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
            response = HttpClientUtil.getResponse(request);
            Page page = new Page();
            page.setStatusCode(response.getStatusLine().getStatusCode());
            page.setHtml(EntityUtils.toString(response.getEntity()));
            page.setUrl(request.getURI().toString());
            return page;
    }

    public boolean deserializeCookieStore(String path){
        try {
            CookieStore cookieStore = (CookieStore) HttpClientUtil.deserializeObject(path);
            HttpClientUtil.setCookieStore(cookieStore);
        } catch (Exception e){
            logger.warn("反序列化Cookie失败,没有找到Cookie文件");
            return false;
        }
        return true;
    }
}
