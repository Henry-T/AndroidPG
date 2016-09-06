package com.lolofinil.AndroidPG.Network.NetworkTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.content.DialogInterface;

import com.lolofinil.AndroidPG.Common.BaseLib.util.EStringFormat;
import com.lolofinil.AndroidPG.Common.BaseLib.util.HttpRequestAgent;
import com.lolofinil.AndroidPG.Common.BaseLib.util.HttpRequestTask;
import com.lolofinil.AndroidPG.Common.BaseLib.util.HttpResponseInfo;
import com.lolofinil.AndroidPG.Common.BaseLib.util.IHttpRequestHandler;
import com.lolofinil.AndroidPG.Common.BaseLib.util.Util;

import java.net.HttpRetryException;
// import com.lolofinil.AndroidPG

public class MainActivity extends Activity {
    private static String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHello = (Button)findViewById(R.id.btnHello);

        btnHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HttpRequestTask httpRequestTask = new HttpRequestTask(new IHttpRequestHandler() {
//                    @Override
//                    public void Callback(HttpResponseInfo httpResponseInfo) {
//                        httpResponseInfo.Dump();
//                    }
//                });
//                httpRequestTask.execute("http://www.lolofinil2.com");

                HttpRequestAgent httpRequestAgent = new HttpRequestAgent(MainActivity.this, null, null, EStringFormat.JSON, new IHttpRequestHandler() {
                    @Override
                    public void Callback(HttpResponseInfo httpResponseInfo) {
                        Log.i(tag, httpResponseInfo.Content);
                    }
                }, new IHttpRequestHandler() {
                    @Override
                    public void Callback(HttpResponseInfo httpResponseInfo) {
                        Log.i(tag, httpResponseInfo.Content);
                        String msg = "Status: " + httpResponseInfo.Status.name()+"\n";
                        if (TextUtils.isEmpty(httpResponseInfo.Content)) {
                            msg += "Content: <empty>";
                        } else {
                            msg += "Content: " + httpResponseInfo.Content.subSequence(0, 50>httpResponseInfo.Content.length()-1?httpResponseInfo.Content.length()-1:50);
                        }
                        Util.ShowAlert(MainActivity.this, msg);
                    }
                });

                httpRequestAgent.execute("http://sdk.apowogame.com/api/gettimestamp");
//                httpRequestAgent.execute("http://www.lolofinil.com");
            }
        });
    }
}
