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
public abstract class CommonCallback extends Callback<String>
{
    private boolean isSuccess;

    @Override
    public String parseNetworkResponse(Response response) throws Exception
    {
        String content = response.body().string();
        L.w("CommonCallback", "content" + content);
        DataInfo dataInfo = new Gson().fromJson(content, DataInfo.class);
        String datas = "";
        isSuccess = false;
        if (dataInfo.success)
        {
            isSuccess = true;
            JSONObject jsonObject = new JSONObject(content);
            JSONObject obj = jsonObject.getJSONObject("obj");
            datas = obj.toString();
        }
        else
        {
            onNetworkError(false, dataInfo.msg);
        }
        return datas;
    }

    @Override
    public void onError(Call call, Exception e)
    {
        if (isSuccess == false)
        {
            onNetworkError(true, e.getMessage());
        }
    }

    public abstract void onNetworkError(boolean isNetwork, String error);
}
