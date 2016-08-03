package com.lolofinil.AndroidPG.Structure.InitializeOptimize;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.content.Context;

public class MainActivity extends Activity {
    private static String tag = MainActivity.class.getSimpleName();

    private ModuleNeedInit moduleNeedInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moduleNeedInit = new ModuleNeedInit();

        Button btnInitialize = (Button)findViewById(R.id.btnInitialize);
        Button btnAPIDependsSyncInit = (Button)findViewById(R.id.btnAPIDependsSyncInit);
        Button btnAPIDependsAsyncInit = (Button)findViewById(R.id.btnAPIDependsAsyncInit);

        btnInitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNeedInit.Initialize();
            }
        });
        btnAPIDependsSyncInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNeedInit.APIDependsOnSyncInit(MainActivity.this);
            }
        });
        btnAPIDependsAsyncInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleNeedInit.Initialized())
                    moduleNeedInit.APIDependsOnAsyncInit(MainActivity.this);
                else
                    Toast.makeText(MainActivity.this, "async init not finished", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
