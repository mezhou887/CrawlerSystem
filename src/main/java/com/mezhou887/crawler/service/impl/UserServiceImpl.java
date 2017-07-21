package com.mezhou887.crawler.service.impl;

import com.mezhou887.crawler.dao.UserMapper;
import com.mezhou887.crawler.model.User;
import com.mezhou887.crawler.service.UserService;
import com.mezhou887.system.core.AbstractService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/07/17.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
