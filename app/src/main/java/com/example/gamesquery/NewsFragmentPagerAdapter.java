package com.example.gamesquery;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gamesquery.activity.NewsActivity;
import com.example.gamesquery.fragment.JiheNewsFragment;
import com.example.gamesquery.fragment.SteamCNNewsFragment;
import com.example.gamesquery.fragment.YouminNewsFragment;

/**
 * @ 创建时间: 2019/7/4 on 17:25.
 * @ 描述: 自定义FragmentPagerAdapter类
 * @ 作者: 李琪
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
//    private final int PAGER_COUNT = 4;
    private final int PAGER_COUNT = 3;
    private YouminNewsFragment youminNewsFragment = null;
    private SteamCNNewsFragment steamCNNewsFragment = null;
    private JiheNewsFragment jiheNewsFragment = null;

    public NewsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        youminNewsFragment = new YouminNewsFragment();
        steamCNNewsFragment = new SteamCNNewsFragment();
        jiheNewsFragment = new JiheNewsFragment();

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case NewsActivity.PAGE_ONE:
                fragment = youminNewsFragment;
                break;
            case NewsActivity.PAGE_TWO:
                fragment = steamCNNewsFragment;
                break;
            case NewsActivity.PAGE_THREE:
                fragment = jiheNewsFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

}
