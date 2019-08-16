package com.example.gamesquery.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamesquery.BaseApplication;
import com.example.gamesquery.MyWebView;
import com.example.gamesquery.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class BaseWebFragment extends Fragment implements View.OnClickListener {
    private FrameLayout fl;
    private List<String> urlList;
    private int childCount = 0;
    private MyWebView mWeb;
    private String url;
    private LinearLayout ll_error;
    private ProgressBar pb_loading;
    private WindowManager manager;
    private View fullScreenView = null;
    private SwipeRefreshLayout refreshWeb;
    private Button btn_error;
    private WebSettings settings;
    private SharedPreferences preferences;
    private ImageView iv_loading;
    private LinearLayout ll_loading;
    private AnimationDrawable drawable;
    // 用来计算返回键的点击间隔时间
    private static long exitTime = 0;
    private BaseApplication application;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        manager = getActivity().getWindowManager();
        urlList = new ArrayList<>();
        init(view);
        if (setApplicationWebClose()) {
            addApplicationWeb();
        }
        if (setWebClose()) {
            addWeb(getUrl());
        }
        setSwipeRefreshLayout();
        setBtnClickListener();
        JudgStatus();
        deleteWebCache();
        setExitOnKeyListener();
//        setBackOnKeyListener();

        return view;
    }

    /**
     * 控件初始化
     */
    public void init(View view) {
        manager = getActivity().getWindowManager();
        fl = view.findViewById(R.id.fl_base);
        pb_loading = view.findViewById(R.id.pb_load);
        refreshWeb = view.findViewById(R.id.srl_base);
        ll_error = view.findViewById(R.id.ll_error_base);
        btn_error = view.findViewById(R.id.btn_error_base);
        iv_loading = view.findViewById(R.id.iv_loading);
        drawable = (AnimationDrawable) iv_loading.getDrawable();
        ll_loading = view.findViewById(R.id.ll_loading);


    }

    /**
     * 添加全局WebView
     */
    public void addApplicationWeb() {
        application = (BaseApplication) getActivity().getApplication();


        fl.addView(application.wv1);

        setWeb(application.wv1);
    }

    /**
     * 添加webview
     *
     * @param url
     */
    private void addWeb(String url) {
        mWeb = new MyWebView(getActivity());
        fl.addView(mWeb);
        setWeb(mWeb);
        mWeb.loadUrl(url);
    }

    /**
     * 设置WebView
     *
     * @param webView
     */
    public void setWeb(MyWebView webView) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        webView.setLayoutParams(params);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());

        settings = webView.getSettings();
        //设置Html UA
        settings.setUserAgentString("chrome");
        settings.setBlockNetworkImage(true);
        settings.setLoadsImagesAutomatically(true);
        //允许WebView启用js脚本
        settings.setJavaScriptEnabled(true);
        //打开WebView页面缩放功能
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        //设置webview缓存
        settings.setDomStorageEnabled(true);//开启dom缓存就好了,显示弹框
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + "/webcache";
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
        // 禁用 file 协议；
        settings.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        //调用其它浏览器下载文件
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(s);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
        //允许WebView混合加载http和https的网页内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        /**
         * 设置webview到页面顶部时再启用SwipeRefreshLayout
         */
        webView.setOnScrollChangedCallback(new MyWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                if (dy == 0) {
                    refreshWeb.setEnabled(true);
                } else {
                    refreshWeb.setEnabled(false);
                }
            }
        });
        //        /**
