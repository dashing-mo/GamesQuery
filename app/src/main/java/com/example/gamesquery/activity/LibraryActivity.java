package com.example.gamesquery.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamesquery.EditTextWithDel;
import com.example.gamesquery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * @ 创建时间: 2019/6/30 on 09:45.
 * @ 描述: App游戏库界面
 * @ 作者: 李琪
 */
public class LibraryActivity extends BaseWebActivity {

    private static final String APP_CACHE_DIRNAME = "/webcache";
    private final static String URl = "https://wap.gamersky.com/ku/";
    //    private final static String URl = "http://soft.imtt.qq.com/browser/tes/feedback.html";
    private long exitTime = 0;

    private WebSettings settings;
    //    private MyWebView wv_library;
    private FrameLayout wv_library;
    private ProgressBar pb_load_library;
    private FloatingActionButton fab_top;
    private SwipeRefreshLayout srl_library;
    private SharedPreferences preferences;
    private EditTextWithDel et_find_library;
    private ImageView iv_find_library;
    private TextView tv_find_library;
    private String value;
    private FrameLayout fl_video;
    private WebChromeClient.CustomViewCallback callback;
    private WindowManager windowManager;
    private View fullScreenView = null;
    private ImageView iv_library;
    private Button btn_error_library;
    private LinearLayout ll_error_library;
    private String hideHtml = "javascript:function setTop(){" +
            "document.querySelector('.ymw-header2018').style.display='none';" +
            "document.querySelector('.ymw-footer').style.display='none';" +
            "document.querySelector('.yu-btn-wrap').style.display='none';" +
            "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
            "document.getElementById('gsTgWapZPCbtn').style.display='none';" +
            "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
            "document.getElementsByClassName('ymw-rel-list')[0].style.display='none';" +
            "document.getElementsByClassName('ymw-rel-mgame')[0].style.display='none';" +
            "}" +
            "setTop();";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        setContentView(R.layout.activity_library);
        init();
//        JudgStatus();
//        setWebView();
//        setSwipeRefreshLayout();
//        deleteWebCache();
        setHintTextSize(et_find_library, "请输入搜索内容", 13);

//        windowManager = getWindowManager();
        super.onCreate(savedInstanceState);

    }

    /**
     * 初始化控件
     */
    public void init() {
        iv_library = findViewById(R.id.iv_library);
        srl_library = findViewById(R.id.srl_library);
        pb_load_library = findViewById(R.id.pb_load_library);
        wv_library = findViewById(R.id.wv_library);
        tv_find_library = findViewById(R.id.tv_find_library);
        et_find_library = findViewById(R.id.et_find_library);
        iv_find_library = findViewById(R.id.iv_find_library);
        fl_video = findViewById(R.id.fl_video);
        fab_top = findViewById(R.id.fab_top);
        ll_error_library = findViewById(R.id.ll_error_library);
        btn_error_library = findViewById(R.id.btn_error_library);
        fab_top.hide();

        fab_top.setOnClickListener(mylistener);
        iv_find_library.setOnClickListener(mylistener);
        tv_find_library.setOnClickListener(mylistener);
        et_find_library.setOnEditorActionListener(listener);
        et_find_library.setOnFocusChangeListener(listener2);

    }

    /**
     * 输入法右下角单击按钮监听器
     */
    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            JudgeText();
            return true;
        }
    };
    /**
     * 输入框焦点变化监听器
     */
    View.OnFocusChangeListener listener2 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b == false) {
                et_find_library.setVisibility(View.INVISIBLE);
                tv_find_library.setVisibility(View.GONE);
                iv_find_library.setVisibility(View.VISIBLE);
            }
        }
    };

