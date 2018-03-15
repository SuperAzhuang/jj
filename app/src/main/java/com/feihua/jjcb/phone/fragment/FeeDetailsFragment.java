package com.feihua.jjcb.phone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feihua.jjcb.phone.Adapter.FeeDetailsListViewAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.FeeDetails;
import com.feihua.jjcb.phone.bean.FeeDetailsBean;
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
public class FeeDetailsFragment extends Fragment
{
    private View layout;
    private ArrayList<FeeDetails> mDatas;
    private FeeDetailsListViewAdapter adapter;
    private String userbKh;
    private String year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (layout == null)
        {
            layout = inflater.inflate(R.layout.fragment_fee_details, null);

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
        ListView mListView = (ListView) layout.findViewById(R.id.lvFee);
        adapter = new FeeDetailsListViewAdapter(getActivity(), mDatas, R.layout.cb_details_listitem);
        mListView.setAdapter(adapter);
    }

    public void onRefresh(String userbKh, String year, boolean isRefresh)
    {
        this.userbKh = userbKh;
        this.year = year;

        if (isRefresh)
        {
            postHttp();
        }
    }

    private void postHttp()
    {
        OkHttpUtils.post()//
                .url(Constants.GET_FEELIST)//
                .addParams("USERB_KH", userbKh)//
                .addParams("DEBTL_YEAR", year)//
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
                            FeeDetailsBean bean = new Gson().fromJson(datas, FeeDetailsBean.class);
                            List<FeeDetails> userbase = bean.LSSF;
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
