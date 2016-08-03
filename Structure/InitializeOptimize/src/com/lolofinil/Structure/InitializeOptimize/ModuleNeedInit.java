package com.lolofinil.AndroidPG.Structure.InitializeOptimize;


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
    
    public void Initialize() {
        Log.i(tag, "sync initialized");
        initializeSyncPart();
        initializeAsyncPart();
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
        timerHandler.postDelayed(timerRunnable, 5000);
    }

    public void APIDependsOnSyncInit(Context context) {
        if (syncInitialized) 
            Toast.makeText(context, "APIDependsOnSyncInit call succeed", Toast.LENGTH_SHORT).show();
        else 
            Toast.makeText(context, "APIDependsOnSyncInit call failed", Toast.LENGTH_SHORT).show();
    }

    public void APIDependsOnAsyncInit(Context context) {
        if (asyncInitialized) 
            Toast.makeText(context, "APIDependsOnAsyncInit call succeed", Toast.LENGTH_SHORT).show();
        else 
            Toast.makeText(context, "APIDependsOnAsyncInit call failed", Toast.LENGTH_SHORT).show();
    }
}
