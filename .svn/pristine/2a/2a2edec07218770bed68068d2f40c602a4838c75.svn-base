package com.feihua.jjcb.phone.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.YHCXViewPagerAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户查询
 * Created by wcj on 2016-03-02.
 */
public class YHCXfragment extends BaseFragment implements View.OnClickListener
{

    protected TextView tvTitleRight;
    protected TextView tvTitleLeft;
    protected View layoutTitle;
    protected final int TITLE_LEFT = 0;
    protected final int TITLE_RIGHT = 1;
    protected NoScrollViewPager mViewPager;
    protected List<Fragment> fragmentDatas;

    @Override
    public int getLayoutId()
    {
        return R.layout.layout_yhcx;
    }

    protected void setHeadTitle()
    {

    }

    @Override
    public void initList()
    {
        fragmentDatas = new ArrayList<>();
        fragmentDatas.add(new HHSSFragmetn());
        fragmentDatas.add(new TJCXFragment());
    }

    public void initViews(View layout)
    {
        tvTitleLeft = (TextView) layout.findViewById(R.id.tvTitleLeft);
        tvTitleRight = (TextView) layout.findViewById(R.id.tvTitleRight);
        layoutTitle = layout.findViewById(R.id.layoutTitle);
        mViewPager = (NoScrollViewPager) layout.findViewById(R.id.viewPager);
        mViewPager.setNoScroll(true);
        FragmentManager fm = getChildFragmentManager();
        mViewPager.setAdapter(new YHCXViewPagerAdapter(fm, fragmentDatas));

        tvTitleLeft.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);

        setTitleState(TITLE_LEFT);

        setHeadTitle();
    }

    protected void setTitleState(int index)
    {
        boolean isSelected = false;
        if (index == TITLE_LEFT)
        {
            isSelected = true;
            tvTitleLeft.setTextColor(getResources().getColor(R.color.blue_title));
            tvTitleRight.setTextColor(getResources().getColor(R.color.white));
        }
        else
        {
            tvTitleLeft.setTextColor(getResources().getColor(R.color.white));
            tvTitleRight.setTextColor(getResources().getColor(R.color.blue_title));
        }
        layoutTitle.setSelected(!isSelected);
        mViewPager.setCurrentItem(index, false);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvTitleRight:
                setTitleState(TITLE_RIGHT);
                break;
            case R.id.tvTitleLeft:
                setTitleState(TITLE_LEFT);
                break;
            default:
                break;
        }
    }
}
