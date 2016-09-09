package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.conn.DnsResolver;

/**
 * Created by Henry on 9/7/2016.
 */
public class CaresDnsResolver implements DnsResolver {
    private static String tag = CaresDnsResolver.class.getSimpleName();

    public CaresDnsResolver(String dns) {
        this.dns = dns;
    }

    private String dns = "";

    @Override
    public InetAddress[] resolve(String s) throws UnknownHostException {
        boolean ok = caresResolve(dns, s);
        Log.i(tag, "caresResolve call:"+ok);
        return new InetAddress[0];
    }

    public native boolean caresResolve(String dns, String domain);
    public void cares_onResolved(String info){
        Log.i(tag, "cares resolve result:"+info);
    }

    public native String stringFromJNI();

}
