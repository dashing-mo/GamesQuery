package com.example.gamesquery.activity;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gamesquery.R;
import com.example.gamesquery.StatusBarUtil;

/**
 * 设置Activity页面
 * 李琪
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar tl_settings;
    private Switch sh_zhuangtai;
    private LinearLayout ll_qingchu;
    private TextView tv_guanyu;

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter filter;

    private SharedPreferences preferences;
    private AppCompatButton btn_X5Debug;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        init();
        setToolbar();
        setSwitch();
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
    }

    /**
     * 控件初始化
     */
    public void init() {
        tl_settings = findViewById(R.id.tl_settings);
        sh_zhuangtai = findViewById(R.id.sh_zhuangtai);
        ll_qingchu = findViewById(R.id.ll_qingchu);
        tv_guanyu = findViewById(R.id.tv_guanyu);
//        btn_X5Debug = findViewById(R.id.btn_X5Debug);
//        btn_X5Debug.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                preferences.edit().putString("X5Debug", "X5Debug").commit();
//                Log.i("发送X5Debug", "");
//            }
//        });
//        ll_qingchu.setOnClickListener(this);
    }

    /**
     * 设置Toolbar
     */
    public void setToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tl_settings.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SettingsActivity.this.finish();
                }
            });
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        preferences = getSharedPreferences("content", MODE_PRIVATE);
        switch (view.getId()) {
            case R.id.ll_qingchu:
                preferences.edit().putString("delete", "delete").commit();
                Toast.makeText(SettingsActivity.this, "清理完成了^_^", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.btn_X5Debug:
//                preferences.edit().putString("X5Debug", "X5Debug").commit();
//                Log.i("发送X5Debug", "");
//                break;
        }
    }

    /**
     * 设置Switch按钮
     */
    public void setSwitch() {
        preferences = getSharedPreferences("content", MODE_PRIVATE);
        sh_zhuangtai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("Switch按钮", "");
                if (compoundButton.isChecked()) {
                    preferences.edit().putString("fullscreen", "open").putBoolean("flag", true).commit();

                } else {
                    preferences.edit().putString("fullscreen", "close").putBoolean("flag", false).commit();
                }
            }
        });
        Boolean flag = preferences.getBoolean("flag", false);
        if (flag.equals(true)) {
            sh_zhuangtai.setChecked(true);

        } else {
            sh_zhuangtai.setChecked(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        localBroadcastManager.unregisterReceiver(localOpenBcReceiver);
//        localBroadcastManager.unregisterReceiver(localCloseBcReceiver);
//        preferences.edit().remove("fullscreen").commit();

    }

    /**
     * 刷新Activity
     */
    public void onRefresh() {
        onCreate(null);
    }
}
