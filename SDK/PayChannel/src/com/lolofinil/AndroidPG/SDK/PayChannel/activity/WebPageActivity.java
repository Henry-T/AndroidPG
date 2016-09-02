package com.lolofinil.AndroidPG.SDK.PayChannel.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.lolofinil.AndroidPG.SDK.PayChannel.fragment.WebPageFragment;

public class WebPageActivity extends SingleFragmentActivity {
    public static String EXTRA_URL_STR = "yougubaselib_webpage_url_str";
    public static String EXTRA_PARAM_STR = "yougubaselib_webpage_param_str";
    public static String EXTRA_IS_POST = "yougubaselib_webpage_ispost";
    public static String EXTRA_HTML_CONTENT = "yougubaselib_webpage_html_content";

    public static Intent newIntent(Context context, String urlStr, String paramsStr, String htmlContent, boolean isPost) {
        Intent intent = new Intent(context, WebPageActivity.class);
        intent.putExtra(EXTRA_URL_STR, urlStr);
        intent.putExtra(EXTRA_PARAM_STR, paramsStr);
        intent.putExtra(EXTRA_IS_POST, isPost);
        intent.putExtra(EXTRA_HTML_CONTENT, htmlContent);
        return intent;

    }

    @Override
    protected Fragment createFragment() {
        String urlStr = getIntent().getStringExtra(EXTRA_URL_STR);
        String paramsStr = getIntent().getStringExtra(EXTRA_PARAM_STR);
        boolean isPost = getIntent().getBooleanExtra(EXTRA_IS_POST, false);
        String htmlContent = getIntent().getStringExtra(EXTRA_HTML_CONTENT);
        return WebPageFragment.newInstance(urlStr, paramsStr, isPost, htmlContent);
    }

    @Override
    protected void BeforeSetContentView() {

    }
}

