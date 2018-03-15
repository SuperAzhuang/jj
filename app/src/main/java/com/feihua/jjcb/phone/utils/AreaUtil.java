package com.feihua.jjcb.phone.utils;

import android.text.TextUtils;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.TJCXTypeBean;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by wcj on 2016-04-28.
 */
public class AreaUtil
{

    private static AreaUtil instance;
    private TJCXTypeBean bean;

    private AreaUtil()
    {
    }

    public static AreaUtil getInstance()
    {
        if (instance == null)
        {
            instance = new AreaUtil();
        }
        return instance;
    }

    public void getArea(OnDataListener listener)
    {
        if (bean == null)
        {
            getServiceData(listener);
        }
        else
        {
            listener.onData(bean);
        }
    }

    public interface OnDataListener
    {
        public void onData(TJCXTypeBean bean);
    }

    public TJCXTypeBean getBean()
    {
        return bean;
    }

    public void getServiceData()
    {
        getServiceData(null);
    }

    public void getServiceData(final OnDataListener listener)
    {
        OkHttpUtils.post()//
                .url(Constants.GET_ORG_NO)//
                .build()//
                .execute(new CommonCallback()
                {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error)
                    {
                        if (isNetwork)
                        {
                            ToastUtil.showShortToast(R.string.toast_network_anomalies);
                        }
                        else
                        {
                            ToastUtil.showShortToast(error);
                        }

                    }

                    @Override
                    public void onResponse(String datas)
                    {
                        if (!TextUtils.isEmpty(datas))
                        {
                            bean = new Gson().fromJson(datas, TJCXTypeBean.class);
                            if (listener != null)
                            {
                                listener.onData(bean);
                            }
                        }

                    }
                });
    }
}
