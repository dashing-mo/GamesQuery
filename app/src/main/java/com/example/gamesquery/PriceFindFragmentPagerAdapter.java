package com.example.gamesquery;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gamesquery.activity.FindActivity;
import com.example.gamesquery.fragment.SinaAppFragment;
import com.example.gamesquery.fragment.SteamDBFragment;

/**
 * @ 创建时间: 2019/7/8 on 17:25.
 * @ 描述: 自定义FragmentPagerAdapter类
 * @ 作者: 李琪
 */
public class PriceFindFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private SinaAppFragment sinaAppFragment;
    private SteamDBFragment steamDBFragment;


    public PriceFindFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        sinaAppFragment = new SinaAppFragment();
        steamDBFragment = new SteamDBFragment();

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case FindActivity.PAGE_ONE:
                fragment = sinaAppFragment;
                break;
            case FindActivity.PAGE_TWO:
                fragment = steamDBFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
