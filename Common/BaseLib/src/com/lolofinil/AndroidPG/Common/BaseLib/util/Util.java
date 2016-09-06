package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Henry on 9/5/2016.
 */
public class Util {

    public static void ShowAlert(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(content);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}
