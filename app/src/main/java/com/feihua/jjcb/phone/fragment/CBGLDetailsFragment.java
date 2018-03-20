package com.feihua.jjcb.phone.fragment;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.feihua.jjcb.phone.Adapter.CBGLDetailsListAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.application.MyApplication;
import com.feihua.jjcb.phone.base.MyBaseFragment;
import com.feihua.jjcb.phone.bean.Datadic;
import com.feihua.jjcb.phone.bean.DetailsBean;
import com.feihua.jjcb.phone.bean.Fee;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.callback.NotObjCommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.db.BiaoCeDatabase;
import com.feihua.jjcb.phone.db.BiaoKuangDatabase;
import com.feihua.jjcb.phone.db.ChaoBiaoDatabase;
import com.feihua.jjcb.phone.db.DatabaseHelper;
import com.feihua.jjcb.phone.location.LocationService;
import com.feihua.jjcb.phone.navi.Node;
import com.feihua.jjcb.phone.navi.OnNaviListener;
import com.feihua.jjcb.phone.ui.CBGLDetailsActivity;
import com.feihua.jjcb.phone.ui.ErrorReportActivity;
import com.feihua.jjcb.phone.utils.AppUtils;
import com.feihua.jjcb.phone.utils.DateUtil;
import com.feihua.jjcb.phone.utils.DensityUtil;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.LocationUtils;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.feihua.jjcb.phone.utils.StringUtils;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.feihua.jjcb.phone.view.CustomProgressDialog;
import com.feihua.jjcb.phone.voice.OnDialogListener;
import com.feihua.jjcb.phone.voice.XunFeiVoice;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wcj on 2016-03-31.
 */
