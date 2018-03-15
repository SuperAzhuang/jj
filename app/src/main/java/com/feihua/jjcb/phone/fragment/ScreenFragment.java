package com.feihua.jjcb.phone.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.Datadic;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.db.BiaoKuangDatabase;
import com.feihua.jjcb.phone.db.ChaoBiaoDatabase;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wcj on 2016-05-10.
 */
public class ScreenFragment extends BaseFragment implements View.OnClickListener
{

    private int[] rgIds = {R.id.rbScreenItem1, R.id.rbScreenItem2, R.id.rbScreenItem3};
    private HashMap<Integer, RadioButton> rbHM;
    private List<String> cbList;
    private int index = 0;
    private final int HH = 0;
    private final int BK = 1;
    private final int CB = 2;
    private ArrayList<String> waterBkList;
    private ArrayList<Datadic> datadicsList;
    private String WATER_BK;
    private String IS_SAVE = "0";
    private TextView etScreenHh;
    private ChaoBiaoDatabase chaoBiaoDatabase;
    private OnDataListener listener;
    private String volumeNo;

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_screen;
    }

    @Override
    public void initList()
    {
        chaoBiaoDatabase = new ChaoBiaoDatabase(ctx);
        BiaoKuangDatabase biaoKuangDatabase = new BiaoKuangDatabase(ctx);
        rbHM = new HashMap<>();
        cbList = new ArrayList<>();
        cbList.add("未抄");
        cbList.add("已抄");
        datadicsList = biaoKuangDatabase.DeptArrayAll();
        waterBkList = new ArrayList<>();
        for (int i = 0; i < datadicsList.size(); i++)
        {
            waterBkList.add(datadicsList.get(i).getDatadic_name());
        }
        WATER_BK = datadicsList.get(0).getDatadic_value();
    }

    @Override
    public void initViews(View layout)
    {
        etScreenHh = (TextView) layout.findViewById(R.id.etScreenHh);
        layout.findViewById(R.id.btnCommit).setOnClickListener(this);

        RadioButton rbScreenItem1 = (RadioButton) layout.findViewById(R.id.rbScreenItem1);
        RadioButton rbScreenItem2 = (RadioButton) layout.findViewById(R.id.rbScreenItem2);
        RadioButton rbScreenItem3 = (RadioButton) layout.findViewById(R.id.rbScreenItem3);
        rbScreenItem1.setOnClickListener(this);
        rbScreenItem2.setOnClickListener(this);
        rbScreenItem3.setOnClickListener(this);
        rbHM.put(R.id.rbScreenItem1, rbScreenItem1);
        rbHM.put(R.id.rbScreenItem2, rbScreenItem2);
        rbHM.put(R.id.rbScreenItem3, rbScreenItem3);
        initRadioButton(HH);

        Spinner spinnerBk = (Spinner) layout.findViewById(R.id.spinner_bk);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(ctx, R.layout.simple_spinner_item, waterBkList);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerBk.setAdapter(spinnerAdapter);
        spinnerBk.setSelection(26);
        spinnerBk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                WATER_BK = datadicsList.get(position).getDatadic_value();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Spinner spinnerState = (Spinner) layout.findViewById(R.id.spinnerState);
        ArrayAdapter spinnerStateAdapter = new ArrayAdapter<String>(ctx, R.layout.simple_spinner_item, cbList);
        spinnerStateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(spinnerStateAdapter);
        spinnerState.setSelection(0);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                IS_SAVE = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void initRadioButton(int num)
    {
        this.index = num;
        for (int i = 0; i < rgIds.length; i++)
        {
            RadioButton radioButton = rbHM.get(rgIds[i]);
            if (num == i)
            {
                radioButton.setChecked(true);
            }
            else
            {
                radioButton.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rbScreenItem1:
                initRadioButton(HH);
                break;
            case R.id.rbScreenItem2:
                initRadioButton(BK);
                break;
            case R.id.rbScreenItem3:
                initRadioButton(CB);
                break;
            case R.id.btnCommit:
                commit();
                break;
            default:
                break;
        }
    }

    private void commit()
    {
        switch (index)
        {
            case HH:
                queryHHList();
                break;
            case BK:
                queryBKList();
                break;
            case CB:
                queryCBList();
                break;
            default:
                break;
        }
    }

    private void queryCBList()
    {
        if (listener != null)
        {
            listener.onData(chaoBiaoDatabase.DeptArrayCB(volumeNo, IS_SAVE));
        }
    }

    private void queryBKList()
    {
        if (listener != null)
        {
            listener.onData(chaoBiaoDatabase.DeptArrayBK(volumeNo, WATER_BK));
        }
    }

    private void queryHHList()
    {
        String USERB_KH = etScreenHh.getText().toString();
        if (TextUtils.isEmpty(USERB_KH))
        {
            ToastUtil.showShortToast("请输入户号");
            return;
        }
        if (listener != null)
        {
            listener.onData(chaoBiaoDatabase.DeptArrayHH(volumeNo, USERB_KH));
        }
    }

    public void setOnDataListener(String volumeNo, OnDataListener listener)
    {
        this.volumeNo = volumeNo;
        this.listener = listener;
    }

    public interface OnDataListener
    {
        public void onData(List<ChaoBiaoTables> list);
    }


}
