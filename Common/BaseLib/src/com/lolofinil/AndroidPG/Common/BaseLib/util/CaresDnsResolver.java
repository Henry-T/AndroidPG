package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.conn.DnsResolver;

/**
 * Created by Henry on 9/7/2016.
 */
public class CaresDnsResolver implements DnsResolver {
    private static String tag = CaresDnsResolver.class.getSimpleName();

    enum EAresResolveStatus {
        ARES_SUCCESS(0),
        ARES_ENOTIMP(5),
        ARES_EBADNAME(8),
        ARES_ENOTFOUND(4),
        ARES_ENOMEM(15),
        ARES_ECANCELLED(24),
        ARES_EDESTRUCTION(16);

        private final int id;
        EAresResolveStatus(int id) {
            this.id = id;
        }
        public int getValue() {
            return id;
        }

        static EAresResolveStatus[] values = values();
        public static EAresResolveStatus fromInt(int val) {
            for(int i=0; i<values.length; i++) {
                EAresResolveStatus value = values[i];
                if (value.getValue() == val)
                    return value;
            }
            return null;
        }
    }


    public CaresDnsResolver(String dns) {
        this.dns = dns;
    }

    private String dns = "";
    private String resolveResult = null;

    synchronized private String accessResolveResult(String val) {
        if (val != null){
            resolveResult = val;
        }
        return resolveResult;
    }

    @Override
    public InetAddress[] resolve(String s) throws UnknownHostException {
        boolean ok = caresResolve(dns, s);
        String resolveResult = null;
        while(true) {
            resolveResult = accessResolveResult(null);
            if (resolveResult != null) {
                break;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new UnknownHostException("dns resolve result format error: "+ resolveResult);
                }
            }
        }

//        resolveResult = ""; // test exception handling
        String[] stringAry = resolveResult.split(";");
        if (stringAry.length == 0) {
            Log.e(tag, "dns resolve result format error: "+ resolveResult);
            throw new UnknownHostException("dns resolve result format error: "+ resolveResult);
        } else {
            String statusCodeStr = stringAry[0];
            try {
                int statusCode = Integer.parseInt(statusCodeStr);
                if (statusCode == EAresResolveStatus.ARES_SUCCESS.getValue()) {
                    InetAddress[] addrs = new InetAddress[stringAry.length-1];
                    for(int i=1;i<stringAry.length;i++) {
                        InetAddress addr = Inet4Address.getByName(stringAry[i]);
                        addrs[i-1] = addr;
                    }
                    return addrs;
                } else {
                    EAresResolveStatus status = EAresResolveStatus.fromInt(statusCode);
                    if (status == null)
                        throw new UnknownHostException("CaresDnsResolver resolve failed. unknown statusCode:"+status);
                    else
                        throw new UnknownHostException("CaresDnsResolver resolve failed. status:"+status.name());
                }
            } catch (NumberFormatException ex) {
                // todo note custom command
                throw new UnknownHostException("CaresDnsResolver failed to parse status code in resolve result: "+resolveResult);
            }
        }
    }

    public native boolean caresResolve(String dns, String domain);
    public void cares_onResolved(String info){
        Log.i(tag, "cares resolve result:"+info);
        accessResolveResult(info);
    }

    public native String stringFromJNI();

}
