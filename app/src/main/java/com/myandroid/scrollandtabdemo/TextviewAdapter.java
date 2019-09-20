package com.myandroid.scrollandtabdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：Administrator on 2018/4/12 14:05
 */

public class TextviewAdapter extends RecyclerView.Adapter<TextviewAdapter.ListHolder> {

    private Context mContext;
    private List<DemoBean> mList;

    public TextviewAdapter(Context context, List<DemoBean> list) {
        this.mContext = context;
        this.mList=list;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.item_textview,parent,false);
       return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.textView.setText(mList.get(position).getName());
        holder.views.setVisibility(View.GONE);
        if (position==mList.size()-1){
            holder.views.setVisibility(View.VISIBLE);
            int statusHeight = ScreenUtils.getStatusBarHeight(mContext);
            int screenHeight = ScreenUtils.getScreenHeight(mContext)-((mList.get(position).getHeight()/3));
            ViewGroup.LayoutParams layoutParams = holder.views.getLayoutParams();
            layoutParams.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mList.get(position).getHeight(), mContext.getResources().getDisplayMetrics());
            holder.views.setLayoutParams(layoutParams);

//            ViewGroup.LayoutParams layoutParams1 = holder.llTitle.getLayoutParams();
//            layoutParams1.height= ViewGroup.LayoutParams.MATCH_PARENT;
//            holder.llTitle.setLayoutParams(layoutParams1);


        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class ListHolder extends RecyclerView.ViewHolder {
        TextView textView,tv2,tv1;
        View views;
        LinearLayout llTitle;
        public ListHolder(View view) {
            super(view);
            textView=(TextView)view.findViewById(R.id.textView);
            llTitle=view.findViewById(R.id.ll_title);
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
            views=view.findViewById(R.id.view);
        }
    }
}
