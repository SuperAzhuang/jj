package com.feihua.jjcb.phone.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feihua.jjcb.phone.view.CustomProgressDialog;

/**
 * Created by wcj on 2016-04-19.
 */
public abstract class BaseFragment extends Fragment
{


    private View layout;
    protected Context ctx;
    protected CustomProgressDialog mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        layout = inflater.inflate(getLayoutId(), null);
        ctx = getActivity();
        if (mProgressBar == null){
        mProgressBar = new CustomProgressDialog(ctx);
    }

        initList();

        initViews(layout);

        initDatas();

        return layout;
    }

    public void initList(){

    }

    public void initDatas(){

    }

    public abstract int getLayoutId();

    public abstract void initViews(View layout);

}
