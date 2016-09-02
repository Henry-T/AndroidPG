package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		Long startTime = (long) 0;
		
		HttpResponseInfo responseInfo = new HttpResponseInfo();

		try {
			Log.i(this.getClass().getSimpleName(), "Http 请求:" + url[0]);
			HttpGet httpGet = new HttpGet(url[0]);

			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000); // 50000
			HttpConnectionParams.setSoTimeout(params, 5000); // 50000

			startTime = System.currentTimeMillis();
			response = httpClient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			int httpStatus = statusLine.getStatusCode();
			if (httpStatus == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				HeaderIterator headerIterator = response.headerIterator();
				while(headerIterator.hasNext()) {
					Header header = headerIterator.nextHeader();
					Log.i(tag, "Header Name: "+header.getName() + " Header Value:"+header.getValue());
				}

				String encoding = "UTF-8";
				Header encodingHeader = entity.getContentEncoding();
				if (encodingHeader != null) {
					Log.i(tag, "Response Encoding: "+encodingHeader.getValue());
					encoding = encodingHeader.getValue();
				} else {
					Log.i(tag, "Response Encoding not set, use default encoding UTF-8");
				}
				String bodyStr = EntityUtils.toString(entity, encoding);
				Log.i(tag, "Content: "+bodyStr);

				// content may be empty with this approach
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				response.getEntity().writeTo(out);
//				responseString = out.toString();
//				out.close();

				responseInfo.Content = bodyStr;
				if (validResponseBodyFormat(bodyStr))
					responseInfo.Status = EHttpResponseStatus.Succeed;
				else
					responseInfo.Status = EHttpResponseStatus.UnexpectedResponseBodyFormat;
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

	private boolean validResponseBodyFormat(String responseBody) {
		if (ExpectedResponseBodyFormat == EStringFormat.NotSet) {
			return true;
		} else if (ExpectedResponseBodyFormat == EStringFormat.Integer) {
			try {
				long longValue = Long.valueOf(responseBody);
				return true;
			} catch (NumberFormatException ex) {
				return false;
			}
		} else if (ExpectedResponseBodyFormat == EStringFormat.JSON) {
			JsonParser jsonParser = new JsonParser();
			try {
				JsonElement jsonEle= jsonParser.parse(responseBody);
				return true;
			} catch (JsonSyntaxException ex) {
				return false;
			}
		} else {
			Log.w(tag, "ResponseBodyFormatValidation unimplemented for type:"+ExpectedResponseBodyFormat.name());
			new Exception().printStackTrace();
			return false;
		}
	}

}