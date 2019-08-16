package com.example.gamesquery;

import android.app.Activity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;
/**
 * @ 创建时间: 2019/7/3 on 19:25.
 * @ 描述: APP退出判断类
 * @ 作者: 李琪
 */
public class Exit_Detection {
    // 用来计算返回键的点击间隔时间
    private static long exitTime = 0;


    public static boolean onKeyDown(int keyCode, KeyEvent event, Activity activity, WebView webView) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                activity.finish();
            }
            return true;
        }

        return true;
    }
}

