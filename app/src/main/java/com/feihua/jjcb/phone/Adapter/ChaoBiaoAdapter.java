package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.text.TextUtils;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;

import java.util.List;

/**
 * Created by wcj on 2016-03-31.
 */
public class ChaoBiaoAdapter extends CommonAdapter<ChaoBiaoTables>
{
    public ChaoBiaoAdapter(Context context, List<ChaoBiaoTables> datas, int layoutId)
    {
        super(context, datas, layoutId);

    }

    @Override
    public void convert(ViewHolder holder, ChaoBiaoTables chaoBiaoTables, int position)
    {
        holder.setText(R.id.tv_item_huhao, chaoBiaoTables.getUSERB_JBH());
        holder.setText(R.id.tv_item_name, "户名:" + chaoBiaoTables.getUSERB_HM());
        holder.setText(R.id.tv_item_dh, "户号:" + chaoBiaoTables.getUSERB_KH());
        holder.setText(R.id.tv_item_ph, "手机:" + chaoBiaoTables.getUSERB_MT());
        holder.setText(R.id.tv_item_addr, "地址:" + chaoBiaoTables.getUSERB_ADDR());
        holder.setText(R.id.tv_item_sqds, "本期起码:" + chaoBiaoTables.getUSERB_SQDS());
        holder.setText(R.id.tv_item_check_code, "本期止码:" + chaoBiaoTables.getWATERM_CHECK_CODE());
        holder.setText(R.id.tv_item_yield, "水量:" + chaoBiaoTables.getWATERM_YIELD());
        holder.setText(R.id.tv_item_updata, "上传状态:" + getUpdataStute(holder, chaoBiaoTables.getIS_UPDATA()));
        if (TextUtils.isEmpty(chaoBiaoTables.getIS_SAVE()) || TextUtils.isEmpty(chaoBiaoTables.getIS_UPDATA()))
        {
            holder.getView(R.id.layoutAll).setBackgroundResource(R.drawable.selector_back_bg);
        }
        else if (Integer.valueOf(chaoBiaoTables.getIS_SAVE()) == 0 && Integer.valueOf(chaoBiaoTables.getIS_UPDATA()) == 0)
        {
            holder.getView(R.id.layoutAll).setBackgroundResource(R.drawable.selector_back_bg);
        }
        else if (Integer.valueOf(chaoBiaoTables.getIS_SAVE()) == 1 && Integer.valueOf(chaoBiaoTables.getIS_UPDATA()) == 0)
        {
            holder.getView(R.id.layoutAll).setBackgroundResource(R.drawable.selector_save_bg);
        }
        else if (Integer.valueOf(chaoBiaoTables.getIS_UPDATA()) == 1)
        {
            holder.getView(R.id.layoutAll).setBackgroundResource(R.drawable.selector_upload_bg);
        }
    }

    public String getUpdataStute(ViewHolder holder, String stute)
    {
        if (TextUtils.isEmpty(stute))
        {
            return "未上传";
        }
        if (stute.equals("0"))
        {
            return "未上传";
        }
        return "已上传";
    }
}
