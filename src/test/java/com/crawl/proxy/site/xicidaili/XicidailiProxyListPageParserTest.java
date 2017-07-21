package com.crawl.proxy.site.xicidaili;


import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;

import com.mezhou887.proxy.ProxyHttpClient;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.proxy.site.xicidaili.XicidailiProxyListPageParser;
import com.mezhou887.util.HttpClientUtil;
import com.mezhou887.zhihu.entity.Page;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class XicidailiProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        RequestConfig.Builder requestBuilder = HttpClientUtil.getRequestConfigBuilder();
        requestBuilder.setProxy(new HttpHost("125.31.19.27", 80));
        HttpGet request = new HttpGet("http://www.xicidaili.com/wt/1.html");
        request.setConfig(requestBuilder.build());
        Page page = ProxyHttpClient.getInstance().getWebPage(request);
        List<Proxy> urlList = new XicidailiProxyListPageParser().parse(page.getHtml());
        System.out.println(urlList.size());
    }
}
