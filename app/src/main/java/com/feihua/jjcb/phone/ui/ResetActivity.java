package com.feihua.jjcb.phone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.application.MyApplication;
import com.feihua.jjcb.phone.callback.NotObjCommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;


/**
 * A login screen that offers login via email/password.
 */
public class ResetActivity extends BaseActivity implements OnClickListener
{

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etVerifyPassword;
    private View resetLayout;
    private String oldPassword;
    private String newPassword;
    private View resultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        initViews();
    }

    private void initViews()
    {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btnCommit).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etVerifyPassword = (EditText) findViewById(R.id.etVerifyPassword);
        resetLayout = findViewById(R.id.resetLayout);
        resultLayout = findViewById(R.id.resultLayout);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnBack:
            case R.id.btn_left:
                finish();
                break;
            case R.id.btnCommit:
                doCommit();
                break;
            case R.id.btnLogin:
                doLogin();
                break;
            default:
                break;
        }
    }

    private void doLogin()
    {
        startActivity(new Intent(this, LoginActivity.class));
        MyApplication.getInstance().exit();
    }

    private void doCommit()
    {
        if (verifyDatas())
        {
            commit();
        }
    }

    private void commit()
    {
        mProgressBar.setProgressMsg(getResources().getString(R.string.progress_upload_all));
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        String userId = SharedPreUtils.getString(Constants.USER_ID, this);
        String username = SharedPreUtils.getString(Constants.USERNAME, this);
        OkHttpUtils.post()//
                .url(Constants.RESET_PASSWORD)//
                .addParams("J_PASSWORD", oldPassword)//
                .addParams("PASSWORD", newPassword)//
                .addParams("USER_ID", userId)//
                .addParams("USERNAME", username)//
                .build()//
                .execute(new NotObjCommonCallback()
                {
                    @Override
                    public void onResponse(Boolean response)
                    {
                        if (response)
                        {
                            SharedPreUtils.putString("PASSWORD","",ResetActivity.this);
                            resultLayout.setVisibility(View.VISIBLE);
                            resetLayout.setVisibility(View.GONE);
                            mProgressBar.dismiss();
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
                        mProgressBar.dismiss();
                    }
                });
    }

    private boolean verifyDatas()
    {
        oldPassword = etOldPassword.getText().toString();
        newPassword = etNewPassword.getText().toString();
        String verifyPassword = etVerifyPassword.getText().toString();
        if (TextUtils.isEmpty(oldPassword))
        {
            ToastUtil.showShortToast(R.string.reset_old_password);
            return false;
        }
        if (TextUtils.isEmpty(newPassword))
        {
            ToastUtil.showShortToast(R.string.reset_new_password);
            return false;
        }
        if (TextUtils.isEmpty(verifyPassword))
        {
            ToastUtil.showShortToast(R.string.reset_verify_password);
            return false;
        }
        if (!newPassword.equals(verifyPassword))
        {
            ToastUtil.showShortToast("新密码与确认密码不一致,请重新输入");
            return false;
        }
        return true;
    }
}

