package com.example.gamesquery;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;

import java.util.List;


/**
 * @ 创建时间: 2019/7/4 on 20:25.
 * @ 描述: 自定义OnKeyListener
 * @ 作者: 李琪
 */
public class BackOnKeyListener implements View.OnKeyListener {
    private WebView webView;
    private Activity activity;
    private View view1;
    private WindowManager windowManager;
    private int childCount;
    private FrameLayout fl;
    private List<String> urlList;

    public BackOnKeyListener(WebView webView, Activity activity, View view, WindowManager windowManager, int childCount, FrameLayout fl, List<String> urlList) {
        this.webView = webView;
        this.activity = activity;
        this.view1 = view;
        this.windowManager = windowManager;
        this.windowManager = windowManager;
        this.childCount = childCount;
        this.fl = fl;
        this.urlList = urlList;

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        if (keyCode == KeyEvent.KEYCODE_BACK
                && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (view != null) {
                windowManager.removeViewImmediate(view);
                view1 = null;
            } else if (webView != null && webView.canGoBack()) {
                webView.goBack();
            } else if (childCount > 1) {
                fl.removeViewAt(childCount - 1);
                urlList.remove(urlList.size() - 1);
            } else {
                activity.finish();
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }

        return true;
    }
}
