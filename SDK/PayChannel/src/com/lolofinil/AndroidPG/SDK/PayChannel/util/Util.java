package com.lolofinil.AndroidPG.SDK.PayChannel.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.RawRes;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Henry on 2/26/2016.
 */
public class Util {
    private static String tag = Util.class.getSimpleName();
    // 字符串是否为null或""
    public static boolean StringIsNullOrEmpty(String str)
    {
        return (str==null || str.equals(""));
    }

    public static JsonObject StringToJsonObj(String jsonStr) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonStr).getAsJsonObject();
    }

    public static String ObjToJsonStr(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static JSONObject StringToJSONObj(String jsonStr) throws JSONException {
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONObject jsonObj = (JSONObject)jsonTokener.nextValue();
        return jsonObj;
    }

    public static JSONArray StringToJSONAry(String jsonStr) throws JSONException {
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONArray jsonAry = (JSONArray)jsonTokener.nextValue();
        return jsonAry;
    }

    public static String LoadJsonStringFromRawRes(Context context, @RawRes int redId) throws IOException {
        InputStream is = context.getResources().openRawResource(redId);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, n);
        }
        is.close();
        return writer.toString();
    }

    public static String GetStackTraceString(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public static String GetStringResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public static void ShowToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void ShowAlert(Context context, String content) {
        Log.i(tag, "ShowAlert called");
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
