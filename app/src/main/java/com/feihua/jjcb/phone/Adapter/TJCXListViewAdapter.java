package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.text.TextUtils;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;

import java.util.List;

/**
 * Created by wcj on 2016-04-18.
 */
public class TJCXListViewAdapter extends CommonAdapter<ChaoBiaoTables>
{
    public TJCXListViewAdapter(Context context, List<ChaoBiaoTables> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ChaoBiaoTables table, int position)
    {
        holder.setText(R.id.tvKH,table.getUSERB_KH());
        holder.setText(R.id.tvHM,table.getUSERB_HM());
        holder.setText(R.id.tvYSXZ,table.getUSERB_ADDR());
        holder.setText(R.id.tvQAN,getMt(table.getUSERB_MT()));
    }

    private String getMt(String mt){
        if (TextUtils.isEmpty(mt)){
            return "";
        }
        return mt.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1*$2");
    }
}
