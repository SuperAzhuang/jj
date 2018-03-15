package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.feihua.jjcb.phone. R;
import com.feihua.jjcb.phone.bean.DetailsBean;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.utils.L;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.feihua.jjcb.phone.R.id.tvContent;

/**
 * Created by wcj on 2016-03-31.
 */
public class CBGLDetailsListAdapter extends CommonAdapter<DetailsBean>
{
    public CBGLDetailsListAdapter(Context context, List<DetailsBean> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DetailsBean datas, int position)
    {
        holder.setText(R.id.tvName, datas.getName());
        holder.setText(tvContent, datas.getContent());
        holder.setImageResource(R.id.imgIcon, datas.getIconId());
        holder.getView(R.id.layoutMore).setVisibility(View.GONE);
        if (datas.getName().equals("基本吨户:"))
        {
            holder.getView(R.id.cbCheck).setVisibility(View.VISIBLE);
            holder.getView(tvContent).setVisibility(View.GONE);
            CheckBox cbCheck = holder.getView(R.id.cbCheck);
            if (datas.getContent().equals("1"))
            {
                cbCheck.setChecked(true);
            }
            else
            {
                cbCheck.setChecked(false);
            }
        }
        else
        {
            holder.getView(R.id.cbCheck).setVisibility(View.GONE);
            holder.getView(tvContent).setVisibility(View.VISIBLE);
            if ("换表日期:".equals(datas.getName())) {
//                if (!TextUtils.isEmpty(datas.getContent())) {
//                    holder.setText(tvContent, getNormalTime(Long.parseLong(datas.getContent())));
//                    L.d("tvContent","tvContent = "+ getNormalTime(Long.parseLong(datas.getContent())));
//                }

            }

        }
        if (datas.getNameMore() != null)
        {
            holder.getView(R.id.layoutMore).setVisibility(View.VISIBLE);
            holder.setText(R.id.tvNameMore, datas.getNameMore());
            holder.setText(R.id.tvContentMore, datas.getContentMore());
            holder.setImageResource(R.id.imgIconMore, datas.getIconMoreId());
        }

    }
    public  String getNormalTime(long value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd") ;
        String time = format.format(new Date(value)) ;

        return time;
    }
}
