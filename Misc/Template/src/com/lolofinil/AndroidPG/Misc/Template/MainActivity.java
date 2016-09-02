package com.lolofinil.AndroidPG.Misc.Tempate;

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
import android.util.Log;
import android.content.DialogInterface;

public class MainActivity extends Activity {
    private static String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHello = (Button)findViewById(R.id.btnHello);

        btnHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "hello world!";
                Log.i(tag, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("AlertDialog")
                    .setMessage(msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                        }
                    }).show();
            }
        });
    }
}
