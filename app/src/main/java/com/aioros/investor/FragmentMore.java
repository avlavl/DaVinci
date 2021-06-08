package com.aioros.investor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import static com.aioros.investor.Constant.FRAGMENT_FLAG_MORE;
import static com.aioros.investor.R.id.webView;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentMore extends BaseFragment {
    public WebView mWebView;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        mWebView = (WebView) view.findViewById(webView);
        //WebView加载web资源
        mWebView.loadUrl("https://m.yicai.com");
        mWebView.reload();

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
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setBuiltInZoomControls(true);

        mTextView1 = (TextView) view.findViewById(R.id.textViewWeb1);
        mTextView1.setTextColor(Color.rgb(240, 160, 80));
        mTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView1.setTextColor(Color.rgb(240, 160, 80));
                mTextView2.setTextColor(Color.rgb(255, 255, 255));
                mTextView3.setTextColor(Color.rgb(255, 255, 255));
                mTextView4.setTextColor(Color.rgb(255, 255, 255));
                webTabOnClick(v, "https://m.yicai.com");
            }
        });
        mTextView2 = (TextView) view.findViewById(R.id.textViewWeb2);
        mTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView1.setTextColor(Color.rgb(255, 255, 255));
                mTextView2.setTextColor(Color.rgb(240, 160, 80));
                mTextView3.setTextColor(Color.rgb(255, 255, 255));
                mTextView4.setTextColor(Color.rgb(255, 255, 255));
                webTabOnClick(v, "https://www.jisilu.cn/data/new_stock/#hkipo");
            }
        });
        mTextView3 = (TextView) view.findViewById(R.id.textViewWeb3);
        mTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView1.setTextColor(Color.rgb(255, 255, 255));
                mTextView2.setTextColor(Color.rgb(255, 255, 255));
                mTextView3.setTextColor(Color.rgb(240, 160, 80));
                mTextView4.setTextColor(Color.rgb(255, 255, 255));
                webTabOnClick(v, "http://www.cffex.com.cn");
            }
        });
        mTextView4 = (TextView) view.findViewById(R.id.textViewWeb4);
        mTextView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView1.setTextColor(Color.rgb(255, 255, 255));
                mTextView2.setTextColor(Color.rgb(255, 255, 255));
                mTextView3.setTextColor(Color.rgb(255, 255, 255));
                mTextView4.setTextColor(Color.rgb(240, 160, 80));
                webTabOnClick(v, "http://www.dapenti.com");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = FRAGMENT_FLAG_MORE;
    }

    private void webTabOnClick(View v, String url) {
        mWebView.loadUrl(url);
    }
}
