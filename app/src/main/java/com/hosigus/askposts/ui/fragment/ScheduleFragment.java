package com.hosigus.askposts.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosigus.askposts.R;

/**
 * Created by 某只机智 on 2018/5/26.
 */

public class ScheduleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main_schedule,container,false);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {

    }

    private void initData() {

    }
}
