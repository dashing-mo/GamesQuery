package com.example.gamesquery.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.gamesquery.R;

public class PersonalActivity extends BaseActivity {
    // 用来计算返回键的点击间隔时间
    private static long exitTime = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }
    //页面后退或APP退出判断
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
