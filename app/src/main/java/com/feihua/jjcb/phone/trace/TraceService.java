package com.feihua.jjcb.phone.trace;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.LocationInfoBean;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.utils.DateUtil;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TraceService extends Service
{

    private LBSTraceClient client;
    private Trace trace;
    private long interval;
//    private long serviceId = 131180;
    private long serviceId = 117028;
    private int startTime;
    private int endTime;
    public boolean isStart;
    private String entityName;
    public String SERVICE_NAME = "com.baidu.trace.LBSTraceService";
    public boolean isCheck = false;
    public boolean isRunning = false;
    public TraceBinder traceBinder;
    private int gatherInterval = 15;

    public TraceService()
    {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        flags = START_STICKY;


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        initBaiDuYingYan();
        traceBinder = new TraceBinder();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle("晋江抄表轨迹服务");
        builder.setContentText("晋江抄表轨迹服务");
        builder.setSmallIcon(R.mipmap.icon);
        startForeground(123, builder.build());
        timerControlSwitch();
    }

    private void timerControlSwitch()
    {
        // 在清单里不能加android:process=":remote",因为加了,表示调用到不同进程的SP,在MainActivity系统进程修改SP,在另外的remote进程肯定得不到修改后的值.
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            // 用Timer备注：如果手动修改系统时间，往后修改没问题，往前修改，会暂停到恢复原来时间后继续运行
            @Override
            public void run()
            {
                Log.e("TarceService", "getWorkState = " + getWorkState());
                L.w("TraceService", "getWorkState() = " + getWorkState());
                if (getWorkState())
                {
                    if (!isStart)
                    {
                        traceBinder.startTrace();
                    }
                }
                else
                {
                    if (isStart)
                    {
                        traceBinder.stopTrace();
                    }
                }
                if (isStart)
                {
//                    L.w("TraceService", "isServiceWork = " + isServiceWork(TraceService.this, SERVICE_NAME));
                    if (!isServiceWork(TraceService.this, SERVICE_NAME))
                    {
                        client.startTrace(trace, startListener);
                    }
                }
            }
        };
        timer.schedule(task, 1000, 5 * 1000);
    }

    //获取员工是否需要上传:true要false不要
    private boolean getWorkState()
    {
        boolean isLogin = SharedPreUtils.getBoolean(Constants.IS_LOGIN, this);
        String locationInfo = SharedPreUtils.getString(Constants.LOCATION_INFO, this);
        L.w("TraceService", "isLogin = " + isLogin);
        L.w("TraceService", "locationInfo = " + locationInfo);
        if (!isLogin){
            return false;
        }
        if (!TextUtils.isEmpty(locationInfo))
        {
       //     {"TIME":[{"STRAT_TIME":"08:30","END_TIME":"12:00"},{"STRAT_TIME":"12:30","END_TIME":"18:00"},{"STRAT_TIME":"18:30","END_TIME":"21:30"}],"isUpdate":"1"}
            LocationInfoBean bean = new Gson().fromJson(locationInfo, LocationInfoBean.class);
            if (TextUtils.isEmpty(bean.isUpdate))
            {
                return false;
            }
            if (bean.isUpdate.equals("0"))
            {
                return false;
            }
            else
            {
                if (bean.TIME == null || bean.TIME.size() == 0)
                {
                    return false;
                }
                long curTime = DateUtil.HHmmToMm(DateUtil.getCurrentTime());
                boolean update = false;
                for (int i = 0; i < bean.TIME.size(); i++)
                {
                    LocationInfoBean.TimeInfo timeInfo = bean.TIME.get(i);
                    if (timeInfo.STRAT_TIME != null && timeInfo.END_TIME != null)
                    {
                        long startTime = DateUtil.HHmmToMm(timeInfo.STRAT_TIME);
                        long endTime = DateUtil.HHmmToMm(timeInfo.END_TIME);
                        if (startTime < curTime && curTime < endTime)
                        {
                            update = true;
                        }
                    }
                }
                return update;
            }
        }
        return false;
    }

    private void initBaiDuYingYan()
    {
        // 初始化轨迹服务客户端
        client = new LBSTraceClient(getApplicationContext());

        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);

        entityName = SharedPreUtils.getString(Constants.USER_ID, this);

        Log.e("TarceService", "entityName = " + entityName);

        // 初始化轨迹服务
        trace = new Trace(getApplicationContext(), serviceId, entityName, 2);

        initOption();
    }

    // 设置定位参数
    private void initOption()
    {
        // 设置采集周期，打包周期
        // client.setInterval((int) (interval / 1000), 60 * 5);
        client.setInterval(gatherInterval, 20);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return traceBinder;
    }

    public class TraceBinder extends Binder
    {
        //        public void initTrace(String volumeNo){
        //            // 初始化轨迹服务
        //            entityName = SharedPreUtils.getString(Constants.USER_ID, TraceService.this) + volumeNo;
        //            trace = new Trace(getApplicationContext(), serviceId, entityName, 2);
        //        }

        public void startTrace()
        {
            isStart = true;
            Log.e("TarceService", "startTrace = ====="  );
            client.startTrace(trace, startListener);
            // 开始时间
            startTime = (int) (System.currentTimeMillis() / 1000);
            //            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
            if (!isRunning)
            {
                // 开启监听service
                isCheck = true;
                isRunning = true;
            }
        }

        public void stopTrace()
        {
            isStart = false;
            client.stopTrace(trace, stopListener);
            endTime = (int) (System.currentTimeMillis() / 1000);
        }

        public void queryHistoryTrack(OnTrackListener trackListener)
        {
            queryProcessedHistoryTrack(trackListener);
        }

        public void queryRealtimeLocation(OnEntityListener entityListener)
        {
            queryRealtimeLoc(entityListener);
        }

        public boolean getTraceState()
        {
            return isStart;
        }

        public void addEntity(OnEntityListener entityListener)
        {
            addEntity(entityListener);
        }

        public int getGatherInterval()
        {
            return gatherInterval;
        }
    }

    /**
     * 添加entity
     */
    private void addEntity(OnEntityListener entityListener)
    {
        // 属性名称（格式 : "key1=value1,columnKey2=columnValue2......."）
        String columnKey = "name=testName";
        client.addEntity(serviceId, entityName, columnKey, entityListener);
    }

    /**
     * 查询实时轨迹
     *
     * @param
     */
    private void queryRealtimeLoc(OnEntityListener entityListener)
    {
        client.queryRealtimeLoc(serviceId, entityListener);
    }

    /**
     * 查询纠偏后的历史轨迹
     */
    private void queryProcessedHistoryTrack(OnTrackListener trackListener)
    {
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = 1;
        //纠偏选项  仅在is_processed=1时生效，通过为纠偏选项赋1（需要）或0（不需要）来设置是否需要该项数据处理，支持的纠偏选项如下：
        //need_denoise：去噪，默认为1
        //need_vacuate：抽稀，默认为1
        //need_mapmatch：绑路（仅适用于驾车），默认为0（但之前已开通绑路的service，默认为1）
        String processOption = "need_denoise=1,need_vacuate=1,need_mapmatch=1";
        // 开始时间
        endTime = (int) (System.currentTimeMillis() / 1000);

        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

        client.queryHistoryTrack(serviceId, entityName, simpleReturn, isProcessed, processOption, startTime, endTime, pageSize, pageIndex, trackListener);
    }

    OnStopTraceListener stopListener = new OnStopTraceListener()
    {
        @Override
        public void onStopTraceSuccess()
        {
//            ToastUtil.showShortToast("已停止轨迹服务");
        }

        @Override
        public void onStopTraceFailed(int arg0, String message)
        {
        }
    };

    OnStartTraceListener startListener = new OnStartTraceListener()
    {
        @Override
        public void onTracePushCallback(byte arg0, String arg1)
        {
            L.e("startListener", "onTracePushCallback");
        }

        @Override
        public void onTraceCallback(int arg0, String message)
        {
            if (!TextUtils.isEmpty(message) && message.equals("success"))
            {
//                ToastUtil.showShortToast("已开启轨迹服务");
            }
            L.e("startListener", "onTraceCallback message = " + message);
        }
    };

    public void onDestroy()
    {
        stopForeground(true);
        Intent i = new Intent(Constants.TRACESERVICE_DESTORY);
        sendBroadcast(i);
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：com.baidu.trace.LBSTraceService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName)
    {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> myList = myAM.getRunningServices(80);
        if (myList.size() <= 0)
        {
            return false;
        }
        for (int i = 0; i < myList.size(); i++)
        {
            String mName = myList.get(i).service.getClassName().toString();
//            L.e("serviceName : " + mName);
            if (mName.equals(serviceName))
            {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
