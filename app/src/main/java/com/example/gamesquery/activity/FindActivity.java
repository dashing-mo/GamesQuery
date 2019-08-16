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

import com.example.gamesquery.FindFragmentPagerAdapter;
import com.example.gamesquery.R;
import com.example.gamesquery.Transform;
import com.example.gamesquery.fragment.BaiduFindFragment;
import com.example.gamesquery.fragment.SteamCNFindFragment;
import com.example.gamesquery.fragment.YouminFindFragment;
import com.example.gamesquery.fragment.YouxiaFindFragment;

/**
 * @ 创建时间: 2019/6/27 on 16:00.
 * @ 描述: App查找游戏的界面
 * @ 作者: 李琪
 */
public class FindActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup rg_tab_bar_find;
    private RadioButton rb_baidu_find;
    private RadioButton rb_youmin_find;
    private RadioButton rb_SteamCN_find;
    //    private RadioButton rb_sanDM_find;
    private RadioButton rb_youxia_find;

    private ViewPager vp_fragment_find;
    private FindFragmentPagerAdapter adapter;
    private FragmentManager fm;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
//    public static final int PAGE_FIVE = 4;

    private int offset = 0; //移动条图片的偏移量
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private int two = 0; //滑动条移动两页的距离
    private int three = 0; //滑动条移动三页的距离
    //    private int four = 0;//滑动条移动四页的距离
    private int currIndex = 0;//当前页面的编号
    private ImageView iv_cursor_find;
    private ImageView iv_dividing_line_find;
    private ImageView iv_back_find;
    private ImageView iv_close_find;

    private static long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        fm = getSupportFragmentManager();
        adapter = new FindFragmentPagerAdapter(fm);

        init();
        setDividingLine();
        setRadioGroup();
        setViewPager();

        rb_baidu_find.setChecked(true);

    }

    public void init() {
        rg_tab_bar_find = findViewById(R.id.rg_tab_bar_find);
        rb_baidu_find = findViewById(R.id.rb_baidu_find);
        rb_youmin_find = findViewById(R.id.rb_youmin_find);
        rb_SteamCN_find = findViewById(R.id.rb_SteamCN_find);
        rb_youxia_find = findViewById(R.id.rb_youxia_find);
//        rb_sanDM_find = findViewById(R.id.rb_sanDM_find);
        iv_back_find = findViewById(R.id.iv_back_find);
        iv_close_find = findViewById(R.id.iv_close_find);
        iv_dividing_line_find = findViewById(R.id.iv_dividing_line_find);
        rg_tab_bar_find = findViewById(R.id.rg_tab_bar_find);

        iv_back_find.setOnClickListener(this);
        iv_close_find.setOnClickListener(this);
    }

    /**
     * 单击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_find:
                BaiduFindFragment baiduFindFragment = new BaiduFindFragment();
                YouxiaFindFragment youxiaFindFragment = new YouxiaFindFragment();
                YouminFindFragment youminFindFragment = new YouminFindFragment();
                SteamCNFindFragment steamCNFindFragment = new SteamCNFindFragment();
                if (baiduFindFragment.onBackPressed()) {
                    return;
                } else if (youxiaFindFragment.onBackPressed()) {
                    return;
                } else if (youminFindFragment.onBackPressed()) {
                    return;
                } else if (steamCNFindFragment.onBackPressed()) {
                    return;
                } else {
                    finish();
                }
                break;
            case R.id.iv_close_find:
                finish();
                break;
        }
    }


    /**
     * 设置下划线
     */
    public void setDividingLine() {
        int left_width = Transform.dip2px(this, 10);//导航图标左边距。单位px
        int right_width = Transform.dip2px(this, 10);//搜索图标右边距
        int iv_back_find_width = Transform.dip2px(this, 30);//后退图标宽度
        int iv_close_find_width = Transform.dip2px(this, 30);//关闭图标宽度
        //下划线动画的相关设置
        iv_cursor_find = findViewById(R.id.iv_cursor_find);
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.red).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / 5 - bmpWidth) / 2;// 计算偏移量
        offset = ((screenW - left_width - right_width - iv_back_find_width - iv_close_find_width) / 4 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_cursor_find.setImageMatrix(matrix);// 设置动画初始位置

        //下划线移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量
        two = one * 2;// 移动两页的偏移量
        three = one * 3;
//        four = one * 4;
    }

    /**
     * 设置单选按钮
     */
    public void setRadioGroup() {
        rg_tab_bar_find.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_baidu_find:
                        vp_fragment_find.setCurrentItem(PAGE_ONE);
                        break;
                    case R.id.rb_youmin_find:
                        vp_fragment_find.setCurrentItem(PAGE_TWO);
                        break;
                    case R.id.rb_youxia_find:
                        vp_fragment_find.setCurrentItem(PAGE_THREE);
                        break;
                    case R.id.rb_SteamCN_find:
                        vp_fragment_find.setCurrentItem(PAGE_FOUR);
                        break;
                }
            }
        });
    }

    /**
     * 设置ViewPager
     */
    public void setViewPager() {
        vp_fragment_find = findViewById(R.id.vp_fragment_find);
        vp_fragment_find.setAdapter(adapter);
        vp_fragment_find.setOffscreenPageLimit(3);
        vp_fragment_find.setCurrentItem(0);//设置ViewPager当前页，从0开始算
        vp_fragment_find.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        } else if (currIndex == PAGE_THREE) {
                            animation = new TranslateAnimation(two, 0, 0, 0);
                        } else if (currIndex == PAGE_FOUR) {
                            animation = new TranslateAnimation(three, 0, 0, 0);
                        }
