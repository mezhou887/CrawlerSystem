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
    
    // 需重构
    public static String formatUserFolloweesUrl(String userToken, int offset){
        String url = ZhiHuConstants.INDEX_URL + "/api/v4/members/" + userToken + "/followees?include=data%5B*%5D.answer_count%2Carticles_count%2Cfollower_count%2C" +
                "is_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=" + offset + "&limit=20";
        return url;
    }    
    
    
    
}
