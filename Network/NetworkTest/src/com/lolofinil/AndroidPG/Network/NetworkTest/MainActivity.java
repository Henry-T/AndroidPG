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

import com.lolofinil.AndroidPG.Common.BaseLib.util.HttpRequestTask;
import com.lolofinil.AndroidPG.Common.BaseLib.util.HttpResponseInfo;
import com.lolofinil.AndroidPG.Common.BaseLib.util.IHttpRequestHandler;
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
                HttpRequestTask httpRequestTask = new HttpRequestTask(new IHttpRequestHandler() {
                    @Override
                    public void Callback(HttpResponseInfo httpResponseInfo) {
                        httpResponseInfo.Dump();
                    }
                });
                httpRequestTask.execute("http://www.lolofinil2.com");
            }
        });
    }
}
