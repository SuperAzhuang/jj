package com.feihua.jjcb.phone.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.BiaoCeAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.asynctask.DownBiaoKuangAsyncTask;
import com.feihua.jjcb.phone.asynctask.DownTablesAsyncTask;
import com.feihua.jjcb.phone.callback.BiaoCeTables;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.callback.DownTables;
import com.feihua.jjcb.phone.callback.DownTablesCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.db.BiaoCeDatabase;
import com.feihua.jjcb.phone.db.BiaoKuangDatabase;
import com.feihua.jjcb.phone.db.ChaoBiaoDatabase;
import com.feihua.jjcb.phone.db.DatabaseHelper;
import com.feihua.jjcb.phone.trace.TraceService;
import com.feihua.jjcb.phone.ui.CBGLListActivity;
import com.feihua.jjcb.phone.utils.DateUtil;
import com.feihua.jjcb.phone.utils.DensityUtil;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.achartengine.GraphicalView;

import java.util.ArrayList;

import static com.feihua.jjcb.phone.application.MyApplication.app;

/**
 * 抄表管理
 * Created by wcj on 2016-03-02.
 */
public class CBGLfragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private GraphicalView graphicalView;
    private LinearLayout llll;
    private ListView mLv_cbgl;
    private TextView tvHeadTitle;
    private BiaoCeDatabase biaoCeDatabase;
    private ChaoBiaoDatabase chaoBiaoTables;
    private ArrayList<BiaoCeTables> datas;
    private BiaoCeAdapter adapter;
    private static final int TOAST_SUCCESS = 1;
    private static final int TOAST_VOLUME_NO = 2;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int changeData = intent.getIntExtra("change_data", 0);
            if (changeData == Constants.CBGL_BIAO_CE_LIST_RECEIVER) {
                initListDatas();
                adapter.onDatasChanged(datas);
            }
        }
    };
    private boolean isDown;
    private Button btnTrace;
    private TraceService.TraceBinder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (TraceService.TraceBinder) service;
            initButtonTrace(binder.getTraceState());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private StringBuilder append;

    @Override
    public int getLayoutId() {
        return R.layout.layout_cbgl;
    }

    @Override
    public void initList() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CBGL_BIAO_CE_ACTION);
        getActivity().registerReceiver(receiver, filter);

        biaoCeDatabase = new BiaoCeDatabase(ctx);
        chaoBiaoTables = new ChaoBiaoDatabase(ctx);
        BiaoKuangDatabase biaoKDatabase = new BiaoKuangDatabase(ctx);
        int count = biaoKDatabase.queryCount(DatabaseHelper.BIAOK_TABLE_NAME);
        if (count == 0) {
            doCloud();
        }

        initListDatas();

        initBaiduTrace();
    }

    private void initBaiduTrace() {
        Intent service = new Intent(getActivity(), TraceService.class);
        getActivity().startService(service);
        getActivity().bindService(service, conn, 0);
    }

    private void initListDatas() {
        String userId = SharedPreUtils.getString(Constants.USER_ID, ctx);
        datas = biaoCeDatabase.DeptArray(userId);
    }

    private void delTable() {
        L.w("CBGLfragment", "delTable");
        String dateTime = SharedPreUtils.getString("dateTime", ctx);
        if (TextUtils.isEmpty(dateTime)) {
            SharedPreUtils.putString("dateTime", DateUtil.getCurrentDate4(), ctx);
        } else if (!dateTime.equals(DateUtil.getCurrentDate4())) {
            SharedPreUtils.putString("dateTime", DateUtil.getCurrentDate4(), ctx);
            biaoCeDatabase.delete(DatabaseHelper.BIAOCE_TABLE_NAME);
            chaoBiaoTables.delete(DatabaseHelper.CHAOBIAO_TABLE_NAME);
            initListDatas();
            adapter.onDatasChanged(datas);
            ToastUtil.showShortToast(R.string.toast_del_tables_success);
        }
    }

    @Override
    public void initViews(View layout) {
        adapter = new BiaoCeAdapter(ctx, datas, R.layout.listitem_cbgl);
        tvHeadTitle = (TextView) layout.findViewById(R.id.head_title);
        mLv_cbgl = (ListView) layout.findViewById(R.id.lv_cbgl);
        mLv_cbgl.setAdapter(adapter);
        layout.findViewById(R.id.btn_right).setOnClickListener(this);
        layout.findViewById(R.id.btnCloud).setOnClickListener(this);
        btnTrace = (Button) layout.findViewById(R.id.btnTrace);
        btnTrace.setOnClickListener(this);
        mLv_cbgl.setOnItemClickListener(this);
        tvHeadTitle.setText(DateUtil.getCurrentDate4());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                onclickDownload();
                break;
            case R.id.btnCloud:
                doCloud();
                break;
            case R.id.btnTrace:
                doTrace();
                break;
            default:
                break;
        }
    }

    private void doTrace() {
        if (binder == null) {
            ToastUtil.showShortToast("正在初始化");
            return;
        }
        if (binder.getTraceState()) {
            binder.stopTrace();
        } else {
            binder.startTrace();
        }
        initButtonTrace(binder.getTraceState());
    }

    private void initButtonTrace(boolean traceState) {
        if (traceState) {
            btnTrace.setText("结束");
        } else {
            btnTrace.setText("开始");
        }
    }

    //下载表况类型数据
    private void doCloud() {

        mProgressBar.setProgressMsg(getResources().getString(R.string.progress_down_biaokuang));
        mProgressBar.show();
        showDialog(getResources().getString(R.string.progress_down_biaokuang));
        OkHttpUtils.post()//
                .url(Constants.GET_BK)//
                .build()//
                .execute(new CommonCallback() {
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
                    public void onResponse(final String datas) {
                        L.w("CBGLfragment", "onResponse datas = " + datas);
                        if (datas != null) {
                            mProgressBar.setProgressMsg(getResources().getString(R.string.progress_save_biaokuang));
                            DownBiaoKuangAsyncTask dbka = new DownBiaoKuangAsyncTask(ctx) {
                                @Override
                                protected void onPostExecute(String msg) {
                                    super.onPostExecute(msg);
                                    mProgressBar.dismiss();
                                    if (msg != null) {
                                        ToastUtil.showShortToast(msg);
                                    }
                                }
                            };
                            dbka.execute(datas);
                        } else {
                            mProgressBar.dismiss();
                        }
                    }
                });
    }

    //
    private void onclickDownload() {
        //访问服务端是否有新的下载任务,有就弹出下载框,没有就文字提示
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_download, null);
        int cFullFillWidth = DensityUtil.dip2px(getActivity(), 300);
        TextView tv_dialog_cehao = (TextView) layout.findViewById(R.id.tv_dialog_cehao);
        tv_dialog_cehao.setText(DateUtil.getCurrentDate4());
        layout.findViewById(R.id.btn_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载表册
                downTables();
                dialog.dismiss();
            }
        });
        layout.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.setMinimumWidth(cFullFillWidth);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDialog(String msg) {
        mProgressBar.show();
        mProgressBar.setProgressMsg(msg);
    }

    //请求册表清单数据
    private void downTables() {
        if (isDown) {
            ToastUtil.showShortToast(R.string.toast_downing);
            return;
        }
        isDown = true;
        String userId = SharedPreUtils.getString(Constants.USER_ID, ctx);
        String VOLUME_NO = biaoCeDatabase.queryVolumeNo(userId).toString();
        L.w("CBGLfragment", "VOLUME_NO" + VOLUME_NO + ",url = " + Constants.DOWN_TABLES + ",,userId = " + userId);
        append = new StringBuilder(Constants.DOWN_TABLES).append("?USER_ID=").append(userId).append("&VOL_NO=").append(VOLUME_NO);
        L.w("CBGLfragment", "表册url = " + append.toString());
        showDialog(getResources().getString(R.string.progress_down_biaoce));
        OkHttpUtils.post()//
                .url(Constants.DOWN_TABLES)//
                .addParams("USER_ID", userId)//
                .addParams("VOLUME_NO", VOLUME_NO)//
                .build()//
                .execute(new DownTablesCallback() {
                    @Override
                    public void onNetworkError(boolean isNetwork, String error) {
                        isDown = false;
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
                        isDown = false;
                        L.w("CBGLfragment", "onResponse downTables = " + downTables);
                        if (downTables != null) {
                            mProgressBar.setProgressMsg(getResources().getString(R.string.progress_save_biaoce));
                            DownTablesAsyncTask dtat = new DownTablesAsyncTask(ctx) {
                                @Override
                                protected void onPostExecute(String msg) {
                                    super.onPostExecute(msg);
                                    mProgressBar.dismiss();
                                    initListDatas();
                                    adapter.onDatasChanged(datas);
                                    if (msg != null) {
                                        ToastUtil.showShortToast(msg);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CBGLListActivity.class);
        intent.putExtra(DatabaseHelper.BIAOCE_VOLUME_NO, datas.get(position).VOLUME_NO);
        intent.putExtra(DatabaseHelper.BIAOCE_VOLUME_MCOUNT, datas.get(position).VOLUME_MCOUNT);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onStart() {
        super.onStart();
        delTable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
        getActivity().unbindService(conn);
    }
}
