package com.hosigus.simplerecycleadapter;

import android.view.View;

/**
 * Created by 某只机智 on 2018/4/21.
 */

public class SimpleAdapterBuilder<T> {
    private BaseAdapter<T> adapter;
    private ClickListener<T> listener;

    public SimpleAdapterBuilder(final int resId, final CreateHolder<T> createHolder) {
        adapter=new BaseAdapter<T>() {
            @Override
            protected BaseHolder<T> onCreateHolder(View itemView) {
                return createHolder.onCreateHolder(itemView);
            }

            @Override
            protected int getResId(int viewType) {
                return resId;
            }

            @Override
            protected View.OnClickListener getClickListener(final int position) {
                if (listener != null) {
                    return new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(getData(position));
                        }
                    };
                }
                return null;
            }
        };
    }

    public SimpleAdapterBuilder<T> onClick(ClickListener<T> listener) {
        this.listener = listener;
        return this;
    }

    public SimpleAdapterBuilder<T> onLoad(LoadMoreCallBack callBack) {
        adapter.setLoadMoreCallBack(callBack);
        return this;
    }

    public BaseAdapter<T> build() {
        return adapter;
    }
}