//                        else if (currIndex == PAGE_FIVE) {
//                            animation = new TranslateAnimation(four, 0, 0, 0);
//                        }
                        break;
                    case PAGE_TWO:
                        if (currIndex == PAGE_ONE) {
                            animation = new TranslateAnimation(offset, one, 0, 0);
                        } else if (currIndex == PAGE_THREE) {
                            animation = new TranslateAnimation(two, one, 0, 0);
                        } else if (currIndex == PAGE_FOUR) {
                            animation = new TranslateAnimation(three, one, 0, 0);
                        }
//                        else if (currIndex == PAGE_FIVE) {
//                            animation = new TranslateAnimation(four, one, 0, 0);
//                        }
                        break;
                    case PAGE_THREE:
                        if (currIndex == PAGE_ONE) {
                            animation = new TranslateAnimation(offset, two, 0, 0);
                        } else if (currIndex == PAGE_TWO) {
                            animation = new TranslateAnimation(one, two, 0, 0);
                        } else if (currIndex == PAGE_FOUR) {
                            animation = new TranslateAnimation(three, two, 0, 0);
                        }
//                        else if (currIndex == PAGE_FIVE) {
//                            animation = new TranslateAnimation(four, two, 0, 0);
//                        }
                        break;
                    case PAGE_FOUR:
                        if (currIndex == PAGE_ONE) {
                            animation = new TranslateAnimation(offset, three, 0, 0);
                        } else if (currIndex == PAGE_TWO) {
                            animation = new TranslateAnimation(one, three, 0, 0);
                        } else if (currIndex == PAGE_THREE) {
                            animation = new TranslateAnimation(two, three, 0, 0);
                        }
//                        else if (currIndex == PAGE_FIVE) {
//                            animation = new TranslateAnimation(four, three, 0, 0);
//                        }
                        break;
//                    case PAGE_FIVE:
//                        if (currIndex == PAGE_ONE) {
//                            animation = new TranslateAnimation(offset, four, 0, 0);
//                        } else if (currIndex == PAGE_TWO) {
//                            animation = new TranslateAnimation(one, four, 0, 0);
//                        } else if (currIndex == PAGE_THREE) {
//                            animation = new TranslateAnimation(two, four, 0, 0);
//                        } else if (currIndex == PAGE_FOUR) {
//                            animation = new TranslateAnimation(three, four, 0, 0);
//                        }
//                        break;


                }
                currIndex = position;
                animation.setFillAfter(true);//true表示图片停在动画结束位置
                animation.setDuration(200);//设置动画时间为200毫秒
                iv_cursor_find.startAnimation(animation);//开始动画
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (vp_fragment_find.getCurrentItem()) {
                        case PAGE_ONE:
                            rb_baidu_find.setChecked(true);
                            break;
                        case PAGE_TWO:
                            rb_youmin_find.setChecked(true);
                            break;
                        case PAGE_THREE:
                            rb_youxia_find.setChecked(true);
                            break;
                        case PAGE_FOUR:
//                            rb_sanDM_find.setChecked(true);
                            rb_SteamCN_find.setChecked(true);
                            break;
//                        case PAGE_FIVE:
//                            rb_SteamCN_find.setChecked(true);
//                            break;
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
     * 页面后退判断
     */
    @Override
    public void onBackPressed() {
        BaiduFindFragment baiduFindFragment = new BaiduFindFragment();
        YouxiaFindFragment youxiaFindFragment = new YouxiaFindFragment();
        YouminFindFragment youminFindFragment = new YouminFindFragment();
        SteamCNFindFragment steamCNFindFragment = new SteamCNFindFragment();

        if (baiduFindFragment.onBackPressed()) {
            return;
        } else if (youxiaFindFragment.onBackPressed()) {
            return;
        } else if (youminFindFragment.onBackPressed()) {
            return;
        } else if (steamCNFindFragment.onBackPressed()) {
            return;
        } else {
            finish();
        }
    }
}