package com.crazywah.fakewechat.module.framework.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import com.crazywah.fakewechat.crazytools.fragment.BaseFragment;

import java.util.List;

/**
 * Created by FungWah on 2018/2/28.
 */

public class FrameworkMainAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> list;

    public FrameworkMainAdapter(FragmentManager fm,List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
