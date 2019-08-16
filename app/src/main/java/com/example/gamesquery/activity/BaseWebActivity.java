package com.example.gamesquery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamesquery.MyWebView;
import com.example.gamesquery.R;

import java.util.ArrayList;
import java.util.List;

public class BaseWebActivity extends BaseActivity {
    private FrameLayout fl;
    private List<String> urlList;
    private int childCount = 0;
    private MyWebView mWeb;
    private String url;
    private Context context;
    private LinearLayout ll_error;
    private String hidehtml;
    private ProgressBar pb_loading;
    private WindowManager manager;
    private View fullScreenView;
    private SwipeRefreshLayout refreshWeb;
    private Button btn_error;
    private WebSettings settings;
    private SharedPreferences preferences;
    private Intent intent;
    private ImageView iv_back;
    private ImageView iv_close;
    private ImageView iv_home;
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getWindowManager();
        urlList = new ArrayList<>();
        if (closeIntentUrl()) {
            intent = getIntent();
            url = intent.getStringExtra("url");
            addWeb(url);
        }
        if (openStringUrl()) {
            addWeb(setStringUrl());
        }
        setSwipeRefreshLayout();
        setBtnClickListener();
        JudgStatus();
        deleteWebCache();

    }

    /**
     * 添加webview
     *
     * @param url
     */
    private void addWeb(String url) {
        mWeb = new MyWebView(this);
        getFrameLayout().addView(mWeb);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mWeb.setLayoutParams(params);

        mWeb.setWebViewClient(new MyWebViewClient());
        mWeb.setWebChromeClient(new MyWebChromeClient());

        settings = mWeb.getSettings();
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
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
        // 禁用 file 协议；
        settings.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        //调用其它浏览器下载文件
        mWeb.setDownloadListener(new DownloadListener() {
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

        mWeb.loadUrl(url);
        /**
         * 设置webview到页面顶部时再启用SwipeRefreshLayout
         */
        mWeb.setOnScrollChangedCallback(new MyWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                if (dy == 0) {
                    getRefreshWeb().setEnabled(true);
                } else {
                    getRefreshWeb().setEnabled(false);
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
                getLl_error().setVisibility(View.VISIBLE);
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
                getPb_loading().setVisibility(View.GONE);
                view.getSettings().setBlockNetworkImage(false);

            } else {
                getPb_loading().setVisibility(View.VISIBLE);
                getPb_loading().setProgress(newProgress);
            }

        }

        //视频全屏设置
        @Override
        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            //横屏设置
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // 退出全屏播放，我们要把之前添加到界面上的视频播放界面移除
            manager.removeViewImmediate(fullScreenView);
            fullScreenView = null;
        }
    }

    /**
     * 如果之前处于全屏状态，重新进入后需要再次调用全屏
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 如果之前处于全屏状态，重新进入后需要再次调用全屏
        if (fullScreenView != null) fullScreen(fullScreenView);
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
     * 设置SwipeRefreshLayout
     */
    public void setSwipeRefreshLayout() {
        getRefreshWeb().setColorSchemeResources(R.color.text_selected);
        getRefreshWeb().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWeb.reload();
                mWeb.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        //网页加载进度条
                        if (newProgress == 100) {
                            getPb_loading().setVisibility(View.GONE);
                            getRefreshWeb().setRefreshing(false);
                        } else {
                            getPb_loading().setVisibility(View.VISIBLE);
                            getPb_loading().setProgress(newProgress);
                        }

                    }
                });

            }


        });
    }

    /**
     * 设置单击事件
     */
    public void setBtnClickListener() {
        getBtn_error().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeb.reload();
            }
        });
        if (closeBackHome()) {
            getIv_back().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childCount = getFrameLayout().getChildCount();
                    if (mWeb.canGoBack()) {
                        mWeb.goBack();
                    } else if (childCount > 1) {
                        getFrameLayout().removeViewAt(childCount - 1);
                        urlList.remove(urlList.size() - 1);
                    } else {
                        finish();
                    }
                }
            });
            getIv_close().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            getIv_home().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWeb.loadUrl(url);
                }
            });
        }
    }


    /**
     * 判断网络状态调用不同的WebView缓存方式
     */
    public void JudgStatus() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            mWeb.getSettings()
                    .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            mWeb.getSettings()
                    .setCacheMode(WebSettings.LOAD_DEFAULT);
        }
    }

    /**
     * 删除WebView页面缓存
     */
    public void deleteWebCache() {
        preferences = getSharedPreferences("content", MODE_PRIVATE);
        String delete = preferences.getString("delete", "");
        if (delete.equals("delete")) {
            mWeb.clearCache(true);
        }
    }

    /**
     * 返回处理，和传统的mWeb.canGoBack()不一样了，而是直接remove
     */
    @Override
    public void onBackPressed() {
        if (openExit()) {
            setExitValue();
        } else {
            setBackValue();
        }

    }

    /**
     * 设置退出的内容
     */
    public void setExitValue() {
        childCount = getFrameLayout().getChildCount();
        if (mWeb.canGoBack()) {
            mWeb.goBack();
        } else if (childCount > 1) {
            getFrameLayout().removeViewAt(childCount - 1);
            urlList.remove(urlList.size() - 1);
        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
            //弹出提示，可以有多种方式
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 设置返回的内容
     */
    public void setBackValue() {
        childCount = getFrameLayout().getChildCount();
        if (mWeb.canGoBack()) {
            mWeb.goBack();
        } else if (childCount > 1) {
            getFrameLayout().removeViewAt(childCount - 1);
            urlList.remove(urlList.size() - 1);
        } else {
            super.onBackPressed();
        }
    }


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
        return hidehtml;
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


    public ImageView getIv_back() {
        return iv_back;
    }


    public ImageView getIv_close() {
        return iv_close;
    }


    public ImageView getIv_home() {
        return iv_home;
    }

    public int getLayout() {
        return 0;
    }

    public Boolean closeIntentUrl() {
        return true;
    }

    public Boolean openStringUrl() {
        return false;
    }

    public String setStringUrl() {
        return "";

    }

    public Boolean openExit() {
        return false;
    }

    public Boolean closeBackHome() {
        return true;
    }
}
