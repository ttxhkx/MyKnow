package com.example.lenovo.myknow.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.lenovo.myknow.Adapter.ViewPagerAdapter;
import com.example.lenovo.myknow.Bean.LongRemark;
import com.example.lenovo.myknow.Fragment.RemarkFragment;
import com.example.lenovo.myknow.R;

import java.util.ArrayList;
import java.util.List;

public class RemarkActivity extends AppCompatActivity {
    private List<LongRemark> longlist = new ArrayList<>();
    private String ShortUrl;
    private String srkNum;
    ArrayList<RemarkFragment>fragments;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        Intent intent = getIntent();
        String id = intent.getStringExtra("NewsId");                                        //接受ID，标题
        String lrkNum = intent.getStringExtra("long");
        srkNum = intent.getStringExtra("short");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarRemark);                              //初始化Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String LongUrl = "https://news-at.zhihu.com/api/4/story/" + id + "/long-comments";
        ShortUrl = "https://news-at.zhihu.com/api/4/story/" + id + "/short-comments";
        ViewPager viewPager = findViewById(R.id.remarkViewPager);
        tabLayout = findViewById(R.id.remarkTabLayout);

        fragments = new ArrayList<>();                                                                  //创建Fragment
        RemarkFragment remarkFragment = RemarkFragment.newInstance("长评:" + lrkNum,LongUrl);
        fragments.add(remarkFragment);
        remarkFragment = RemarkFragment.newInstance("短评:" + srkNum,ShortUrl);
        fragments.add(remarkFragment);

        tabLayout.setupWithViewPager(viewPager);                  //关联tabLayout
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }
}
