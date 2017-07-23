package com.mezhou887.crawler.httpclient;

// 需重构
public interface IHttpClient {

    // 爬虫入口
    void startCrawl(String url);
    
    // 爬虫入口
    void startCrawl();
    
    // 爬虫停止
}
