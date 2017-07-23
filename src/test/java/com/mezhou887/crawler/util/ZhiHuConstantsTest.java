package com.mezhou887.crawler.util;

import org.junit.Test;

public class ZhiHuConstantsTest {
	
	String userToken = "wo-yan-chen-mo";

	@Test
	public void testFormatUserFolloweesUrlV1() {
		String userFolloweesUrl = String.format(ZhiHuConstants.USER_FOLLOWEES_URL, userToken, 20);
		System.out.println(userFolloweesUrl);

	}	
	
	@Test
	public void testFormatUserFolloweesUrlV2() {
		String userFolloweesUrl = String.format(ZhiHuConstants.USER_FOLLOWEES_URL_V2, userToken, 20);
		System.out.println(userFolloweesUrl);
	}

}
