package com.feihua.jjcb.phone.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.feihua.jjcb.phone.callback.NotObjCommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.trace.TraceService;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class TargetReceiver extends BroadcastReceiver
{
    public TargetReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("com.feihua.jjcb.service.destroy"))
        {
            //TODO
            //在这里写重新启动service的相关操作
            startUploadService(context);
        }
        else if (intent.getAction().equals(Constants.TRACESERVICE_DESTORY))
        {
            startTrace(context);
        }
    }

    private void startTrace(Context context)
    {
        Intent intent = new Intent(context, TraceService.class);
        context.startService(intent);
    }

    private void startUploadService(Context context)
    {
        Intent i = new Intent();
        i.setClass(context, TargetService.class);
        context.startService(i);
    }


}
