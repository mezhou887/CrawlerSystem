package com.crawl.proxy.site.ip181;


import org.junit.Test;

import com.mezhou887.proxy.ProxyHttpClient;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.proxy.site.ip181.Ip181ProxyListPageParser;
import com.mezhou887.zhihu.entity.Page;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class Ip181ProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        System.out.println(Charset.defaultCharset().toString());
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html");
//        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html", "gb2312");
        List<Proxy> urlList = new Ip181ProxyListPageParser().parse(page.getHtml());
        System.out.println(urlList.size());
    }
}
