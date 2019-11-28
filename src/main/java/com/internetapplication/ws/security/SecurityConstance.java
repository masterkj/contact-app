package com.internetapplication.ws.security;

import com.internetapplication.ws.SpringApplicationContext;

public class SecurityConstance {
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/user";

	public static String getTokenSecret() {
		AppProperties appPropertiez = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appPropertiez.getTokenSecret();
	}
}
