package com.example.gamesquery.fragment;

import com.example.gamesquery.R;

public class ThreeBFragment extends BaseFragment {
    @Override
    public Object replaceFragmentA() {
        getFragmentManager().beginTransaction().replace(R.id.container, new FourAFragment(), "3B").addToBackStack(null).commit();
        return null;
    }

    @Override
    public Object replaceFragmentB() {
        getFragmentManager().beginTransaction().replace(R.id.container, new FourBFragment(), "3B").addToBackStack(null).commit();
        return null;
    }

    @Override
    public int setQuestionText() {
        return R.string.Three_dimensional_vertigo;
    }

    @Override
    public int setOpAText() {
        return R.string.btn_Yes;
    }

    @Override
    public int setOpBText() {
        return R.string.btn_No;
    }
}
