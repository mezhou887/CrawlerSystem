package com.mezhou887.proxy.parse;


import com.mezhou887.crawler.parse.Parser;
import com.mezhou887.proxy.entity.Proxy;

import java.util.List;


public interface ProxyListPageParser extends Parser{
    /**
     * 是否只要匿名代理
     */
    static final boolean anonymousFlag = true;
    List<Proxy> parse(String content);
}
