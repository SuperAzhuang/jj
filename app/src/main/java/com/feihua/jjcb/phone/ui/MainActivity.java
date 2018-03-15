package com.feihua.jjcb.phone.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.feihua.jjcb.phone.Adapter.YHCXViewPagerAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.application.MyApplication;
import com.feihua.jjcb.phone.bean.FeeDetails;
import com.feihua.jjcb.phone.bean.QFDetailsBean;
import com.feihua.jjcb.phone.bean.UpdataBean;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.callback.NotObjCommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.fragment.BZCXfragment;
import com.feihua.jjcb.phone.fragment.CBGLfragment;
import com.feihua.jjcb.phone.fragment.QFCXfragment;
import com.feihua.jjcb.phone.fragment.UserFragment;
import com.feihua.jjcb.phone.fragment.YHCXfragment;
import com.feihua.jjcb.phone.service.TargetService;
import com.feihua.jjcb.phone.utils.AppUtils;
import com.feihua.jjcb.phone.utils.DownLoadApk;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.feihua.jjcb.phone.view.NoScrollViewPager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener
{

    private RadioGroup rgroup_main;
    private RadioButton main_cbgl;
    private RadioButton main_yhcx;
    private RadioButton main_qfcx;
    private RadioButton main_bzcx;
    private RadioButton main_user;
    private Fragment mCurrentFragment;
    private static final int REQUECT_CODE_LOCATION = 2;
    private static final int REQUECT_CODE_STORAGE = 3;
    private static final int REQUECT_CODE_PHONE = 4;
    private static final int REQUECT_CODE_CAMERA = 5;

    private long lastTime;
    BroadcastReceiver receiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ToastUtil.showShortToast(R.string.toast_overdue_logged_in);
            //            mUserInfo.clear();
            //            // 做成当被踢掉后，如果没跳转到登陆页面，还是能收到推送的消息。如果改到
            //            // sendBroadcast时候调用，则立即停止接收推送来的消息。
            //            if (!JPushInterface.isPushStopped(MainActivity.this))
            //            {
            //                JPushInterface.stopPush(MainActivity.this);
            //            }
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            MyApplication.getInstance().exit();
        }
    };
    private LocationManager locationManager;
    private NoScrollViewPager mViewPager;
    private ArrayList<Fragment> fragmentDatas;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ProgressDialog mDownloadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOGINTOKEN_ERROR_SERVICE);
        registerReceiver(receiver, filter);

        mDownloadDialog = new ProgressDialog(this);
        mDownloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadDialog.setMessage("正在下载更新");

        initPermissions();

        locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        L.w("MainActivity", "hasGPS() = " + hasGPS());
        if (!hasGPS())
        {
            sendNotification();
        }

        startService();

        initFragment();

        initView();

        checkVersionCode();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //检测版本
    private void checkVersionCode()
    {
        int versionCode = AppUtils.getVersionCode(this);
        L.w("MainActivity", "versionCode=" + versionCode);
        OkHttpUtils.post()//
                .url(Constants.CHECK_VERSING_CODE)//
                .addParams("V_NO", String.valueOf(versionCode))//
                .build()//
                .execute(new CommonCallback()
                {
                    @Override
                    public void onResponse(String datas)
                    {
                        if (!TextUtils.isEmpty(datas))
                        {
                            UpdataBean bean = new Gson().fromJson(datas, UpdataBean.class);
                            UpdataBean.UpdataInfo info =  bean.BB;
                            String remark = info.remark;
                            String url = info.url;
                            String isUpdate = info.isUpdate;
                            if (isUpdate.equals("1"))
                            {
                                showDialog(remark, url);
                            }
                        }
                    }

                    @Override
                    public void onNetworkError(boolean isNetwork, String error)
                    {
                        if (isNetwork)
                        {
                            ToastUtil.showShortToast(R.string.toast_network_anomalies);
                        }
                        else
                        {
                            ToastUtil.showShortToast(error);
                        }
                    }
                });


    }

    //获取定位人员信息
    private void getLocationInfo()
    {
        String userId = SharedPreUtils.getString(Constants.USER_ID,this);
        L.w("MainActivity", "userId=" + userId);
        OkHttpUtils.post()//
                .url(Constants.GET_LOCATION)//
                .addParams("USER_ID", userId)//
                .build()//
                .execute(new CommonCallback()
                {
                    @Override
                    public void onResponse(String datas)
                    {
                        if (!TextUtils.isEmpty(datas))
                        {
                            SharedPreUtils.putString(Constants.LOCATION_INFO,datas,MainActivity.this);
                        }
                    }

                    @Override
                    public void onNetworkError(boolean isNetwork, String error)
                    {
                    }
                });


    }

    private void showDialog(String mgs, final String apkUrl)
    {
        Dialog create = new AlertDialog.Builder(MainActivity.this).setTitle("发现新版本！").setMessage(mgs).setNegativeButton("确定", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                downloadAPK(apkUrl);
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        }).create();
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    private void downloadAPK(String url)
    {
        DownLoadApk downLoadApk = new DownLoadApk(MainActivity.this, url, mHandler);
        new Thread(downLoadApk).start();
        mDownloadDialog.show();
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DownLoadApk.Download_File_Failed:
                    mDownloadDialog.dismiss();
                    ToastUtil.showShortToast(R.string.toast_download_fail);
                    break;
                case DownLoadApk.Download_File_Success:
                    mDownloadDialog.dismiss();
                    ToastUtil.showShortToast(R.string.toast_download_success);
                    break;
                case DownLoadApk.Download_Process:
                    mDownloadDialog.setProgress(msg.getData().getInt("process"));
                    break;
                case DownLoadApk.Download_File_Size:
                    mDownloadDialog.setMax(msg.getData().getInt("max"));
                    break;
                default:
                    break;
            }

        }
    };

    private void initFragment()
    {
        fragmentDatas = new ArrayList<>();
        fragmentDatas.add(new CBGLfragment());
        fragmentDatas.add(new YHCXfragment());
        fragmentDatas.add(new QFCXfragment());
        fragmentDatas.add(new BZCXfragment());
        fragmentDatas.add(new UserFragment());
    }

    //6.0权限确认
    private void initPermissions()
    {
        if (!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION, REQUECT_CODE_LOCATION))
        {
            MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUECT_CODE_STORAGE))
        {
            MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE, REQUECT_CODE_PHONE))
        {
            MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_PHONE, Manifest.permission.READ_PHONE_STATE);
        }
        if (!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA, REQUECT_CODE_CAMERA))
        {
            MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_CAMERA, Manifest.permission.CAMERA);
        }
    }

    @PermissionGrant(REQUECT_CODE_STORAGE)
    public void requestSdcardSuccess()
    {
        //        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_STORAGE)
    public void requestSdcardFailed()
    {
        //        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionGrant(REQUECT_CODE_LOCATION)
    public void requestLocationSuccess()
    {
        //        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_LOCATION)
    public void requestLocationFailed()
    {
        //        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionGrant(REQUECT_CODE_CAMERA)
    public void requestCameraSuccess()
    {
        //        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_CAMERA)
    public void requestCameraFailed()
    {
        //        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionGrant(REQUECT_CODE_PHONE)
    public void requestPhoneSuccess()
    {
        //        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_PHONE)
    public void requestPhoneFailed()
    {
        //        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
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
        ToastUtil.showLongToast("请打开手机GPS定位开关");
    }

    private void startService()
    {
        Intent i = new Intent();
        i.setClass(this, TargetService.class);
        startService(i);
    }

    private void initView()
    {
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        mViewPager.setNoScroll(true);
        mViewPager.setOffscreenPageLimit(5);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new YHCXViewPagerAdapter(fm, fragmentDatas));
        main_cbgl = (RadioButton) findViewById(R.id.main_cbgl);
        main_yhcx = (RadioButton) findViewById(R.id.main_yhcx);
        main_qfcx = (RadioButton) findViewById(R.id.main_qfcx);
        main_bzcx = (RadioButton) findViewById(R.id.main_bzcx);
        main_user = (RadioButton) findViewById(R.id.main_user);
        rgroup_main = (RadioGroup) findViewById(R.id.indicator_group);
        rgroup_main.setOnCheckedChangeListener(this);
        rgroup_main.check(main_cbgl.getId());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.main_user:
                mViewPager.setCurrentItem(4, false);
                break;
            case R.id.main_bzcx:
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.main_qfcx:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.main_yhcx:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.main_cbgl:
                mViewPager.setCurrentItem(0, false);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //连按两次返回键退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastTime) <= 2000)
            {
                finish();
            }
            else
            {
                ToastUtil.showShortToast(R.string.toast_back);
            }
            lastTime = curTime;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        getLocationInfo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //        client.connect();
        //        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
        //                "Main Page", // TODO: Define a title for the content shown.
        //                // TODO: If you have web page content that matches this app activity's content,
        //                // make sure this auto-generated web page URL is correct.
        //                // Otherwise, set the URL to null.
        //                Uri.parse("http://host/path"),
        //                // TODO: Make sure this auto-generated app URL is correct.
        //                Uri.parse("android-app://com.feihua.jjcb.phone.ui/http/host/path"));
        //        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
        //                "Main Page", // TODO: Define a title for the content shown.
        //                // TODO: If you have web page content that matches this app activity's content,
        //                // make sure this auto-generated web page URL is correct.
        //                // Otherwise, set the URL to null.
        //                Uri.parse("http://host/path"),
        //                // TODO: Make sure this auto-generated app URL is correct.
        //                Uri.parse("android-app://com.feihua.jjcb.phone.ui/http/host/path"));
        //        AppIndex.AppIndexApi.end(client, viewAction);
        //        client.disconnect();
    }
}
