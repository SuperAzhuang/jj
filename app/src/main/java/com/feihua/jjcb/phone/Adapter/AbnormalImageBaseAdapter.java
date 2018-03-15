package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.feihua.jjcb.phone.R;

import java.util.List;

/**
 * Created by wcj on 2016-04-07.
 */
public class AbnormalImageBaseAdapter extends CommonAdapter<Bitmap>
{
    public AbnormalImageBaseAdapter(Context context, List<Bitmap> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Bitmap bitmap, int position)
    {
        if (bitmap != null)
        {
            holder.setImageBitmap(R.id.iv_item, bitmap);
        }else{
            holder.setImageResource(R.id.iv_item, R.mipmap.icon_default);
        }
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return 6;
    }

    @Override
    public Bitmap getItem(int position)
    {
        // TODO Auto-generated method stub
        if (mDatas != null && position > (mDatas.size() - 1))
        {
            return null;
        }
        return (Bitmap) (mDatas == null ? 0 : mDatas.get(position));
    }

}
