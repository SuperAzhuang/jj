package com.feihua.jjcb.phone.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.QYQueryListAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.Org;
import com.feihua.jjcb.phone.bean.PianQuArrearage;
import com.feihua.jjcb.phone.bean.QYQueryBean;
import com.feihua.jjcb.phone.bean.TJCXTypeBean;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.utils.AreaUtil;
import com.feihua.jjcb.phone.utils.DatePickDialogUtil;
import com.feihua.jjcb.phone.utils.DateUtil;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcj on 2016-04-19.
 */
public class QYQueryFragment extends BaseFragment implements View.OnClickListener
{
    private Context ctx;
    private List<PianQuArrearage> datasList;
    private TextView tvDate;
    private List<Org> orgList;
    private Spinner spinnerQy;
    private String orgNo = "";
    private QYQueryListAdapter listAdapter;

    @Override
    public int getLayoutId()
    {
        ctx = getActivity();
        datasList = new ArrayList<>();
        return R.layout.layout_qy_query;
    }

    @Override
    public void initDatas()
    {

    }

    private void initSpinnerInfo(TJCXTypeBean bean)
    {
        orgList = bean.qy;
        if (orgList != null)
        {
            ArrayList<String> list = new ArrayList<>();
            for (Org org : orgList)
            {
                list.add(org.org_name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.simple_spinner_item, list);
            spinnerQy.setAdapter(adapter);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        }
    }

    @Override
    public void initViews(View layout)
    {
        spinnerQy = (Spinner) layout.findViewById(R.id.spinnerQy);
        tvDate = (TextView) layout.findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);
        layout.findViewById(R.id.btnQuery).setOnClickListener(this);
        ListView mListView = (ListView) layout.findViewById(R.id.lvQYQuery);
        listAdapter = new QYQueryListAdapter(ctx, datasList, R.layout.listitem_qy_query);
        mListView.setAdapter(listAdapter);

        tvDate.setText(DateUtil.getCurrentDate4());

        spinnerQy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                orgNo = orgList.get(position).org_no;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        doSpinnerData();
    }

    private void doSpinnerData()
    {
        AreaUtil.getInstance().getArea(new AreaUtil.OnDataListener()
        {
            @Override
            public void onData(TJCXTypeBean bean)
            {
                initSpinnerInfo(bean);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnQuery:
                doQuery();
                break;
            case R.id.tvDate:
                showDateDialog();
                break;
            default:
                break;
        }
    }

    private void doQuery()
    {
        String time = tvDate.getText().toString();
        OkHttpUtils.post()//
                .url(Constants.GET_QYCX_LIST)//
                .addParams("ORG_NO", orgNo)//
                .addParams("TIME", time)//
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
                            datasList.clear();
                            QYQueryBean bean = new Gson().fromJson(datas, QYQueryBean.class);
                            if (bean != null && bean.owe != null){
                                datasList.addAll(bean.owe);
                            }
                            if (datasList.size() == 0){
                                ToastUtil.showShortToast(R.string.toast_query);
                            }
                            listAdapter.onDatasChanged(datasList);
                        }

                    }
                });
    }

    private void showDateDialog()
    {
        String date = tvDate.getText().toString();
        DatePickDialogUtil dialogUtil = new DatePickDialogUtil()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Log.w("QYQueryFragment", "year = " + year + ",month = " + (monthOfYear + 1));
                StringBuffer sb = new StringBuffer();
                sb.append(year);
                sb.append("-");
                if ((monthOfYear + 1) < 10)
                {
                    sb.append("0");
                }
                sb.append(monthOfYear + 1);
                tvDate.setText(sb.toString());
            }
        };
        dialogUtil.showMonPicker(ctx, date);
    }
}
