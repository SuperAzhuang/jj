package com.feihua.jjcb.phone.callback;

import android.text.TextUtils;

/**
 * Created by wcj on 2016-03-30.
 */
public class BiaoCeTables
{
    public String VOLUME_NO;
    public String VOLUME_MCOUNT;
    public String USER_ID;
    public String VOLUME_UPDATA;
    public String VOLUME_SAVE;

    public BiaoCeTables(String VOLUME_NO, String VOLUME_MCOUNT, String USER_ID, String VOLUME_UPDATA, String VOLUME_SAVE)
    {
        this.VOLUME_NO = VOLUME_NO;
        this.VOLUME_MCOUNT = VOLUME_MCOUNT;
        this.USER_ID = USER_ID;
        this.VOLUME_UPDATA = VOLUME_UPDATA;
        this.VOLUME_SAVE = VOLUME_SAVE;
    }

    public String getVOLUME_NO()
    {
        return VOLUME_NO == null ? "" : VOLUME_NO.trim();
    }

    public String getVOLUME_MCOUNT()
    {
        return TextUtils.isEmpty(VOLUME_MCOUNT) ? "0" : VOLUME_MCOUNT.trim();
    }

    public String getUSER_ID()
    {
        return USER_ID == null ? "" : USER_ID.trim();
    }

    public String getVOLUME_UPDATA()
    {
        return TextUtils.isEmpty(VOLUME_UPDATA) ? "0" : VOLUME_UPDATA.trim();
    }

    public String getVOLUME_SAVE()
    {
        return TextUtils.isEmpty(VOLUME_SAVE) ? "0" : VOLUME_SAVE.trim();
    }
}
