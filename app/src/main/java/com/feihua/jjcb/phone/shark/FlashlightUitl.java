package com.feihua.jjcb.phone.shark;

import android.hardware.Camera;

import com.feihua.jjcb.phone.utils.ToastUtil;

/**
 * Created by wcj on 2016-09-02.
 */
public class FlashlightUitl
{

    private Camera mCameraDevices;
    private Camera.Parameters mParameters;

    public FlashlightUitl(){
        mCameraDevices = Camera.open();
    }

    /**
     * 设置手电筒的开关状态
     * <uses-permission android:name="android.permission.CAMERA" />
     * @param on ： true则打开，false则关闭
     */
    public void setFlashlightSwitch(boolean on){
        try
        {
            if(mCameraDevices == null){
                mCameraDevices = Camera.open();
            }
            if(mParameters == null){
                mParameters = mCameraDevices.getParameters();
            }
            if(on){
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }else{
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }

            mCameraDevices.setParameters(mParameters);
        }catch (Exception e){
            ToastUtil.showShortToast("闪光灯设备异常");
        }

    }

    public void onFinish(){
        if (mCameraDevices != null){
            mCameraDevices.release();
        }
    }
}
