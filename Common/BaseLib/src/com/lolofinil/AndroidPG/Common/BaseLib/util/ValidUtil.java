package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Henry on 9/5/2016.
 */
public class ValidUtil {
    private static String tag = ValidUtil.class.getSimpleName();

    public static boolean ValidStringFormat(String str, EStringFormat expectedStringFormat) {
        if (expectedStringFormat == EStringFormat.NotSet) {
            return true;
        } else if (expectedStringFormat == EStringFormat.Integer) {
            try {
                long longValue = Long.valueOf(str);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        } else if (expectedStringFormat == EStringFormat.JSON) {
            JsonParser jsonParser = new JsonParser();
            try {
                JsonElement jsonEle= jsonParser.parse(str);
                return true;
            } catch (JsonSyntaxException ex) {
                return false;
            }
        } else {
            Log.w(tag, "ResponseBodyFormatValidation unimplemented for type:"+expectedStringFormat.name());
            new Exception().printStackTrace();
            return false;
        }
    }
}
