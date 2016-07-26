package com.henryt.LifeCycle;

import android.app.AlarmManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import android.widget.RelativeLayout;
import android.widget.Button;
import android.view.View;

import org.json.JSONObject;
import org.json.JSONException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyActivity extends Activity {
	public static String tag = MyActivity.class.getSimpleName();
	public static boolean Initialized = false;
    public static MyActivity STATIC_REF = null;
	
	public MyActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
        STATIC_REF = this;

        // to keep screen on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.i(tag, "Initialized: "+Initialized);

        Initialized = true;

        Button myButton = new Button(this);
        RelativeLayout myLayout = new RelativeLayout(this);
        myLayout.addView(myButton);
        setContentView(myLayout);


		myButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent mStartActivity = new Intent(MyActivity.this, MyActivity.class);
				int mPendingIntentId = 84174541;
				PendingIntent mPendingIntent = PendingIntent.getActivity(MyActivity.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager mgr = (AlarmManager)MyActivity.this.getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
				System.exit(0);
			}
		});
    }

    public static void test1() {
		System.exit(0);
		Intent mStartActivity = new Intent(STATIC_REF, MyActivity.class);
		int mPendingIntentId = 84174541;
		PendingIntent mPendingIntent = PendingIntent.getActivity(STATIC_REF, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager mgr = (AlarmManager)STATIC_REF.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
    }

    public static void test2() {
    	System.exit(0);
    	Intent i =new Intent(STATIC_REF.getBaseContext(), MyActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	STATIC_REF.startActivity(i);
    }
}