public class CBGLDetailsFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private String userbKh;
    private String volumeNo;
    private ChaoBiaoTables tables;
    private ListView mLvCustomerDetails;
    private ArrayList<DetailsBean> datas;
    private CBGLDetailsListAdapter adapter;
    private ChaoBiaoDatabase chaoBiaoDatabase;
    private BiaoCeDatabase biaoCeDatabase;
    private View mFootview;
    private EditText etYield;
    private EditText etCheckCode;
    private TextView tvSQDS;
    private TextView tvLocation;
    private String volumeMcount;
    private String IS_CBWAY = "1";
    private Spinner spinnerBk;
    private BiaoKuangDatabase biaoKuangDatabase;
    private ArrayList<Datadic> datadicsList;
    private ArrayList<String> waterBkList;
    private String WATER_BK;
    private int spinnerIndex = 0;
    private TextView tvFee;
    private TextView tvCurYield;
    private XunFeiVoice voice;
    private OnNaviListener onNaviListener;
    private boolean isNavi = false;
    private LocationService locationService;
    private boolean isLocation;
    private EditText etPhone;
    private MediaPlayer mp;//mediaPlayer对象
    private ViewPager mViewPager;//mediaPlayer对象
    protected CustomProgressDialog mProgressBar;
    protected Context ctx;
    private Map<Integer, String> map;
    private int position;
    /**
     * 是否在onStart()方法中发出网络请求，默认在onStart()中
     */
    private boolean onStartGetNetData = true;
    /**
     * 是否已经发出网络请求
     */
    private boolean hasGotNetData = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && !onStartGetNetData && !hasGotNetData){
            //界面可见、还未请求数据并且不是由onStart()请求数据时执行
//            if(/*判断网络*/){
//                getNetWorkData();//请求网络数据的方法
//                hasGotNetData = true;
//            }else{
//                //用Toast提示没有连接网络
//            }
//            initData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initData() {
        initList();
        etYield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int meterhWaterQan = 0;
                if (!TextUtils.isEmpty(tables.getMETERH_WATERQAN())) {
                    meterhWaterQan = Integer.valueOf(tables.getMETERH_WATERQAN());
                }
                int yield = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    yield = Integer.valueOf(s.toString());
                }
                L.w("CBGLDetailsFragment", "etYield onTextChanged");
                tvCurYield.setText(String.valueOf(meterhWaterQan + yield));
                tvFee.setText(getFee(meterhWaterQan + yield));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, waterBkList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBk.setAdapter(spinnerAdapter);
        spinnerBk.setSelection(spinnerIndex);
        setBKState(spinnerIndex);
        spinnerBk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                L.w("CBGLDetailsFragment", "onItemSelected");
                setBKState(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new CBGLDetailsListAdapter(ctx, datas, R.layout.listitem_cbgl_details);
        mLvCustomerDetails.setAdapter(adapter);

        tvSQDS.setText(tables.getUSERB_SQDS());
        String checkCode = tables.getWATERM_CHECK_CODE();
        if (checkCode.equals("0") && tables.getIS_SAVE().equals("0")) {
            checkCode = "";
        }
        etCheckCode.setText(checkCode);
        etYield.setText(tables.getWATERM_YIELD());
        tvLocation.setText(tables.getGPS());
    }

    @Override
    public void onStart() {
        if(getUserVisibleHint() && onStartGetNetData && !hasGotNetData){
            //界面可见并从未请求过数据时加载数据
//            if(/*判断网络的方法*/){
//                getNetWorkData();//请求网络数据的方法
//                hasGotNetData = true;
//            }else{
//                //Toast提示没有连接网络
//            }
//            initData();
        }else if(onStartGetNetData){
            onStartGetNetData = false;//界面不可见并且是由onStart()加载数据时置为false，委托给setUserVisibleHint()去请求数据
        }
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(myBaiduLocationListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

        super.onStart();
    }

    @Override
    public void onDestroy() {
        hasGotNetData = false;
        onStartGetNetData = true;//Fragment的状态会被所属的Activity保存，但不知能保存多久，保险起见变量设为初值
        if (voice!=null) {
            voice.onFinish();
        }
        if (map!=null) {
            map.clear();
        }
        super.onDestroy();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cbgl_dtails, container, false);
    }

    @Override
    public void onViewCreated(View layout, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(layout, savedInstanceState);
        ctx = getActivity();
        initViews(layout);
        initData();
    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_cbgl_dtails;
//    }

//    @Override
    public void initList() {
        initVoice();


        biaoCeDatabase = new BiaoCeDatabase(ctx);
        biaoKuangDatabase = new BiaoKuangDatabase(ctx);

        datas = new ArrayList<>();
        chaoBiaoDatabase = new ChaoBiaoDatabase(ctx);
        tables = chaoBiaoDatabase.DeptArrayOne(volumeNo, userbKh);
        datas.add(new DetailsBean(R.mipmap.icon_details_c1, "户号:", tables.getUSERB_KH(), R.mipmap.icon_details_c12, "表号:", tables.getWATERM_NO()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c17, "户名:", tables.getUSERB_HM()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c2, "地址:", tables.getUSERB_ADDR()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c29, "上月用量:", tables.getUSERB_SYSL(), R.mipmap.icon_details_c6, "基本吨:", tables.getBASICTON_QAN()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c27, "换表水量:", tables.getMETERH_WATERQAN(), R.mipmap.qianfei, "欠费金额：", tables.getQFJE()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c7, "用水性质:", tables.getWATERT_NAME(), R.mipmap.icon_details_c8, "水价:", tables.getUSERB_CUP()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c5, "口径:", tables.getMETERC_CALIBRE(), R.mipmap.icon_details_c18, "表型:", tables.getWATERM_TYPE()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c9, "安装日期:", tables.getWATERM_AZRQ()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c10, "换表日期:", tables.getWATERM_REPDATE()));
        datas.add(new DetailsBean(R.mipmap.icon_details_c4, "手机:", tables.getUSERB_MT(), R.mipmap.icon_details_c3, "电话:", tables.getUSERB_DH()));

        for (DetailsBean data : datas) {
            L.d("DetailsBean", "DetailsBean = " + data);
        }

        datadicsList = biaoKuangDatabase.DeptArrayAll();
        waterBkList = new ArrayList<>();
        spinnerIndex = getBKIndex("抄正常");
//        L.w("CBGLDetailsFragment", "基本吨tables.getBASICTON_TAG() = " + tables.getBASICTON_TAG() + "户号getUSERB_KH = " + tables.getUSERB_KH() + " 地址 = " + tables.getUSERB_ADDR());
        for (int i = 0; i < datadicsList.size(); i++) {
            if (tables.getWATER_BK().equals(datadicsList.get(i).getDatadic_value())) {
                spinnerIndex = i;
            }
            waterBkList.add(datadicsList.get(i).getDatadic_name());
        }
        WATER_BK = datadicsList.get(spinnerIndex).getDatadic_value();
        IS_CBWAY = tables.getIS_CBWAY();
        mp = MediaPlayer.create(getActivity(), R.raw.didi);//创建mediaplayer对象
    }

    private void initVoice() {
        voice = new XunFeiVoice(getActivity());
        voice.setOnDialogListener(new OnDialogListener() {
            @Override
            public void onResult(String result) {
                L.e("CBGLDetailsFragment", "result=" + result);
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(result);
                if (!m.matches()) {
                    ToastUtil.showShortToast("你所说的不是数字");
                } else {
                    etCheckCode.setText(result);
                }
            }
        });
    }

    private int getBKIndex(String name) {
        for (int i = 0; i < datadicsList.size(); i++) {
            if (datadicsList.get(i).getDatadic_name().equals(name)) {
                return i;
            }
        }
        return 0;
    }

    public void setOnNaviListener(OnNaviListener onNaviListener) {
        this.onNaviListener = onNaviListener;
    }

    private BDLocationListener myBaiduLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location) {
                Message msg = mHandler.obtainMessage();
                msg.obj = location;
                msg.what = LocationUtils.MSG_LOCATION_FINISH;
                mHandler.sendMessage(msg);
                locationService.stop();
            }
        }
    };

