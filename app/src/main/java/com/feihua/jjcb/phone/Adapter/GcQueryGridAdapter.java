package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.feihua.jjcb.phone.R;

import java.util.List;

/**
 * Created by wcj on 2016-04-22.
 */
public class GcQueryGridAdapter extends CommonAdapter<String>
{
    public GcQueryGridAdapter(Context context, List<String> datas, int layoutId)
    {
        super(context, datas, layoutId);

    }

    @Override
    public void convert(ViewHolder holder, String s, int position)
    {
        if (position == 0 || getCount() == position + 1){
            holder.getView(R.id.layoutItem1).setVisibility(View.VISIBLE);
            holder.getView(R.id.layoutItem2).setVisibility(View.GONE);
        }else{
            holder.getView(R.id.layoutItem1).setVisibility(View.GONE);
            holder.getView(R.id.layoutItem2).setVisibility(View.VISIBLE);
        }

        holder.getView(R.id.imgArrowsDown).setVisibility(View.INVISIBLE);
        if (position == 1 || (position - 1) % 4 == 0 || (position - 1) % 4 == 1)
        {
            holder.getView(R.id.imgArrowsDown).setVisibility(View.VISIBLE);
        }
        holder.getView(R.id.imgArrowsHorizontal).setVisibility(View.VISIBLE);
        if ((position + 1) % 2 == 0)
        {
            holder.getView(R.id.imgArrowsHorizontal).setVisibility(View.INVISIBLE);

        }
        else if ((position - 1) % 4 == 1)
        {
            holder.setImageResource(R.id.imgArrowsHorizontal, R.mipmap.img_arrows_left_normal);
        }

        if (getCount() == position + 1){
            holder.getView(R.id.imgArrowsHorizontal).setVisibility(View.INVISIBLE);
            holder.getView(R.id.imgArrowsDown).setVisibility(View.INVISIBLE);
            holder.setText(R.id.tvStart, "结束");
            holder.setImageResource(R.id.imgStart,R.mipmap.img_end);
        }
    }
}
