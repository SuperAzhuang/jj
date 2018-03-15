package com.feihua.jjcb.phone.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.ui.LoginActivity;
import com.feihua.jjcb.phone.ui.ResetActivity;
import com.feihua.jjcb.phone.utils.AppUtils;
import com.feihua.jjcb.phone.utils.SharedPreUtils;

/**
 * 我的
 * Created by wcj on 2016-03-02.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener
{

    private TextView tvUserName;
    private TextView tvUserPosition;
    private TextView tvUserNumber;
    private TextView tvUserPhone;
    private TextView tvUserArea;
    private TextView tvUserUpdate;

    @Override
    public int getLayoutId()
    {
        return R.layout.layout_user;
    }

    @Override
    public void initDatas()
    {
        tvUserName.setText(SharedPreUtils.getString(Constants.NAME,ctx));
        tvUserPosition.setText(SharedPreUtils.getString(Constants.POSITION,ctx));
        tvUserArea.setText(SharedPreUtils.getString(Constants.DEPT_ID,ctx));
        tvUserPhone.setText(SharedPreUtils.getString(Constants.PHONE,ctx));
        tvUserNumber.setText(SharedPreUtils.getString(Constants.USERNAME,ctx));
        tvUserUpdate.setText("V" + AppUtils.getVersionName(getActivity()));
    }

    @Override
    public void initViews(View layout)
    {
        tvUserName = (TextView) layout.findViewById(R.id.tv_user_name);
        tvUserPosition = (TextView) layout.findViewById(R.id.tv_user_position);
        tvUserNumber = (TextView) layout.findViewById(R.id.tv_user_number);
        tvUserPhone = (TextView) layout.findViewById(R.id.tv_user_phone);
        tvUserArea = (TextView) layout.findViewById(R.id.tv_user_area);
        tvUserUpdate = (TextView) layout.findViewById(R.id.tv_user_update);
        TextView tvResetPassword = (TextView) layout.findViewById(R.id.tvResetPassword);
        tvResetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tvResetPassword.setOnClickListener(this);
        layout.findViewById(R.id.btnUserCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.btnUserCancel:
                doUserCancel();
                break;
            case R.id.tvResetPassword:
                startReset();
                break;
            default:
                break;
        }
    }

    private void startReset()
    {
        Intent intent = new Intent(ctx, ResetActivity.class);
        getActivity().startActivity(intent);
    }

    private void doUserCancel()
    {
        SharedPreUtils.putBoolean(Constants.IS_LOGIN,false,ctx);
        Intent intent = new Intent(ctx, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
