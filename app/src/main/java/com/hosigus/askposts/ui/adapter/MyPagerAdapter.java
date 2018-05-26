package com.hosigus.askposts.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hosigus.askposts.ui.fragment.MyFragment;
import com.hosigus.askposts.ui.fragment.QuestionFragment;
import com.hosigus.askposts.ui.fragment.ScheduleFragment;
import com.hosigus.askposts.ui.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 某只机智 on 2018/5/26.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments ;
    private List<String> titles;

    public MyPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        fragments = fragmentList;
    }

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        fragments = fragmentList;
        titles = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? super.getPageTitle(position) : titles.get(position);
    }
}
