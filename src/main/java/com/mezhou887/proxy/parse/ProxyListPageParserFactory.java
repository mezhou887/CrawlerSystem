package com.mezhou887.proxy.parse;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ProxyListPageParserFactory {
	private static Logger logger = Logger.getLogger(ProxyListPageParserFactory.class);
    private static Map<String, ProxyListPageParser> map  = new HashMap<>();

    public static ProxyListPageParser getProxyListPageParser(Class clazz){
        String parserName = clazz.getSimpleName();
        if (map.containsKey(parserName)){
            return map.get(parserName);
        }
        else {
            try {
                ProxyListPageParser parser = (ProxyListPageParser) clazz.newInstance();
                parserName = clazz.getSimpleName();
                map.put(parserName, parser);
                return parser;
            } catch (InstantiationException e) {
            	logger.error(e);
            } catch (IllegalAccessException e) {
            	logger.error(e);
            }
        }
        return null;
    }
}
