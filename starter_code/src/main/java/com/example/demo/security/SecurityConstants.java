package com.example.demo.security;

/**
 * @author RafaelBizi
 * @created 16/11/2021 - 22:15
 * @project project_4_udacity
 */

public class SecurityConstants {
    public static final String REQUEST_ERROR = "Error while request login body!";
    public static final String SECRET = "secretkey";
    public static final long EXPIRATION_TIME = 604_800_000; // 7 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGNUP_URL = "/api/user/create";
}
