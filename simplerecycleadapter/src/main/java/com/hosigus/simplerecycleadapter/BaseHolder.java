package com.hosigus.simplerecycleadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by 某只机智 on 2018/4/19.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{
    protected View itemView;
    private final SparseArray<View> mViews;

    public BaseHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
        this.itemView = itemView;
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        try {
            return (V) view;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void bindData(T data);

    public final void bindData(T data, View.OnClickListener listener) {
        if (listener != null) {
            itemView.setOnClickListener(listener);
        }
        bindData(data);
    }
}
