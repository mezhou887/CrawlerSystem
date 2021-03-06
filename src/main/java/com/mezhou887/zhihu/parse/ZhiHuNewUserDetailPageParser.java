package com.mezhou887.zhihu.parse;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.mezhou887.crawler.parse.DetailPageParser;
import com.mezhou887.util.ZhiHuConstants;
import com.mezhou887.zhihu.ZhiHuHttpClient;
import com.mezhou887.zhihu.entity.Page;
import com.mezhou887.zhihu.entity.User;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://www.zhihu.com/people/wo-yan-chen-mo/following
 * 新版following页面解析出用户详细信息
 */
public class ZhiHuNewUserDetailPageParser implements DetailPageParser {
	
	private static Logger logger = Logger.getLogger(ZhiHuNewUserDetailPageParser.class);
	
    private volatile static ZhiHuNewUserDetailPageParser instance;
    public static ZhiHuNewUserDetailPageParser getInstance(){
        if (instance == null){
            synchronized (ZhiHuHttpClient.class){
                if (instance == null){
                    instance = new ZhiHuNewUserDetailPageParser();
                }
            }
        }
        return instance;
    }
    
    private ZhiHuNewUserDetailPageParser(){

    }
    
    @Override
    public User parseDetailPage(Page page) {
        Document doc = Jsoup.parse(page.getHtml());
        User user = new User();
        String userToken = getUserToken(page.getUrl());
        user.setUserToken(userToken);
        user.setUrl(ZhiHuConstants.INDEX_URL + "/people/" + userToken);//用户主页
        getUserByJson(user, userToken, doc.select("[data-state]").first().attr("data-state"));
        return user;
    }
    
    private void getUserByJson(User user, String userToken, String dataStateJson){

        String type = "['" + userToken + "']";//转义
        String commonJsonPath = "$.entities.users." + type;
        try {
            JsonPath.parse(dataStateJson).read(commonJsonPath);
        } catch (PathNotFoundException e){
            commonJsonPath = "$.entities.users.null";
        }
        setUserInfoByJsonPth(user, "username", dataStateJson, commonJsonPath + ".name");//username
        setUserInfoByJsonPth(user, "hashId", dataStateJson, commonJsonPath + ".id");//hashId
        setUserInfoByJsonPth(user, "followees", dataStateJson, commonJsonPath + ".followingCount");//关注人数
        setUserInfoByJsonPth(user, "location", dataStateJson, commonJsonPath + ".locations[0].name");//位置
        setUserInfoByJsonPth(user, "business", dataStateJson, commonJsonPath + ".business.name");//行业
        setUserInfoByJsonPth(user, "employment", dataStateJson, commonJsonPath + ".employments[0].company.name");//公司
        setUserInfoByJsonPth(user, "position", dataStateJson, commonJsonPath + ".employments[0].job.name");//职位
        setUserInfoByJsonPth(user, "education", dataStateJson, commonJsonPath + ".educations[0].school.name");//学校
        setUserInfoByJsonPth(user, "answers", dataStateJson, commonJsonPath + ".answerCount");//回答数
        setUserInfoByJsonPth(user, "asks", dataStateJson, commonJsonPath + ".questionCount");//提问数
        setUserInfoByJsonPth(user, "posts", dataStateJson, commonJsonPath + ".articlesCount");//文章数
        setUserInfoByJsonPth(user, "followers", dataStateJson, commonJsonPath + ".followerCount");//粉丝数
        setUserInfoByJsonPth(user, "agrees", dataStateJson, commonJsonPath + ".voteupCount");//赞同数
        setUserInfoByJsonPth(user, "thanks", dataStateJson, commonJsonPath + ".thankedCount");//感谢数
        try {
            Integer gender = JsonPath.parse(dataStateJson).read(commonJsonPath + ".gender");
            if (gender != null && gender == 1){
                user.setSex("male");
            }
            else if(gender != null && gender == 0){
                user.setSex("female");
            }
        } catch (PathNotFoundException e){
            //没有该属性  //log
        }
    }

    /**
     * jsonPath获取值，并通过反射直接注入到user中
     * @param user
     * @param fieldName
     * @param json
     * @param jsonPath
     */
    private void setUserInfoByJsonPth(User user, String fieldName, String json, String jsonPath){
        try {
            Object o = JsonPath.parse(json).read(jsonPath);
            Field field = user.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(user, o);
        } catch (PathNotFoundException e1) {
            //no results
        } catch (Exception e){
        	logger.error(e);
        }
    }

    /**
     * 根据url解析出用户id
     * @param url
     * @return
     */
    private String getUserToken(String url){
        Pattern pattern = Pattern.compile(ZhiHuConstants.INDEX_URL + "/[a-z]+/(.*)/following");
        Matcher matcher = pattern.matcher(url);
        String userId = null;
        if(matcher.find()){
            userId = matcher.group(1);
            return userId;
        }
        throw new RuntimeException("not parseListPage userId");
    }
}