package com.lolofinil.AndroidPG.Structure.InitializeOptimize;

import android.app.ProgressDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

public class ModuleNeedInit extends Activity {
    private static String tag = MainActivity.class.getSimpleName();

    private boolean syncInitialized = false;
    private boolean asyncInitialized = false;

    private long asyncInitMilliSeconds = 8000;
    private long timeoutMilliSeconds = 6000;

    public void SetAsyncInitMilliSeconds(long milli) {
        asyncInitMilliSeconds = milli;
    }

    public void SetTimeoutMilliSeconds(long milli) {
        timeoutMilliSeconds = milli;
    }
    
    public void Initialize() {
        Log.i(tag, "sync initialized");
        initializeSyncPart();
        initializeAsyncPart();
    }

    public void Deinitialize(Context context) {
        syncInitialized = false;
        asyncInitialized = false;
        Toast.makeText(context, "Deinitialize done!", Toast.LENGTH_SHORT).show();
    }

    public boolean Initialized() {
        return syncInitialized && asyncInitialized;
    }


    public boolean SyncInitialized() {
        return syncInitialized;
    }

    public boolean AsyncInitialized() {
        return asyncInitialized;
    }

    private void initializeSyncPart() {
        syncInitialized = true;
    }

    private void initializeAsyncPart() {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                asyncInitialized = true;
            }
        };
        timerHandler.postDelayed(timerRunnable, asyncInitMilliSeconds);
    }

    public void API_DependsOnSyncInit(Context context) {
        if (syncInitialized) 
            Toast.makeText(context, "API_DependsOnSyncInit call succeed", Toast.LENGTH_SHORT).show();
        else 
            Toast.makeText(context, "API_DependsOnSyncInit call failed", Toast.LENGTH_SHORT).show();
    }

    public void SyncAPI_DependsOnAsyncInit(Context context) {
        if (asyncInitialized)
            Toast.makeText(context, "SyncAPI_DependsOnAsyncInit call succeed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "SyncAPI_DependsOnAsyncInit call failed", Toast.LENGTH_SHORT).show();
    }

    public void AsyncAPI_DependsOnAsyncInit(final Context context) {
        waitUntilAsyncInit(context, timeoutMilliSeconds, new IActionHandler(){
            public void Callback() {
                Toast.makeText(context, "AsyncAPI_DependsOnAsyncInit call succeed", Toast.LENGTH_SHORT).show();
            }
            public void TimedoutCallback() {
                Toast.makeText(context, "AsyncAPI_DependsOnAsyncInit call failed - init timedout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void waitUntilAsyncInit(Context context, final long timeoutMilliSeconds, final IActionHandler onInitHandler) {
        final long startTime = System.currentTimeMillis();
        final Handler timerHandler = new Handler();
        final ProgressDialog dialog = ProgressDialog.show(context, "Initializing", "Please wait...", true);
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                long deltaTime = System.currentTimeMillis()-startTime;
                if (asyncInitialized) {
                    onInitHandler.Callback();
                    dialog.dismiss();
                } else if (deltaTime > timeoutMilliSeconds) {
                    onInitHandler.TimedoutCallback();
                    dialog.dismiss();
                } else {
                    timerHandler.postDelayed(this, 500);
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private interface IActionHandler {
        void Callback();
        void TimedoutCallback();
    }
}
