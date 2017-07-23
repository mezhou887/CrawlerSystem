package com.mezhou887.crawler.util;


public class ZhiHuConstants {
	
    public final static int TIMEOUT = 16000;

    // 单个ip请求间隔，单位ms
    public final static long TIME_INTERVAL = 1000;

    //知乎首页
    public final static String INDEX_URL = "https://www.zhihu.com";

    public final static String USER_FOLLOWEES_URL = "https://www.zhihu.com/api/v4/members/%s/followees?" +
            "include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count," +
            "gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following," +
            "badge[?(type=best_answerer)].topics&offset=%d&limit=20";
    
    public final static String USER_FOLLOWEES_URL_V2 = "https://www.zhihu.com/api/v4/members/%s/followees?" +
    		"include=data[*].answer_count,articles_count,follower_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=%d&limit=20";
    
}
