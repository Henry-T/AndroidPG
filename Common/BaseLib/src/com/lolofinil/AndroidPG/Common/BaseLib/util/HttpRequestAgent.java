package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.os.AsyncTask;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.List;

// todo support network availability check

/**
 * Created by Henry on 9/1/2016.
 */
public class HttpRequestAgent extends AsyncTask<String, String, HttpResponseInfo> {
    public int RetryCount = 1;
    public String PreresolvedHost = "";
    public EStringFormat ExpectedResponseBodyFormat = EStringFormat.NotSet;

    public HttpRequestAgent(String reservedDNS, List<String> preresolvedBanlaceHostList, EStringFormat expectedResponseBodyFormat, final IHttpRequestHandler invalidResponseHandler, IHttpRequestHandler handler) {

    }

    private void buildTrailList() {

    }

    private boolean requestOnce(String reservedDNS, String preresolvedHost, String expectedResponseBodyFormat, final IHttpRequestHandler invalidResponseHandler, final IHttpRequestHandler handler) {
        HttpRequestTask httpRequestTask = new HttpRequestTask(new IHttpRequestHandler() {
            @Override
            public void Callback(HttpResponseInfo httpResponseInfo) {
                if (httpResponseInfo.Status == EHttpResponseStatus.Succeed) {
                    handler.Callback(httpResponseInfo);
                } else {
                    invalidResponseHandler.Callback(httpResponseInfo);
                }
            }
        }, expectedResponseBodyFormat);
    }

    private boolean isRetryMakeSense(HttpResponseInfo httpResponseInfo) {
        // todo 没有联网就不要重试了
    }
}
