package com.example.gamesquery.fragment;

import android.widget.Toast;

import com.example.gamesquery.R;

public class FiveAFragment extends BaseFragment {
    @Override
    public Object replaceFragmentA() {
        Toast.makeText(getActivity(), "敬请期待。", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public Object replaceFragmentB() {
        Toast.makeText(getActivity(), "敬请期待。", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public int setQuestionText() {
        return R.string.Combat_simulation;
    }

    @Override
    public int setOpAText() {
        return R.string.Combat;
    }

    @Override
    public int setOpBText() {
        return R.string.simulation;
    }
}
