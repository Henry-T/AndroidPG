package com.lolofinil.AndroidPG.WebView.AutoCloseActivityWhenGoBack;

import android.net.Uri;
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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;
import android.content.Intent;

public class WebViewActivity extends Activity {
    public static String tag = WebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.addJavascriptInterface(new Object(){
                @JavascriptInterface
                public void reqCloseActivity() {
                    // NEXT  
                    finish();
                }

                @JavascriptInterface
                public String getParam1() {
                    // NEXT 
                    return "file:///android_asset/ExampleThirdPartyPage.html";
                }
            }, "androidInterface");
        }
        // html content mode won't have history, so I put it in local html page 
        webView.loadUrl("file:///android_asset/AutoCloseOnBack.html");
    }
}
