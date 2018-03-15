package com.feihua.jjcb.phone.callback;

import com.feihua.jjcb.phone.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by wcj on 2016-03-30.
 */
public class ChaoBiaoTables implements Serializable
{
    public String VOLUME_NO;//册号
    public String USERB_KH;//户号
    public String USERB_HM;//户名
    public String USERB_ADDR;//地址
    public String USERB_DH;//电话
    public String USERB_MT;//手机
    public String METERC_CALIBRE;//口径
    public String WATERM_TYPE;//表型
    public String METERC_BASIC;//基本吨
    public String USERB_YSXZ;//用水性质/比例
    public String USERB_CUP;//水价
    public String WATERM_AZRQ;//安装日期
    public String USERB_LHRQ;//报装日期
    public String WATERM_NX;//现用水表年限
    public String WATERM_NO;//表号
    public String USERB_SQDS;//本期起码
    public String USERB_JBH;//抄表序号
    public String GPS;//GPS
    public String WATERM_CHECK_CODE;//止码
    public String WATERM_YIELD;//水量
    public String IS_UPDATA;//上传状态
    public String IS_SAVE;//抄表状态
    public String Q3Q;//平均3个月用水量
    public String WATERM_REPDATE;//换表日期
    public String USERB_YHLX;//用户类型
    public String WATERT_NAME;//用户类型
    public String WATERU_YEAR;//抄表年
    public String WATERU_MONTH;//抄表月
    public String TIME;//手机抄表时间
    public String WATER_BK;//表况
    public String IS_CBWAY;//实抄1估抄0
    public String METERH_WATERQAN;//换表水量
    public String AREA_NO;//片区
    public String WATERU_QAN;//水量-条件查询
    public String USERB_LXR;//联系人-条件查询
    public String USERB_TOTAL;//户籍人数-条件查询
    public String USERB_BQDS;//本期止码-条件查询
    public String SC;//上传状态-表册下载
    public String BASICTON_TAG;//基本吨户0不是1是
    public String BASICTON_QAN;//基本吨
    public String FIRST_TAG;// 首次抄表标识
    public String THREEMON_AVG;//上期水量增减幅度
    public String LASTYEAR_QAN;//前三期均量增减幅度
    public String LAST_QAN;//去年同期增减幅度
    public String COUNT_QAN;//抄表情况-水量
    public String USERB_SFFS;//收费方式
//    public String USERB_TOTAL;//
    public String ISCHARGING;//
//    public String USERB_CUP;//
    public String FREEPULL_TAG;//
//    public String BASICTON_TAG;//
//    public String BASICTON_QAN;//
    public String LADDER_TAG;//
    public String SEASON_TAG;//
    public String WZ_TAG;//
    public String USERB_SYSL;//
    public String QFJE;//

    public String getQFJE()
    {
        return QFJE == null ? "" : QFJE.trim();
    }


    public String getISCHARGING()
    {
        return ISCHARGING == null ? "" : ISCHARGING.trim();
    }

    public String getFREEPULL_TAG()
    {
        return FREEPULL_TAG == null ? "" : FREEPULL_TAG.trim();
    }

    public String getLADDER_TAG()
    {
        return LADDER_TAG == null ? "" : LADDER_TAG.trim();
    }

    public String getSEASON_TAG()
    {
        return SEASON_TAG == null ? "" : SEASON_TAG.trim();
    }

    public String getWZ_TAG()
    {
        return WZ_TAG == null ? "" : WZ_TAG.trim();
    }

    public String getUSERB_SYSL()
    {
        return USERB_SYSL == null ? "" : USERB_SYSL.trim();
    }

    public String getTHREEMON_AVG(){
        return THREEMON_AVG == null ? "" : THREEMON_AVG.trim();
    }
    public String getUSERB_SFFS(){
        return USERB_SFFS == null ? "" : USERB_SFFS.trim();
    }
    public String getCOUNT_QAN(){
        return COUNT_QAN == null ? "" : COUNT_QAN.trim();
    }
    public String getLASTYEAR_QAN(){
        return LASTYEAR_QAN == null ? "" : LASTYEAR_QAN.trim();
    }
    public String getLAST_QAN(){
        return LAST_QAN == null ? "" : LAST_QAN.trim();
    }
    public String getSC()
    {
        return SC == null ? "" : SC.trim();
    }
    public String getFIRST_TAG()
    {
        return FIRST_TAG == null ? "" : FIRST_TAG.trim();
    }
    public String getMETERH_WATERQAN()
    {
        return METERH_WATERQAN == null ? "0" : METERH_WATERQAN.trim();
    }

    public String getUSERB_BQDS()
    {
        return USERB_BQDS == null ? "" : USERB_BQDS.trim();
    }

