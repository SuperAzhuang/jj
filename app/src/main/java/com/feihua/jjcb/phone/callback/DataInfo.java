package com.feihua.jjcb.phone.callback;

import org.json.JSONObject;

/**
 * Created by wcj on 2016-03-29.
 */
public class DataInfo
{
    public boolean success;
    public String msg;

    public DataInfo()
    {
    }

    public DataInfo(boolean success, String msg)
    {
        this.success = success;
        this.msg = msg;
    }

}
