package com.example.gamesquery.fragment;

import android.widget.Toast;

import com.example.gamesquery.R;

public class TwoAFragment extends BaseFragment {


    @Override
    public Object replaceFragmentA() {
        Toast.makeText(getActivity(), "敬请期待。", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public Object replaceFragmentB() {
        getFragmentManager().beginTransaction().replace(R.id.container, new ThreeBFragment(), "2A").addToBackStack(null).commit();
        return null;
    }

    @Override
    public int setQuestionText() {
        return R.string.daily_preference;
    }

    @Override
    public int setOpAText() {
        return R.string.btn_Online_game;
    }

    @Override
    public int setOpBText() {
        return R.string.btn_Stand_alone_game;
    }
}
