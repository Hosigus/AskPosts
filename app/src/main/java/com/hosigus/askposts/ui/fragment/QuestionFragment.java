package com.hosigus.askposts.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosigus.askposts.R;
import com.hosigus.askposts.ui.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 某只机智 on 2018/5/26.
 */

public class QuestionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main_question,container,false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        TabLayout tab = v.findViewById(R.id.tb_question);
        ViewPager pager = v.findViewById(R.id.vp_question);

        List<Fragment> fList = new ArrayList<>();
        List<String> tList = new ArrayList<>();
        tList.add("全部");
        fList.add(new QuestionTopicFragment().setKind("全部"));
        tList.add("学习");
        fList.add(new QuestionTopicFragment().setKind("学习"));
        tList.add("生活");
        fList.add(new QuestionTopicFragment().setKind("生活"));
        tList.add("情感");
        fList.add(new QuestionTopicFragment().setKind("情感"));
        tList.add("其他");
        fList.add(new QuestionTopicFragment().setKind("其他"));

        pager.setAdapter(new MyPagerAdapter(getFragmentManager(), fList, tList));
        pager.setOffscreenPageLimit(5);
        tab.setupWithViewPager(pager);
    }

}
