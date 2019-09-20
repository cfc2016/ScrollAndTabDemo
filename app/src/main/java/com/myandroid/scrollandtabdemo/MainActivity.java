package com.myandroid.scrollandtabdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;

/**
 * Created by John on 2017/10/31.
 * <p>
 * 上下滑动标题联动、标题栏固定到顶部 效果
 */
public class MainActivity extends Activity{
    // 滑动 scrollview
    private MyScrollView my_scrollview;
    // 固定标题和滑动标题 LinearLayout
    private LinearLayout ll_title_in_scrollview, ll_title_top;
    // ScrollView 中的标题
    private MagicIndicator magic_indicator,magic_indicator2;
    // 动画图片
    private ImageView imgv_cursor;
    // 动画图片偏移量
    private int offset = 0;
    // 上一个界面 id
    private int lastTabIndex = 0;
    RecyclerView recyclerView;
    List<DemoBean> list;
    RecyclerView.LayoutManager linearLayoutManager;
    LinearLayoutManager linearManager;
    TextviewAdapter testAdapter;
    /**
     * 用于在同一个内容模块内滑动，锁定导航标签，防止重复刷新标签
     * true: 锁定
     * false ; 没有锁定
     */
    private boolean content2NavigateFlagInnerLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addClickListener();
        addScrollListener();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        ll_title_in_scrollview = findViewById(R.id.ll_title_in_scrollview);
        ll_title_top = findViewById(R.id.ll_title_top);
        magic_indicator = findViewById(R.id.magic_indicator);
        magic_indicator2 = findViewById(R.id.magic_indicator2);


        recyclerView = findViewById(R.id.recycler_view);
        my_scrollview = findViewById(R.id.my_scrollview);



