package com.feihua.jjcb.phone.fragment;

import android.view.View;
import android.widget.TextView;

import com.feihua.jjcb.phone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-04-19.
 */
public class BZStatisticsFragment extends BaseFragment implements View.OnClickListener
{

    private int[] monthResIds = {R.id.tvMonth1, R.id.tvMonth2, R.id.tvMonth3, R.id.tvMonth4, R.id.tvMonth5, R.id.tvMonth6, R.id.tvMonth7, R.id.tvMonth8, R.id.tvMonth9, R.id.tvMonth10, R.id.tvMonth11, R.id.tvMonth12};
    private int[] ysxzResIds = {R.id.tvYSXZ1, R.id.tvYSXZ2, R.id.tvYSXZ3, R.id.tvYSXZ4};
    private int[] yhlbResIds = {R.id.tvYHLB1, R.id.tvYHLB2, R.id.tvYHLB3, R.id.tvYHLB4};
    private int[] gcxzResIds = {R.id.tvGCXZ1, R.id.tvGCXZ2, R.id.tvGCXZ3};
    private ArrayList<TextView> tvMonthList;
    private ArrayList<TextView> ysxzList;
    private ArrayList<TextView> yhlbList;
    private ArrayList<TextView> gcxzList;

    @Override
    public int getLayoutId()
    {
        return R.layout.layout_bz_statistics;
    }

    @Override
    public void initList()
    {
        tvMonthList = new ArrayList<>();
        ysxzList = new ArrayList<>();
        yhlbList = new ArrayList<>();
        gcxzList = new ArrayList<>();
    }

    @Override
    public void initDatas()
    {
    }

    @Override
    public void initViews(View layout)
    {
        tvMonthList.clear();
        for (int i = 0; i < monthResIds.length; i++)
        {
            final int index = i;
            TextView tv = (TextView) layout.findViewById(monthResIds[i]);
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    initTextViewState(index, tvMonthList);
                }
            });
            tvMonthList.add(tv);
        }
        initTextViewState(0, tvMonthList);

        for (int i = 0; i < ysxzResIds.length; i++)
        {
            final int index = i;
            TextView tv = (TextView) layout.findViewById(ysxzResIds[i]);
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    initTextViewState(index, ysxzList);
                }
            });
            ysxzList.add(tv);
        }
        initTextViewState(0, ysxzList);

        for (int i = 0; i < yhlbResIds.length; i++)
        {
            final int index = i;
            TextView tv = (TextView) layout.findViewById(yhlbResIds[i]);
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    initTextViewState(index, yhlbList);
                }
            });
            yhlbList.add(tv);
        }
        initTextViewState(0, yhlbList);

        for (int i = 0; i < gcxzResIds.length; i++)
        {
            final int index = i;
            TextView tv = (TextView) layout.findViewById(gcxzResIds[i]);
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    initTextViewState(index, gcxzList);
                }
            });
            gcxzList.add(tv);
        }
        initTextViewState(0, gcxzList);
    }

    private void initTextViewState(int index, List<TextView> tvList)
    {
        if (tvList == null)
        {
            return;
        }
        for (int i = 0; i < tvList.size(); i++)
        {
            if (i == index)
            {
                tvList.get(i).setSelected(true);
            }
            else
            {
                tvList.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnQuery:

                break;
            default:
                break;
        }
    }
}
