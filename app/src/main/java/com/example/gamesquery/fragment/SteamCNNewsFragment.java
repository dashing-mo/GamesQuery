package com.example.gamesquery.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamesquery.FragmentUtils;
import com.example.gamesquery.MyWebView;
import com.example.gamesquery.R;

/**
 * SteamCN新闻界面Fragment
 */
public class SteamCNNewsFragment extends BaseWebFragment {
    public static final String URl = "https://steamcn.com/";
    public static MyWebView wv_news;
    public WebSettings settings;
    public ProgressBar pb_load;
    public SwipeRefreshLayout srl_news;
    public SharedPreferences preferences;
    private Button btn_error;
    private LinearLayout ll_error;
    private AnimationDrawable drawable;
    private ImageView iv_loading;
    private LinearLayout ll_loading;
    // 用来计算返回键的点击间隔时间
    private static long exitTime = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_base_find, container, false);
        wv_news = view.findViewById(R.id.wv_news);
        pb_load = view.findViewById(R.id.pb_load);
        srl_news = view.findViewById(R.id.srl_news);
        ll_error = view.findViewById(R.id.ll_error);
        btn_error = view.findViewById(R.id.btn_error);
        iv_loading = view.findViewById(R.id.iv_loading);
        drawable = (AnimationDrawable) iv_loading.getDrawable();
        ll_loading = view.findViewById(R.id.ll_loading);

        FragmentUtils.setWebView(wv_news, settings, getActivity(), null, pb_load, URl, ll_error, iv_loading, drawable, ll_loading);
        FragmentUtils.setSwipeRefreshLayout(srl_news, wv_news, pb_load);
        FragmentUtils.JudgStatus(getActivity(), settings, wv_news);
        FragmentUtils.deleteWebCache(preferences, getActivity(), wv_news);
        FragmentUtils.setErrorReload(btn_error, wv_news);

        wv_news.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (wv_news != null && wv_news.canGoBack()) {
                        wv_news.goBack();

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
        return view;
    }

    /**
     * WebView后退判断
     *
     * @return
     */
    public static Boolean onBackPressed() {
        if (wv_news.canGoBack()) {
            wv_news.goBack();
            return true;
        }
        return false;
    }

}