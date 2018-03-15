package com.feihua.jjcb.phone.bean;

/**
 * Created by wcj on 2016-05-23.
 */
public class YHQuery
{
    public String USERB_KH;
    public String USERB_HM;
    public String WATERT_NAME;
    public float DEBTL_STOTAL;
    public String PAY_WAY;

    public String getUSERB_KH()
    {
        return USERB_KH == null ? "" : USERB_KH;
    }

    public String getUSERB_HM()
    {
        return USERB_HM == null ? "" : USERB_HM;
    }

    public String getWATERT_NAME()
    {
        return WATERT_NAME == null ? "" : WATERT_NAME;
    }

    public float getDEBTL_STOTAL()
    {
        return DEBTL_STOTAL;
    }

    public String getPAY_WAY()
    {
        return PAY_WAY == null ? "" : PAY_WAY;
    }
}
