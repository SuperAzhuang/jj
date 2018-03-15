package com.feihua.jjcb.phone.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.feihua.jjcb.phone.constants.Constants;

/**
 * 下载apk并安装
 * 
 * @author Administrator
 * 
 */
public class DownLoadApk implements Runnable
{
    public static final int Download_File_Success = 98;
    public static final int Download_File_Failed = 97;
    public static final int Download_File_Size = 96;
    public static final int Download_Process = 95;
    private static final String DownLoad_Apk_Name = Constants.APK_NAME;
    private String url;
    private Context context;
    private Handler handler;

    public DownLoadApk(Context context, String url, Handler handler)
    {
        this.context = context;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        try
        {
            File file = getFileFromServer(url, handler);
            if (file != null)
            {
                handler.sendEmptyMessage(Download_File_Success);
                installApk(context, file);
            }
            else
                handler.sendEmptyMessage(Download_File_Failed);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("tag", e.toString());
        }
    }

    /**
     * 下载更新apk
     * 
     * @param path
     *            下载链接
     * @param handler
     * @return 下载文件
     * @throws Exception
     */
    public static File getFileFromServer(String path, Handler handler)
            throws Exception
    {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            Log.d("tag", "update url " + path);
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            Message msg1 = new Message();
            msg1.what = Download_File_Size;
            Bundle data = new Bundle();
            data.putInt("max", conn.getContentLength() / 1024);
            msg1.setData(data);
            handler.sendMessage(msg1);
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),
                    DownLoad_Apk_Name);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            int temp = conn.getContentLength() / 30;
            int tempstep = 0;
            while ((len = bis.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                tempstep += len;
                if (tempstep >= temp)
                {
                    tempstep = 0;
                    Message msg = new Message();
                    msg.what = Download_Process;
                    Bundle data2 = new Bundle();
                    data2.putInt("process", total / 1024);
                    msg.setData(data2);
                    handler.sendMessage(msg);
                }
            }

            if (null != is)
            {
                is.close();
            }
            if (null != fos)
            {
                fos.close();
            }
            if (null != bis)
            {
                bis.close();
            }
            return file;
        }
        else
        {
            return null;
        }
    }

    /**
     * 安装apk
     * 
     * @param file
     *            apk文件
     * @param context
     */
    private void installApk(Context context, File file)
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        context.startActivity(intent);
    }
}
