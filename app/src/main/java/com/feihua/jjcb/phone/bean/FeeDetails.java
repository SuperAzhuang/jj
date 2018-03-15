package com.feihua.jjcb.phone.bean;

/**
 * Created by wcj on 2016-08-08.
 */
public class FeeDetails
{
    public String USERB_KH;//户号
    public String WATERU_QAN;//抄表水量
    public String DEBTL_STOTAL;//水费
    public String DEBTL_YEAR;//年
    public String DEBTL_MON;//月
    public String PAY_TAG;//欠费状态

    public String getUSERB_KH()
    {
        return USERB_KH == null ? "" : USERB_KH.trim();
    }

    public String getWATERU_QAN()
    {
        return WATERU_QAN == null ? "" : WATERU_QAN.trim();
    }

    public String getDEBTL_YEAR()
    {
        return DEBTL_YEAR == null ? "" : DEBTL_YEAR.trim();
    }

    public String getDEBTL_MON()
    {
        return DEBTL_MON == null ? "" : DEBTL_MON.trim();
    }

    public String getDEBTL_STOTAL()
    {
        return DEBTL_STOTAL == null ? "" : DEBTL_STOTAL.trim();
    }

    public String getPAY_TAG()
    {
        return PAY_TAG == null ? "" : PAY_TAG.trim();
    }

}
