package com.feihua.jjcb.phone.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.location.LocationService;
import com.feihua.jjcb.phone.utils.AreaUtil;
import com.feihua.jjcb.phone.utils.Global;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wcj on 2016-03-01.
 */
public class MyApplication extends Application
{
    public static MyApplication app;
    private static DisplayImageOptions options;
    private List<Activity> activityList;
    public LocationService locationService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        activityList = new LinkedList<Activity>();
        Global.setApplicationContext(this);

        //碎片化管理
        AutoLayoutConifg.getInstance().useDeviceSize();
        //获取Spinner所需的数据
        AreaUtil.getInstance().getServiceData();

        locationService = new LocationService(getApplicationContext());
        //百度地图
        SDKInitializer.initialize(getApplicationContext());
        //okhttp初始化
        CookieJarImpl cookieJar1 = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)//
                .readTimeout(30000L, TimeUnit.MILLISECONDS)//
//                .writeTimeout(10000L, TimeUnit.MILLISECONDS)//
//                .addInterceptor(new LoggerInterceptor("TAG"))//
                .cookieJar(cookieJar1)//
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(null, null, null))//
                .build();
        OkHttpUtils.initClient(okHttpClient);

        initImageLoader(this);

        initVoice();
    }

    private void initVoice()
    {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        SpeechUtility.createUtility(this, "appid=" + "57c7f08b");

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
         Setting.setShowLog(false);
    }

    public static MyApplication getInstance()
    {
        if (null == app)
        {
            app = new MyApplication();
        }
        return app;
    }

    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }

    public void exit()
    {
        for (Activity activity : activityList)
        {
            activity.finish();
        }
//        System.exit(0);
    }

    public void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void initImageLoader(Context context)
    {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.no_media)
                .showImageForEmptyUri(R.mipmap.no_media)
                .showImageOnFail(R.mipmap.no_media).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.]
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.defaultDisplayImageOptions(options);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

    }

}