        list = new ArrayList<DemoBean>();
        DemoBean bean1=new DemoBean();
        bean1.setName("标题1");
        DemoBean bean2=new DemoBean();
        bean2.setName("标题2");
        DemoBean bean3=new DemoBean();
        bean3.setName("标题3");
        DemoBean bean4=new DemoBean();
        bean4.setName("标题4");
        DemoBean bean5=new DemoBean();
        bean5.setName("标题5");
        DemoBean bean6=new DemoBean();
        bean6.setName("标题6");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);
        testAdapter = new TextviewAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);
        linearLayoutManager=recyclerView.getLayoutManager();
        linearManager = (LinearLayoutManager) linearLayoutManager;
        int height = linearManager.getHeight();
        my_scrollview.smoothScrollTo(0, 0);
        //取消recycleview的滑动
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


    }


    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private int num=1;
    /**
     * 添加标题点击监听
     */
    private void addClickListener() {

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(list.get(index).getName());
                clipPagerTitleView.setTextColor(Color.BLACK);
                clipPagerTitleView.setClipColor(Color.parseColor("#e94220"));

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (num==1){
                            int height = recyclerView.getChildAt(list.size()-1).getHeight();
                            int statusHeight = ScreenUtils.getStatusBarHeight(context);
                            int LastHeight = ScreenUtils.getScreenHeight(context)-(height/3)-(statusHeight+44);
                            for (int i=0;i<list.size();i++){
                                DemoBean demoBean = list.get(i);
                                demoBean.setHeight(LastHeight);
                            }
                            testAdapter.notifyDataSetChanged();
                            num++;
                        }
                        mFragmentContainerHelper.handlePageSelected(index);
                        int tops = recyclerView.getTop();
                        int topN = 0;
                        topN = recyclerView.getChildAt(index).getTop();
                        my_scrollview.smoothScrollTo(0, tops + topN - ll_title_in_scrollview.getHeight());
                        // switchPages(index);
                    }
                });



                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);

                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(80f);
                indicator.setRoundRadius(4f);
                indicator.setColors(Color.parseColor("#bc2a2a"));
                return indicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magic_indicator);


        CommonNavigator commonNavigator2 = new CommonNavigator(this);
        commonNavigator2.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(list.get(index).getName());
                clipPagerTitleView.setTextColor(Color.BLACK);
                clipPagerTitleView.setClipColor(Color.parseColor("#e94220"));


                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mFragmentContainerHelper.handlePageSelected(index);
                        int tops = recyclerView.getTop();
                        int topN = 0;
                        topN = recyclerView.getChildAt(index).getTop();
                        my_scrollview.smoothScrollTo(0, tops + topN - ll_title_in_scrollview.getHeight());

                       // switchPages(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);

                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(80f);
                indicator.setRoundRadius(4f);
                indicator.setColors(Color.parseColor("#bc2a2a"));
                return indicator;
            }
        });
        magic_indicator2.setNavigator(commonNavigator2);
        mFragmentContainerHelper.attachMagicIndicator(magic_indicator2);



    }

    /**
     * 添加滑动监听
     */
    private void addScrollListener() {
        my_scrollview.setScrollViewListener(new MyScrollView.ScrollViewListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                scrollRefreshNavigationTag(scrollView);
            }

            @Override
            public void onScrollStop(boolean isScrollStop) {

            }
        });
    }

    /**
     * 内容区域滑动刷新导航标签
     *
     * @param scrollView 内容模块容器
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void scrollRefreshNavigationTag(ScrollView scrollView) {
        if (scrollView == null) {
            return;
        }
        // 获得ScrollView滑动距离
        int scrollY = scrollView.getScrollY();

        // 确定头部标题显示或隐藏
        if (scrollY >= ll_title_in_scrollview.getTop()) {
            ll_title_top.setVisibility(View.VISIBLE);
        } else {
            ll_title_top.setVisibility(View.GONE);
        }

//        int tops = recyclerView.getTop();
//        int top2=0;
//        int top3=0;
//        View view1 = recyclerView.getChildAt(1);
//        View view2 = recyclerView.getChildAt(2);
//        if (view1 != null) {
//            top2 = view1.getTop();
//        }
//        if (view2 != null) {
//            top3 = view2.getTop();
//        }
//        if (scrollY >= tops+top3- ll_title_in_scrollview.getHeight()) {
//            magic_indicator2.onPageSelected(2);
//            mFragmentContainerHelper.handlePageSelected(2);
//        } else if (scrollY >= tops+top2 - ll_title_in_scrollview.getHeight()) {
//            magic_indicator2.onPageSelected(1);
//            mFragmentContainerHelper.handlePageSelected(1);
//        } else if (scrollY >= tops - ll_title_in_scrollview.getHeight()) {
//            magic_indicator2.onPageSelected(0);
//            mFragmentContainerHelper.handlePageSelected(0);
//        }
        // 确定ScrollView当前展示的顶部内容属于哪个内容模块

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(list.get(i).getName())) {
                int tops = recyclerView.getTop();
                int topN=recyclerView.getChildAt(i).getTop();
                if (scrollY >= tops+topN- ll_title_in_scrollview.getHeight()) {
                    magic_indicator2.onPageSelected(i);
                    mFragmentContainerHelper.handlePageSelected(i);
                }
            }
        }


    }




    public int getCurrentViewIndex() {
        int firstVisibleItem = linearManager.findFirstVisibleItemPosition();
        int lastVisibleItem = linearManager.findLastVisibleItemPosition();
        int currentIndex = firstVisibleItem;
        int lastHeight = 0;
        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
            View view = linearManager.getChildAt(i - firstVisibleItem);
            if (null == view) {
                continue;
            }

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            Rect localRect = new Rect();
            view.getLocalVisibleRect(localRect);

            int showHeight = localRect.bottom - localRect.top;
            if (showHeight > lastHeight) {
                currentIndex = i;
                lastHeight = showHeight;
            }
        }

        if (currentIndex < 0) {
            currentIndex = 0;
        }

        return currentIndex;
    }




//    public void savePositionState(){
//        int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
//        mPositionState[0] = firstVisibleItemPosition;
////        View view = mRecyclerView.getChildAt(firstVisibleItemPosition);
//        View view = mLinearLayoutManager.findViewByPosition(firstVisibleItemPosition);
//        if (view != null) {
//            int top = view.getTop();
//            mPositionState[1] = top;
//        }
//
//        DebugLog.d(TAG,"InfoStream_savePositionState mPositionState[0]:"+mPositionState[0]+" mPositionState[1]:"+mPositionState[1]);
//    }
//
//    public void restorePosistionState(){
//        DebugLog.d(TAG,"InfoStream_restorePosistionState mPositionState[0]:"+mPositionState[0]+" mPositionState[1]:"+mPositionState[1]);
//        mLinearLayoutManager.scrollToPositionWithOffset(mPositionState[0], mPositionState[1]);
//        mFirstCompletelyVisibleItemPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
//    }

}
