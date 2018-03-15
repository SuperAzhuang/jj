package com.feihua.jjcb.phone.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.navi.NaviUtil;

public class NaviActivity extends Activity implements View.OnClickListener
{
    private NaviUtil naviUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        FrameLayout layoutNavi = (FrameLayout) findViewById(R.id.layoutNavi);
        findViewById(R.id.btn_left).setOnClickListener(this);

        naviUtil = new NaviUtil(this);

        View view = naviUtil.getNaviView();
        if (view != null)
        {
            layoutNavi.addView(view);
            //            setContentView(view);
        }


        naviUtil.setNaviRoutePlanNode(getIntent());

        naviUtil.addCustomized();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        naviUtil.onResume();
    }

    protected void onPause()
    {
        super.onPause();
        naviUtil.onPause();
    }

    ;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        naviUtil.onDestroy();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        naviUtil.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        naviUtil.onStop();
    }

    @Override
    public void onBackPressed()
    {
        naviUtil.onBackPressed();
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
