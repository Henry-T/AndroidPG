package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderIterator;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.config.RegistryBuilder;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.conn.DnsResolver;
import cz.msebera.android.httpclient.conn.HttpClientConnectionManager;
import cz.msebera.android.httpclient.conn.socket.ConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.socket.PlainConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.SSLConnectionSocketFactory;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.impl.conn.BasicHttpClientConnectionManager;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Henry on 9/5/2016.
 */
public class NetworkUtil {
    private static String tag = NetworkUtil.class.getSimpleName();

    public static HttpResponseInfo RequestWithApacheHttpClient(String url, String reservedDNS, String preresolvedHost) {
        // todo note HttpClientBuilder
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClientConnectionManager connManager = null;
        if (!TextUtils.isEmpty(reservedDNS)) {
            connManager = NetworkUtil.CreateHttpClientConnectionManagerWithCustomDNSServer(reservedDNS);
        } else if (!TextUtils.isEmpty(preresolvedHost)) {
            connManager = NetworkUtil.CreateHttpClientConnectionManagerWithForceIP(preresolvedHost);
        }
        if (connManager!=null)
            builder.setConnectionManager(connManager);

        int DefaultTimeout = 5000;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DefaultTimeout)
                .setSocketTimeout(DefaultTimeout)
                .setConnectionRequestTimeout(DefaultTimeout).build();
        builder.setDefaultRequestConfig(requestConfig);

        HttpClient httpClient = builder.build();

        HttpResponse response;
        Long startTime = (long) 0;
        HttpResponseInfo responseInfo = new HttpResponseInfo();
        try {
            Log.i(tag, "Http 请求:" + url);
            HttpGet httpGet = new HttpGet(url);


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

                // todo note  content may be empty with this approach
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

    private static DnsResolver createDNSResolverWithDnsServer(String dnsServer) {
        return new CaresDnsResolver(dnsServer);
    }

    private static DnsResolver createDNSResolverWithForceIP(final String ip) {
        // todo note InMemoryDnsResolver with custom HttpClientConnectionManager
        DnsResolver dnsResolver = new DnsResolver() {
            @Override
            public InetAddress[] resolve(String s) throws UnknownHostException {
                InetAddress addr = Inet4Address.getByName(ip);
                return new InetAddress[]{addr};
            }
        };
        return dnsResolver;
    }

    private static HttpClientConnectionManager createHttpClientConnectionManager(DnsResolver dnsResolver) {
        // todo note create custom HttpClientConnectionManager
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
        return connManager;
    }

    public static HttpClientConnectionManager CreateHttpClientConnectionManagerWithCustomDNSServer(String dnsServer) {
        DnsResolver dnsResolver = createDNSResolverWithDnsServer(dnsServer);
        return createHttpClientConnectionManager(dnsResolver);
    }

    public static HttpClientConnectionManager CreateHttpClientConnectionManagerWithForceIP( String ip) {
        DnsResolver dnsResolver = createDNSResolverWithForceIP(ip);
        return createHttpClientConnectionManager(dnsResolver);
    }
}
