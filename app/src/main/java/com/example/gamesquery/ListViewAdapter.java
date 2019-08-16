package com.example.gamesquery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamesquery.bean.PlatformBean;

import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter {
    private LinkedList<PlatformBean> linkedList;
    private Context context;

    public ListViewAdapter(LinkedList<PlatformBean> linkedList, Context context) {
        this.linkedList = linkedList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return linkedList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_platform, viewGroup, false);
            holder = new ViewHolder();
            holder.icon = view.findViewById(R.id.iv_icon);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.tv_explain = view.findViewById(R.id.tv_explain);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.icon.setBackgroundResource(linkedList.get(i).getIcon());
        holder.tv_title.setText(linkedList.get(i).getTv_title());
        holder.tv_explain.setText(linkedList.get(i).getTv_explain());
        return view;
    }

    static class ViewHolder {
        ImageView icon;
        TextView tv_title;
        TextView tv_explain;
    }

}
