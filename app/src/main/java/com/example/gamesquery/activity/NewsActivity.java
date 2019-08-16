package com.example.gamesquery.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.gamesquery.EditTextWithDel;
import com.example.gamesquery.NewsFragmentPagerAdapter;
import com.example.gamesquery.R;
import com.example.gamesquery.Transform;

/**
 * @ 创建时间: 2019/7/3 on 21:00.
 * @ 描述: App游戏资讯界面
 * @ 作者: 李琪
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup rg_tab_bar;
    private RadioButton rb_youmin;
    private RadioButton rb_jihe;
    private RadioButton rb_SteamCN;
//    private RadioButton rb_shiguang;

    private EditTextWithDel et_find_news;
    private ImageView iv_find_news;
    private TextView tv_find_news;
    private String value;
    private SharedPreferences preferences;

    private ViewPager vp_fragment;
    private NewsFragmentPagerAdapter adapter;
    private FragmentManager fm;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
//    public static final int PAGE_FOUR = 3;

    private int offset = 0; //移动条图片的偏移量
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private int two = 0; //滑动条移动两页的距离
    //    private int three = 0; //滑动条移动三页的距离
    private int currIndex = 0;//当前页面的编号
    private ImageView iv_cursor;

    private static long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//视频为了避免闪屏和透明问题
        setContentView(R.layout.activity_news);
        fm = getSupportFragmentManager();
        adapter = new NewsFragmentPagerAdapter(fm);
        init();
        setCursor();
        setViewPager();
        setHintTextSize(et_find_news, "请输入搜索内容", 13);
        rb_youmin.setChecked(true);

    }

    /**
     * 初始化控件
     */
    public void init() {
        rg_tab_bar = findViewById(R.id.rg_tab_bar);
        rb_jihe = findViewById(R.id.rb_jihe);
        rb_youmin = findViewById(R.id.rb_youmin);
        rb_SteamCN = findViewById(R.id.rb_SteamCN);
//        rb_shiguang = findViewById(R.id.rb_shiguang);

        et_find_news = findViewById(R.id.et_find_news);
        iv_find_news = findViewById(R.id.iv_find_news);
        tv_find_news = findViewById(R.id.tv_find_news);

        iv_find_news.setOnClickListener(this);
        tv_find_news.setOnClickListener(this);
        et_find_news.setOnFocusChangeListener(listener);
        et_find_news.setOnEditorActionListener(listener2);

    }

    /**
     * 输入框焦点变化监听器
     */
    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b == false) {
                et_find_news.setVisibility(View.GONE);
                rg_tab_bar.setVisibility(View.VISIBLE);
                iv_cursor.setVisibility(View.VISIBLE);
                tv_find_news.setVisibility(View.GONE);
                iv_find_news.setVisibility(View.VISIBLE);

            }
        }
    };
    /**
     * 输入法右下角单击按钮监听器
     */
    TextView.OnEditorActionListener listener2 = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            JudgeText();
            return true;
        }
    };

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_find_news:
                rg_tab_bar.setVisibility(View.GONE);
                et_find_news.setVisibility(View.VISIBLE);
                iv_cursor.setVisibility(View.INVISIBLE);
                iv_find_news.setVisibility(View.GONE);
                tv_find_news.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_find_news:
                et_find_news.setVisibility(View.GONE);
                rg_tab_bar.setVisibility(View.VISIBLE);
                iv_cursor.setVisibility(View.VISIBLE);
                tv_find_news.setVisibility(View.GONE);
                iv_find_news.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 设置ViewPager
     */
    public void setViewPager() {
        vp_fragment = findViewById(R.id.vp_fragment);
        vp_fragment.setAdapter(adapter);
        vp_fragment.setCurrentItem(0);
        //增加缓冲页面(当前页的前2页和后2页)
        vp_fragment.setOffscreenPageLimit(2);
        vp_fragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        }
                        break;
                    case PAGE_TWO:
                        if (currIndex == PAGE_ONE) {
                            animation = new TranslateAnimation(offset, one, 0, 0);
                        } else if (currIndex == PAGE_THREE) {
                            animation = new TranslateAnimation(two, one, 0, 0);
                        }
                        break;
                    case PAGE_THREE:
                        if (currIndex == PAGE_ONE) {
                            animation = new TranslateAnimation(offset, two, 0, 0);
                        } else if (currIndex == PAGE_TWO) {
                            animation = new TranslateAnimation(one, two, 0, 0);
                        }
                        break;


                }
                currIndex = position;
                animation.setFillAfter(true);//动画结束后停留在目前的位置
                animation.setDuration(200);
                iv_cursor.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (vp_fragment.getCurrentItem()) {
                        case PAGE_ONE:
                            rb_youmin.setChecked(true);
                            break;
                        case PAGE_TWO:
                            rb_SteamCN.setChecked(true);
                            break;
                        case PAGE_THREE:
                            rb_jihe.setChecked(true);
                            break;
                    }
                }
            }
        });
    }

    /**
     * 设置下划线
     */
    public void setCursor() {
        int left_width = Transform.dip2px(this, 10);//导航图标左边距。单位px
        int right_width = Transform.dip2px(this, 10);//搜索图标右边距
        int iv_dehaze_news_width = Transform.dip2px(this, 30);//导航图标宽度
        int iv_find_news_width = Transform.dip2px(this, 30);//搜索图标宽度
        Log.i("i宽度:", String.valueOf(iv_dehaze_news_width));
        //下划线动画的相关设置
        iv_cursor = findViewById(R.id.iv_cursor);
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.red).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        Log.i("屏幕分辨率宽度", String.valueOf(screenW));
//        offset = ((screenW - (iv_dehaze_news_width + left_width + right_width + iv_find_news_width)) / 4 - bmpWidth) / 2;// 计算偏移量
        offset = ((screenW - (iv_dehaze_news_width + left_width + right_width + iv_find_news_width)) / 3 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_cursor.setImageMatrix(matrix);// 设置动画初始位置

        //下划线移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量
        two = one * 2;// 移动两页的偏移量
//        three = one * 3;

        rg_tab_bar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_youmin:
                        vp_fragment.setCurrentItem(PAGE_ONE);
                        break;
                    case R.id.rb_SteamCN:
                        vp_fragment.setCurrentItem(PAGE_TWO);
                        break;
                    case R.id.rb_jihe:
                        vp_fragment.setCurrentItem(PAGE_THREE);
                        break;
//                    case R.id.rb_shiguang:
//                        vp_fragment.setCurrentItem(PAGE_FOUR);
//                        break;
                }
            }
        });
    }

    /**
     * 判断输入内容是否为空
     */
    public void JudgeText() {
        value = et_find_news.getText().toString().trim();
        if (value.isEmpty()) {
            Toast.makeText(NewsActivity.this, "输入内容不能为空(●'◡'●)", Toast.LENGTH_SHORT).show();
        } else {
            preferences = getSharedPreferences("content", MODE_PRIVATE);
            preferences.edit().putString("neirong", value).commit();

            Intent intent = new Intent(NewsActivity.this, FindActivity.class);
            startActivity(intent);
            et_find_news.setVisibility(View.GONE);
            tv_find_news.setVisibility(View.GONE);
            iv_find_news.setVisibility(View.VISIBLE);
            et_find_news.setText("");
        }
    }

    /**
     * 设置 hint字体大小
     *
     * @param editText 输入控件
     * @param hintText hint的文本内容
     * @param textSize hint的文本的文字大小（以dp为单位设置即可）
     */
    public static void setHintTextSize(EditText editText, String hintText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hintText);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

//    /**
//     * APP后退或退出判断
//     */
//    @Override
//    public void onBackPressed() {
//        SteamCNNewsFragment steamCNNewsFragment = new SteamCNNewsFragment();
//
//        if (steamCNNewsFragment.onBackPressed()) {
//            return;
//        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
//            //弹出提示，可以有多种方式
//            Toast.makeText(getApplicationContext(), "再按一次退出程序!", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
//            NewsActivity.this.finish();
//            System.exit(0);
//        }
//
//    }

//    //APP退出判断
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                //弹出提示，可以有多种方式
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                NewsActivity.this.finish();
//            }
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}

