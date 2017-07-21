package com.crawl.proxy.site.mimiip;


import org.junit.Test;

import com.mezhou887.proxy.ProxyHttpClient;
import com.mezhou887.proxy.entity.Proxy;
import com.mezhou887.proxy.site.mimiip.MimiipProxyListPageParser;
import com.mezhou887.zhihu.entity.Page;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class MimiipProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        System.out.println(Charset.defaultCharset().toString());
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.mimiip.com/gngao/");
        List<Proxy> urlList = new MimiipProxyListPageParser().parse(page.getHtml());
        System.out.println(urlList.size());
    }
}
