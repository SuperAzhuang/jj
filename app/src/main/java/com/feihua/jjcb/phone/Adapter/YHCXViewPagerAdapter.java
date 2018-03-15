package com.feihua.jjcb.phone.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wcj on 2016-04-15.
 */
public class YHCXViewPagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> fragmentDatas;

    public YHCXViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentDatas)
    {
        super(fm);
        this.fragmentDatas = fragmentDatas;
    }


    @Override
    public Fragment getItem(int position)
    {
        return fragmentDatas.get(position);
    }

    @Override
    public int getCount()
    {
        return fragmentDatas == null ? 0 : fragmentDatas.size();
    }
}
