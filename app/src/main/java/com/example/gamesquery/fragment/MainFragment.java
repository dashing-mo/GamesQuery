package com.example.gamesquery.fragment;

import android.view.View;
import android.widget.Toast;

import com.example.gamesquery.R;

public class MainFragment extends BaseFragment {

    @Override
    public Object replaceFragmentA() {
        return getFragmentManager().beginTransaction().replace(R.id.container, new TwoAFragment(), "2A").addToBackStack(null).commit();

    }

    @Override
    public Object replaceFragmentB() {
        Toast.makeText(getActivity(), "所以你点进来的目的是什么= =。", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public int setExplanationText() {
        return R.string.main_annotate;
    }

    @Override
    public int setExpVisibility() {
        return View.VISIBLE;
    }
}
