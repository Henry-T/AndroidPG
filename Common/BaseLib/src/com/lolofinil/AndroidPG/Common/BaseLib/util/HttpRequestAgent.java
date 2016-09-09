package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// todo support network availability check

/**
 * Created by Henry on 9/1/2016.
 */
public class HttpRequestAgent extends AsyncTask<String, String, HttpResponseInfo> {
    private static String tag = HttpRequestAgent.class.getSimpleName();

    private Context context;
    public int RetryCount = 1;

    private EStringFormat expectedResponseBodyFormat = EStringFormat.NotSet;
    private List<String> reservedDNSList;
    private List<String> preresolvedBanlaceHostList;
    private IHttpRequestHandler invalidResponseHandler;
    private IHttpRequestHandler handler;

    private String url;

    public HttpRequestAgent(Context context, List<String> reservedDNSList, List<String> preresolvedBanlaceHostList, EStringFormat expectedResponseBodyFormat, final IHttpRequestHandler invalidResponseHandler, IHttpRequestHandler handler) {
        this.context = context;
        this.reservedDNSList = reservedDNSList;
        this.preresolvedBanlaceHostList = preresolvedBanlaceHostList;
        this.expectedResponseBodyFormat = expectedResponseBodyFormat;
        this.invalidResponseHandler = invalidResponseHandler;
        this.handler = handler;

        buildTrailList();
    }

    class TrialOnceData {
        public String DNS = "";
        public String PreresolvedHost = "";
    }

    // todo note
    private ArrayList<TrialOnceData> trialOnceDataList;

    private void buildTrailList() {
        trialOnceDataList = new ArrayList<>();
        for(int i=0; i<RetryCount+1; i++) {
            TrialOnceData data = new TrialOnceData();
            trialOnceDataList.add(data);
        }
        if (reservedDNSList != null) {
            for(int i=0; i<reservedDNSList.size(); i++) {
                TrialOnceData data = new TrialOnceData();
                data.DNS = reservedDNSList.get(i);
                trialOnceDataList.add(data);
            }
        }
        if (preresolvedBanlaceHostList != null) {
            for (int i = 0; i < preresolvedBanlaceHostList.size(); i++) {
                TrialOnceData data = new TrialOnceData();
                data.PreresolvedHost = preresolvedBanlaceHostList.get(i);
                trialOnceDataList.add(data);
            }
        }
    }

    @Override
    protected HttpResponseInfo doInBackground(String... params) {
        url = params[0];

        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            // no request & no retry when network unavailable
            HttpResponseInfo httpResponseInfo = new HttpResponseInfo();
            httpResponseInfo.Status = EHttpResponseStatus.NetworkUnavailable;
            return httpResponseInfo;
        }

        Iterator iter = trialOnceDataList.iterator();
        HttpResponseInfo lastTrialResponse = null;
        int count = 0;
        while(iter.hasNext()) {
            TrialOnceData trialOnceData = (TrialOnceData)iter.next();
            String msg = String.format("trial count:%s DNS:%s Host:%s", count, trialOnceData.DNS, trialOnceData.PreresolvedHost);
            Log.i(tag, msg);
            lastTrialResponse = requestOnce(trialOnceData.DNS, trialOnceData.PreresolvedHost, expectedResponseBodyFormat);
            if (lastTrialResponse.Status == EHttpResponseStatus.Succeed) {
                break;
            } else {
                invalidResponseHandler.Callback(lastTrialResponse);
            }
        }
        return lastTrialResponse;
    }

    private HttpResponseInfo requestOnce(String reservedDNS, String preresolvedHost, EStringFormat expectedResponseBodyFormat) {
        HttpResponseInfo responseInfo = NetworkUtil.RequestWithMesberaHttpClient(url, reservedDNS, preresolvedHost);
        if (responseInfo.Status == EHttpResponseStatus.Succeed && !ValidUtil.ValidStringFormat(responseInfo.Content, expectedResponseBodyFormat))
            responseInfo.Status = EHttpResponseStatus.UnexpectedResponseBodyFormat;
        return responseInfo;
    }

    @Override
    protected void onPostExecute(HttpResponseInfo httpResponseInfo) {
        super.onPostExecute(httpResponseInfo);
        handler.Callback(httpResponseInfo);
    }
}
