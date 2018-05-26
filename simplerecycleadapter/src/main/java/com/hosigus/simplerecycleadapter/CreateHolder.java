package com.hosigus.simplerecycleadapter;

import android.view.View;

/**
 * Created by 某只机智 on 2018/4/21.
 */

public interface CreateHolder<T> {
    BaseHolder<T> onCreateHolder(View itemView);
}
