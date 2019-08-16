package com.example.gamesquery;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gamesquery.activity.FindActivity;
import com.example.gamesquery.fragment.BaiduFindFragment;
import com.example.gamesquery.fragment.SteamCNFindFragment;
import com.example.gamesquery.fragment.YouminFindFragment;
import com.example.gamesquery.fragment.YouxiaFindFragment;

/**
 * @ 创建时间: 2019/7/4 on 17:25.
 * @ 描述: 自定义FragmentPagerAdapter类
 * @ 作者: 李琪
 */
public class FindFragmentPagerAdapter extends FragmentPagerAdapter {
//    private final int PAGER_COUNT = 5;
    private final int PAGER_COUNT = 4;
    private YouminFindFragment youminFindFragment;
    private BaiduFindFragment baiduFindFragment;
    private SteamCNFindFragment steamCNFindFragment;
    private YouxiaFindFragment youxiaFindFragment;

    public FindFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        youminFindFragment = new YouminFindFragment();
        baiduFindFragment = new BaiduFindFragment();
        steamCNFindFragment = new SteamCNFindFragment();
        youxiaFindFragment = new YouxiaFindFragment();

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case FindActivity.PAGE_ONE:
                fragment = baiduFindFragment;
                break;
            case FindActivity.PAGE_TWO:
                fragment = youminFindFragment;
                break;
            case FindActivity.PAGE_THREE:
                fragment = youxiaFindFragment;
                break;
            case FindActivity.PAGE_FOUR:
                fragment = steamCNFindFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
