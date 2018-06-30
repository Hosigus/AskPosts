package com.hosigus.askposts.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by 某只机智 on 2018/6/3.
 */
class MyPagerAdapter(private var fragments: List<Fragment>, private var titles: List<String>? = null, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (titles != null) titles!![position] else super.getPageTitle(position)
    }
}