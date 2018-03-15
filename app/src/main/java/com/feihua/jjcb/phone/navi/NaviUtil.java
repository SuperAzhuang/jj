package com.feihua.jjcb.phone.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.feihua.jjcb.phone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-09-05.
 */
public class NaviUtil
{

    private Activity act;
    /*
         * 对于导航模块有两种方式来实现发起导航。 1：使用通用接口来实现 2：使用传统接口来实现
         *
         */
    // 是否使用通用接口
    private boolean useCommonInterface = true;
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;
    private Handler hd = null;
    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;

    public NaviUtil(Activity act)
    {
        this.act = act;
        createHandler(act);
    }

    private void createHandler(Activity act) {
        if (hd == null) {
            hd = new Handler(act.getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {
                        addCustomizedLayerItems();
                    } else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {
//                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
//                                new BNRoutePlanNode(116.21142, 40.85087, "百度大厦11", null, BNRoutePlanNode.CoordinateType.GCJ02));
                    }
                };
            };
        }
    }

    public void addCustomized(){
        //显示自定义图标
        if (hd != null) {
            hd.sendEmptyMessageAtTime(MSG_SHOW, 5000);
        }
    }

    private void addCustomizedLayerItems() {
        List<BNRouteGuideManager.CustomizedLayerItem> items = new ArrayList<BNRouteGuideManager.CustomizedLayerItem>();
        BNRouteGuideManager.CustomizedLayerItem item1 = null;
        if (mBNRoutePlanNode != null) {
            item1 = new BNRouteGuideManager.CustomizedLayerItem(mBNRoutePlanNode.getLongitude(), mBNRoutePlanNode.getLatitude(),
                    mBNRoutePlanNode.getCoordinateType(), act.getResources().getDrawable(R.mipmap.ic_launcher),
                    BNRouteGuideManager.CustomizedLayerItem.ALIGN_CENTER);
            items.add(item1);

            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
        }
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
    }

    //获取导航View
    public View getNaviView()
    {
        View view = null;
        if (useCommonInterface)
        {
            //使用通用接口
            mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, act, BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, mOnNavigationListener);
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onCreate();
                view = mBaiduNaviCommonModule.getView();
            }

        }
        else
        {
            //使用传统接口
            view = BNRouteGuideManager.getInstance().onCreate(act, mOnNavigationListener);
        }
        return view;
    }

    public void setNaviRoutePlanNode(Intent intent)
    {
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(NaviInitUtil.ROUTE_PLAN_NODE);
            }
        }

    }

    private BNRouteGuideManager.OnNavigationListener mOnNavigationListener = new BNRouteGuideManager.OnNavigationListener()
    {

        @Override
        public void onNaviGuideEnd()
        {
            //退出导航
            act.finish();
        }

        @Override
        public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj)
        {

            if (actionType == 0)
            {
                //导航到达目的地 自动退出
                Log.i("notifyOtherAction", "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
            }

            Log.i("notifyOtherAction", "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
        }

    };

    public void onPause()
    {
        if (useCommonInterface)
        {
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onPause();
            }
        }
        else
        {
            BNRouteGuideManager.getInstance().onPause();
        }
    }

    public void onDestroy()
    {
        if (useCommonInterface)
        {
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onDestroy();
            }
        }
        else
        {
            BNRouteGuideManager.getInstance().onDestroy();
        }
    }

    public void onStart()
    {
        if (useCommonInterface)
        {
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onStart();
            }
        }
        else
        {
            BNRouteGuideManager.getInstance().onStart();
        }
    }

    public void onStop()
    {
        if (useCommonInterface)
        {
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onStop();
            }
        }
        else
        {
            BNRouteGuideManager.getInstance().onStop();
        }
    }

    public void onResume()
    {
        if (useCommonInterface)
        {
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onResume();
            }
        }
        else
        {
            BNRouteGuideManager.getInstance().onResume();
        }
    }

    public void onBackPressed()
    {
        if (useCommonInterface)
        {
            if (mBaiduNaviCommonModule != null)
            {
                mBaiduNaviCommonModule.onBackPressed(false);
            }
        }
        else
        {
            BNRouteGuideManager.getInstance().onBackPressed(false);
        }
    }
}
