package com.feihua.jjcb.phone.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.feihua.jjcb.phone.Adapter.TJCXListViewAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.Datadic;
import com.feihua.jjcb.phone.bean.Org;
import com.feihua.jjcb.phone.bean.PianQuBean;
import com.feihua.jjcb.phone.bean.TJCXListBean;
import com.feihua.jjcb.phone.bean.TJCXTypeBean;
import com.feihua.jjcb.phone.bean.Watert;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.ui.QueryDetailsActivity;
import com.feihua.jjcb.phone.utils.AreaUtil;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-04-15.
 */
public class TJCXFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener
{

    private ListView mListView;
    private List<ChaoBiaoTables> datasList;
    private TJCXListViewAdapter adapter;
    private EditText etKH;
    private EditText etSJ;
    private EditText etAddr;

    @Override
    public int getLayoutId()
    {
        return R.layout.layout_tjcx;
    }

    @Override
    public void initList()
    {
        datasList = new ArrayList<ChaoBiaoTables>();
    }



    @Override
    public void initViews(View layout)
    {
        mProgressBar.setProgressMsg(getResources().getString(R.string.progress_loading));
        etKH = (EditText) layout.findViewById(R.id.etKH);
        etSJ = (EditText) layout.findViewById(R.id.etSJ);
        etAddr = (EditText) layout.findViewById(R.id.etAddr);
        layout.findViewById(R.id.btnQuery).setOnClickListener(this);
        mListView = (ListView) layout.findViewById(R.id.lvTjcx);
        adapter = new TJCXListViewAdapter(ctx, datasList, R.layout.listitem_tjcx);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnQuery:
                doQuery();
                break;
            default:
                break;
        }
    }

    //根据条件查询数据
    private void doQuery()
    {
        String addr = etAddr.getText().toString();
        String mt = etSJ.getText().toString();
        String hm = etKH.getText().toString();
        if (TextUtils.isEmpty(addr)&&TextUtils.isEmpty(mt)&&TextUtils.isEmpty(hm)){
            ToastUtil.showShortToast("请输入需要查询的内容");
            return;
        }
        mProgressBar.show();
        OkHttpUtils.post()//
                .url(Constants.GET_TJCX_LIST)//
                .addParams("USERB_HM", hm)//
                .addParams("USERB_MT", mt)//
                .addParams("USERB_ADDR", addr)//
                .build()//
                .execute(new CommonCallback()
                {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error)
                    {
                        mProgressBar.dismiss();
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
                        mProgressBar.dismiss();
                        if (!TextUtils.isEmpty(datas))
                        {
                            TJCXListBean bean = new Gson().fromJson(datas, TJCXListBean.class);
                            List<ChaoBiaoTables> userbase = bean.userbase;
                            datasList.clear();
                            if (userbase != null && userbase.size() != 0)
                            {
                                datasList.addAll(userbase);
                            }
                            else
                            {
                                ToastUtil.showShortToast(R.string.toast_tjcx_list_not);
                            }
                            adapter.onDatasChanged(datasList);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        startQuery(datasList.get(position).getUSERB_KH());
    }

    private void startQuery(String USERB_KH)
    {
        if (TextUtils.isEmpty(USERB_KH))
        {
            ToastUtil.showShortToast(R.string.toast_tjcx_kh_abnormal);
        }
        Intent intent = new Intent(getActivity(), QueryDetailsActivity.class);
        intent.putExtra("USERB_KH", USERB_KH);
        startActivity(intent);
    }
}