//    /**
//     * 设置SwipeRefreshLayout
//     */
//    public void setSwipeRefreshLayout() {
//        srl_library.setColorSchemeResources(R.color.text_selected);
//        srl_library.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                wv_library.reload();
//                wv_library.setWebChromeClient(new WebChromeClient() {
//                    @Override
//                    public void onProgressChanged(WebView view, int newProgress) {
//                        //网页元素过滤
//                        view.loadUrl("javascript:function setTop(){" +
//                                "document.querySelector('.ymw-header2018').style.display='none';" +
//                                "document.querySelector('.ymw-footer').style.display='none';" +
//                                "document.querySelector('.yu-btn-wrap').style.display='none';" +
//                                "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
//                                "document.getElementById('gsTgWapZPCbtn').style.display='none';" +
//                                "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
//                                "document.getElementsByClassName('ymw-rel-list')[0].style.display='none';" +
//                                "document.getElementsByClassName('ymw-rel-mgame')[0].style.display='none';" +
//                                "}" +
//                                "setTop();");
//                        if (newProgress == 100) {
//                            pb_load_library.setVisibility(View.GONE);
//                            srl_library.setRefreshing(false);
//                        } else {
//                            pb_load_library.setVisibility(View.VISIBLE);
//                            pb_load_library.setProgress(newProgress);
//                        }
//                        super.onProgressChanged(view, newProgress);
//                    }
//                });
//            }
//        });
//    }

    /**
     * 点击事件
     */
    View.OnClickListener mylistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_top:
                    wv_library.setVerticalScrollbarPosition(0);
                    break;
                case R.id.iv_find_library:
                    et_find_library.setVisibility(View.VISIBLE);
                    iv_find_library.setVisibility(View.GONE);
                    tv_find_library.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_find_library:
                    et_find_library.setVisibility(View.INVISIBLE);
                    tv_find_library.setVisibility(View.GONE);
                    iv_find_library.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

