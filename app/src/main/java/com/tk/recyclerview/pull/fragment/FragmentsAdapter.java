package com.tk.recyclerview.pull.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : adapter
 * </pre>
 */

public class FragmentsAdapter extends FragmentPagerAdapter {
    private List<Fragment> fmList;
    private List<String> titleList;

    public FragmentsAdapter(FragmentManager fm, List<Fragment> fmList, List<String> titleList) {
        super(fm);
        this.fmList = fmList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        return fmList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
