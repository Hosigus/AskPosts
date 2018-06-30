package com.hosigus.askposts.ui.fragment

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.hosigus.askposts.R
import com.hosigus.askposts.ui.adapter.MyPagerAdapter

/**
 * Created by 某只机智 on 2018/6/3.
 */
class QuestionFragment : BaseFragment() {

    override fun getResID(): Int {
        return R.layout.fragment_main_question
    }

    override fun init(v: View) {
        val tab: TabLayout = v.findViewById(R.id.tb_question)
        val pager: ViewPager = v.findViewById(R.id.vp_question)

        val fragmentList = listOf<Fragment>(QuestionTopicFragment().setKind("全部"),
                QuestionTopicFragment().setKind("学习"),
                QuestionTopicFragment().setKind("生活"),
                QuestionTopicFragment().setKind("情感"),
                QuestionTopicFragment().setKind("其他"))

        val titleList = listOf<String>("全部", "学习", "生活", "情感", "其他")

        pager.adapter = MyPagerAdapter(fragmentList, titleList, fragmentManager)
        pager.offscreenPageLimit = 5
        tab.setupWithViewPager(pager)


    }

}