//    @Override
    public void initViews(View layout) {
        mFootview = layout.findViewById(R.id.layoutFootView);
        mFootview.findViewById(R.id.btnGetLocation).setOnClickListener(this);
        mFootview.findViewById(R.id.btUpdatePhone).setOnClickListener(this);
        etPhone = (EditText) mFootview.findViewById(R.id.etPhone);


        tvCurYield = (TextView) mFootview.findViewById(R.id.tvCurYield);
        tvFee = (TextView) mFootview.findViewById(R.id.tvFee);
        tvSQDS = (TextView) mFootview.findViewById(R.id.tvSQDS);
        tvLocation = (TextView) mFootview.findViewById(R.id.tvLocation);
        etCheckCode = (EditText) mFootview.findViewById(R.id.etCheckCode);
        etCheckCode.addTextChangedListener(this);
        etYield = (EditText) mFootview.findViewById(R.id.etYield);
//        etYield.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                int meterhWaterQan = 0;
//                if (!TextUtils.isEmpty(tables.getMETERH_WATERQAN())) {
//                    meterhWaterQan = Integer.valueOf(tables.getMETERH_WATERQAN());
//                }
//                int yield = 0;
//                if (!TextUtils.isEmpty(s.toString())) {
//                    yield = Integer.valueOf(s.toString());
//                }
//                L.w("CBGLDetailsFragment", "etYield onTextChanged");
//                tvCurYield.setText(String.valueOf(meterhWaterQan + yield));
//                tvFee.setText(getFee(meterhWaterQan + yield));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        spinnerBk = (Spinner) layout.findViewById(R.id.spinner_bk);
        layout.findViewById(R.id.btn_save).setOnClickListener(this);
        layout.findViewById(R.id.btn_error_report).setOnClickListener(this);
        layout.findViewById(R.id.btn_updata).setOnClickListener(this);
        layout.findViewById(R.id.btnFee).setOnClickListener(this);
        layout.findViewById(R.id.btnVoice).setOnClickListener(this);
        layout.findViewById(R.id.btnNavigation).setOnClickListener(this);
        mLvCustomerDetails = (ListView) layout.findViewById(R.id.lv_customer_details);


    }

    private void setBKState(int position) {
        spinnerIndex = position;
        WATER_BK = datadicsList.get(position).getDatadic_value();
        String datadicName = datadicsList.get(position).getDatadic_name();
        if (datadicName.equals("抄正常")) {
            IS_CBWAY = "1";
            etYield.setEnabled(false);
            etCheckCode.setText(etCheckCode.getText().toString());
        } else {
            IS_CBWAY = "0";
            etYield.setEnabled(true);
        }
    }


    public void setUserbKh(String volumeNo, String userbKh, String volumeMcount,int position) {
        this.volumeNo = volumeNo;
        this.userbKh = userbKh;
        this.volumeMcount = volumeMcount;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onclickSave(false);
                break;
            case R.id.btnGetLocation:
                getLocation();
                break;
            case R.id.btn_updata:
                onclickSave(true);
                break;
            case R.id.btn_error_report:
                onClickAbnormal();
                break;
            case R.id.btnFee:
                doFee();
                break;
            case R.id.btnVoice:
                voice.showDialog();
                break;
            case R.id.btnNavigation:
                doNavi();
                break;
            case R.id.btUpdatePhone:
//                修改用户手机
                upDatePhone();
                break;
            default:
                break;
        }
    }

    /**
     * 更新手机号码
     */
    private void upDatePhone() {

        if (etPhone != null) {
            String phoneNum = etPhone.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNum)) {
                mProgressBar.setProgressMsg(getResources().getString(R.string.progress_upload_all));
                mProgressBar.show();
//"http://218.207.198.197:1439/fh_jjwater_service/androidController.do?method=downLoad",
//                String userId = SharedPreUtils.getString(Constants.USER_ID, ctx);
//                L.d("update","userbKh = "+userbKh+"  number = "+phoneNum);
                OkHttpUtils.post()//
                        .url(Constants.UPDATE_PHONE)//
                        .addParams("USERB_KH", userbKh)//
                        .addParams("PHONE", phoneNum)//
                        .build()//
                        .execute(new NotObjCommonCallback() {

                            @Override
                            public void onResponse(Boolean aBoolean) {
                                mProgressBar.dismiss();
                                if (aBoolean) {

                                    ToastUtil.showShortToast("修改成功");
                                }

                            }

                            @Override
                            public void onNetworkError(boolean isNetwork, String error) {
                                if (isNetwork) {
                                    ToastUtil.showShortToast(R.string.toast_network_anomalies);
                                } else {
                                    ToastUtil.showShortToast(error);
                                }
                                mProgressBar.dismiss();
                            }
                        });


            } else {
                ToastUtil.showShortToast("请输入手机号码");
            }

        }
    }

    private void doNavi() {
        String location = tvLocation.getText().toString();
        isNavi = true;
        if (TextUtils.isEmpty(location)) {
            ToastUtil.showShortToast("水表坐标不能为空");
            return;
        }
        getLocation();
    }

    private void toNavi(String startLocation) {
        String location = tvLocation.getText().toString();
        if (startLocation.equals(location)) {
            ToastUtil.showShortToast("当前位置与水表位置是同一位置");
            return;
        }

        Node eNode = locationToNode(location);
        Node sNode = locationToNode(startLocation);
        if (sNode != null && eNode != null) {
            if (onNaviListener != null) {
                onNaviListener.setNaviNode(sNode, eNode);
            }
        }
    }

    private Node locationToNode(String location) {
        Node node = null;
        try {
            String[] locations = location.split(",");
            node = new Node(Double.valueOf(locations[0]), Double.valueOf(locations[1]), "");
        } catch (Exception e) {
            ToastUtil.showShortToast("坐标格式错误");
        }
        return node;
    }

    private void doFee() {
        String yield = tvCurYield.getText().toString();
        if (TextUtils.isEmpty(yield)) {
            ToastUtil.showShortToast("水量数值异常");
            return;
        }

        OkHttpUtils.post()//
                .url(Constants.GET_WATERPAY)//
                .addParams("WATERT_NO", tables.getUSERB_YHLX())//
                .addParams("USERB_KH", userbKh)//
                .addParams("WATERP_QAN", yield)//
                .addParams("DEBTL_MON", tables.getWATERU_MONTH())//
                .build()//
                .execute(new CommonCallback() {
                    @Override
                    public void onResponse(String datas) {
                        Fee fee = new Gson().fromJson(datas, Fee.class);
                        if (fee != null && fee.SF != null && fee.SF.size() > 0) {
                            Fee.Payl payl = fee.SF.get(0);
                            tvFee.setText(toDecima(payl.PAYL_TOTAL));
                        }
                    }

                    @Override
                    public void onNetworkError(boolean isNetwork, String error) {
                        if (isNetwork) {
                            ToastUtil.showShortToast(R.string.toast_network_anomalies);
                        } else {
                            ToastUtil.showShortToast(error);
                        }
                    }
                });
    }

    private void onClickAbnormal() {
        String location = tvLocation.getText().toString();
        Intent intent = new Intent(ctx, ErrorReportActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("tables", tables);
        //        intent.putExtra("YEAR", tables.getWATERU_YEAR());
        //        intent.putExtra("MONTH", tables.getWATERU_MONTH());
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void getLocation() {
        isLocation = true;
        // 启动定位
        locationService.start();
        mHandler.sendEmptyMessage(LocationUtils.MSG_LOCATION_START);
    }

    private void onUpdata(boolean isUpdata) {
        tables.GPS = tvLocation.getText().toString();
        tables.WATERM_CHECK_CODE = etCheckCode.getText().toString();
        tables.WATERM_YIELD = etYield.getText().toString();
        tables.TIME = DateUtil.format(new Date());
        tables.WATER_BK = WATER_BK;
        tables.IS_CBWAY = IS_CBWAY;
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCheckCode.getWindowToken(), 0);
        L.w("CBGLDetailsFragment", "onUpdata tables.getWATER_BK() = " + tables.getWATER_BK());

        //        BiaoCeTables biaoCeTables = biaoCeDatabase.queryBiaoCeStatu(volumeNo);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CHAOBIAO_IS_SAVE, "1");
        values.put(DatabaseHelper.CHAOBIAO_IS_UPDATA, "0");
        values.put(DatabaseHelper.CHAOBIAO_WATERM_LOALTION, tables.getGPS());
        values.put(DatabaseHelper.CHAOBIAO_WATERM_CHECK_CODE, tables.getWATERM_CHECK_CODE());
        values.put(DatabaseHelper.CHAOBIAO_WATERM_YIELD, tables.getWATERM_YIELD());
        values.put(DatabaseHelper.CHAOBIAO_TIME, tables.getTIME());
        values.put(DatabaseHelper.CHAOBIAO_WATER_BK, tables.getWATER_BK());
        values.put(DatabaseHelper.CHAOBIAO_IS_CBWAY, tables.getIS_CBWAY());
        chaoBiaoDatabase.updatatable(values, userbKh);

        int saveNum = chaoBiaoDatabase.queryCount(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?" + " and " + DatabaseHelper.CHAOBIAO_IS_SAVE + "=?", new String[]{volumeNo, "1"});
        int updataNum = chaoBiaoDatabase.queryCount(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?" + " and " + DatabaseHelper.CHAOBIAO_IS_UPDATA + "=?", new String[]{volumeNo, "1"});
        ContentValues baioceValues = new ContentValues();
        baioceValues.put(DatabaseHelper.BIAOCE_VOLUME_UPDATA, updataNum);
        baioceValues.put(DatabaseHelper.BIAOCE_VOLUME_SAVE, saveNum);
        biaoCeDatabase.updatatable(baioceValues, volumeNo);
        if (isUpdata) {
            updataServer(tables.getGPS(), tables.getWATERM_CHECK_CODE(), tables.getWATERM_YIELD(), tables.getTIME());
        } else {
            ToastUtil.showShortToast(R.string.toast_updata_save);
            nextPage();
        }
    }

    private void updataServer(String location, String checkCode, String yield, String time) {
        mProgressBar.setProgressMsg(getResources().getString(R.string.progress_upload_all));
        mProgressBar.show();
        L.w("CBGLDetailsFragment", "AREA_NO = " + tables.getAREA_NO());
        String userId = SharedPreUtils.getString(Constants.USER_ID, ctx);
        OkHttpUtils.post()//
                .url(Constants.UPDATA_CBGL_DETAILSE)//
                .addParams("RECORDER", userId)//
                .addParams("VOLUME_NO", volumeNo)//
                .addParams("USERB_KH", userbKh)//
                .addParams("USERB_SQDS", tables.getUSERB_SQDS())//
                .addParams("USERB_BQDS", checkCode)//
                .addParams("GPS", tables.getGPS())//
                .addParams("WATERU_QAN", yield)//
                .addParams("METERH_WATERQAN", tables.getMETERH_WATERQAN())//
                .addParams("THREEMON_AVG", tables.getQ3Q())//
                .addParams("USERB_HM", tables.getUSERB_HM())//
                .addParams("USERB_YHLX", tables.getUSERB_YHLX())//
                .addParams("WATERU_YEAR", tables.getWATERU_YEAR())//
                .addParams("WATERU_MONTH", tables.getWATERU_MONTH())//
                .addParams("WATER_BK", tables.getWATER_BK())//
                .addParams("IS_CBWAY", tables.getIS_CBWAY())//
                .addParams("AREA_NO", tables.getAREA_NO())//
                .addParams("FIRST_TAG", tables.getFIRST_TAG())//
                .addParams("RECORD_DATE", tables.getTIME())//
                .addParams("USERB_SFFS", tables.getUSERB_SFFS())//
                .addParams("USERB_TOTAL", tables.getUSERB_TOTAL())//
                .addParams("ISCHARGING", tables.getISCHARGING())//
                .addParams("USERB_CUP", tables.getUSERB_CUP())//
                .addParams("FREEPULL_TAG", tables.getFREEPULL_TAG())//
                .addParams("BASICTON_TAG", tables.getBASICTON_TAG())//
                .addParams("BASICTON_QAN", tables.getBASICTON_QAN())//
                .addParams("LADDER_TAG", tables.getLADDER_TAG())//
                .addParams("SEASON_TAG", tables.getSEASON_TAG())//
                .addParams("WZ_TAG", tables.getWZ_TAG())//
                .addParams("USERB_SYSL", tables.getUSERB_SYSL())//
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
                        mProgressBar.dismiss();
                        if (isSuccess) {
                            ToastUtil.showShortToast(R.string.toast_updata_success);

                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.CHAOBIAO_IS_UPDATA, "1");
                            chaoBiaoDatabase.updatatable(values, userbKh);

                            int updataNum = chaoBiaoDatabase.queryCount(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?" + " and " + DatabaseHelper.CHAOBIAO_IS_UPDATA + "=?", new String[]{volumeNo, "1"});
                            ContentValues baioceValues = new ContentValues();
                            baioceValues.put(DatabaseHelper.BIAOCE_VOLUME_UPDATA, updataNum);
                            biaoCeDatabase.updatatable(baioceValues, volumeNo);

                            if (!TextUtils.isEmpty(volumeMcount) && updataNum == (Integer.valueOf(volumeMcount))) {
                                updataVolscbs();
                            }
                            nextPage();
                        }
                    }
                });
    }

    private void updataVolscbs() {
        String year = tables.getWATERU_YEAR();
        String month = tables.getWATERU_MONTH();
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

    private void onclickSave(boolean isUpdata) {
        if (!isAbnormal(isUpdata)) {
            onUpdata(isUpdata);
        }
    }

    private void showDialog(final String dialogContent, final boolean isUpdata) {
        //做两次判断，并弹出框提示1、输入值不得小于本期起码  2、水量不得多或少于前三期平均水量的30%
        final Dialog dialog = new Dialog(ctx, R.style.Theme_Dialog);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_reminder, null, false);
        int cFullFillWidth = DensityUtil.dip2px(ctx, 300);
        layout.setMinimumWidth(cFullFillWidth);
        TextView tvDialogContent = (TextView) layout.findViewById(R.id.tvDialogContent);
        tvDialogContent.setText(dialogContent);

        layout.findViewById(R.id.btn_dialog_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //成功保存到数据库后,做文字提示成功!
                int yield = Integer.valueOf(etYield.getText().toString());
                String datadicName = datadicsList.get(spinnerIndex).getDatadic_name();

                if (dialogContent.equals(getResources().getString(R.string.dialog_q3q_abnormal_more)) && !datadicName.equals("新表")) {
                    spinnerIndex = getBKIndex("抄偏大");
                    spinnerBk.setSelection(spinnerIndex);
                } else if (dialogContent.equals(getResources().getString(R.string.dialog_q3q_abnormal_few)) && !datadicName.equals("新表")) {
                    spinnerIndex = getBKIndex("抄偏少");
                    spinnerBk.setSelection(spinnerIndex);
                }
                setBKState(spinnerIndex);
                if (Integer.valueOf(yield) >= 0) {
                    onUpdata(isUpdata);
                }
                dialog.dismiss();
            }
        });
        layout.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //自动结算水量
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //        String datadicName = datadicsList.get(spinnerIndex).getDatadic_name();
        //        if (datadicName.equals("抄正常") || datadicName.equals("基本吨") || datadicName.equals("抄偏大") || datadicName.equals("抄偏小") || datadicName.equals("新表"))
        //        {
        //        }
        //        else
        //        {
        //            return;
        //        }
        if (tables==null) {
            return;
        }
        if (StringUtils.isEmpty(tables.getUSERB_SQDS())) {
            return;
        }
        if (StringUtils.isEmpty(s.toString())) {
            return;
        }
        int checkCode = Integer.valueOf(s.toString());
        int sqds = Integer.valueOf(tables.getUSERB_SQDS());
        int yield = checkCode - sqds;
