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
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.CBDetailsListBean;
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
public class CBDetailsFragment extends Fragment
{
    private View layout;
    private ArrayList<ChaoBiaoTables> mDatas;
    private CBDetailsListViewAdapter adapter;
    private String userbKh;
    private String year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (layout == null)
        {
            layout = inflater.inflate(R.layout.fragment_cb_details, null);

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
        ListView mListView = (ListView) layout.findViewById(R.id.lvCB);
        adapter = new CBDetailsListViewAdapter(getActivity(), mDatas, R.layout.cb_details_listitem);
        adapter.setUserbKH(userbKh);
        mListView.setAdapter(adapter);
    }

    public void onRefresh(String userbKh, String year, boolean isRefresh)
    {
        this.userbKh = userbKh;
        this.year = year;

        if (isRefresh){
            postHttp();
        }
    }

    private void postHttp(){
        OkHttpUtils.post()//
                .url(Constants.GET_CBLIST)//
                .addParams("USERB_KH", userbKh)//
                .addParams("WATERU_YEAR", year)//
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
                            CBDetailsListBean bean = new Gson().fromJson(datas, CBDetailsListBean.class);
                            List<ChaoBiaoTables> userbase = bean.CBSJ;
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
