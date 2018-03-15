package com.feihua.jjcb.phone.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by wcj on 2016-12-08.
 * 电量
 */

public class PowerUtil
{
    private Context ctx;
    private IntentFilter batteryLevelFilter;
    private PowerListener listerer;
    private BroadcastReceiver batteryLevelRcvr = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            int rawlevel = intent.getIntExtra("level", -1);
            int scale = intent.getIntExtra("scale", -1);
            int status = intent.getIntExtra("status", -1);
            int health = intent.getIntExtra("health", -1);
            int level = -1; // percentage, or -1 for unknown
            if (rawlevel >= 0 && scale > 0)
            {
                level = (rawlevel * 100) / scale;
            }

            if (listerer != null){
                listerer.onPowerDate(String.valueOf(level));
            }

        }
    };

    public PowerUtil(Context ctx)
    {
        this.ctx = ctx;
        monitorBatteryState();
    }

    private void monitorBatteryState()
    {
        batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        ctx.registerReceiver(batteryLevelRcvr, batteryLevelFilter);
    }

    public void setOnPowerListener(PowerListener listener){
        this.listerer = listener;
    }

    public void unregister()
    {
        ctx.unregisterReceiver(batteryLevelRcvr);
    }
}
