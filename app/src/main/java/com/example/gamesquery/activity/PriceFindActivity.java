package com.example.gamesquery.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.gamesquery.EditTextWithDel;
import com.example.gamesquery.R;

/**
 * @ 创建时间: 2019/7/3 on 21:00.
 * @ 描述: App价格查询界面
 * @ 作者: 李琪
 */
public class PriceFindActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_price;
    private EditTextWithDel et_price;
    private String value;

    private SharedPreferences preferences;

    // 用来计算返回键的点击间隔时间
    private static long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_find);
        init();

    }

    /**
     * 控件初始化
     */
    public void init() {
        tv_price = findViewById(R.id.tv_price);
        et_price = findViewById(R.id.et_price);
        tv_price.setOnClickListener(this);
        //小键盘action事件监听器
        et_price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                JudgeText();
                return true;
            }
        });
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        JudgeText();
    }

    /**
     * 判断输入内容是否为空
     */
    public void JudgeText() {
        value = et_price.getText().toString().trim();
        if (value.isEmpty()) {
            Toast.makeText(this, "输入内容不能为空(●'◡'●)", Toast.LENGTH_SHORT).show();
        } else {
            preferences = getSharedPreferences("content", MODE_PRIVATE);
            preferences.edit().putString("price", value).commit();

            Intent intent = new Intent(this, PriceActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 页面后退或APP退出判断
     *
     * @param keyCode
     * @param event
     * @return
     */
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