    public String getUSERB_TOTAL()
    {
        return USERB_TOTAL == null ? "" : USERB_TOTAL.trim();
    }

    public String getUSERB_LXR()
    {
        return USERB_LXR == null ? "" : USERB_LXR.trim();
    }


    public String getAREA_NO()
    {
        return AREA_NO == null ? "" : AREA_NO.trim();
    }

    public String getWATER_BK()
    {
        return WATER_BK == null ? "" : WATER_BK.trim();
    }

    public String getIS_CBWAY()
    {
        return IS_CBWAY == null ? "1" : IS_CBWAY.trim();
    }
    public String getBASICTON_TAG()
    {
        return BASICTON_TAG == null ? "0" : BASICTON_TAG.trim();
    }
    public String getBASICTON_QAN()
    {
        return BASICTON_QAN == null ? "0" : BASICTON_QAN.trim();
    }

    public String getVOLUME_NO()
    {
        return VOLUME_NO == null ? "" : VOLUME_NO.trim();
    }

    public String getUSERB_KH()
    {
        return USERB_KH == null ? "" : USERB_KH.trim();
    }

    public String getUSERB_HM()
    {
        return USERB_HM == null ? "" : USERB_HM.trim();
    }

    public String getUSERB_ADDR()
    {
        return USERB_ADDR == null ? "" : USERB_ADDR.trim();
    }

    public String getUSERB_DH()
    {
        return USERB_DH == null ? "" : USERB_DH.trim();
    }

    public String getUSERB_MT()
    {
        return USERB_MT == null ? "" : USERB_MT.trim();
    }

    public String getMETERC_CALIBRE()
    {
        return METERC_CALIBRE == null ? "" : METERC_CALIBRE.trim();
    }

    public String getWATERM_TYPE()
    {
        return WATERM_TYPE == null ? "" : WATERM_TYPE.trim();
    }

    public String getMETERC_BASIC()
    {
        return METERC_BASIC == null ? "" : METERC_BASIC.trim();
    }

    public String getWATERU_QAN()
    {
        return WATERU_QAN == null ? "" : WATERU_QAN.trim();
    }

    public String getUSERB_YSXZ()
    {
        return USERB_YSXZ == null ? "" : USERB_YSXZ.trim();
    }

    public String getUSERB_CUP()
    {
        return USERB_CUP == null ? "" : USERB_CUP.trim();
    }

    public String getWATERM_AZRQ()
    {
        return WATERM_AZRQ == null ? "" : WATERM_AZRQ.trim();
    }

    public String getUSERB_LHRQ()
    {
        return USERB_LHRQ == null ? "" : USERB_LHRQ.trim();
    }

    public String getWATERM_NX()
    {
        return WATERM_NX == null ? "" : WATERM_NX.trim();
    }

    public String getWATERM_NO()
    {
        return WATERM_NO == null ? "" : WATERM_NO.trim();
    }

    public String getUSERB_SQDS()
    {
        return USERB_SQDS == null ? "0" : USERB_SQDS.trim();
    }

    public String getUSERB_JBH()
    {
        return USERB_JBH == null ? "" : USERB_JBH.trim();
    }

    public String getGPS()
    {
        return GPS == null ? "" : GPS.trim();
    }

    public String getWATERM_CHECK_CODE()
    {
        return WATERM_CHECK_CODE == null ? "" : WATERM_CHECK_CODE.trim();
    }

    public String getWATERM_YIELD()
    {
        return WATERM_YIELD == null ? "" : WATERM_YIELD.trim();
    }

    public String getIS_UPDATA()
    {
        return IS_UPDATA == null ? "" : IS_UPDATA.trim();
    }

    public String getIS_SAVE()
    {
        return IS_SAVE == null ? "" : IS_SAVE.trim();
    }

    public String getQ3Q()
    {
        return Q3Q == null ? "" : Q3Q.trim();
    }

    public String getWATERM_REPDATE()
    {
        return WATERM_REPDATE == null ? "" : WATERM_REPDATE.trim();
    }

    public String getUSERB_YHLX()
    {
        return USERB_YHLX == null ? "" : USERB_YHLX.trim();
    }

    public String getWATERU_YEAR()
    {
        return WATERU_YEAR == null ? "" : WATERU_YEAR.trim();
    }

    public String getWATERU_MONTH()
    {
        return WATERU_MONTH == null ? "" : WATERU_MONTH.trim();
    }

    public String getTIME()
    {
        return TIME == null ? "" : TIME.trim();
    }

    public String getWATERT_NAME()
    {
        return WATERT_NAME == null ? "" : WATERT_NAME.trim();
    }
}
