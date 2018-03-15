package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.feihua.jjcb.phone.R;

import java.util.List;

/**
 * Created by wcj on 2016-03-14.
 */
public class ImageBaseAdapter extends BaseAdapter
{

    private LayoutInflater inflater;
    private Context context;
    private List<Bitmap> datas;

    public ImageBaseAdapter(Context context,List<Bitmap> datas)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount()
    {
        return 6;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (null == convertView)
        {
            convertView = (ImageView) inflater.inflate(R.layout.griditem_image, null, false);
        }

        return convertView;
    }
}
