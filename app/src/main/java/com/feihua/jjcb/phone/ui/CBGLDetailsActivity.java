package com.feihua.jjcb.phone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.CBGLDetailsViewPagerAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.db.BiaoCeDatabase;
import com.feihua.jjcb.phone.db.ChaoBiaoDatabase;
import com.feihua.jjcb.phone.db.DatabaseHelper;
import com.feihua.jjcb.phone.fragment.CBGLDetailsFragment;
import com.feihua.jjcb.phone.navi.NaviInitUtil;
import com.feihua.jjcb.phone.navi.Node;
import com.feihua.jjcb.phone.shark.FlashlightUitl;
import com.feihua.jjcb.phone.shark.OnSharkListener;
import com.feihua.jjcb.phone.shark.SharkUtil;
import com.feihua.jjcb.phone.utils.DepthPageTransformer;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.ToastUtil;

import java.util.ArrayList;

/*
* 抄表详情
* */
public class CBGLDetailsActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private String volumeNo;
    private Context ctx;
    private ChaoBiaoDatabase chaoBiaoDatabase;
    private BiaoCeDatabase biaoCeDatabase;
    private ArrayList<ChaoBiaoTables> datas;
    private int index;
    private TextView tvJBH;
    private ViewPager mViewPager;
    private ArrayList<String> khDatas;
    private CBGLDetailsViewPagerAdapter adapter;
    private SharkUtil shark;
    private FlashlightUitl flashlight;
    private final int SENSOR_SHAKE = 1;
    private boolean isFlashlight = false;
    private NaviInitUtil initUtil;
    //    private String volumeMcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbgl_details);

        ctx = this;
        chaoBiaoDatabase = new ChaoBiaoDatabase(ctx);
        biaoCeDatabase = new BiaoCeDatabase(ctx);
        chaoBiaoDatabase = new ChaoBiaoDatabase(ctx);

        initNavi();

        initShark();

        initExtra();

        initDatas();

        initView();
    }

    private void initNavi() {
        initUtil = new NaviInitUtil(this) {
            @Override
            public Class<?> toNaviActivity() {
                return NaviActivity.class;
            }
        };
    }

    public void toNaiv(Node sNode, Node eNode) {
//        Node sNode = new Node(118.102571,24.494428,"飞华");
//        Node eNode = new Node(118.104781,24.4947,"金星");
        initUtil.toNavi(sNode, eNode);
    }

    private void initShark() {
        shark = new SharkUtil(this);
        shark.setOnSharkListener(new OnSharkListener() {
            @Override
            public void onShark() {
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        });


    }

    private void initExtra() {
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        volumeNo = intent.getStringExtra(DatabaseHelper.BIAOCE_VOLUME_NO);
        khDatas = intent.getStringArrayListExtra("kh_datas");
        //        volumeMcount = intent.getStringExtra(DatabaseHelper.BIAOCE_VOLUME_MCOUNT);
    }

    private void initDatas() {
        datas = chaoBiaoDatabase.DeptArray(volumeNo);
    }

    private void initView() {
        tvJBH = (TextView) findViewById(R.id.tvJBH);
        TextView tvHeadTitle = (TextView) findViewById(R.id.head_title);
        tvHeadTitle.setText(volumeNo);

        mViewPager = (ViewPager) findViewById(R.id.vp_customer_details);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        FragmentManager fm = getSupportFragmentManager();
        adapter = new CBGLDetailsViewPagerAdapter(fm, khDatas, volumeNo, this, mViewPager);
        mViewPager.setAdapter(adapter);

        mViewPager.setCurrentItem(index, false);

        findViewById(R.id.btn_left).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        shark.register();
        flashlight = new FlashlightUitl();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shark.unregister();
        flashlight.onFinish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(Constants.CBGL_CHAO_BIAO_ACTION);
        intent.putExtra("change_data", Constants.CBGL_CHAO_BIAO_LIST_RECEIVER);
        intent.putExtra("index", index);
        sendBroadcast(intent);
        Intent intentBiaoCe = new Intent(Constants.CBGL_BIAO_CE_ACTION);
        intentBiaoCe.putExtra("change_data", Constants.CBGL_BIAO_CE_LIST_RECEIVER);
        sendBroadcast(intentBiaoCe);
    }

    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    //                    Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                    Log.i("MainActivity", "检测到摇晃，执行操作！");
                    if (isFlashlight) {
                        isFlashlight = false;
                    } else {
                        isFlashlight = true;
                    }
                    flashlight.setFlashlightSwitch(isFlashlight);
                    break;
            }
        }

    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        L.w("CBGLDetailsFragment", "onPageSelected = position =" + position);

        index = position;
        tvJBH.setText(datas.get(position).USERB_JBH.trim());

        if (adapter != null) {
            CBGLDetailsFragment fragment = (CBGLDetailsFragment) adapter.getItem(index);
//            adapter = new CBGLDetailsViewPagerAdapter(fm, khDatas, volumeNo, this, mViewPager);
            fragment.setUserbKh(volumeNo, khDatas.get(index), String.valueOf(khDatas.size()), index);
            fragment.isBasictonTag(index);
        }
    }
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CBGLDetailsFragment fragment = (CBGLDetailsFragment) adapter.getItem(index);
//            adapter = new CBGLDetailsViewPagerAdapter(fm, khDatas, volumeNo, this, mViewPager);
                    fragment.setUserbKh(volumeNo, khDatas.get(index), String.valueOf(khDatas.size()), index);
                    fragment.isBasictonTag(index);
                }
            }, 800);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void nextPage() {
        index++;
        if (index < datas.size()) {
            mViewPager.setCurrentItem(index, true);
        } else {
            ToastUtil.showShortToast("已经是最后一页");
            index--;
        }
    }

}
