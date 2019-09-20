package com.myandroid.scrollandtabdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by John on 2017/10/31.
 *
 * 重写带有滑动监听的 ScrollView
 */
public class MyScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;
    private OnScrollViewTouchDown onScrollViewTouchDown = null;
    private int handlerWhatId = 65984;
    private int timeInterval = 20;
    private int lastY = 0;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == handlerWhatId) {
                if (lastY == getScrollY()) {
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStop(true);
                    }
                } else {
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStop(false);
                    }
                    handler.sendMessageDelayed(handler.obtainMessage(handlerWhatId, this), timeInterval);
                    lastY = getScrollY();
                }
            }
        }
    };

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 滚动监听
     */
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    /**
     * 自定义 ScrollView 滑动监听
     */
    public interface ScrollViewListener {
        /**
         * 滑动监听
         *
         * @param scrollView ScrollView控件
         * @param x          x轴坐标
         * @param y          y轴坐标
         * @param oldx       上一个x轴坐标
         * @param oldy       上一个y轴坐标
         */
        void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);

        /**
         * 是否滑动停止
         *
         * @param isScrollStop true:滑动停止;false:未滑动停止
         */
        void onScrollStop(boolean isScrollStop);
    }

    /**
     * 按下监听
     */
    public void setOnScrollViewTouchDown(OnScrollViewTouchDown onScrollViewTouchDown) {
        this.onScrollViewTouchDown = onScrollViewTouchDown;
    }

    /**
     * 自定义 ScrollView 触摸监听
     */
    public interface OnScrollViewTouchDown {
        /**
         * 按下监听
         *
         * @param isTouchDown 是否按下
         */
        void onTouchDown(boolean isTouchDown);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(handler.obtainMessage(handlerWhatId, this), timeInterval);
            if (onScrollViewTouchDown != null) {
                onScrollViewTouchDown.onTouchDown(true);
            }
        }
        return super.onTouchEvent(ev);
    }
}
