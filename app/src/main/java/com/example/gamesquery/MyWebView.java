package com.example.gamesquery;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;


/**
 * @ 创建时间: 2019/7/3 on 18:25.
 * @ 描述: 自定义MyWebView,WebView滚动事件的监听
 * @ 作者: 李琪
 */
public class MyWebView extends WebView {
    private OnScrollChangedCallback onScrollChangedCallback;
    private OnDrawListener onDrawListener;
    private SharedPreferences preferences;
    private static String X5Debug;

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return onScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollChangedCallback) {
        this.onScrollChangedCallback = onScrollChangedCallback;
    }

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedCallback != null) {
            onScrollChangedCallback.onScroll(l, t);
        }
    }

    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onDrawListener != null) {
            onDrawListener.onDrawCallBack();
        }
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public interface OnDrawListener {
        public void onDrawCallBack();
    }

    public static String setX5Debug(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        X5Debug = preferences.getString("X5Debug", "");
        return X5Debug;
    }

//    /**
//     * 腾讯X5内核调试
//     */
//    @Override
//    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        boolean ret = super.drawChild(canvas, child, drawingTime);
//        canvas.save();
//        Paint paint = new Paint();
//        paint.setColor(0x7fff0000);
//        paint.setTextSize(50.f);
//        paint.setAntiAlias(true);
//
////        if (X5Debug.equals("X5Debug")) {
//            if (getX5WebViewExtension() != null) {
//                canvas.drawText(this.getContext().getPackageName() + "-pid:"
//                        + android.os.Process.myPid(), 10, 50, paint);
//                canvas.drawText(
//                        "X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
//                        100, paint);
//            } else {
//                canvas.drawText(this.getContext().getPackageName() + "-pid:"
//                        + android.os.Process.myPid(), 10, 50, paint);
//                canvas.drawText("Sys Core", 10, 100, paint);
//            }
//
//        canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
//        canvas.drawText(Build.MODEL, 10, 200, paint);
//        canvas.restore();
////        }
//        return ret;
//    }
}
