package com.feihua.jjcb.phone.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.BiaoCeTables;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.DownTables;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.db.BiaoCeDatabase;
import com.feihua.jjcb.phone.db.ChaoBiaoDatabase;
import com.feihua.jjcb.phone.db.DatabaseHelper;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-04-18.
 */
public abstract class DownUserbaseAsyncTask extends AsyncTask<DownTables, Void, String>
{
    private String volumeNo;
    private Context ctx;
    private BiaoCeDatabase biaoCeDatabase;
    private ChaoBiaoDatabase chaoBiaoTables;
    private StringBuffer msg;

    public DownUserbaseAsyncTask(Context ctx, String volumeNo)
    {
        this.ctx = ctx;
        this.volumeNo = volumeNo;
        biaoCeDatabase = new BiaoCeDatabase(ctx);
        chaoBiaoTables = new ChaoBiaoDatabase(ctx);
    }

    @Override
    protected String doInBackground(DownTables... params)
    {
        String content = null;
        if (params[0] != null)
        {
            content = saveDatas(params[0]);
        }

        return content;
    }

    //保存已下载成功的数据到数据库
    private String saveDatas(DownTables downTables)
    {
        msg = new StringBuffer(1000);

        L.w("DownUserbaseAsyncTask", "开始表册存储");
        List<BiaoCeTables> volume = downTables.volume;
        //        L.w("CBGLfragment", "volume.size() = " + volume.size());
        //表册
        if (volume != null && volume.size() == 0)
        {
            //            ToastUtil.showShortToast(R.string.down_tables_not);
            return ctx.getResources().getString(R.string.down_tables_not);
        }

        String userId = SharedPreUtils.getString(Constants.USER_ID, ctx);

        boolean isSend = false;

        List<ChaoBiaoTables> userbase = downTables.userbase;
        if (userbase != null)
        {
            ArrayList<String> userKhDatas = chaoBiaoTables.queryUserKh(userbase.get(0).getVOLUME_NO());
            L.w("DownUserbaseAsyncTask", "userKhDatas = " + userKhDatas);
            L.w("DownUserbaseAsyncTask", "userbase.size = " + userbase.size());
            for (ChaoBiaoTables userTables : userbase)
            {
                if (userTables.getUSERB_KH() != null && !userKhDatas.contains(userTables.getUSERB_KH()))
                {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.CHAOBIAO_VOLUME_NO, userTables.getVOLUME_NO());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_KH, userTables.getUSERB_KH());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_HM, userTables.getUSERB_HM());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_ADDR, userTables.getUSERB_ADDR());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_DH, userTables.getUSERB_DH());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_MT, userTables.getUSERB_MT());
                    values.put(DatabaseHelper.CHAOBIAO_METERC_CALIBRE, userTables.getMETERC_CALIBRE());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_TYPE, userTables.getWATERM_TYPE());
                    values.put(DatabaseHelper.CHAOBIAO_METERC_BASIC, userTables.getMETERC_BASIC());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_YSXZ, userTables.getUSERB_YSXZ());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_CUP, userTables.getUSERB_CUP());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_AZRQ, userTables.getWATERM_AZRQ());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_LHRQ, userTables.getUSERB_LHRQ());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_NX, userTables.getWATERM_NX());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_NO, userTables.getWATERM_NO());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_SQDS, userTables.getUSERB_SQDS());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_JBH, userTables.getUSERB_JBH());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_LOALTION, userTables.getGPS());
                    values.put(DatabaseHelper.CHAOBIAO_Q3Q, userTables.getQ3Q());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_REPDATE, userTables.getWATERM_REPDATE());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_YHLX, userTables.getUSERB_YHLX());
                    values.put(DatabaseHelper.CHAOBIAO_WATERT_NAME, userTables.getWATERT_NAME());
                    values.put(DatabaseHelper.CHAOBIAO_WATERU_YEAR, userTables.getWATERU_YEAR());
                    values.put(DatabaseHelper.CHAOBIAO_WATERU_MONTH, userTables.getWATERU_MONTH());
                    values.put(DatabaseHelper.CHAOBIAO_AREA_NO, userTables.getAREA_NO());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_CHECK_CODE, userTables.getUSERB_BQDS());
                    values.put(DatabaseHelper.CHAOBIAO_WATERM_YIELD, userTables.getWATERU_QAN());
                    values.put(DatabaseHelper.CHAOBIAO_METERH_WATERQAN, userTables.getMETERH_WATERQAN());
                    values.put(DatabaseHelper.CHAOBIAO_IS_UPDATA, userTables.getSC());
                    if (userTables.getSC().equals("1"))
                    {
                        values.put(DatabaseHelper.CHAOBIAO_IS_SAVE, "1");
                    }
                    else
                    {
                        values.put(DatabaseHelper.CHAOBIAO_IS_SAVE, "0");
                    }
                    values.put(DatabaseHelper.CHAOBIAO_WATER_BK, userTables.getWATER_BK());
                    values.put(DatabaseHelper.CHAOBIAO_IS_CBWAY, userTables.getIS_CBWAY());
                    values.put(DatabaseHelper.CHAOBIAO_BASICTON_TAG, userTables.getBASICTON_TAG());
                    values.put(DatabaseHelper.CHAOBIAO_BASICTON_QAN, userTables.getBASICTON_QAN());
                    values.put(DatabaseHelper.CHAOBIAO_FIRST_TAG, userTables.getFIRST_TAG());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_SFFS, userTables.getUSERB_SFFS());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_TOTAL, userTables.getUSERB_TOTAL());
                    values.put(DatabaseHelper.CHAOBIAO_ISCHARGING, userTables.getISCHARGING());
                    values.put(DatabaseHelper.CHAOBIAO_FREEPULL_TAG, userTables.getFREEPULL_TAG());
                    values.put(DatabaseHelper.CHAOBIAO_LADDER_TAG, userTables.getLADDER_TAG());
                    values.put(DatabaseHelper.CHAOBIAO_SEASON_TAG, userTables.getSEASON_TAG());
                    values.put(DatabaseHelper.CHAOBIAO_WZ_TAG, userTables.getWZ_TAG());
                    values.put(DatabaseHelper.CHAOBIAO_USERB_SYSL, userTables.getUSERB_SYSL());
                    values.put(DatabaseHelper.CHAOBIAO_QFJE,userTables.getQFJE());

                    chaoBiaoTables.insert(DatabaseHelper.CHAOBIAO_TABLE_NAME, values);
                    userKhDatas.add(userTables.getUSERB_KH());
                }
                else
                {
                    L.w("DownUserbaseAsyncTask", "userTables.getUSERB_KH() = " + userTables.getUSERB_KH());
                    //TODO
                }
            }
            int updataNum = chaoBiaoTables.queryCount(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?" + " and " + DatabaseHelper.CHAOBIAO_IS_UPDATA + "=?", new String[]{volumeNo, "1"});
            L.w("DownUserbaseAsyncTask", "userbase.get(0).getVOLUME_NO() = " + userbase.get(0).getVOLUME_NO());
            L.w("DownUserbaseAsyncTask", "userbase.get(0).getUSERB_KH() = " + userbase.get(0).getUSERB_KH());
            int saveNum = chaoBiaoTables.queryCount(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?" + " and " + DatabaseHelper.CHAOBIAO_IS_SAVE + "=?", new String[]{volumeNo, "1"});
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.BIAOCE_VOLUME_SAVE, String.valueOf(saveNum));
            values.put(DatabaseHelper.BIAOCE_VOLUME_UPDATA, String.valueOf(updataNum));
            biaoCeDatabase.updatatable(values, userbase.get(0).getVOLUME_NO());
        }
        //抄表


        if (!isSend)
        {
            msg.append(ctx.getResources().getString(R.string.down_tables_success));
            //            Message msg = new Message();
            //            msg.what = TOAST_SUCCESS;
            //            handler.sendMessage(msg);
        }
        else
        {
            msg.append("表册已存在");
        }
        L.w("CBGLfragment", "抄表存储完成");
        return msg.toString();

    }
}
