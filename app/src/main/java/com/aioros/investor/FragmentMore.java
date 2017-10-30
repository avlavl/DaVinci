package com.aioros.investor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.aioros.investor.R.id.webView;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentMore extends BaseFragment {
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        mWebView = (WebView) view.findViewById(webView);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //WebView加载web资源
        mWebView.loadUrl("https://www.jisilu.cn/question/15659");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_MORE;
    }
}
