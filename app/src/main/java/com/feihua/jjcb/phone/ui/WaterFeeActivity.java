package com.feihua.jjcb.phone.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.YHCXViewPagerAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.fragment.CBDetailsFragment;
import com.feihua.jjcb.phone.fragment.FeeDetailsFragment;
import com.feihua.jjcb.phone.fragment.HHSSFragmetn;
import com.feihua.jjcb.phone.fragment.QFDetailsFragment;
import com.feihua.jjcb.phone.fragment.TJCXFragment;
import com.feihua.jjcb.phone.view.NoScrollViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WaterFeeActivity extends BaseActivity implements View.OnClickListener
{

    private NoScrollViewPager mViewPager;
    private ArrayList<Fragment> fragmentDatas;
    private TextView tvCB;
    private TextView tvSF;
    private TextView tvQF;
    private final int TITLE_CB = 0;
    private final int TITLE_SF = 1;
    private final int TITLE_QF = 2;
    private ArrayList<Integer> titleImgs;
    private ArrayList<TextView> titleTv;
    private View layoutTitle;
    private String userbKh;
    private String userbHm;
    private View tvNext;
    private View tvPrevious;
    private String curYear;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_fee);

        initExtra();

        initList();

        initView();

        initNext();
    }

    private void initNext()
    {
        int currentItem = mViewPager.getCurrentItem();
        if (curYear.equals(new SimpleDateFormat("yyyy").format(new Date())))
        {
            tvNext.setVisibility(View.GONE);
        }
        else
        {
            if (currentItem != 2)
            {
                tvNext.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initExtra()
    {
        Intent intent = getIntent();
        userbKh = intent.getStringExtra("USERB_KH");
        userbHm = intent.getStringExtra("USERB_HM");
    }

    public void initList()
    {
        fragmentDatas = new ArrayList<>();
        fragmentDatas.add(new CBDetailsFragment());
        fragmentDatas.add(new FeeDetailsFragment());
        fragmentDatas.add(new QFDetailsFragment());
        curYear = new SimpleDateFormat("yyyy").format(new Date());
        initFragmentInfo(curYear, false);
        titleImgs = new ArrayList<>();
        titleTv = new ArrayList<>();
        titleImgs.add(R.mipmap.img_water_fee_title_1);
        titleImgs.add(R.mipmap.img_water_fee_title_2);
        titleImgs.add(R.mipmap.img_water_fee_title_3);
    }

    private void initFragmentInfo(String year, boolean isRefresh)
    {
        CBDetailsFragment cbFragment = (CBDetailsFragment) fragmentDatas.get(0);
        FeeDetailsFragment feeFragment = (FeeDetailsFragment) fragmentDatas.get(1);
        QFDetailsFragment qfFragment = (QFDetailsFragment) fragmentDatas.get(2);
        cbFragment.onRefresh(userbKh, year, isRefresh);
        feeFragment.onRefresh(userbKh, year, isRefresh);
        qfFragment.onRefresh(userbKh);
    }

    private void initView()
    {
        tvCB = (TextView) findViewById(R.id.tvCB);
        tvSF = (TextView) findViewById(R.id.tvSF);
        tvQF = (TextView) findViewById(R.id.tvQF);
        tvCB.setOnClickListener(this);
        tvSF.setOnClickListener(this);
        tvQF.setOnClickListener(this);
        titleTv.add(tvCB);
        titleTv.add(tvSF);
        titleTv.add(tvQF);
        tvPrevious = findViewById(R.id.previous);
        tvPrevious.setOnClickListener(this);
        tvNext = findViewById(R.id.next);
        tvNext.setOnClickListener(this);
        findViewById(R.id.btn_left).setOnClickListener(this);
        layoutTitle = findViewById(R.id.layoutTitle);

        TextView tvHM = (TextView) findViewById(R.id.tvHM);
        tvHM.setText("户名:" + userbHm);

        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        mViewPager.setNoScroll(true);
        mViewPager.setOffscreenPageLimit(4);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new YHCXViewPagerAdapter(fm, fragmentDatas));
        setTitleState(TITLE_CB);
    }

    protected void setTitleState(int index)
    {
        for (int i = 0; i < titleTv.size(); i++)
        {
            TextView tv = titleTv.get(i);
            if (i == index)
            {
                tv.setTextColor(getResources().getColor(R.color.blue_title));
            }
            else
            {
                tv.setTextColor(getResources().getColor(R.color.white));
            }
        }
        layoutTitle.setBackgroundResource(titleImgs.get(index));
        mViewPager.setCurrentItem(index, false);
        if (index == 2)
        {
            tvPrevious.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
        }
        else
        {
            tvPrevious.setVisibility(View.VISIBLE);
            tvNext.setVisibility(View.VISIBLE);
        }
        initNext();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.previous:
                toPrevious();
                break;
            case R.id.btn_left:
                finish();
                break;
            case R.id.tvSF:
                setTitleState(TITLE_SF);
                break;
            case R.id.tvQF:
                setTitleState(TITLE_QF);
                break;
            case R.id.tvCB:
                setTitleState(TITLE_CB);
                break;
            case R.id.next:
                toNext();
                break;
            default:
                break;
        }

    }

    private void toPrevious()
    {
        int year = Integer.valueOf(curYear);
        curYear = String.valueOf(year - 1);
        initFragmentInfo(curYear, true);
        initNext();
    }

    private void toNext()
    {
        int year = Integer.valueOf(curYear);
        curYear = String.valueOf(year + 1);
        initFragmentInfo(curYear, true);
        initNext();
    }
}
