package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.text.TextUtils;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;

import java.util.List;

/**
 * Created by wcj on 2016-04-18.
 */
public class CBDetailsListViewAdapter extends CommonAdapter<ChaoBiaoTables>
{
    private String userbKh;

    public CBDetailsListViewAdapter(Context context, List<ChaoBiaoTables> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ChaoBiaoTables table, int position)
    {
        holder.setText(R.id.tvKH,userbKh);
        holder.setText(R.id.tvDate,table.getWATERU_YEAR() + "." + table.getWATERU_MONTH());
        holder.setText(R.id.tvSQDS,table.getUSERB_SQDS());
        holder.setText(R.id.tvBQDS,table.getUSERB_BQDS());
        holder.setText(R.id.tvQAN,table.getCOUNT_QAN());
    }

    public void setUserbKH(String userbKh){
        this.userbKh = userbKh;
    }

}
