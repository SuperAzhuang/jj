package com.feihua.jjcb.phone.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wcj on 2016-04-05.
 */
public abstract class UpdataDetailsCallback extends Callback<User>
{
    @Override
    public User parseNetworkResponse(Response response) throws Exception
    {
        String content = response.body().string();
        Log.w("UpdataDetailsCallback", "content" + content);
        DataInfo dataInfo = new Gson().fromJson(content, DataInfo.class);
        DownTables tables = null;
        if (dataInfo.success)
        {
//            JSONObject jsonObject = new JSONObject(content);
//            JSONObject obj = jsonObject.getJSONObject("obj");
//            tables = new Gson().fromJson(obj.toString(), DownTables.class);
        }
        else
        {
            onNetworkError(false,dataInfo.msg);
        }
        return null;
    }

    @Override
    public void onError(Call call, Exception e)
    {
        onNetworkError(true,e.getMessage());
    }

    public abstract void onNetworkError(boolean isNetwork,String error);
}
