package com.apowo.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestTask extends AsyncTask<String, String, HttpResponseInfo> {
	private IHttpRequestHandler handler;

	public HttpRequestTask(IHttpRequestHandler handler) {
		this.handler = handler;
	}

	@Override
	protected HttpResponseInfo doInBackground(String... url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		Long startTime = (long) 0;
		
		HttpResponseInfo responseInfo = new HttpResponseInfo();

		try {
			Log.i(this.getClass().getSimpleName(), "Http 请求:" + url[0]);
			HttpGet httpGet = new HttpGet(url[0]);

			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 50000);
			HttpConnectionParams.setSoTimeout(params, 50000);

			startTime = System.currentTimeMillis();
			response = httpClient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			int httpStatus = statusLine.getStatusCode();
			if (httpStatus == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				responseString = out.toString();
				out.close();
				responseInfo.Status = EHttpResponseStatus.Succeed;
				responseInfo.Content = responseString;
			} else {
				response.getEntity().getContent().close();
				responseInfo.Status = EHttpResponseStatus.HttpStatusNot200;
				responseInfo.InternalErrorCode = httpStatus;
				responseInfo.InternalErrorMsg = statusLine.getReasonPhrase();
			}
		} catch (SocketTimeoutException se) {
			Long endTime = System.currentTimeMillis();
			String msg = "SocketTimeoutException :: time elapsed :: "
					+ (endTime - startTime);
			responseInfo.Status = EHttpResponseStatus.SocketTimedOut;
			responseInfo.InternalErrorMsg = msg;
		} catch (ConnectTimeoutException cte) {
			Long endTime = System.currentTimeMillis();
			String msg = "ConnectTimeoutException :: time elapsed :: "
							+ (endTime - startTime);
			responseInfo.Status = EHttpResponseStatus.ConnectionTimedOut;
			responseInfo.InternalErrorMsg = msg;
		} catch (ClientProtocolException e) {
			responseInfo.Status = EHttpResponseStatus.Failed;
			responseInfo.InternalErrorMsg = "ClientProtocolException";
		} catch (UnknownHostException e) {
			responseInfo.Status = EHttpResponseStatus.UnknownHost;
			responseInfo.InternalErrorMsg = "UnknownHostException";
		} catch (IOException e) {
			Long endTime = System.currentTimeMillis();
			String msg = "IOException :: time elapsed :: "
					+ (endTime - startTime);
			responseInfo.Status = EHttpResponseStatus.Failed;
			responseInfo.InternalErrorMsg = msg;
		}

		return responseInfo;
	}

	@Override
	protected void onPostExecute(HttpResponseInfo reponseInfo) {
		super.onPostExecute(reponseInfo);
		Log.i(this.getClass().getSimpleName(), "Http回复:" + reponseInfo.Content);
		handler.Callback(reponseInfo);
	}
}