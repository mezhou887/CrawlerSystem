package com.mezhou887.crawler.service.impl;

import com.mezhou887.crawler.dao.UrlMapper;
import com.mezhou887.crawler.model.Url;
import com.mezhou887.crawler.service.UrlService;
import com.mezhou887.system.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/07/17.
 */
@Service
@Transactional
public class UrlServiceImpl extends AbstractService<Url> implements UrlService {
    @Resource
    private UrlMapper urlMapper;

}
