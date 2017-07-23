package com.mezhou887.proxy.site.mimiip;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mezhou887.proxy.ProxyListPageParser;
import com.mezhou887.proxy.entity.Proxy;

import java.util.ArrayList;
import java.util.List;

import static com.mezhou887.util.ZhiHuConstants.TIME_INTERVAL;

public class MimiipProxyListPageParser implements ProxyListPageParser{
    @Override
    public List<Proxy> parse(String hmtl) {
        Document document = Jsoup.parse(hmtl);
        Elements elements = document.select("table[class=list] tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (int i = 1; i < elements.size(); i++){
            String isAnonymous = elements.get(i).select("td:eq(3)").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿")){
                String ip = elements.get(i).select("td:eq(0)").first().text();
                String port  = elements.get(i).select("td:eq(1)").first().text();
                proxyList.add(new Proxy(ip, Integer.valueOf(port), TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
