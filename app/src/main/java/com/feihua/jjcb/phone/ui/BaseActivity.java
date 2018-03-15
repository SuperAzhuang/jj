package com.feihua.jjcb.phone.ui;

import android.app.Dialog;
import android.os.Bundle;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.application.MyApplication;
import com.feihua.jjcb.phone.view.CustomProgressDialog;
import com.zhy.autolayout.AutoLayoutActivity;

public class BaseActivity extends AutoLayoutActivity
{
    public CustomProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        MyApplication.getInstance().addActivity(this);
        mProgressBar = new CustomProgressDialog(this);
    }


    public void finish()
    {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy()
    {
        MyApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
