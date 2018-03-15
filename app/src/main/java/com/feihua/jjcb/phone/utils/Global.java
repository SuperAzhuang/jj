package com.feihua.jjcb.phone.utils;

import android.content.Context;

/**
 * Created by wcj on 2016-03-01.
 */
public class Global
{
    public static Context mContext;
    public static Context getApplicationContext()
    {
        return mContext;
    }

    public synchronized  static void setApplicationContext(Context context)
    {
        mContext = context;
    }
}
