package com.example.gamesquery.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamesquery.FragmentUtils;
import com.example.gamesquery.MyWebView;
import com.example.gamesquery.R;

/**
 * SteamCN搜索界面Fragment
 */
public class SteamCNFindFragment extends Fragment {

    public WebSettings settings;
    public static MyWebView wv_news;
    public ProgressBar pb_load;
    public SwipeRefreshLayout srl_news;
    public SharedPreferences preferences;
    private LinearLayout ll_error;
    private Button btn_error;
    private String value;
    private AnimationDrawable drawable;
    private ImageView iv_loading;
    private LinearLayout ll_loading;
    public String hidehtml =
            "javascript:function setHidehtml(){" +
                    "document.getElementsByClassName('se-page-hd-content')[0].style.display='none';" +
                    "document.getElementsByClassName('se-relativewords')[0].style.display='none';" +
                    "document.getElementsByClassName('se-ft-wrap')[0].style.display='none';" +
                    "document.getElementsByClassName('c-recomm-wrap new-ux-recom-wrapper animation')[0].style.display='none';" +
                    "document.getElementsByClassName('se-page-copyright ')[0].style.display='none';" +
                    "document.getElementsByClassName('se-page-ft')[0].style.display='none';" +
                    "document.getElementsByClassName('se-page-relative')[0].style.display='none';" +
                    "document.getElementById('page-ft').style.display='none';" +
                    "document.getElementById('page-copyright').style.display='none';" +
                    "document.getElementById('foot-logo').style.display='none';" +
                    "document.getElementsByClassName('return-link')[0].style.display='none';" +
                    "}" +
                    "setHidehtml();";


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
        value = FragmentUtils.setHtmlTranscoding(preferences, getActivity(), value, "neirong");
        String URI = "https://www.baidu.com/s?wd=site%3Asteamcn.com%20" + value;
        FragmentUtils.setWebView(wv_news, settings, getActivity(), hidehtml, pb_load, URI,ll_error, iv_loading, drawable, ll_loading);
        FragmentUtils.setSwipeRefreshLayout(srl_news, wv_news,pb_load);
        FragmentUtils.JudgStatus(getActivity(), settings, wv_news);
        FragmentUtils.deleteWebCache(preferences, getActivity(), wv_news);
        FragmentUtils.setErrorReload(btn_error, wv_news);
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