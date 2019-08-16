package com.example.gamesquery.bean;

public class PlatformBean {
    int icon;
    int tv_title;
    int tv_explain;

    public PlatformBean(int icon, int tv_title, int tv_explain) {
        this.icon = icon;
        this.tv_explain = tv_explain;
        this.tv_title = tv_title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTv_title() {
        return tv_title;
    }

    public void setTv_title(int tv_title) {
        this.tv_title = tv_title;
    }

    public int getTv_explain() {
        return tv_explain;
    }

    public void setTv_explain(int tv_explain) {
        this.tv_explain = tv_explain;
    }


}
