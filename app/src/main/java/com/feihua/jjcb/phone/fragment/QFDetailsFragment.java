package com.feihua.jjcb.phone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feihua.jjcb.phone.Adapter.CBDetailsListViewAdapter;
import com.feihua.jjcb.phone.Adapter.QFDetailsListViewAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.CBDetailsListBean;
import com.feihua.jjcb.phone.bean.FeeDetails;
import com.feihua.jjcb.phone.bean.QFDetailsBean;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-08-08.
 */
public class QFDetailsFragment extends Fragment
{

    private View layout;
    private ArrayList<FeeDetails> mDatas;
    private QFDetailsListViewAdapter adapter;
    private String userbKh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (layout == null)
        {
            layout = inflater.inflate(R.layout.fragment_qf_details, null);

            initList();

            initView();

            postHttp();
        }
        return layout;
    }

    private void initList()
    {
        mDatas = new ArrayList<>();
    }

    private void initView()
    {
        ListView mListView = (ListView) layout.findViewById(R.id.lvQF);
        adapter = new QFDetailsListViewAdapter(getActivity(), mDatas, R.layout.qf_details_listitem);
        mListView.setAdapter(adapter);
    }

    public void onRefresh(String userbKh)
    {
        this.userbKh = userbKh;
    }

    public void postHttp()
    {
        OkHttpUtils.post()//
                .url(Constants.GET_PAYLIST)//
                .addParams("USERB_KH", userbKh)//
                .build()//
                .execute(new CommonCallback()
                {
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

                    }

                    @Override
                    public void onResponse(String datas)
                    {
                        if (!TextUtils.isEmpty(datas))
                        {
                            QFDetailsBean bean = new Gson().fromJson(datas, QFDetailsBean.class);
                            List<FeeDetails> userbase = bean.QFQK;
                            mDatas.clear();
                            if (userbase != null && userbase.size() != 0)
                            {
                                mDatas.addAll(userbase);
                            }
                            adapter.onDatasChanged(mDatas);
                        }
                    }
                });
    }
}
