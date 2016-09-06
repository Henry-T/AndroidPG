package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestTask extends AsyncTask<String, String, HttpResponseInfo> {
	private static String tag = HttpRequestTask.class.getSimpleName();

	private IHttpRequestHandler handler;
	public EStringFormat ExpectedResponseBodyFormat = EStringFormat.NotSet;

	public HttpRequestTask(IHttpRequestHandler handler) {
		new HttpRequestTask(handler, EStringFormat.NotSet);
	}

	public HttpRequestTask(IHttpRequestHandler handler, EStringFormat expectedResponseBodyFormat) {
		this.handler = handler;
		ExpectedResponseBodyFormat = expectedResponseBodyFormat;
	}

	@Override
	protected HttpResponseInfo doInBackground(String... url) {
		HttpResponseInfo responseInfo = NetworkUtil.RequestWithApacheHttpClient(url[0]);
		if (responseInfo.Status == EHttpResponseStatus.Succeed && !ValidUtil.ValidStringFormat(responseInfo.Content, ExpectedResponseBodyFormat))
			responseInfo.Status = EHttpResponseStatus.UnexpectedResponseBodyFormat;
		return responseInfo;
	}

	@Override
	protected void onPostExecute(HttpResponseInfo reponseInfo) {
		super.onPostExecute(reponseInfo);
		Log.i(this.getClass().getSimpleName(), "Http回复:" + reponseInfo.Content);
		handler.Callback(reponseInfo);
	}

}