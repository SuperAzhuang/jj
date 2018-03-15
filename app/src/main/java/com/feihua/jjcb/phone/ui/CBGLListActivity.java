package com.feihua.jjcb.phone.ui;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.ChaoBiaoAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.asynctask.DownUserbaseAsyncTask;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.DownTables;
import com.feihua.jjcb.phone.callback.DownTablesCallback;
import com.feihua.jjcb.phone.callback.NotObjCommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.db.BiaoCeDatabase;
import com.feihua.jjcb.phone.db.ChaoBiaoDatabase;
import com.feihua.jjcb.phone.db.DatabaseHelper;
import com.feihua.jjcb.phone.fragment.ScreenFragment;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 抄表清单
 */
public class CBGLListActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnClickListener {

    private ListView mLvCustomerList;
    private Context ctx;
    private ChaoBiaoDatabase chaoBiaoDatabase;
    private ArrayList<ChaoBiaoTables> datas;
    private ChaoBiaoAdapter adapter;
    private TextView tvHeadTitle;
    private String volumeNo;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int changeData = intent.getIntExtra("change_data", 0);
            if (changeData == Constants.CBGL_CHAO_BIAO_LIST_RECEIVER) {
                initDatas();
                adapter.onDatasChanged(datas);
                int index = intent.getIntExtra("index", 0);
                int lvp = mLvCustomerList.getLastVisiblePosition();
                int fvp = mLvCustomerList.getFirstVisiblePosition();
                if (fvp <= index && index <= lvp) {
                    return;
                }
                mLvCustomerList.setSelection(index);
            }
        }
    };
    private BiaoCeDatabase biaoCeDatabase;
    private String volumeMcount;
    private boolean isAdd = false;
    private ScreenFragment fragment;
    private TextView tvScreen;
    private ArrayList<String> khDatas;
    private int page = 1;
    private StringBuilder append;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbgl_list);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CBGL_CHAO_BIAO_ACTION);
        registerReceiver(receiver, filter);
        ctx = this;

        biaoCeDatabase = new BiaoCeDatabase(ctx);
        chaoBiaoDatabase = new ChaoBiaoDatabase(ctx);

        initExtra();

        initDatas();

        initUserbase();

        initView();

        setContent();
        append = new StringBuilder(Constants.DOWN_TABLES_USERBASE).append("?").append("page=" + String.valueOf(page)).
                append("&VOLUME_NO=" + volumeNo + "&BILL_YEAR=" + 2017 + "&BILL_MONTH=" + 12);
        L.d("url = ","append = "+append.toString());
    }

    private void initUserbase() {
        mProgressBar.setProgressMsg("正在下载数据...");
        L.w("CBGLListActivity", "datas.size()=" + datas.size() + ",volumeMcount=" + volumeMcount);
        if (0 < datas.size() && datas.size() < Integer.valueOf(volumeMcount)) {
            mProgressBar.setProgressMsg("数据不全,重新下载数据...");
        } else if (datas.size() >= Integer.valueOf(volumeMcount)) {
            return;
        }
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        getUserbase();
    }

    private void initExtra() {
        Intent intent = getIntent();
        volumeNo = intent.getStringExtra(DatabaseHelper.BIAOCE_VOLUME_NO);
        volumeMcount = intent.getStringExtra(DatabaseHelper.BIAOCE_VOLUME_MCOUNT);
        khDatas = new ArrayList<>();

    }

    private void getUserbase() {
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = new SimpleDateFormat("M").format(new Date());


        append = new StringBuilder(Constants.DOWN_TABLES_USERBASE).append("?").append("page=" + String.valueOf(page)).
                append("&VOLUME_NO=" + volumeNo + "&BILL_YEAR=" + year + "&BILL_MONTH=" + month);
        L.d("url = ","append = "+append.toString());
//        http://218.207.198.197:1439/fh_jjwater_service/androidController.do?method=downLoad",
        OkHttpUtils.post()//
                .url(Constants.DOWN_TABLES_USERBASE)//
                .addParams("page", String.valueOf(page))//
                .addParams("VOLUME_NO", volumeNo)//
                .addParams("BILL_YEAR", year)//
                .addParams("BILL_MONTH", month)//
                .build()//
                .execute(new DownTablesCallback() {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error) {
                        if (isNetwork) {
                            ToastUtil.showShortToast(R.string.toast_network_anomalies);
                        } else {
                            ToastUtil.showShortToast(error);
                        }
                        mProgressBar.dismiss();
                        L.w("CBGLfragment", "error = " + error);
                    }

                    @Override
                    public void onResponse(final DownTables downTables) {
                        L.w("CBGLfragment", "onResponse downTables = " + downTables);
                        if (downTables != null) {
                            DownUserbaseAsyncTask dtat = new DownUserbaseAsyncTask(ctx, volumeNo) {
                                @Override
                                protected void onPostExecute(String msg) {
                                    super.onPostExecute(msg);
                                    if (page == downTables.pages) {
                                        mProgressBar.dismiss();
                                        initDatas();
                                        adapter.onDatasChanged(datas);
                                        Intent intent = new Intent(Constants.CBGL_BIAO_CE_ACTION);
                                        intent.putExtra("change_data", Constants.CBGL_BIAO_CE_LIST_RECEIVER);
                                        sendBroadcast(intent);
                                        if (msg != null) {
                                            ToastUtil.showShortToast(msg);
                                        }
                                    } else {
                                        page++;
                                        getUserbase();
                                    }
                                }
                            };
                            dtat.execute(downTables);
                        } else {
                            mProgressBar.dismiss();
                        }
                    }
                });
    }

    private void setContent() {
        tvHeadTitle.setText(volumeNo);
    }

    private void initDatas() {
        datas = chaoBiaoDatabase.DeptArray(volumeNo);
    }

    private void initView() {
        tvHeadTitle = (TextView) findViewById(R.id.head_title);
        tvScreen = (TextView) findViewById(R.id.tvScreen);
        findViewById(R.id.btnUploadAll).setOnClickListener(this);
        findViewById(R.id.layoutReset).setOnClickListener(this);
        findViewById(R.id.layoutScreen).setOnClickListener(this);
        adapter = new ChaoBiaoAdapter(ctx, datas, R.layout.listitem_cbgl_list);
        mLvCustomerList = (ListView) findViewById(R.id.lv_customer_list);
        mLvCustomerList.setAdapter(adapter);
        mLvCustomerList.setOnItemClickListener(this);
        findViewById(R.id.btn_left).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        khDatas.clear();
        for (int i = 0; i < datas.size(); i++) {
            khDatas.add(datas.get(i).getUSERB_KH());
            L.d("khDatas", "str = " + datas.get(i).getUSERB_KH() + " userid = " + datas.get(i).getVOLUME_NO());
        }

        Intent intent = new Intent(this, CBGLDetailsActivity.class);
        intent.putExtra("index", position);
        intent.putStringArrayListExtra("kh_datas", khDatas);
        intent.putExtra(DatabaseHelper.BIAOCE_VOLUME_NO, volumeNo);
        //        intent.putExtra(DatabaseHelper.BIAOCE_VOLUME_MCOUNT, volumeMcount);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btnUploadAll:
                mProgressBar.setProgressMsg(getResources().getString(R.string.progress_upload_all));
                mProgressBar.show();
                uploadAll();
                break;
            case R.id.layoutReset:
                initDatas();
                adapter.onDatasChanged(datas);
                delScreen();
                break;
            case R.id.layoutScreen:
                switchScreen();
                break;
            default:
                break;
        }
    }

    private void switchScreen() {
        if (isAdd) {
            delScreen();
        } else {
            addScreen();
        }
    }

    private void delScreen() {
        if (fragment != null) {
            isAdd = false;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            fragment = null;
        }
        Drawable drawable = getResources().getDrawable(R.mipmap.img_cbgl_point_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvScreen.setCompoundDrawables(null, null, drawable, null);
    }

    private void addScreen() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new ScreenFragment();
        fragment.setOnDataListener(volumeNo, new ScreenFragment.OnDataListener() {
            @Override
            public void onData(List<ChaoBiaoTables> list) {
                datas.clear();
                if (list != null) {
                    datas.addAll(list);
                    adapter.onDatasChanged(datas);
                    delScreen();
                }
            }
        });
        ft.add(R.id.addLayout, fragment);
        ft.addToBackStack(null);
        ft.commit();
        isAdd = true;
        Drawable drawable = getResources().getDrawable(R.mipmap.img_cbgl_point_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvScreen.setCompoundDrawables(null, null, drawable, null);
    }

    private void uploadAll() {
        final String userId = SharedPreUtils.getString(Constants.USER_ID, ctx);
        final JSONArray jsonArray = chaoBiaoDatabase.DeptArraySaveAll(volumeNo, userId, 10);
        if (jsonArray.length() == 0) {
            ToastUtil.showShortToast(R.string.toast_upload_not_data);
            mProgressBar.dismiss();
            return;
        }
        OkHttpUtils.post()//
                .url(Constants.UPDATA_CBGL_DETAILSE_ALL)//
                .addParams("userbase", jsonArray.toString())//
                .build()//
                .execute(new NotObjCommonCallback() {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error) {
                        if (isNetwork) {
                            ToastUtil.showShortToast(R.string.toast_network_anomalies);
                        } else {
                            ToastUtil.showShortToast(error);
                        }
                        mProgressBar.dismiss();
                    }

                    @Override
                    public void onResponse(Boolean isSuccess) {
                        if (isSuccess) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ContentValues values = new ContentValues();
                                    values.put(DatabaseHelper.CHAOBIAO_IS_UPDATA, "1");
                                    chaoBiaoDatabase.updatatable(values, jsonObject.optString("USERB_KH"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            int updataNum = chaoBiaoDatabase.queryCount(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?" + " and " + DatabaseHelper.CHAOBIAO_IS_UPDATA + "=?", new String[]{volumeNo, "1"});
                            ContentValues baioceValues = new ContentValues();
                            baioceValues.put(DatabaseHelper.BIAOCE_VOLUME_UPDATA, updataNum);
                            biaoCeDatabase.updatatable(baioceValues, volumeNo);

                            initDatas();
                            adapter.onDatasChanged(datas);

                            JSONArray array = chaoBiaoDatabase.DeptArraySaveAll(volumeNo, userId);
                            if (array.length() == 0) {
                                ToastUtil.showShortToast(R.string.toast_updata_success);
                                Intent intent = new Intent(Constants.CBGL_BIAO_CE_ACTION);
                                intent.putExtra("change_data", Constants.CBGL_BIAO_CE_LIST_RECEIVER);
                                sendBroadcast(intent);
                                L.w("CBGLListActivity", "volumeMcount = " + volumeMcount + ",volumeMcount = " + volumeMcount);
                                if (!TextUtils.isEmpty(volumeMcount) && updataNum == (Integer.valueOf(volumeMcount))) {
                                    updataVolscbs();
                                }
                                mProgressBar.dismiss();
                            } else {
                                uploadAll();
                            }
                        } else {
                            mProgressBar.dismiss();
                        }
                    }
                });
    }

    private void updataVolscbs() {
        L.w("CBGLListActivity", "updataVolscbs");
        String year = datas.get(0).getWATERU_YEAR();
        String month = datas.get(0).getWATERU_MONTH();
        OkHttpUtils.post()//
                .url(Constants.UPDATA_CBGL_VOLSCBS)//
                .addParams("VOLS_YEAR", year)//
                .addParams("VOLS_MONTH", month)//
                .addParams("VOLUME_NO", volumeNo)//
                .build()//
                .execute(new NotObjCommonCallback() {
                    @Override
                    public void onResponse(Boolean aBoolean) {

                    }

                    @Override
                    public void onNetworkError(boolean isNetwork, String error) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isAdd) {
            delScreen();
        }
        return super.onKeyDown(keyCode, event);
    }
}
