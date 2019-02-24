package com.example.lenovo.myknow.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.example.lenovo.myknow.Bean.CollectColumn;
import com.example.lenovo.myknow.Fragment.ColumnFragment;
import com.example.lenovo.myknow.Fragment.NewsFragment;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<CollectColumn> mlist = new ArrayList<>();
    //private SwipeRefreshLayout swipeRefresh;
    ArrayList<Fragment>fragments;
    MyAdapter adapter;
    TabLayout tabLayout;
    private List<String> titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        dbHelper = new MyDatabaseHelper(this, "theColumn.db", null, 1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarCollect);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ViewPager viewPager = findViewById(R.id.collectViewPager);                       // 初始化ViewPager
        tabLayout = findViewById(R.id.collectTabLayout);
        titles = new ArrayList<>();                                                     //设置TabLayout标题
        titles.add("消息");
        titles.add("栏目");

        fragments = new ArrayList<>();                                                  //创建Fragment
        fragments.add(new NewsFragment());
        fragments.add(new ColumnFragment());

        adapter = new MyAdapter(getSupportFragmentManager());                        //绑定
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private class MyAdapter extends FragmentPagerAdapter                                 //设置适配器
    {
        public MyAdapter(FragmentManager fm) {
            super(fm);
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
            return titles.get(position);
        }
    }


}
