package com.mezhou887.crawler.model;

import javax.persistence.*;

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_token")
    private String userToken;

    private String location;

    private String business;

    private String sex;

    private String employment;

    private String education;

    private String username;

    private String url;

    private Integer agrees;

    private Integer thanks;

    private Integer asks;

    private Integer answers;

    private Integer posts;

    private Integer followees;

    private Integer followers;

    @Column(name = "hashId")
    private String hashid;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return user_token
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * @param userToken
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return business
     */
    public String getBusiness() {
        return business;
    }

    /**
     * @param business
     */
    public void setBusiness(String business) {
        this.business = business;
    }

    /**
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return employment
     */
    public String getEmployment() {
        return employment;
    }

    /**
     * @param employment
     */
    public void setEmployment(String employment) {
        this.employment = employment;
    }

    /**
     * @return education
     */
    public String getEducation() {
        return education;
    }

    /**
     * @param education
     */
    public void setEducation(String education) {
        this.education = education;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return agrees
     */
    public Integer getAgrees() {
        return agrees;
    }

    /**
     * @param agrees
     */
    public void setAgrees(Integer agrees) {
        this.agrees = agrees;
    }

    /**
     * @return thanks
     */
    public Integer getThanks() {
        return thanks;
    }

    /**
     * @param thanks
     */
    public void setThanks(Integer thanks) {
        this.thanks = thanks;
    }

    /**
     * @return asks
     */
    public Integer getAsks() {
        return asks;
    }

    /**
     * @param asks
     */
    public void setAsks(Integer asks) {
        this.asks = asks;
    }

    /**
     * @return answers
     */
    public Integer getAnswers() {
        return answers;
    }

    /**
     * @param answers
     */
    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    /**
     * @return posts
     */
    public Integer getPosts() {
        return posts;
    }

    /**
     * @param posts
     */
    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    /**
     * @return followees
     */
    public Integer getFollowees() {
        return followees;
    }

    /**
     * @param followees
     */
    public void setFollowees(Integer followees) {
        this.followees = followees;
    }

    /**
     * @return followers
     */
    public Integer getFollowers() {
        return followers;
    }

    /**
     * @param followers
     */
    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    /**
     * @return hashId
     */
    public String getHashid() {
        return hashid;
    }

    /**
     * @param hashid
     */
    public void setHashid(String hashid) {
        this.hashid = hashid;
    }
}