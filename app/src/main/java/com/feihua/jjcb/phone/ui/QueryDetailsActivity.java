package com.feihua.jjcb.phone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.feihua.jjcb.phone.Adapter.QueryListAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.DetailsBean;
import com.feihua.jjcb.phone.bean.TJCXDetailsBean;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryDetailsActivity extends BaseActivity implements View.OnClickListener
{

    private Context ctx;
    private List<DetailsBean> datasList;
    private QueryListAdapter adapter;
    private ChaoBiaoTables tables;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_details);

        ctx = this;
        datasList = new ArrayList<>();

        initExtra();

        initDatas();

        initViews();

    }

    private void initExtra()
    {
        Intent intent = getIntent();
        String userbKh = intent.getStringExtra("USERB_KH");
        if (TextUtils.isEmpty(userbKh)){

        }else{
            getTableDetails(userbKh);
        }
    }

    private void getTableDetails(String userbKh)
    {
        mProgressBar.setProgressMsg(getResources().getString(R.string.progress_loading));
        mProgressBar.show();
        OkHttpUtils.post()//
                .url(Constants.GET_TJCX_DETAILS)//
                .addParams("USERB_KH", userbKh)//
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
                            TJCXDetailsBean bean = new Gson().fromJson(datas, TJCXDetailsBean.class);
                            tables = bean.userbaseDetail;
                            initDatas();
                            adapter.onDatasChanged(datasList);
                        }
                    }
                });
    }

    private void initDatas()
    {
        datasList.clear();
        if (tables == null){
            return;
        }
        datasList.add(new DetailsBean(R.mipmap.icon_details_c1, "户号:", tables.getUSERB_KH(), R.mipmap.icon_details_c12, "表号:", tables.getWATERM_NO()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c17, "户名:", tables.getUSERB_HM()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c2, "地址:", tables.getUSERB_ADDR()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c23, "联系人:", tables.getUSERB_LXR(), R.mipmap.icon_details_c24, "户籍人数:", tables.getUSERB_TOTAL()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c4, "手机:", tables.getUSERB_MT(),R.mipmap.icon_details_c5, "口径:", tables.getMETERC_CALIBRE()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c3, "电话:", tables.getUSERB_DH(), R.mipmap.icon_details_c18, "表型:", tables.getWATERM_TYPE()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c7, "用水性质:", tables.getUSERB_YSXZ(), R.mipmap.icon_details_c8, "水价:", tables.getUSERB_CUP()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c9, "安装日期:", tables.getWATERM_AZRQ()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c10, "换表日期:", tables.getWATERM_REPDATE()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c22, "现用水表年限:", tables.getWATERM_NX()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c13, "表位(GPS):", tables.getGPS()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c14, "本期起码:", tables.getUSERB_SQDS(), R.mipmap.icon_details_c15, "本期止码:", tables.getUSERB_BQDS()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c16, "本期水量:", tables.getWATERU_QAN()));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c21, "比同期水量增减幅度:", tables.getTHREEMON_AVG() + "%"));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c20, "比前三期均量增减幅度:", tables.getLASTYEAR_QAN() + "%"));
        datasList.add(new DetailsBean(R.mipmap.icon_details_c19, "比去年同期增减幅度:", tables.getLAST_QAN() + "%"));
    }

    private void initViews()
    {
        findViewById(R.id.btn_left).setOnClickListener(this);
        ListView mListView = (ListView) findViewById(R.id.lvQueryDetails);
        adapter = new QueryListAdapter(ctx, datasList, R.layout.listitem_query_details);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }
}
