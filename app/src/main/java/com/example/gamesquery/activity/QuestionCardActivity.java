package com.example.gamesquery.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.gamesquery.R;
import com.example.gamesquery.StatusBarUtil;
import com.example.gamesquery.fragment.MainFragment;

import java.util.List;

/**
 * @ 创建时间: 2019/7/3 on 08:25.
 * @ 描述: App问题卡界面
 * @ 作者: 李琪
 */
public class QuestionCardActivity extends BaseActivity {
    private Toolbar toolbar;
    private MainFragment mainFragment;
    private List<Fragment> current;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        //将屏幕设置为全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioncard);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);

        toolbar = findViewById(R.id.tl_question);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.inflateMenu(R.menu.menu_question);
        toolbar.setNavigationOnClickListener(listener);
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment, "main").commit();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_close:
                        QuestionCardActivity.this.finish();
                        break;
                }
                return false;
            }
        });
    }

    //后退按钮，判断当前是否在显示MainFragment界面
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            current = getSupportFragmentManager().getFragments();
            for (Fragment fragment : current) {
                if (fragment instanceof MainFragment) {
                    QuestionCardActivity.this.finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }

        }
    };
}

