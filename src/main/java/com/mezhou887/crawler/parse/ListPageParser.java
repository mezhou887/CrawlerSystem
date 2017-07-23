package com.mezhou887.crawler.parse;

import com.mezhou887.crawler.parse.Parser;
import com.mezhou887.zhihu.entity.Page;
import com.mezhou887.zhihu.entity.User;

import java.util.List;

public interface ListPageParser extends Parser {
    List<User> parseListPage(Page page);
}
