package com.lolofinil.AndroidPG.Misc.Disabuse;

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
import android.app.ProgressDialog;
import android.view.Gravity;

public class MainActivity extends Activity {
    public static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHello = (Button)findViewById(R.id.btnHello);

        btnHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test_Show2ProgressDialogSameTime();
            }
        });
    }

    private void Test_Show2ProgressDialogSameTime() {
        // this works fine, two ProgressDialog on screen
        final ProgressDialog dialog1 = ProgressDialog.show(this, "dialog1", "dialog1...", true);
        final ProgressDialog dialog2 = ProgressDialog.show(this, "dialog2", "dialog2...", true);
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
    }
}
