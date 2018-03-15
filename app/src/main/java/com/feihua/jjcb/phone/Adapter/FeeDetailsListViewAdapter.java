package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.text.TextUtils;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.FeeDetails;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;

import java.util.List;

/**
 * Created by wcj on 2016-04-18.
 */
public class FeeDetailsListViewAdapter extends CommonAdapter<FeeDetails>
{
    private String userbKh;

    public FeeDetailsListViewAdapter(Context context, List<FeeDetails> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, FeeDetails table, int position)
    {
        holder.setText(R.id.tvKH,table.getUSERB_KH());
        holder.setText(R.id.tvDate,table.getDEBTL_YEAR() + "." + table.getDEBTL_MON());
        holder.setText(R.id.tvSQDS,table.getWATERU_QAN());
        holder.setText(R.id.tvBQDS,table.getDEBTL_STOTAL());
        holder.setText(R.id.tvQAN,table.getPAY_TAG());
    }

    public void setUserbKH(String userbKh){
        this.userbKh = userbKh;
    }

}
