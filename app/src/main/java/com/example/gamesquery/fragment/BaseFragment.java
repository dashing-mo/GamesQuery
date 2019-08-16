package com.example.gamesquery.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gamesquery.R;

/**
 * @ 创建时间: 2019/6/26 on 16:25.
 * @ 描述: fragment基类
 * @ 作者: 李琪
 */

public class BaseFragment extends Fragment {
    private Button btn_optionA;
    private Button btn_optionB;
    private Button btn_optionC;
    private Button btn_optionD;
    private View view;
    private TextView tv_question;
    private TextView tv_explanation;
    private Activity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_questioncard, container, false);

        btn_optionA = view.findViewById(R.id.btn_optionA);
        btn_optionB = view.findViewById(R.id.btn_optionB);
        btn_optionC = view.findViewById(R.id.btn_optionC);
        btn_optionD = view.findViewById(R.id.btn_optionD);
        tv_explanation = view.findViewById(R.id.tv_explanation);
        tv_question = view.findViewById(R.id.tv_question);

        tv_question.setText(setQuestionText());
        tv_explanation.setText(setExplanationText());
        tv_explanation.setVisibility(setExpVisibility());
        btn_optionC.setVisibility(setOpCVisibility());
        btn_optionD.setVisibility(setOpDVisibility());
        btn_optionA.setText(setOpAText());
        btn_optionB.setText(setOpBText());
        btn_optionC.setText(setOpCText());
        btn_optionD.setText(setOpDText());

        btn_optionA.setOnClickListener(listener);
        btn_optionB.setOnClickListener(listener);
        btn_optionC.setOnClickListener(listener);
        btn_optionD.setOnClickListener(listener);
        return view;
    }


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_optionA:
//                        getFragmentManager().beginTransaction().replace(R.id.container,fragment,"1");
                    replaceFragmentA();
                    break;
                case R.id.btn_optionB:
//                        getFragmentManager().beginTransaction().replace(R.id.container,fragment,"2");
                    replaceFragmentB();
                    break;
                case R.id.btn_optionC:
//                        getFragmentManager().beginTransaction().replace(R.id.container,fragment,"3");
                    replaceFragmentC();
                    break;
                case R.id.btn_optionD:
//                        getFragmentManager().beginTransaction().replace(R.id.container,fragment,"4");
                    replaceFragmentD();
                    break;
            }
        }

    };

    /**
     * 替换当前的fragment
     */
    public Object replaceFragmentA() {
        return 0;
    }


    public Object replaceFragmentB() {
        return 0;
    }


    public Object replaceFragmentC() {
        return 0;
    }


    public Object replaceFragmentD() {
        return 0;
    }

    /**
     * 设置问题的内容
     */
    public int setQuestionText() {
        return R.string.like_game;
    }

    /**
     * 设置说明文字的内容
     */
    public int setExplanationText() {
        return R.string.main_annotate;
    }

    /**
     * 设置选项A B C D的内容
     */
    public int setOpAText() {
        return R.string.btn_like;
    }

    public int setOpBText() {
        return R.string.btn_dislike;
    }

    public String setOpCText() {
        return null;
    }

    public String setOpDText() {
        return null;
    }

    /**
     * 设置说明文字是否显示
     */
    public int setExpVisibility() {
        return View.INVISIBLE;
    }

    /**
     * 设置选项C是否显示
     */
    public int setOpCVisibility() {
        return View.INVISIBLE;
    }

    /**
     * 设置选项D是否显示
     */
    public int setOpDVisibility() {
        return View.INVISIBLE;
    }
}
