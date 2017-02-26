package com.example.gdmap0fflinemap.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/29.
 */

public class MyPagerAdapter extends PagerAdapter {
    List<View> list = new ArrayList<View>();

    public MyPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager pViewPager = ((ViewPager) container);
        pViewPager.removeView(list.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ViewPager pViewPager = ((ViewPager) container);
        pViewPager.addView(list.get(position));
        return list.get(position);
    }
}
