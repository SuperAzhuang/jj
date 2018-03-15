package com.feihua.jjcb.phone.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.view.CustomProgressDialog;

/**
 * Created by azhuang on 2018/3/15.
 */
public abstract class MyBaseFragment extends Fragment {

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    private View layout;
    protected Context ctx;
    protected CustomProgressDialog mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(getLayoutId(), null);
        ctx = getActivity();
        if (mProgressBar == null) {
            mProgressBar = new CustomProgressDialog(ctx);
        }
//        initList();
        initViews(layout);
        initDatas();
        return layout;
    }

    public void initList() {

    }

    public void initDatas() {

    }

    public abstract int getLayoutId();

    public abstract void initViews(View layout);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        L.w("CBGLDetailsFragment", "this.fragment = " + this + "--- getUserVisibleHint = " + getUserVisibleHint());
        if (getUserVisibleHint()) {
            this.isVisible = true;
            onVisible();
        } else {
            this.isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        initDatas();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {

    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
//    protected abstract void lazyLoad();
//    protected abstract void lazyLoad();
}