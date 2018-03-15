package com.feihua.jjcb.phone.callback;

import android.util.Log;

import com.feihua.jjcb.phone.utils.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wcj on 2016-03-29.
 */
public abstract class DownTablesCallback extends Callback<DownTables>
{

    @Override
    public DownTables parseNetworkResponse(Response response) throws Exception
    {
        String content = response.body().string();
        L.w("DownTablesCallback", "content" + content);
        DataInfo dataInfo = new Gson().fromJson(content, DataInfo.class);
        DownTables tables = null;
        if (dataInfo.success)
        {
            JSONObject jsonObject = new JSONObject(content);
            JSONObject obj = jsonObject.getJSONObject("obj");
            tables = new Gson().fromJson(obj.toString(), DownTables.class);
        }
        else
        {
            onNetworkError(false,dataInfo.msg);
        }
        return tables;
    }

    @Override
    public void onError(Call call, Exception e)
    {
        onNetworkError(true,e.getMessage());
    }

    public abstract void onNetworkError(boolean isNetwork,String error);

}
