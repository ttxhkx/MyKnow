package com.example.lenovo.myknow.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lenovo.myknow.Fragment.RemarkFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<RemarkFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<RemarkFragment>fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
