package com.feihua.jjcb.phone.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-09-05.
 */
public abstract class NaviInitUtil
{
    private Activity act;
    private static final String APP_FOLDER_NAME = "jjcb";
    private String mSDCardPath = null;
    String authinfo = null;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public NaviInitUtil(Activity act){
        this.act = act;
        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(false);

        if (initDirs()) {
            initNavi();
        }
    }
    //获取SD卡路径
    private String getSdcardDir()
    {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
        {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    //初始化路径
    private boolean initDirs()
    {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null)
        {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists())
        {
            try
            {
                f.mkdir();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    //初始化导航
    private void initNavi()
    {

        BaiduNaviManager.getInstance().init(act, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener()
        {
            @Override
            public void onAuthResult(int status, String msg)
            {
//                if (0 == status)
//                {
//                    authinfo = "校验成功!";
//                }
//                else
//                {
//                    authinfo = "校验失败, " + msg;
//                }
//
//                act.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(act, authinfo, Toast.LENGTH_LONG).show();
//                    }
//                });
            }

            public void initSuccess()
            {
//                Toast.makeText(act, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            public void initStart()
            {
//                Toast.makeText(act, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed()
            {
//                Toast.makeText(act, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }


        }, null);

    }
    //初始化导航设置
    private void initSetting()
    {
        // 设置是否双屏显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        //        // 设置导航播报模式
        //        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }
    //设置起始/结束坐标并跳转到导航界面
    public void toNavi(Node sNode,Node eNode){
        if (BaiduNaviManager.isNaviInited())
        {
            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL,sNode,eNode);
        }
    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType, Node sNode, Node eNode)
    {
        BNRoutePlanNode startNode = new BNRoutePlanNode(sNode.getLon(), sNode.getLat(), sNode.getAddress(), null, coType);
        BNRoutePlanNode endNode = new BNRoutePlanNode(eNode.getLon(), eNode.getLat(), eNode.getAddress(), null, coType);

        if (sNode != null && eNode != null)
        {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(startNode);
            list.add(endNode);
            BaiduNaviManager.getInstance().launchNavigator(act, list, 1, true, new NaviRoutePlanListener(startNode));
        }
    }

    private class NaviRoutePlanListener implements BaiduNaviManager.RoutePlanListener
    {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public NaviRoutePlanListener(BNRoutePlanNode node)
        {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator()
        {
            /*
             * 设置途径点以及resetEndNode会回调该接口
			 */

            Intent intent = new Intent(act, toNaviActivity());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            act.startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed()
        {
            // TODO Auto-generated method stub
//            Toast.makeText(act, "算路失败", Toast.LENGTH_SHORT).show();
            Toast.makeText(act, "路径设置失败", Toast.LENGTH_SHORT).show();
        }
    }
    //获取导航界面Activity类
    public abstract Class<?> toNaviActivity();

}
