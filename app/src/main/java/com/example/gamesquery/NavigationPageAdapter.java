package com.example.gamesquery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @ 创建时间: 2019/7/4 on 09:25.
 * @ 描述: 引导页的自定义适配器
 * @ 作者: 李琪
 */
public class NavigationPageAdapter extends PagerAdapter {
    private List<View> viewLists;
    private Context context;

    public NavigationPageAdapter(Context context,List<View> viewLists) {
        this.viewLists = viewLists;
        this.context=context;
    }


    @Override
    public int getCount() {
        return viewLists.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewLists.get(position));
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(@NonNull final View container, final int position, @NonNull final Object object) {
        container.removeOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                destroyItem(container, position, object);
            }
        });
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
