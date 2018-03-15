package com.feihua.jjcb.phone.Adapter;

import android.content.Context;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.YHQuery;

import java.util.List;

/**
 * Created by wcj on 2016-05-23.
 */
public class YHQUeryListAdapter extends CommonAdapter<YHQuery>
{
    public YHQUeryListAdapter(Context context, List<YHQuery> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, YHQuery yhQuery, int position)
    {
        holder.setText(R.id.tvKH,yhQuery.getUSERB_KH());
        holder.setText(R.id.tvHM,yhQuery.getUSERB_HM());
        holder.setText(R.id.tvYSXZ,yhQuery.getWATERT_NAME());
        holder.setText(R.id.tvSFFS,yhQuery.getPAY_WAY());
        holder.setText(R.id.tvQFJE,String.valueOf(yhQuery.getDEBTL_STOTAL()));
    }
}
