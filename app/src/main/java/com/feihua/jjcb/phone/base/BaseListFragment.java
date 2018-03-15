package com.feihua.jjcb.phone.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feihua.jjcb.phone.view.CustomProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseListFragment extends Fragment  {

	/**
	 * 标志位，标志已经初始化完成
	 */
	private boolean isViewCreated;

	/**
	 * 是否已被加载过一次，第二次就不再去请求数据了
	 */
	public boolean isLoadCompleted;
	private View mFragmentView;
    public boolean mIsShowLoading = true;

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
		initViews(layout);
//		initListener();
		isViewCreated = true;
		return mFragmentView;
	}
	public abstract int getLayoutId();

	public abstract void initViews(View layout);
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isViewCreated && !isLoadCompleted) {
//			LogUtils.v("--setUserVisibleHint");
			initDatas();
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getUserVisibleHint()) {
			initDatas();
		}

	}

	/**
	 * 重试
	 */
	public void initDatas() {
	}

}
