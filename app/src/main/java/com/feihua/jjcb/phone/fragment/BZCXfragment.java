package com.feihua.jjcb.phone.fragment;

import java.util.ArrayList;

/**
 * 报装查询
 * Created by wcj on 2016-03-02.
 */
public class BZCXfragment extends YHCXfragment
{

    public void setHeadTitle()
    {
        tvTitleRight.setText("工程查询");
        tvTitleLeft.setText("报装统计");
    }

    public void initList()
    {
        fragmentDatas = new ArrayList<>();
        fragmentDatas.add(new BZStatisticsFragment());
        fragmentDatas.add(new GCQueryFragment());
    }
}
