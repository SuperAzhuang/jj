package com.feihua.jjcb.phone.shark;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * Created by wcj on 2016-09-02.
 */
public class SharkUtil
{
    private Vibrator vibrator;
    private SensorManager sensorManager;
    private long lastTime;
    private int sharkNum;
    private OnSharkListener listener;

    //<uses-permission android:name="android.permission.VIBRATE"/>
    public SharkUtil(Context ctx)
    {
        sensorManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
        vibrator = (Vibrator) ctx.getSystemService(ctx.VIBRATOR_SERVICE);
    }

    //注册
    public void register()
    {
        if (sensorManager != null)
        {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    //注销
    public void unregister()
    {
        if (sensorManager != null)
        {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    public void setOnSharkListener(OnSharkListener listener)
    {
        this.listener = listener;
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener()
    {

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue)
            {
                vibrator.vibrate(200);
                long time = System.currentTimeMillis();
                if ((time - lastTime) < 500)
                {
                    sharkNum++;
                    if (sharkNum == 2)
                    {
                        listener.onShark();
                        sharkNum = 0;
                    }
                }
                else
                {
                    sharkNum = 0;
                }
                lastTime = time;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }
    };

}
