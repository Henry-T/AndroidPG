package com.lolofinil.AndroidPG.WebView.AutoCloseActivityWhenGoBack;

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
import android.content.Intent;

public class MainActivity extends Activity {
    public static String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowWebViewActivity = (Button)findViewById(R.id.btn_show_webviewactivity);
        btnShowWebViewActivity.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
	            	startActivity(intent);
	            }
	        }
        );
    }
}
