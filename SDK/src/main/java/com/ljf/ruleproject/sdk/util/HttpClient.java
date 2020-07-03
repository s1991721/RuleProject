package com.ljf.ruleproject.sdk.util;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by mr.lin on 2020/7/3
 */
public class HttpClient {

    public static final int ConnectTimeOut = 10;//网络超时时间
    public static final int ReadTimeOut = 30;
    public static final int WriteTimeOut = 60;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(ConnectTimeOut, TimeUnit.SECONDS)
            .readTimeout(ReadTimeOut, TimeUnit.SECONDS)
            .writeTimeout(WriteTimeOut, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

}
