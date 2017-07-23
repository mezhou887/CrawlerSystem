package com.mezhou887.proxy.site.ip181;


import com.mezhou887.proxy.ProxyListPageParser;
import com.mezhou887.proxy.entity.Proxy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.mezhou887.crawler.util.ZhiHuConstants.TIME_INTERVAL;

import java.util.ArrayList;
import java.util.List;

public class Ip181ProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String content) {
//        try {
//            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
//            Charset UTF_8 = Charset.forName("UTF-8");
//            Charset GB2312 = Charset.forName("GB2312");
//            System.out.println(content);
//            System.out.println(new String(content.getBytes(GB2312),UTF_8));
//            content = Charset.defaultCharset().decode(GB2312.encode(content)).toString();
//        } catch (UnsupportedEncodingException e) {
//            logger.error(e);
//        }
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table tr:gt(0)");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = element.select("td:eq(2)").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
