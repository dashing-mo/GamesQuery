package com.example.gamesquery.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamesquery.R;

/**
 * APP游戏平台查看界面
 */
public class PlatformWebViewActivity extends BaseWebActivity {
    private ImageView iv_back;
    private ImageView iv_close;
    private ImageView iv_home;
    private FrameLayout fl_platform;
    private SwipeRefreshLayout srl_platform;
    private ProgressBar pb_load_platform;
    private LinearLayout ll_error_platform;
    private Button btn_error_platform;
    private String hideHtml =  "javascript:function setHidehtml(){" +
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
            "setHidehtml();";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_platform_webview);
        init();
        super.onCreate(savedInstanceState);

    }

    /**
     * 控件初始化
     */
    private void init() {
        iv_back = findViewById(R.id.iv_back_platform);
        iv_close = findViewById(R.id.iv_close_platform);
        iv_home = findViewById(R.id.iv_home_platform);
        fl_platform = findViewById(R.id.fl_platform);
        srl_platform = findViewById(R.id.srl_platform);
        pb_load_platform = findViewById(R.id.pb_load_platform);
        ll_error_platform = findViewById(R.id.ll_error_platform);
        btn_error_platform = findViewById(R.id.btn_error_platform);
    }

    @Override
    public FrameLayout getFrameLayout() {
        return fl_platform;
    }

    @Override
    public LinearLayout getLl_error() {
        return ll_error_platform;
    }

    @Override
    public String getHidehtml() {
        return hideHtml;
    }

    @Override
    public ProgressBar getPb_loading() {
        return pb_load_platform;
    }

    @Override
    public SwipeRefreshLayout getRefreshWeb() {
        return srl_platform;
    }

    @Override
    public Button getBtn_error() {
        return btn_error_platform;
    }

    @Override
    public ImageView getIv_back() {
        return iv_back;
    }

    @Override
    public ImageView getIv_close() {
        return iv_close;
    }

    @Override
    public ImageView getIv_home() {
        return iv_home;
    }

}
