package com.feihua.jjcb.phone.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.CommonCallback;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.ui.QueryDetailsActivity;
import com.feihua.jjcb.phone.utils.SharedPreUtils;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 户号搜索
 * Created by wcj on 2016-04-15.
 */
public class HHSSFragmetn extends BaseFragment implements View.OnClickListener
{

    private EditText etSearch;
    private TextView tvSearchHistory1;
    private TextView tvSearchHistory2;
    private TextView tvSearchHistory3;
    private TextView tvSearchHistory4;
    private TextView tvSearchHistory5;
    private TextView tvSearchHistory6;
    private TextView tvSearchHistory7;
    private TextView tvSearchHistory8;
    private List<TextView> tvList;
    private List<String> khList;
    private String search;

    @Override
    public int getLayoutId()
    {
        tvList = new ArrayList<>();
        khList = new ArrayList<>();
        return R.layout.layout_hhss;
    }

    @Override
    public void initDatas()
    {
        khList.clear();
        String content = SharedPreUtils.getString(Constants.SEARCH_HISTORY, ctx);
        if (!TextUtils.isEmpty(content))
        {
            try
            {
                JSONArray array = new JSONArray(content);
                for (int i = 0; i < array.length(); i++)
                {
                    String kh = array.getString(i);
                    khList.add(kh);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        changeHistoryContent();
    }

    @Override
    public void initViews(View layout)
    {
        mProgressBar.setProgressMsg(getResources().getString(R.string.progress_search));
        etSearch = (EditText) layout.findViewById(R.id.etSearch);
        layout.findViewById(R.id.imgSearch).setOnClickListener(this);
        tvSearchHistory1 = (TextView) layout.findViewById(R.id.tvSearchHistory1);
        tvSearchHistory2 = (TextView) layout.findViewById(R.id.tvSearchHistory2);
        tvSearchHistory3 = (TextView) layout.findViewById(R.id.tvSearchHistory3);
        tvSearchHistory4 = (TextView) layout.findViewById(R.id.tvSearchHistory4);
        tvSearchHistory5 = (TextView) layout.findViewById(R.id.tvSearchHistory5);
        tvSearchHistory6 = (TextView) layout.findViewById(R.id.tvSearchHistory6);
        tvSearchHistory7 = (TextView) layout.findViewById(R.id.tvSearchHistory7);
        tvSearchHistory8 = (TextView) layout.findViewById(R.id.tvSearchHistory8);
        tvList.add(tvSearchHistory1);
        tvList.add(tvSearchHistory2);
        tvList.add(tvSearchHistory3);
        tvList.add(tvSearchHistory4);
        tvList.add(tvSearchHistory5);
        tvList.add(tvSearchHistory6);
        tvList.add(tvSearchHistory7);
        tvList.add(tvSearchHistory8);

        for (TextView tv : tvList)
        {
            tv.setOnClickListener(this);
        }


    }

    private void changeHistoryContent()
    {
        for (int i = 0; i < tvList.size(); i++)
        {
            if (i < khList.size())
            {
                tvList.get(i).setVisibility(View.VISIBLE);
                tvList.get(i).setText(khList.get(i));
            }
            else
            {
                tvList.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgSearch:
                saveSearchHistory();
                doSearch();
                break;
            case R.id.tvSearchHistory1:
                search = tvSearchHistory1.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory2:
                search = tvSearchHistory2.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory3:
                search = tvSearchHistory3.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory4:
                search = tvSearchHistory4.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory5:
                search = tvSearchHistory5.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory6:
                search = tvSearchHistory6.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory7:
                search = tvSearchHistory7.getText().toString();
                doSearch();
                break;
            case R.id.tvSearchHistory8:
                search = tvSearchHistory8.getText().toString();
                doSearch();
                break;
            default:
                break;
        }
    }

    private void saveSearchHistory()
    {
        search = etSearch.getText().toString();
        if (TextUtils.isEmpty(search) || khList.contains(search))
        {
            return;
        }

        int size = khList.size();
        if (size == 0)
        {
            khList.add(search);
        }
        else if (0 < size && size < 8)
        {
            khList.add(0, search);
        }
        else
        {
            khList.add(0, search);
            khList.remove(size);
        }

        changeHistoryContent();

        JSONArray array = new JSONArray();
        for (String content : khList)
        {
            array.put(content);
        }
        SharedPreUtils.putString(Constants.SEARCH_HISTORY, array.toString(), ctx);
    }

    private void doSearch()
    {
        if (TextUtils.isEmpty(search))
        {
            ToastUtil.showShortToast(R.string.toast_tjcx_search_abnormal);
            return;
        }
        mProgressBar.show();
        OkHttpUtils.post()//
                .url(Constants.GET_TJCX_DETAILS)//
                .addParams("USERB_KH", search)//
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
                            Intent intent = new Intent(getActivity(), QueryDetailsActivity.class);
                            intent.putExtra("USERB_KH",search);
                            startActivity(intent);
                        }
                    }
                });

    }
}
