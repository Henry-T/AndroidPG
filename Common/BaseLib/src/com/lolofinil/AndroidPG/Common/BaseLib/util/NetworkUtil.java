package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.net.Network;
import android.util.Log;

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
import org.apache.http.conn.
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Henry on 9/5/2016.
 */
public class NetworkUtil {
    private static String tag = NetworkUtil.class.getSimpleName();

    public static HttpResponseInfo RequestWithApacheHttpClient(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        Long startTime = (long) 0;

        HttpResponseInfo responseInfo = new HttpResponseInfo();

        try {
            Log.i(tag, "Http 请求:" + url);
            HttpGet httpGet = new HttpGet(url);

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
                responseInfo.Status = EHttpResponseStatus.Succeed;
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


    public static HttpResponseInfo RequestWithHttpConnection(String url) {
//        // ===================================
//        URL url = new URL();
//        HttpURLConnection httpURLConnection = new HttpURLConnection() {
//            @Override
//            public void disconnect() {
//
//            }
//
//            @Override
//            public boolean usingProxy() {
//                return false;
//            }
//
//            @Override
//            public void connect() throws IOException {
//
//            }
//        };
//
//
//        HttpRequestTask httpRequestTask = new HttpRequestTask(new IHttpRequestHandler() {
//            @Override
//            public void Callback(HttpResponseInfo httpResponseInfo) {
//                if (httpResponseInfo.Status == EHttpResponseStatus.Succeed) {
//                    handler.Callback(httpResponseInfo);
//                } else {
//                    invalidResponseHandler.Callback(httpResponseInfo);
//                }
//            }
//        }, expectedResponseBodyFormat);
        return null;
    }

    public static void CreateHttpClientConnectionManager(String domain, String ip) {
        /* Custom DNS resolver */
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("my.host.com")) {
            /* If we match the host we're trying to talk to,
               return the IP address we want, not what is in DNS */
                    return new InetAddress[] { InetAddress.getByName("127.0.0.1") };
                } else {
            /* Else, resolve it as we would normally */
                    return super.resolve(host);
                }
            }
        };

/* HttpClientConnectionManager allows us to use custom DnsResolver */
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
    /* We're forced to create a SocketFactory Registry.  Passing null
       doesn't force a default Registry, so we re-invent the wheel. */
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null, /* Default ConnectionFactory */
                null, /* Default SchemePortResolver */
                dnsResolver  /* Our DnsResolver */
        );

    }
}
