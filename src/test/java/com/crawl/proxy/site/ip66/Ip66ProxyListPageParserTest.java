package com.crawl.proxy.site.ip66;


import org.junit.Test;

import com.mezhou887.proxy.ProxyHttpClient;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.proxy.site.ip66.Ip66ProxyListPageParser;
import com.mezhou887.zhihu.entity.Page;

import java.io.IOException;
import java.util.List;

public class Ip66ProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.66ip.cn/index.html");
        page.setHtml(new String(page.getHtml().getBytes("GB2312"), "GB2312"));
        List<Proxy> urlList = new Ip66ProxyListPageParser().parse(page.getHtml());
        System.out.println(urlList.size());
    }
}
