package com.example.gamesquery;

import com.example.gamesquery.activity.LibraryActivity;
import com.example.gamesquery.activity.NewsActivity;
import com.example.gamesquery.activity.PersonalActivity;
import com.example.gamesquery.activity.PriceFindActivity;
/**
 * @ 创建时间: 2019/7/3 on 17:25.
 * @ 描述: TabHost的常量类
 * @ 作者: 李琪
 */
public class Constant {
    public static final class ConValus {
        /*
         * tab选项卡的图标布局，带有选中和未选中的图片、文字切换
         * */
        public static int mLayoutViewArray[] = {
                R.layout.news_tab_layout,
                R.layout.library_tab_layout,
                R.layout.price_tab_layout,
                R.layout.personal_tab_layout
        };
        /*
         * tab选项卡的文字
         * */
        public static String mTextViewArray[] = {
                "新闻资讯", "游戏库", "查价格", "我的"
        };

        /*
         * 每个tab显示的activity页面
         * */
        public static Class<?> mTabClassArray[] = {
                NewsActivity.class,
                LibraryActivity.class,
                PriceFindActivity.class,
                PersonalActivity.class
        };
    }
}
