package com.lolofinil.AndroidPG.SDK.PayChannel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.lolofinil.AndroidPG.SDK.PayChannel.R;
import com.lolofinil.AndroidPG.SDK.PayChannel.util.EHttpResponseStatus;
import com.lolofinil.AndroidPG.SDK.PayChannel.util.HttpRequestTask;
import com.lolofinil.AndroidPG.SDK.PayChannel.util.HttpResponseInfo;
import com.lolofinil.AndroidPG.SDK.PayChannel.util.IHttpRequestHandler;
import com.lolofinil.AndroidPG.SDK.PayChannel.util.Util;

public class MainActivity extends Activity {
    private static String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHello = (Button)findViewById(R.id.btnHello);
        Button btnQQPay = (Button)findViewById(R.id.btnQQPay);

        btnHello.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "hello world!";
                Log.i(tag, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("AlertDialog")
                    .setMessage(msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                        }
                    }).show();
            }
        });

        btnQQPay.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                HttpRequestTask orderRequestTask = new HttpRequestTask(new IHttpRequestHandler() {
                    @Override
                    public void Callback(HttpResponseInfo httpResponseInfo) {
                        if (httpResponseInfo.Status == EHttpResponseStatus.Succeed)
                        {
                            JsonObject orderInfo = Util.StringToJsonObj(httpResponseInfo.Content);
                            String order = orderInfo.get("order").getAsString();
                            String url = orderInfo.get("url").getAsString();
                            Intent intent = WebPageActivity.newIntent(MainActivity.this, url, null, null, false);
                            startActivity(intent);
                        }
                    }
                });
                orderRequestTask.execute("http://node.lolofinil.com/unify_pay/create?title=test_title&desc=test_desc&price=1");
            }
        });
    }
}
