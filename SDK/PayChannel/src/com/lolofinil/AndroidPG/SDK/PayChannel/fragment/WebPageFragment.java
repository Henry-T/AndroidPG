package com.lolofinil.AndroidPG.SDK.PayChannel.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lolofinil.AndroidPG.SDK.PayChannel.util.IActionHandler;
import com.lolofinil.AndroidPG.SDK.PayChannel.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebPageFragment extends Fragment {
    public static String ARG_URL_STR = "yougubaselib_webpage_url_str";
    public static String ARG_PARAM_STR = "yougubaselib_webpage_param_str";
    public static String ARG_IS_POST = "yougubaselib_webpage_ispost";
    public static String ARG_HTML_CONTENT = "yougubaselib_webpage_html_content";

    private static String tag = WebPageFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String urlStr;
    private String paramsStr;
    private boolean isPost;
    private String htmlContent;

    private WebView mWebView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    //  * @param uri Parameter 1.
     * @return A new instance of fragment PayPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebPageFragment newInstance(String urlStr, String paramsStr, boolean isPost, String htmlContent) {
        WebPageFragment fragment = new WebPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL_STR, urlStr);
        args.putString(ARG_PARAM_STR, paramsStr);
        args.putBoolean(ARG_IS_POST, isPost);
        args.putString(ARG_HTML_CONTENT, htmlContent);
        fragment.setArguments(args);
        return fragment;
    }

    public WebPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlStr = getArguments().getString(ARG_URL_STR);
            paramsStr = getArguments().getString(ARG_PARAM_STR);
            isPost = getArguments().getBoolean(ARG_IS_POST);
            htmlContent = getArguments().getString(ARG_HTML_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getResources().getIdentifier("fragment_web_page", "layout", getActivity().getPackageName()), container, false);

        // final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "请稍等...");

        mWebView = (WebView) v.findViewById(getResources().getIdentifier("webView", "id", getActivity().getPackageName()));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin:") || url.startsWith("mqqapi:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (dialog != null && dialog.isShowing())
//                    dialog.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.addJavascriptInterface(new Object(){
                @JavascriptInterface
                public void reqCloseActivity() {
                    if (reqCloseActivityHandler != null)
                        reqCloseActivityHandler.Callback();
                }

                @JavascriptInterface
                public String getParam1() {
                    return androidInterfaceParam1;
                }
            }, "androidInterface");
        }

        if(Util.StringIsNullOrEmpty(urlStr))
        {
            Log.i(tag, "htmlContent:"+htmlContent);
            mWebView.loadDataWithBaseURL("", htmlContent, "text/html", "UTF-8", "");
        }
        else
        {
            if (isPost) {
                mWebView.postUrl(urlStr, paramsStr.getBytes());
            } else {
                if (TextUtils.isEmpty(paramsStr))
                    mWebView.loadUrl(urlStr);
                else
                    mWebView.loadUrl(urlStr+"?"+paramsStr);
            }
        }

        return v;
    }

    private IActionHandler reqCloseActivityHandler;
    public void SetOnReqCloseActivityHandler(IActionHandler actionHandler) {
        reqCloseActivityHandler = actionHandler;
    }

    private String androidInterfaceParam1 = "";
    public void SetAndroidInterfaceParam1(String param1) {
        androidInterfaceParam1 = param1;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
