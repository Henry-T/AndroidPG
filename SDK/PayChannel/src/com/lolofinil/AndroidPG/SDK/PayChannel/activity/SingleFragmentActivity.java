package com.lolofinil.AndroidPG.SDK.PayChannel.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


/**
 * Created by Henry on 2/22/2016.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    protected  int getLayoutResId() {
<<<<<<< HEAD
        return getResources().getIdentifier("activity_single_fragment", "layout", getPackageName());
=======
        return getResources().getIdentifier("activity_apowobaselib_single_fragment", "layout", getPackageName());
>>>>>>> f2cfa14a66e986e4ed2d9677aecbc67db5cc9315
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BeforeSetContentView();
        setContentView(getLayoutResId());
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(getResources().getIdentifier("fragmentContainer", "id", getPackageName()));

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction().add(getResources().getIdentifier("fragmentContainer", "id", getPackageName()), fragment).commit();
        }
    }

    protected abstract void BeforeSetContentView();
}
