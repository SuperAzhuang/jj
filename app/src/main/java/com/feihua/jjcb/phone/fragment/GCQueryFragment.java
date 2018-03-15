package com.feihua.jjcb.phone.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.feihua.jjcb.phone.Adapter.GcQueryGridAdapter;
import com.feihua.jjcb.phone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-04-19.
 */
public class GCQueryFragment extends BaseFragment implements OnClickListener
{
    private List<String> dataList;

    @Override
    public int getLayoutId()
    {
        return R.layout.layout_gc_query;
    }

    @Override
    public void initDatas()
    {
        dataList = new ArrayList<>();


        for (int i = 0; i < 10; i++)
        {
            dataList.add(i + "");
        }
    }



    @Override
    public void initViews(View layout)
    {

        layout.findViewById(R.id.btnQuery).setOnClickListener(this);
        GridView gvQueryList = (GridView) layout.findViewById(R.id.gvQueryList);
        gvQueryList.setAdapter(new GcQueryGridAdapter(ctx, dataList, R.layout.listitem_gc_query));


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.btnQuery:

                break;
            default:
                break;
        }
    }


}