//        L.w("CBGLDetailsFragment", "基本吨tables.getBASICTON_TAG() = " + tables.getBASICTON_TAG() + "户号getUSERB_KH = " + tables.getUSERB_KH() + " 地址 = " + tables.getUSERB_ADDR());

        if (tables.getBASICTON_TAG().equals("1")) {
            if (yield < Integer.valueOf(tables.getBASICTON_QAN())) {
                L.w("CBGLDetailsFragment", "基本吨tables.getBASICTON_TAG() = " + tables.getBASICTON_TAG() + "户号getUSERB_KH = " + tables.getUSERB_KH() + " 地址 = " + tables.getUSERB_ADDR());

//                yield = Integer.valueOf(tables.getBASICTON_QAN());
                map = AppUtils.getMap();
                if (!map.containsKey(position)) {
                    map.put(position, "1");
                }
                spinnerIndex = getBKIndex("基本吨");
                spinnerBk.setSelection(spinnerIndex);
                setBKState(spinnerIndex);
            }
        }
        etYield.setText(String.valueOf(yield));
    }

    private String getFee(int yield) {
        String userb_cup = tables.getUSERB_CUP();
        if (TextUtils.isEmpty(userb_cup)) {
            ToastUtil.showShortToast("水价数据异常");
            return "";
        }
        String fee = String.valueOf(yield * Float.valueOf(userb_cup));
        return toDecima(fee);
    }

    private String toDecima(String fee) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(Double.parseDouble(fee));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //true有异常false无异常
    public boolean isAbnormal(boolean isUpdata) {
        //做两次判断，并弹出框提示1、输入值不得小于本期起码  2、水量不得多或少于前三期平均水量的30%
        String yield = etYield.getText().toString();
        L.w("CBGLDetailsFragment", "tables.Q3Q = " + tables.Q3Q);
        if (StringUtils.isEmpty(yield)) {
            ToastUtil.showShortToast(R.string.toast_zhima_not);
            return true;
        } else {
            int numYield = Integer.valueOf(yield);
            if (numYield < 0) {
                showDialog(getResources().getString(R.string.dialog_check_code_abnormal), isUpdata);
                return true;
            }
            if (StringUtils.isEmpty(tables.Q3Q) || tables.Q3Q.equals("0")) {
                return false;
            }
            if ((Math.abs(numYield - Integer.valueOf(tables.Q3Q)) > ((Integer.valueOf(tables.Q3Q) * 30) / 100))) {
                if (numYield > Integer.valueOf(tables.Q3Q)) {
                    play();
                    showDialog(getResources().getString(R.string.dialog_q3q_abnormal_more), isUpdata);
                } else if (numYield < Integer.valueOf(tables.Q3Q)) {
                    play();
                    showDialog(getResources().getString(R.string.dialog_q3q_abnormal_few), isUpdata);
                }
                return true;
            }
        }

        return false;
    }

    private void play() {
        try {
            mp.reset();
            mp = MediaPlayer.create(getActivity(), R.raw.didi);//创建mediaplayer对象
            mp.start();//开始播放

        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                //开始定位
                case LocationUtils.MSG_LOCATION_START:
                    ToastUtil.showShortToast("正在定位...");
                    break;
                // 定位完成
                case LocationUtils.MSG_LOCATION_FINISH:
                    if (!isLocation) {
                        return;
                    }
                    isLocation = false;
                    BDLocation loc = (BDLocation) msg.obj;
                    String result = LocationUtils.getLocationLatLon(loc);
                    if (loc.getLocType() == 161 || loc.getLocType() == 61) {
                        if (isNavi) {
                            isNavi = false;
                            toNavi(result);
                        } else {
                            tables.GPS = result;
                            tvLocation.setText(result);
                            ToastUtil.showShortToast("定位完成");
                        }
                    } else {
                        ToastUtil.showShortToast(result);
                    }
                    break;
                //停止定位
                case LocationUtils.MSG_LOCATION_STOP:
                    ToastUtil.showShortToast("定位停止");
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public void onStop() {
        super.onStop();
        locationService.unregisterListener(myBaiduLocationListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }



    private void nextPage() {
        CBGLDetailsActivity activity = (CBGLDetailsActivity) getActivity();
        activity.nextPage();
    }
    /**
     * 判断是否是基本吨用户
     */
    public void isBasictonTag(int position) {

        if (AppUtils.getMap().containsKey(position)) {
            ToastUtil.showShortToast("该用户是基本吨户");
        }
    }
}
