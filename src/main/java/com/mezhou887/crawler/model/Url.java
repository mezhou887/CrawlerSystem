package com.mezhou887.crawler.model;

import javax.persistence.*;

public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "md5_url")
    private String md5Url;

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
     * @return md5_url
     */
    public String getMd5Url() {
        return md5Url;
    }

    /**
     * @param md5Url
     */
    public void setMd5Url(String md5Url) {
        this.md5Url = md5Url;
    }
}