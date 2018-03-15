package com.feihua.jjcb.phone.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.TJCXListViewAdapter;
import com.feihua.jjcb.phone.Adapter.YHQUeryListAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.Org;
import com.feihua.jjcb.phone.bean.PianQuBean;
import com.feihua.jjcb.phone.bean.TJCXListBean;
import com.feihua.jjcb.phone.bean.TJCXTypeBean;
import com.feihua.jjcb.phone.bean.YHQuery;
import com.feihua.jjcb.phone.bean.YHQueryBean;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.ui.QueryDetailsActivity;
import com.feihua.jjcb.phone.ui.WaterFeeActivity;
import com.feihua.jjcb.phone.utils.AreaUtil;
import com.feihua.jjcb.phone.utils.DatePickDialogUtil;
import com.feihua.jjcb.phone.utils.DateUtil;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wcj on 2016-04-19.
 */
public class YHQueryFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener
{

    private ListView mListView;
    private ArrayList<ChaoBiaoTables> datasList;
    private TJCXListViewAdapter adapter;
    private EditText etKH;
    private EditText etHM;
    private EditText etAddr;
    private EditText etMT;

    @Override
    public int getLayoutId()
    {
        return R.layout.layout_yh_query;
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
        layout.findViewById(R.id.btnQuery).setOnClickListener(this);

        etKH = (EditText) layout.findViewById(R.id.etKH);
        etHM = (EditText) layout.findViewById(R.id.etHM);
        etAddr = (EditText) layout.findViewById(R.id.etAddr);
        etMT = (EditText) layout.findViewById(R.id.etMT);

        mListView = (ListView) layout.findViewById(R.id.lvYHQuery);
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
        String mt = etMT.getText().toString();
        String kh = etKH.getText().toString();
        String hm = etHM.getText().toString();
        if (TextUtils.isEmpty(addr)&&TextUtils.isEmpty(mt)&&TextUtils.isEmpty(hm)&&TextUtils.isEmpty(kh)){
            ToastUtil.showShortToast("请输入需要查询的内容");
            return;
        }
        mProgressBar.show();
        OkHttpUtils.post()//
                .url(Constants.GET_TJCX_LIST)//
                .addParams("USERB_HM", hm)//
                .addParams("USERB_MT", mt)//
                .addParams("USERB_ADDR", addr)//
                .addParams("USERB_KH", kh)//
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
        startQuery(datasList.get(position).getUSERB_KH(),datasList.get(position).getUSERB_HM());
    }

    private void startQuery(String USERB_KH,String USERB_HM)
    {
        if (TextUtils.isEmpty(USERB_KH))
        {
            ToastUtil.showShortToast(R.string.toast_tjcx_kh_abnormal);
        }
        Intent intent = new Intent(getActivity(), WaterFeeActivity.class);
        intent.putExtra("USERB_KH", USERB_KH);
        intent.putExtra("USERB_HM", USERB_HM);
        startActivity(intent);
    }
}
