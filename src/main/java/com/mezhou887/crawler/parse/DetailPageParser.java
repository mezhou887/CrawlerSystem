package com.mezhou887.crawler.parse;

import com.mezhou887.crawler.parse.Parser;
import com.mezhou887.zhihu.entity.Page;
import com.mezhou887.zhihu.entity.User;

public interface DetailPageParser extends Parser {
    User parseDetailPage(Page page);
}
