package com.example.gamesquery.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamesquery.StatusBarUtil;

/**
 * Activity基类
 */
public class BaseActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        //修改状态栏字体颜色
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
    }

    /**
     * 设置全屏
     */
    public void setFullscreen() {
        preferences = getSharedPreferences("content", MODE_PRIVATE);
        String fullscreen = preferences.getString("fullscreen", "");
        if (fullscreen.equals("open")) {
            //隐藏系统状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
