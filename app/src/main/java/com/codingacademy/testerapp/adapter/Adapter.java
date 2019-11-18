package com.codingacademy.testerapp.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Adapter  extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public Adapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;

    }
    @NonNull
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

