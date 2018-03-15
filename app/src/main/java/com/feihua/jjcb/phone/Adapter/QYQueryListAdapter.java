package com.feihua.jjcb.phone.Adapter;

import android.content.Context;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.PianQuArrearage;

import java.util.List;

/**
 * Created by wcj on 2016-04-19.
 */
public class QYQueryListAdapter extends CommonAdapter<PianQuArrearage>
{
    public QYQueryListAdapter(Context context, List<PianQuArrearage> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, PianQuArrearage bean, int position)
    {
        holder.setText(R.id.tvAll, bean.getAll_total());
        holder.setText(R.id.tvName, bean.getOrg_name());
        holder.setText(R.id.tvJM, bean.getM_total());
        holder.setText(R.id.tvJY, bean.getS_total());
        holder.setText(R.id.tvFJY, bean.getQ_total());
    }
}
