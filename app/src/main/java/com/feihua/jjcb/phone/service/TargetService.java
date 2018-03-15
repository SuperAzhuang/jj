package com.feihua.jjcb.phone.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.NotObjCommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.trace.TraceService;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.PowerListener;
import com.feihua.jjcb.phone.utils.PowerUtil;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class TargetService extends Service
{
    private Context ctx;
    private boolean isUpload = true;
    private Handler handler = new Handler();
    private Vibrator vibrator;
    private LocationManager locationManager;
    private int stateNum = 0;
    ContentObserver mGpsMonitor = new ContentObserver(null)
    {
        @Override
        public void onChange(boolean selfChange)
        {
            super.onChange(selfChange);
            boolean hasGPS = hasGPS();
            L.w("TargetService", "mGpsMonitor");
            if (!hasGPS)
            {
                //                if (getIsUploadLocation())
                //                {// 工作时间才发送通知
                sendNotification();
                //                }
            }
        }
    };
    private TraceService.TraceBinder binder;
    private boolean isStart;
    private ServiceConnection conn = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            binder = (TraceService.TraceBinder) service;
            isStart = binder.getTraceState();

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };
    private PowerUtil powerUtil;
    public String powerDate = "";

    public TargetService()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        settingGpsSwitch();// 监听GPS开关状态,在工作时间没打开时,发送通知

        initPower();

        initBaiduTrace();
        startUpload();

    }

    private void initPower()
    {
        powerUtil = new PowerUtil(ctx);
        powerUtil.setOnPowerListener(new PowerListener()
        {
            @Override
            public void onPowerDate(String powerDate)
            {
                TargetService.this.powerDate = powerDate;
            }
        });
    }

    private void initBaiduTrace()
    {
        Intent service = new Intent(this, TraceService.class);
        startService(service);
        bindService(service, conn, 0);
    }

    private void settingGpsSwitch()
    {// 监测GPS开关状态
        getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED), true, mGpsMonitor);
    }

    private boolean hasGPS()
    {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void sendNotification()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setTicker("请打开手机GPS定位开关");
        builder.setSmallIcon(R.mipmap.icon);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setContentTitle("请打开手机GPS定位开关");
        builder.setContentText("为了确保你的地理位置上传信息准确，请打开");
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());
    }


    private void startUpload()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                L.w("TargetService", "startUpload");
                if (!isUpload)
                {
                    return;
                }
                stateNum++;
                stateNum = stateNum%12;
                if (stateNum == 0){
                    postState();
                }
                if (SharedPreUtils.getBoolean(Constants.IS_LOGIN, false, ctx))
                {
                    getLoginToken();
                }
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }



    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        getContentResolver().unregisterContentObserver(mGpsMonitor);
        isUpload = false;
        Intent i = new Intent(Constants.TARGETSERVICE_DESTORY);
        sendBroadcast(i);
        vibrator.cancel();
        unbindService(conn);
        powerUtil.unregister();
        super.onDestroy();
    }

    private void postState(){
        String USER_ID = SharedPreUtils.getString(Constants.USER_ID, ctx);
        String gpsState = "";
        if (hasGPS()){
            gpsState = "1";
        }else{
            gpsState = "0";
        }
        OkHttpUtils.post()//
                .url(Constants.USER_STATE)//
                .addParams("USER_ID", USER_ID)//
                .addParams("GPS_STATUS", gpsState)//
                .addParams("CHARGE_REST", powerDate)//
                .build()//
                .execute(new NotObjCommonCallback()
                {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error)
                    {
                        L.w("TargetService", "onNetworkError = " + error);

                    }

                    @Override
                    public void onResponse(Boolean isSuccess)
                    {
                        L.w("TargetService", "onResponse = ");
                    }
                });
    }

    private void getLoginToken()
    {
        L.w("TargetService", "getLoginToken");
        String loginTokens = SharedPreUtils.getString(Constants.LOGIN_TOKENS, ctx);
        String USER_ID = SharedPreUtils.getString(Constants.USER_ID, ctx);

        OkHttpUtils.post()//
                .url(Constants.USER_IS_KICK)//
                .addParams("USER_ID", USER_ID)//
                .addParams("LOGIN_TOKENS", loginTokens)//
                .build()//
                .execute(new NotObjCommonCallback()
                {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error)
                    {
                        L.w("TargetService", "onNetworkError = " + error);
                        if (isNetwork)
                        {

                        }
                        else
                        {
                            if ("您被踢下线！".equals(error))
                            {
                                L.w("TargetService", "您被踢下线了！");
                                SharedPreUtils.putBoolean(Constants.IS_LOGIN, false, ctx);
                                sendBroadcast(new Intent(Constants.LOGINTOKEN_ERROR_SERVICE));
                            }
                        }
                    }

                    @Override
                    public void onResponse(Boolean isSuccess)
                    {
                        L.w("TargetService", "onResponse = ");
                    }
                });
    }
}

