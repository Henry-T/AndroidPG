package com.lolofinil.relaunch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Relaunch(MainActivity.this);
            }
        });
    }

    public static void Relaunch(Activity context) {
        Log.i(tag, "Relaunch called");

        Log.i(tag, "Activity.finish before call");
        context.finish();
        Log.i(tag, "Activity.finish called");

        Log.i(tag, "Intent before register");
        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        Log.i(tag, "Intent registered");

        Log.i(tag, "killProcess before call");
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        Log.i(tag, "killProcess called");

        Log.i(tag, "System.exit before call");
        System.exit(0);
        Log.i(tag, "System.exit called");
    }
}
