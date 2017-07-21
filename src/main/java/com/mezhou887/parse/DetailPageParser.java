package com.mezhou887.parse;

import com.mezhou887.parse.Parser;
import com.mezhou887.zhihu.entity.Page;
import com.mezhou887.zhihu.entity.User;

public interface DetailPageParser extends Parser {
    User parseDetailPage(Page page);
}
