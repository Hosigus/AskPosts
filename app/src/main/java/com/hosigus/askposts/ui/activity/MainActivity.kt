package com.hosigus.askposts.ui.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.hosigus.askposts.App
import com.hosigus.askposts.R
import com.hosigus.askposts.ui.adapter.MyPagerAdapter
import com.hosigus.askposts.ui.fragment.MyInfoFragment
import com.hosigus.askposts.ui.fragment.QuestionFragment
import com.hosigus.askposts.ui.fragment.ScheduleFragment
import com.hosigus.askposts.ui.fragment.SearchFragment
import com.hosigus.askposts.utils.disableShiftMode
import com.hosigus.askposts.utils.startLoginActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by 某只机智 on 2018/6/30.
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = ""
        setSupportActionBar(toolbar)

        vp_main.adapter = MyPagerAdapter(makeFragmentList(), fm = supportFragmentManager)
        vp_main.offscreenPageLimit = 4
        vp_main.addOnPageChangeListener(this)

        disableShiftMode(bnv_main)
        bnv_main.setOnNavigationItemSelectedListener(this)
    }

    private fun makeFragmentList(): List<Fragment> {
        return listOf(ScheduleFragment(),QuestionFragment(),SearchFragment(),MyInfoFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_toolbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        for (i: Int in 0 until menu!!.size()) {
            menu.getItem(i).isVisible = false
        }
        when (vp_main.currentItem) {
            0 -> menu.findItem(R.id.menu_add).isVisible = true
            1 -> menu.findItem(R.id.menu_filtrate).isVisible = true
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        vp_main.currentItem = menuID2Position(item.itemId)
        return true
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            1 -> if (!App.INSTANCE.isLogin) { startLoginActivity(this) }
            3 -> {
                toolbar.visibility = View.GONE
                if (!App.INSTANCE.isLogin) { startLoginActivity(this) }
            }
            else -> toolbar.visibility = View.VISIBLE
        }

        bnv_main.selectedItemId = position2MenuId(position)
        toolbar_title.setText(position2Title(position))
        invalidateOptionsMenu()
    }

    private fun menuID2Position(id: Int) = when (id) {
        R.id.menu_schedule -> 0
        R.id.menu_question -> 1
        R.id.menu_search -> 2
        R.id.menu_my -> 3
        else -> -1
    }

    private fun position2MenuId(pos: Int) = when (pos) {
        0 -> R.id.menu_schedule
        1 -> R.id.menu_question
        2 -> R.id.menu_search
        3 -> R.id.menu_my
        else -> -1
    }

    private fun position2Title(pos: Int) = when (pos) {
        0 -> R.string.schedule
        1 -> R.string.question
        2 -> R.string.search
        3 -> R.string.my
        else -> -1
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
}