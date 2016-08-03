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
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
    private static String tag = MainActivity.class.getSimpleName();

    private ModuleNeedInit moduleNeedInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moduleNeedInit = new ModuleNeedInit();

        final TextView tv_asyncInitSecouds = (TextView)findViewById(R.id.tv_asyncInitSecouds);
        final TextView tv_timeoutSecouds = (TextView)findViewById(R.id.tv_timeoutSecouds);
        SeekBar seekBar_asyncInitSeconds = (SeekBar)findViewById(R.id.seekBar_asyncInitSeconds);
        SeekBar seekBar_timeoutSeconds = (SeekBar)findViewById(R.id.seekBar_timeoutSeconds);
        
        Button btnInitialize = (Button)findViewById(R.id.btnInitialize);
        Button btnAPI_DependsSyncInit = (Button)findViewById(R.id.btnAPI_DependsSyncInit);
        Button btnSyncAPI_DependsAsyncInit = (Button)findViewById(R.id.btnSyncAPI_DependsAsyncInit);
        Button btnAsyncAPI_DependsAsyncInit = (Button)findViewById(R.id.btnAsyncAPI_DependsAsyncInit);
        Button btnDeinitialize = (Button)findViewById(R.id.btnDeinitialize);

        seekBar_asyncInitSeconds.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                 @Override
                 public void onStopTrackingTouch(SeekBar seekBar) {
                 }
                 @Override
                 public void onStartTrackingTouch(SeekBar seekBar) {
                 }
                 @Override
                 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tv_asyncInitSecouds.setText("InitTime:"+String.valueOf(progress));
                    moduleNeedInit.SetAsyncInitMilliSeconds(progress*1000);
                 }
             });

        seekBar_timeoutSeconds.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                 @Override
                 public void onStopTrackingTouch(SeekBar seekBar) {
                 }
                 @Override
                 public void onStartTrackingTouch(SeekBar seekBar) {
                 }
                 @Override
                 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tv_timeoutSecouds.setText("Timeout:"+String.valueOf(progress));
                    moduleNeedInit.SetTimeoutMilliSeconds(progress*1000);
                 }
             });

        btnInitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNeedInit.Initialize();
            }
        });
        btnAPI_DependsSyncInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNeedInit.API_DependsOnSyncInit(MainActivity.this);
            }
        });
        btnSyncAPI_DependsAsyncInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleNeedInit.Initialized())
                    moduleNeedInit.SyncAPI_DependsOnAsyncInit(MainActivity.this);
                else
                    Toast.makeText(MainActivity.this, "async init not finished", Toast.LENGTH_SHORT).show();
            }
        });
        btnAsyncAPI_DependsAsyncInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNeedInit.AsyncAPI_DependsOnAsyncInit(MainActivity.this);
            }
        });

        btnDeinitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNeedInit.Deinitialize(MainActivity.this);
            }
        });
    }
}
