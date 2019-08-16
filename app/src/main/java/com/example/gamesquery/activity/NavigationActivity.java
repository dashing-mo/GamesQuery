package com.example.gamesquery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.gamesquery.NavigationPageAdapter;
import com.example.gamesquery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ 创建时间: 2019/7/2 on 17:25.
 * @ 描述: App引导页
 * @ 作者: 李琪
 */
public class NavigationActivity extends AppCompatActivity {
    private List<View> list;
    private List<ImageView> mDotViews;

    private int[] mLayoutIDs = {R.layout.item_viewpager_one, R.layout.item_viewpager_two, R.layout.item_viewpager_three};
    private ViewPager viewPager;
    private ViewGroup mDotViewGroup;
    private Button btn_Go;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏系统状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigation);
        viewPager = findViewById(R.id.vp_Navigation);
        mDotViewGroup = findViewById(R.id.dot_layout);
        btn_Go = findViewById(R.id.btn_Go);

//        getNavigationStatus();

        list = new ArrayList<>();
        mDotViews = new ArrayList<>();

        for (int i = 0; i < mLayoutIDs.length; i++) {
            View views = getLayoutInflater().inflate(mLayoutIDs[i], null);
            list.add(views);

            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.guide_dot_nomal);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            layoutParams.leftMargin = 20;
            dot.setLayoutParams(layoutParams);
            dot.setEnabled(false);

            mDotViewGroup.addView(dot);

            mDotViews.add(dot);
            viewPager.setOnPageChangeListener(listener);
        }

        viewPager.setAdapter(new NavigationPageAdapter(this, list));
        viewPager.setOffscreenPageLimit(3);

        viewPager.setCurrentItem(0);
        setDotView(0);

    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDotView(position);

            if (position == 2) {
                mDotViewGroup.setVisibility(View.INVISIBLE);
                btn_Go.setVisibility(View.VISIBLE);
                btn_Go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            } else {
                mDotViewGroup.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setDotView(int position) {
        for (int i = 0; i < mDotViews.size(); i++) {
            mDotViews.get(i).setImageResource(position == i ? R.drawable.guide_dot_select : R.drawable.guide_dot_nomal);
        }
    }


}
