package com.feihua.jjcb.phone.fragment;

import java.util.ArrayList;

/**
 * 欠费查询
 * Created by wcj on 2016-03-02.
 */
public class QFCXfragment extends YHCXfragment
{

    public void setHeadTitle()
    {
        tvTitleRight.setText("区域查询");
        tvTitleLeft.setText("用户查询");
    }

    public void initList()
    {
        fragmentDatas = new ArrayList<>();
        fragmentDatas.add(new YHQueryFragment());
        fragmentDatas.add(new QYQueryFragment());
    }

}
