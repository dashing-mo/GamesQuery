package com.example.gamesquery.fragment;

import android.widget.Toast;

import com.example.gamesquery.R;

public class FourAFragment extends BaseFragment {
    @Override
    public Object replaceFragmentA() {
        getFragmentManager().beginTransaction().replace(R.id.container, new FiveAFragment(), "4A").addToBackStack(null).commit();
        return null;
    }

    @Override
    public Object replaceFragmentB() {
        Toast.makeText(getActivity(), "试着接受吧。", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public int setQuestionText() {
        return R.string.Two_dimensional_game;
    }

    @Override
    public int setOpAText() {
        return R.string.btn_like;
    }

    @Override
    public int setOpBText() {
        return R.string.btn_dislike;
    }
}
