package com.lolofinil.AndroidPG.Common.BaseLib.util;

import android.util.Log;

public class HttpResponseInfo {
	private static String tag = HttpResponseInfo.class.getSimpleName();

	public EHttpResponseStatus Status;
	public int InternalErrorCode;
	public String InternalErrorMsg = "";
	public String Content = "";

	public void Dump() {
		Log.i(tag, "Status:"+Status);
		Log.i(tag, "InternalErrorCode:"+InternalErrorCode);
		Log.i(tag, "Content:");
		Log.i(tag, "Content:"+(Content==null?"":Content));
		Log.i(tag, "InternalErrorMsg:"+(InternalErrorMsg==null?"":InternalErrorMsg));
	}
}
