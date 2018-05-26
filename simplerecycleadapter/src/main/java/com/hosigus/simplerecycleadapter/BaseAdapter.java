package com.hosigus.simplerecycleadapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 某只机智 on 2018/4/19.
 *
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>>{
    private List<T> dataList;
    private LoadMoreCallBack loadMoreCallBack;
    private Handler mHandler = new Handler();

    public BaseAdapter() {
        dataList = new ArrayList<>();
    }

    public BaseAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    public void setLoadMoreCallBack(LoadMoreCallBack callBack) {
        this.loadMoreCallBack = callBack;
    }

    protected abstract int getResId(int viewType);

    protected abstract BaseHolder<T> onCreateHolder(View itemView);

    protected View.OnClickListener getClickListener(int position){
        return null;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public final T getData(int position) {
        return dataList.get(position);
    }

    public final void refresh(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public final void addData(T data) {
        dataList.add(data);
        notifyItemInserted(dataList.size()-1);
    }

    public final void addData(List<T> newDataList) {
        int pos = dataList.size();
        this.dataList.addAll(newDataList);
        notifyItemRangeInserted(pos,newDataList.size());
    }

    public final void delData(int pos, int count) {
        for (int i = 0; i < count; i++) {
            dataList.remove(pos);
        }
        notifyItemRangeRemoved(pos, count);
    }

    public final void delData(int pos) {
        dataList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateHolder(LayoutInflater.from(parent.getContext()).inflate(getResId(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        holder.bindData(dataList.get(position),getClickListener(position));

        if (position == getItemCount() - 1 && loadMoreCallBack != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    loadMoreCallBack.loadMore();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
