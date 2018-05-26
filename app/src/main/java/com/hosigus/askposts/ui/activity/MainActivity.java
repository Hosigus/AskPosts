package com.hosigus.askposts.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hosigus.askposts.R;
import com.hosigus.askposts.ui.adapter.MyPagerAdapter;
import com.hosigus.askposts.ui.fragment.MyFragment;
import com.hosigus.askposts.ui.fragment.QuestionFragment;
import com.hosigus.askposts.ui.fragment.ScheduleFragment;
import com.hosigus.askposts.ui.fragment.SearchFragment;
import com.hosigus.askposts.utils.BNVHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    public static void start(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private TextView title;
    private ViewPager mainPager;
    private BottomNavigationView mainNavigaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        title = findViewById(R.id.toolbar_title);

        mainPager = findViewById(R.id.vp_main);
        List<Fragment> list = new ArrayList<>();
        list.add(new ScheduleFragment());
        list.add(new QuestionFragment());
        list.add(new SearchFragment());
        list.add(new MyFragment());
        mainPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),list));
        mainPager.setOffscreenPageLimit(4);
        mainPager.addOnPageChangeListener(this);

        mainNavigaView = findViewById(R.id.bnv_main);
        BNVHelper.disableShiftMode(mainNavigaView);
        mainNavigaView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_filtrate).setVisible(false);
        switch (mainPager.getCurrentItem()) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(true);
                break;
            case 1:
                menu.findItem(R.id.menu_filtrate).setVisible(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_schedule:
                mainPager.setCurrentItem(0);
                break;
            case R.id.menu_question:
                mainPager.setCurrentItem(1);
                break;
            case R.id.menu_search:
                mainPager.setCurrentItem(2);
                break;
            case R.id.menu_my:
                mainPager.setCurrentItem(3);
                break;
        }
        return true;
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mainNavigaView.setSelectedItemId(R.id.menu_schedule);
                toolbar.setVisibility(View.VISIBLE);
                title.setText(R.string.schedule);
                invalidateOptionsMenu();
                break;
            case 1:
                mainNavigaView.setSelectedItemId(R.id.menu_question);
                toolbar.setVisibility(View.VISIBLE);
                title.setText(R.string.question);
                invalidateOptionsMenu();
                break;
            case 2:
                mainNavigaView.setSelectedItemId(R.id.menu_search);
                toolbar.setVisibility(View.VISIBLE);
                title.setText(R.string.search);
                invalidateOptionsMenu();
                break;
            case 3:
                mainNavigaView.setSelectedItemId(R.id.menu_my);
                toolbar.setVisibility(View.GONE);
                title.setText(R.string.my);
                break;
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    @Override
    public void onPageScrollStateChanged(int state) {}
}
