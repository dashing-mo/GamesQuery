package com.example.gamesquery;

import android.app.Application;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.x;

/**
 * 自定义Application
 */
public class BaseApplication extends Application {
    public MyWebView wv1;
    public static final String URI1 = "https://wap.gamersky.com";

    @Override

    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        initX5();
        wv1 = new MyWebView(getApplicationContext());
        setWebView(wv1, URI1);


    }

    private void initX5() {
        //        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                // TODO Auto-generated method stub
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.d("app", " onViewInitFinished is " + arg0);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//                // TODO Auto-generated method stub
//            }
//        };
//        //流量下载内核
//        QbSdk.setDownloadWithoutWifi(true);
//        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }

    /**
     * 设置WebView
     */
    private void setWebView(MyWebView webView, final String url) {

        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //网页元素过滤
                view.loadUrl("javascript:function setTop(){" +
                        "document.querySelector('.ymw-header2018').style.display='none';" +
                        "document.querySelector('.ymw-footer').style.display='none';" +
                        "document.querySelector('.yu-btn-wrap').style.display='none';" +
                        "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
                        "document.querySelector('.ymw-search-res-nav ymw-search-res-nav2018').style.display='none';" +
                        "document.getElementsByTagName('body')[0].innerHTML;" +
                        "document.getElementsByTagName('div')[7].style.display='none';" +
                        "document.getElementsByClassName('appDownloadTip')[0].style.display='none';" +
                        "document.getElementById('gsTgWapZPCbtn').style.display='none';" +
                        "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
                        "document.getElementsByClassName('ymw-rel-list')[0].style.display='none';" +
                        "document.getElementsByClassName('ymw-rel-mgame')[0].style.display='none';" +
                        "}" +
                        "setTop();");
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(url);
    }
}
