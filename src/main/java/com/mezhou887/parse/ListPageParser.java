package com.mezhou887.parse;

import com.mezhou887.parse.Parser;
import com.mezhou887.zhihu.entity.Page;
import com.mezhou887.zhihu.entity.User;

import java.util.List;

public interface ListPageParser extends Parser {
    List<User> parseListPage(Page page);
}