//    /**
//     * 设置WebView
//     */
//    public void setWebView() {
//        //实现WebView滚动事件的监听
//        wv_library.setOnScrollChangedCallback(new MyWebView.OnScrollChangedCallback() {
//            @Override
//            public void onScroll(int dx, int dy) {
//                if (dy < -100) {
//                    //向上滑动
//                    fab_top.hide();
//                } else if (dy > 5) {
//                    //向下滑动
//                    fab_top.show();
//                }
//            }
//        });
//        wv_library.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                if (url.contains("qqjyo")) {
//                    wv_library.stopLoading();
//                    Log.i("拦截返回", "");
//                }
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.contains("qqjyo")) {
//                    wv_library.stopLoading();
//                    Log.i("拦截返回", "");
//                    return true;
//                }
////                else if (url.startsWith("https:") || url.startsWith("http:")) {
////                    view.loadUrl(url);
////                    return true;
////                }
////                else {
////                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
////                    startActivity(intent);
////                    return false;
////                }
////                view.loadUrl(url);
//                return false;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                //网页元素过滤
//                view.loadUrl("javascript:function setTop(){" +
//                        "document.querySelector('.ymw-header2018').style.display='none';" +
//                        "document.querySelector('.ymw-footer').style.display='none';" +
//                        "document.querySelector('.yu-btn-wrap').style.display='none';" +
//                        "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
//                        "document.getElementById('gsTgWapZPCbtn').style.display='none';" +
//                        "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
//                        "document.getElementsByClassName('ymw-rel-list')[0].style.display='none';" +
//                        "document.getElementsByClassName('ymw-rel-mgame')[0].style.display='none';" +
//                        "}" +
//                        "setTop();");
//                super.onPageFinished(view, url);
//            }
//        });
//        wv_library.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                //网页元素过滤
//                view.loadUrl("javascript:function setTop(){" +
//                        "document.querySelector('.ymw-header2018').style.display='none';" +
//                        "document.querySelector('.ymw-footer').style.display='none';" +
//                        "document.querySelector('.yu-btn-wrap').style.display='none';" +
//                        "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
//                        "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
//                        "}" +
//                        "setTop();");
//                if (newProgress == 100) {
//                    pb_load_library.setVisibility(View.GONE);
//                } else {
//                    pb_load_library.setVisibility(View.VISIBLE);
//                    pb_load_library.setProgress(newProgress);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//
//            @Override
//            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
//                //横屏设置
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                // 此处的 view 就是全屏的视频播放界面，需要把它添加到我们的界面上
//                windowManager.addView(view, new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION));
//                // 去除状态栏和导航按钮
//                fullScreen(view);
//                fullScreenView = view;
//                wv_library.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onHideCustomView() {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                // 退出全屏播放，我们要把之前添加到界面上的视频播放界面移除
//                windowManager.removeViewImmediate(fullScreenView);
//                fullScreenView = null;
//                wv_library.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        //调用其它浏览器下载文件
//        wv_library.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
//                Uri uri = Uri.parse(s);
//                Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(i);
//            }
//        });
//        wv_library.getSettings().setJavaScriptEnabled(true);
//        wv_library.loadUrl(URl);
//
//
//        //打开WebView页面缩放功能
//        settings = wv_library.getSettings();
////        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDisplayZoomControls(false);
//        settings.setSupportZoom(true);
//    }
//
//    /**
//     * 判断网络状态调用不同的WebView缓存方式
//     */
//    public void JudgStatus() {
//        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
//        if (activeNetworkInfo == null) {
//            setWebCache(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//            setWebCache(WebSettings.LOAD_DEFAULT);
//        }
//    }
//
//    /**
//     * 设置WebView页面缓存
//     *
//     * @param i
//     */
//    public void setWebCache(int i) {
//        settings = wv_library.getSettings();
//        settings.setCacheMode(i);
//        settings.setDomStorageEnabled(true);
//        settings.setDatabaseEnabled(true);
//        String cacheDirPath = this.getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
//        settings.setAppCachePath(cacheDirPath);
//        settings.setAppCacheEnabled(true);
//    }
//
//    /**
//     * 删除WebView页面缓存
//     */
//    public void deleteWebCache() {
//        preferences = getSharedPreferences("content", MODE_PRIVATE);
//        String delete = preferences.getString("delete", "");
//        if (delete.equals("delete")) {
//            wv_library.clearCache(true);
//        }
//    }

    /**
     * 判断输入内容是否为空
     */
    public void JudgeText() {
        value = et_find_library.getText().toString().trim();
        if (value.isEmpty()) {
            Toast.makeText(LibraryActivity.this, "输入内容不能为空(●'◡'●)", Toast.LENGTH_SHORT).show();
        } else {
            preferences = getSharedPreferences("content", MODE_PRIVATE);
            preferences.edit().putString("neirong", value).commit();

            Intent intent = new Intent(LibraryActivity.this, FindActivity.class);
            startActivity(intent);
            et_find_library.setVisibility(View.INVISIBLE);
            tv_find_library.setVisibility(View.GONE);
            iv_find_library.setVisibility(View.VISIBLE);
            et_find_library.setText("");
        }
    }

    /**
     * 设置 hint字体大小
     *
     * @param editText 输入控件
     * @param hintText hint的文本内容
     * @param textSize hint的文本的文字大小（以dp为单位设置即可）
     */
    public static void setHintTextSize(EditText editText, String hintText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hintText);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

//    /**
//     * 页面后退或APP退出判断
//     *
//     * @return
//     */
//    @Override
//    public void onBackPressed() {
//        if (fullScreenView != null) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            windowManager.removeViewImmediate(fullScreenView);
//            fullScreenView = null;
//            wv_library.setVisibility(View.VISIBLE);
//        } else if (wv_library.canGoBack()) {
//            wv_library.goBack();
//        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
//            //弹出提示，可以有多种方式
//            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
//            finish();
//            System.exit(0);
//        }
//    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        // 如果之前处于全屏状态，重新进入后需要再次调用全屏
//        if (fullScreenView != null) fullScreen(fullScreenView);
//    }

    //    /**
//     * 视频全屏设置
//     * @param view
//     */
//    private void fullScreen(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        } else {
//            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        }
//    }
    @Override
    public FrameLayout getFrameLayout() {
        return wv_library;
    }

    @Override
    public Boolean closeIntentUrl() {
        return false;
    }

    @Override
    public LinearLayout getLl_error() {
        return ll_error_library;
    }

    @Override
    public String getHidehtml() {
        return hideHtml;
    }

    @Override
    public ProgressBar getPb_loading() {
        return pb_load_library;
    }

    @Override
    public SwipeRefreshLayout getRefreshWeb() {
        return srl_library;
    }

    @Override
    public Button getBtn_error() {
        return btn_error_library;
    }

    @Override
    public Boolean openStringUrl() {
        return true;
    }

    @Override
    public String setStringUrl() {
        return URl;
    }

    @Override
    public Boolean openExit() {
        return true;
    }

    @Override
    public Boolean closeBackHome() {
        return false;
    }
}
