package com.example.gamesquery.activity;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.gamesquery.PriceFindFragmentPagerAdapter;
import com.example.gamesquery.R;
import com.example.gamesquery.Transform;
import com.example.gamesquery.fragment.SinaAppFragment;
import com.example.gamesquery.fragment.SteamDBFragment;

/**
 * @ 创建时间: 2019/7/8 on 20:00.
 * @ 描述: App价格信息显示界面
 * @ 作者: 李琪
 */
public class PriceActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup rg_tab_bar_price;
    private RadioButton rb_sinaapp_price;
    private RadioButton rb_steamdb_price;

    private ViewPager vp_fragment_price;
    private PriceFindFragmentPagerAdapter adapter;
    private FragmentManager fm;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    private int offset = 0; //移动条图片的偏移量
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private int currIndex = 0;//当前页面的编号
    private ImageView iv_cursor_price;

    private ImageView iv_back_price;
    private ImageView iv_close_price;
    private SteamDBFragment steamDBFragment;
    private SinaAppFragment sinaAppFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        init();
        setCursor();
        setViewPager();
        steamDBFragment = new SteamDBFragment();
        sinaAppFragment = new SinaAppFragment();
    }

    /**
     * 初始化控件
     */
    public void init() {
        iv_back_price = findViewById(R.id.iv_back_price);
        iv_close_price = findViewById(R.id.iv_close_price);
        rb_sinaapp_price = findViewById(R.id.rb_sinaapp_price);
        rb_steamdb_price = findViewById(R.id.rb_steamdb_price);
        iv_cursor_price = findViewById(R.id.iv_cursor_price);
        rg_tab_bar_price = findViewById(R.id.rg_tab_bar_price);
        vp_fragment_price = findViewById(R.id.vp_fragment_price);

        iv_back_price.setOnClickListener(this);
        iv_close_price.setOnClickListener(this);
    }

    /**
     * 单击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_price:
                if (steamDBFragment.onBackPressed()) {
                    return;
                } else if (sinaAppFragment.onBackPressed()) {
                    return;
                } else {
                    finish();
                }
                break;
            case R.id.iv_close_price:
                finish();
                break;
        }
    }

    /**
     * 设置下划线
     */
    public void setCursor() {
        //下划线动画的相关设置
        int left_width = Transform.dip2px(this, 10);//导航图标左边距。单位px
        int right_width = Transform.dip2px(this, 10);//搜索图标右边距
        int iv_back_width = Transform.dip2px(this, 30);//导航图标宽度
        int iv_close_width = Transform.dip2px(this, 30);//搜索图标宽度
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.red).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = ((screenW - left_width - right_width - iv_back_width - iv_close_width) / 2 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_cursor_price.setImageMatrix(matrix);// 设置动画初始位置

        //下划线移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量

        rg_tab_bar_price.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_sinaapp_price:
                        vp_fragment_price.setCurrentItem(PAGE_ONE);
                        break;
                    case R.id.rb_steamdb_price:
                        vp_fragment_price.setCurrentItem(PAGE_TWO);
                        break;
                }
            }
        });
    }

    /**
     * 设置ViewPager
     */
    public void setViewPager() {
        fm = getSupportFragmentManager();
        adapter = new PriceFindFragmentPagerAdapter(fm);
        vp_fragment_price.setAdapter(adapter);
        vp_fragment_price.setCurrentItem(0);//设置ViewPager当前页，从0开始算
        vp_fragment_price.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Animation animation = null;
                switch (position) {
                    case PAGE_ONE:
                        if (currIndex == PAGE_TWO) {
                            animation = new TranslateAnimation(one, 0, 0, 0);
                        }
                        break;
                    case PAGE_TWO:
                        if (currIndex == PAGE_ONE) {
                            animation = new TranslateAnimation(offset, one, 0, 0);
                        }
                        break;
                }
                currIndex = position;
                animation.setFillAfter(true);//true表示图片停在动画结束位置
                animation.setDuration(200);//设置动画时间为200毫秒
                iv_cursor_price.startAnimation(animation);//开始动画
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (vp_fragment_price.getCurrentItem()) {
                        case PAGE_ONE:
                            rb_sinaapp_price.setChecked(true);
                            break;
                        case PAGE_TWO:
                            rb_steamdb_price.setChecked(true);
                            break;
                    }
                }
            }
        });
    }
//    //页面后退
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            finish();
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 退出键设置
     */
    @Override
    public void onBackPressed() {
        if (steamDBFragment.onBackPressed()) {
            return;
        } else if (sinaAppFragment.onBackPressed()) {
            return;
        } else {
            finish();
        }

    }
}
