package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
	
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;
	
	public CommonAdapter(Context context,List<T> datas,int layoutId){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return (T) (mDatas == null ? 0 : mDatas.get(position));
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, position, layoutId);
		//对于listview，注意添加这一行，即可在item上使用高度
        AutoUtils.autoSize(holder.getConvertView());
		convert(holder, getItem(position),position);
		return holder.getConvertView();
	}

	
	public void onDatasChanged(List<T> mDatas){
		this.mDatas = mDatas;
		this.notifyDataSetChanged();
		
	}
	
	public abstract void convert(ViewHolder holder,T t,int position);

}
