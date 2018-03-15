package com.feihua.jjcb.phone.callback;

import android.util.Log;

import com.feihua.jjcb.phone.utils.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wcj on 2016-04-05.
 */
public abstract class NotObjCommonCallback extends Callback<Boolean>
{
    @Override
    public Boolean parseNetworkResponse(Response response) throws Exception
    {
        String content = response.body().string();
        L.w("CommonCallback", "content" + content);
        DataInfo dataInfo = new Gson().fromJson(content, DataInfo.class);
        boolean isSuccess = false;
        if (dataInfo.success)
        {
            isSuccess = true;
        }
        else
        {
            onNetworkError(false,dataInfo.msg);
        }
        return isSuccess;
    }

    @Override
    public void onError(Call call, Exception e)
    {
        L.w("CommonCallback", "e.getMessage()" + e.getMessage());
        onNetworkError(true,e.getMessage());
    }

    public abstract void onNetworkError(boolean isNetwork,String error);
}
