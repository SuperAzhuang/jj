package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.view.View;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.DetailsBean;
import com.feihua.jjcb.phone.bean.QueryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-04-19.
 */
public class QueryListAdapter extends CommonAdapter<DetailsBean>
{
    public QueryListAdapter(Context context, List datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DetailsBean bean, int position)
    {
        holder.setText(R.id.tvName, bean.getName());
        holder.setText(R.id.tvContent, bean.getContent());
        holder.setImageResource(R.id.imgIcon, bean.getIconId());
        holder.getView(R.id.layoutMore).setVisibility(View.GONE);
        if (bean.getNameMore() != null)
        {
            holder.getView(R.id.layoutMore).setVisibility(View.VISIBLE);
            holder.setText(R.id.tvNameMore, bean.getNameMore());
            holder.setText(R.id.tvContentMore, bean.getContentMore());
            holder.setImageResource(R.id.imgIconMore, bean.getIconMoreId());
        }

    }
}