//         * 设置webview到页面顶部时再启用SwipeRefreshLayout
//         */
//        refreshWeb.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
//            @Override
//            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
//                return mWeb.getScrollY() > 0;
//            }
//        });
    }

    /**
     * 设置WebViewClient
     * 截获跳转
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url.contains("qqjyo")) {
                view.stopLoading();
                Log.i("拦截返回", "");
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("shouldOverrideUrl", "shouldOverrideUrlLoading: " + url);
            if (url.contains("qqjyo") || url.contains("jd") || url.contains("changba") || url.contains("jd") || url.contains("uc") || url.contains("tb") || url.contains("alicdn")) {
                view.stopLoading();
                Log.i("拦截返回", "");
                return true;
            } else if (!(url.startsWith("http:") || url.startsWith("https:"))) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else if (!urlList.contains(url)) {
                addWeb(url);
                urlList.add(url);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String s, String s1) {
            super.onReceivedError(webView, errorCode, s, s1);
            // 断网或者网络连接超时
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                webView.loadUrl("about:blank"); // 避免出现默认的错误界面
                // 在这里显示自定义错误页
                webView.setVisibility(View.GONE);
                ll_error.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl(getHidehtml());
        }
    }

    /**
     * 设置WebChromeClient
     */
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
//            //网页元素过滤
//            view.loadUrl(getHidehtml());
            //网页加载进度条
            if (newProgress == 100) {
                pb_loading.setVisibility(View.GONE);
                view.getSettings().setBlockNetworkImage(false);

            } else {
                pb_loading.setVisibility(View.VISIBLE);
                pb_loading.setProgress(newProgress);
            }

        }

        //视频全屏设置
        @Override
        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            //横屏设置
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // 此处的 view 就是全屏的视频播放界面，需要把它添加到我们的界面上
            manager.addView(view, new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION));
            // 去除状态栏和导航按钮
            fullScreen(view);
            fullScreenView = view;

        }

        //视频退出全屏设置
        @Override
        public void onHideCustomView() {
            //竖屏设置
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // 退出全屏播放，我们要把之前添加到界面上的视频播放界面移除
            manager.removeViewImmediate(fullScreenView);
            fullScreenView = null;
        }
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
     * 如果之前处于全屏状态，重新进入后需要再次调用全屏
     */
    @Override
    public void onResume() {
        super.onResume();
        // 如果之前处于全屏状态，重新进入后需要再次调用全屏
        if (fullScreenView != null) fullScreen(fullScreenView);
    }

    /**
     * 设置SwipeRefreshLayout
     */
    public void setSwipeRefreshLayout() {
        refreshWeb.setColorSchemeResources(R.color.text_selected);
        refreshWeb.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (setWebClose()) {
                    setRefreshValue(mWeb);
                } else {
                    setRefreshValue(application.wv1);
                }
            }


        });
    }

    /**
     * 设置setOnRefreshListener的内容
     *
     * @param webView
     */
    public void setRefreshValue(MyWebView webView) {
        webView.reload();
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //网页加载进度条
                if (newProgress == 100) {
                    pb_loading.setVisibility(View.GONE);
                    refreshWeb.setRefreshing(false);
                } else {
                    pb_loading.setVisibility(View.VISIBLE);
                    pb_loading.setProgress(newProgress);
                }

            }
        });
    }

    /**
     * 设置单击事件
     */
    public void setBtnClickListener() {
        btn_error.setOnClickListener(this);
    }

    /**
     * 单击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_error_base:
                if (setWebClose()) {
                    mWeb.reload();
                } else {
                    application.wv1.loadUrl("https://wap.gamersky.com");
                }
                break;

        }
    }

    /**
     * 判断网络状态调用不同的WebView缓存方式
     */
    public void JudgStatus() {

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (setApplicationWebClose()) {
            if (activeNetworkInfo == null) {
                application.wv1.getSettings()
                        .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                application.wv1.getSettings()
                        .setCacheMode(WebSettings.LOAD_DEFAULT);
            }
        } else {
            if (activeNetworkInfo == null) {
                mWeb.getSettings()
                        .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                mWeb.getSettings()
                        .setCacheMode(WebSettings.LOAD_DEFAULT);
            }
        }
    }

    /**
     * 删除WebView页面缓存
     */
    public void deleteWebCache() {
        if (setApplicationWebClose()) {
            preferences = getActivity().getSharedPreferences("content", MODE_PRIVATE);
            String delete = preferences.getString("delete", "");
            if (delete.equals("delete")) {
                application.wv1.clearCache(true);
            }
        } else {
            preferences = getActivity().getSharedPreferences("content", MODE_PRIVATE);
            String delete = preferences.getString("delete", "");
            if (delete.equals("delete")) {
                mWeb.clearCache(true);
            }
        }
    }


    /**
     * 退出APP判断
     */
    public void setExitOnKeyListener() {
        Log.i("setExitOnKeyListener:", "setExitOnKeyListener: ");
        if (setWebClose()) {
            mWeb.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        childCount = fl.getChildCount();
//                    if (fullScreenView != null) {
//                        manager.removeViewImmediate(view);
//                        fullScreenView = null;
//                    } else
                        if (mWeb != null && mWeb.canGoBack()) {
                            mWeb.goBack();
                        } else if (childCount > 1) {
                            fl.removeViewAt(childCount - 1);
                            urlList.remove(urlList.size() - 1);
                        } else
                            if ((System.currentTimeMillis() - exitTime) > 2000) {
                            //弹出提示，可以有多种方式
                            Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        } else {
                            getActivity().finish();
                        }
                        return true;
                    }
                    return false;
                }
            });
        } else {
            application.wv1.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        childCount = fl.getChildCount();
//                    if (fullScreenView != null) {
//                        manager.removeViewImmediate(view);
//                        fullScreenView = null;
//                    } else
                        if (application.wv1 != null && application.wv1.canGoBack()) {
                            application.wv1.goBack();

                        } else if (childCount > 1) {
                            fl.removeViewAt(childCount - 1);
                            urlList.remove(urlList.size() - 1);

                        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
                            //弹出提示，可以有多种方式
                            Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();

                        } else {
                            getActivity().finish();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

    }

//    /**
//     * 设置OnKeyListener内容
//     *
//     * @param webView
//     */
//    public void setExitValue(MyWebView webView) {
//        childCount = fl.getChildCount();
////                    if (fullScreenView != null) {
////                        manager.removeViewImmediate(view);
////                        fullScreenView = null;
////                    } else
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else if (childCount > 1) {
//            fl.removeViewAt(childCount - 1);
//            urlList.remove(urlList.size() - 1);
//        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
//            //弹出提示，可以有多种方式
//            Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
//            getActivity().finish();
//        }
//    }

//    /**
//     * 页面后退判断
//     */
//    public void setBackOnKeyListener() {
//        Log.i("setBackOnKeyListener:", "setBackOnKeyListener: ");
//        if (setWebClose()) {
//            mWeb.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK
//                            && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                        childCount = fl.getChildCount();
//                        if (fullScreenView != null) {
//                            manager.removeViewImmediate(view);
//                            fullScreenView = null;
//                        } else if (mWeb != null && mWeb.canGoBack()) {
//                            mWeb.goBack();
//                        } else if (childCount > 1) {
//                            fl.removeViewAt(childCount - 1);
//                            urlList.remove(urlList.size() - 1);
//                        } else {
//                            getActivity().finish();
//                        }
//                    }
//                    return false;
//                }
//            });
//        } else {
//            application.wv1.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK
//                            && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                        childCount = fl.getChildCount();
//                        if (fullScreenView != null) {
//                            manager.removeViewImmediate(view);
//                            fullScreenView = null;
//                        } else if (mWeb != null && mWeb.canGoBack()) {
//                            mWeb.goBack();
//                        } else if (childCount > 1) {
//                            fl.removeViewAt(childCount - 1);
//                            urlList.remove(urlList.size() - 1);
//                        } else {
//                            getActivity().finish();
//                        }
//                    }
//                    return false;
//                }
//            });
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fl.removeAllViews();
    }

    //    /**
//     * 返回处理，和传统的mWeb.canGoBack()不一样了，而是直接remove
//     */
//    @Override
//    public void onBackPressed() {
//        childCount = getFrameLayout().getChildCount();
//        if (mWeb.canGoBack()) {
//            mWeb.goBack();
//        } else if (childCount > 1) {
//            getFrameLayout().removeViewAt(childCount - 1);
//            urlList.remove(urlList.size() - 1);
//        } else {
//            super.onBackPressed();
//        }
//
//    }


//    /**
//     * 添加webview
//     *
//     * @param url
//     */
//    private void addWeb(String url) {
//        mWeb = new MyWebView(this);
//        mWeb.getSettings().setJavaScriptEnabled(true);
//        mWeb.getSettings().setDomStorageEnabled(true);//开启dom缓存就好了,显示弹框
//        mWeb.setWebChromeClient(new WebChromeClient());
//        mWeb.setWebViewClient(new MyWebViewClient());
//        mWeb.loadUrl(url);
//        getFrameLayout().addView(mWeb);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//        );
//        mWeb.setLayoutParams(params);
//    }
//
//    //截获跳转
//    private class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            Log.i("shouldOverrideUrl", "shouldOverrideUrlLoading: " + url);
//            if (!urlList.contains(url)) {
//                addWeb(url);
//                urlList.add(url);
//                return true;
//            } else {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        }
//    }


    /**
     * 暴露出的方法
     */
    public FrameLayout getFrameLayout() {
        return fl;
    }

    public LinearLayout getLl_error() {
        return ll_error;
    }

    public String getHidehtml() {
        return "javascript:function setHidehtml(){" +
                "}" +
                "setHidehtml();";
    }

    public ProgressBar getPb_loading() {
        return pb_loading;
    }

    public SwipeRefreshLayout getRefreshWeb() {
        return refreshWeb;
    }


    public Button getBtn_error() {
        return btn_error;
    }


    public int getLayout() {
        return 0;
    }

    public String getUrl() {
        return "";
    }

    public Boolean setWebClose() {
        return true;
    }

    public Boolean setApplicationWebClose() {
        return false;
    }

}

