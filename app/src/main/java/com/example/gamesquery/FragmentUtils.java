package com.example.gamesquery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;

public class FragmentUtils {
    /**
     * 设置WebView
     */
    public static void setWebView(final MyWebView webView, WebSettings settings, final Activity activity, final String hidehtml,
                                  final ProgressBar progressBar, String URI, final LinearLayout linearLayout, final ImageView imageView,
                                  final AnimationDrawable animationDrawable, final LinearLayout linearLayout2) {

        webView.setDrawingCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setVisibility(View.GONE);
//                linearLayout2.setVisibility(View.VISIBLE);
//                animationDrawable.start();
                if (url.contains("qqjyo")) {
                    webView.stopLoading();
                    Log.i("拦截返回", "");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("http:") || url.startsWith("https:")) {
//                    view.loadUrl(url);
//                    return true;
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return false;
//                }
                if (url.contains("qqjyo") || url.contains("jd") || url.contains("changba") || url.contains("jd") || url.contains("uc") || url.contains("tb") || url.contains("alicdn")) {
                    webView.stopLoading();
                    Log.i("拦截返回", "");
                    return true;
                } else if (!(url.startsWith("http:") || url.startsWith("https:"))) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        activity.startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }
//                if (url == null) return false;

                //处理http和https开头的url
//                if (url.startsWith("http:") || url.startsWith("https:")) {
//
//                    return false;
//                }
//
//                WebView.HitTestResult hitTestResult = view.getHitTestResult();


//                //其他自定义的scheme
//                try {
//                    //hitTestResult==null解决重定向问题
//                    if (!TextUtils.isEmpty(url) && hitTestResult == null) {
//                        view.loadUrl(url);
//                        return true;
//                    } else if (url.equals("https://llq.lvezqr.cn/safe/liulanqi/2019.html?pkg=18") || url.equals("http://activity.qqjyo.com/activity/index?id=13786&slotId=192586&login=normal&appKey=3QJy6gDkqzYLVsdVwTQ4ofDL1A8v&deviceId=9dec6f17-57f2-4243-b40e-06e79670b915&dsm=1.192586.0.0&dsm2=1.192586.2.13786&tenter=SOW&subActivityWay=1&tck_rid_6c8=0ad02462jyu047a3-18143371&tck_loc_c5d=tactivity-13786&dcm=401.192586.0.0&&tenter=SOW&specialType=0&userType=1&isTestActivityType=0&visType=0&ipUaMd5=c12a1b4ceb35641e1a4fda22f260a52f&")) {
//                        return true;
//                    } else if (url.startsWith("https:")) {
//                        webView.loadUrl(url);
//                        return true;
//                    } else {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        activity.startActivity(intent);
//                        return false;
//                    }
//                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
//                    return false;
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //网页元素过滤
                view.loadUrl(hidehtml);
                linearLayout.setVisibility(View.GONE);
//                linearLayout2.setVisibility(View.GONE);
//                animationDrawable.stop();
                webView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String s, String s1) {
                super.onReceivedError(webView, errorCode, s, s1);
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    webView.loadUrl("about:blank"); // 避免出现默认的错误界面
                    // 在这里显示自定义错误页
                    webView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.GONE);
                }
            }
//
//            @Override
//            public void onReceivedError(WebView webView, WebResourceRequest
//                    webResourceRequest, WebResourceError webResourceError) {
//                super.onReceivedError(webView, webResourceRequest, webResourceError);
//                webView.setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);
//                linearLayout2.setVisibility(View.GONE);
//            }
        });

        webView.setOnDrawListener(new MyWebView.OnDrawListener() {
            @Override
            public void onDrawCallBack() {
                webView.loadUrl(hidehtml);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            View fullScreenView;
            WindowManager windowManager = activity.getWindowManager();

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //网页元素过滤
                view.loadUrl(hidehtml);
                //网页加载进度条
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    webView.getSettings().setBlockNetworkImage(false);

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }

            }

            //视频全屏设置
            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                //横屏设置
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                // 此处的 view 就是全屏的视频播放界面，需要把它添加到我们的界面上
                windowManager.addView(view, new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION));
                // 去除状态栏和导航按钮
                fullScreen(view);
                fullScreenView = view;
                Log.i("onShowCustomView", "onShowCustomView: ");

            }

            //视频退出全屏设置
            @Override
            public void onHideCustomView() {
                //竖屏设置
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                // 退出全屏播放，我们要把之前添加到界面上的视频播放界面移除
                windowManager.removeViewImmediate(fullScreenView);
                fullScreenView = null;
            }
        });
        webView.loadUrl(URI);


        //调用其它浏览器下载文件
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(s);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(i);
            }
        });

        settings = webView.getSettings();
        //允许WebView混合加载http和https的网页内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        settings.setBlockNetworkImage(true);
        //允许WebView启用js脚本
        settings.setJavaScriptEnabled(true);
        //打开WebView页面缩放功能
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);


    }


    /**
     * 设置视频全屏，隐藏屏幕上显示的View
     *
     * @param view
     */
    public static void fullScreen(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * 设置WebView滚动按钮
     *
     * @param webView
     */
    public static void setScrollBar(WebView webView) {
//        //禁用滑动按钮
//        if (webView.getX5WebViewExtension() != null) {
////            webView.getX5WebViewExtension().setHorizontalScrollBarEnabled(false);//水平不显示滚动按钮
////            webView.getX5WebViewExtension().setVerticalScrollBarEnabled(false); //垂直不显示滚动按钮
//            webView.getX5WebViewExtension().setScrollBarFadingEnabled(false);
//        } else {
////            webView.setHorizontalScrollBarEnabled(false);//水平不显示
////            webView.setVerticalScrollBarEnabled(false); //垂直不显示
//    }
    }

    /**
     * 设置SwipeRefreshLayout
     */
    public static void setSwipeRefreshLayout(final SwipeRefreshLayout swipeRefreshLayout, final MyWebView webView, final ProgressBar progressBar) {
        swipeRefreshLayout.setColorSchemeResources(R.color.text_selected);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        //网页加载进度条
                        if (newProgress == 100) {
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(newProgress);
                        }

                    }
                });

            }


        });
    }

    /**
     * 设置刷新按钮单击事件
     *
     * @param button
     * @param webView
     */
    public static void setErrorReload(Button button, final WebView webView) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
    }

    /**
     * 判断网络状态调用不同的WebView缓存方式
     */
    public static void JudgStatus(Context context, WebSettings settings, MyWebView webView) {
//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
//        if (activeNetworkInfo == null) {
        settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//            settings = webView.getSettings();
//            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        }
    }

    /**
     * 删除WebView页面缓存
     */
    public static void deleteWebCache(SharedPreferences preferences, Context context, MyWebView webView) {
        preferences = context.getSharedPreferences("content", MODE_PRIVATE);
        String delete = preferences.getString("delete", "");
        if (delete.equals("delete")) {
            webView.clearCache(true);
        }
    }

    /**
     * Html内容转码
     *
     * @param preferences
     * @param context
     * @param value
     * @return
     */
    public static String setHtmlTranscoding(SharedPreferences preferences, Context context, String value, String key) {
        preferences = context.getSharedPreferences("content", Context.MODE_PRIVATE);
        String s = preferences.getString(key, "");

        try {
            value = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
