package com.feihua.jjcb.phone.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.feihua.jjcb.phone.Adapter.PhotoBaseAdapter;
import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.bean.PhotoInfo;
import com.feihua.jjcb.phone.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.Collections;

public class MultiplePickPhotoActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{

    private ImageButton back_btn;
    private GridView gridGallery;
    private Button btnGalleryOk;
    private PhotoBaseAdapter adapter;
    private ImageView imgNoMedia;
    private Handler handler;
    private int size;
    private int way;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_pick_photo);
        Intent intent = getIntent();
        size = intent.getIntExtra("size", 0);
        way = intent.getIntExtra("way", 1);

        initView();
        initData();

    }

    private void initData()
    {
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                if (msg.arg1 == 1)
                {
                    ArrayList<PhotoInfo> allPhoto = (ArrayList<PhotoInfo>) msg.obj;
                    adapter.addAll(allPhoto);
                    if (adapter.isEmpty())
                    {
                        imgNoMedia.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        imgNoMedia.setVisibility(View.GONE);
                    }
                }

            }
        };

        new Thread()
        {
            private ArrayList<PhotoInfo> allPhoto;

            @Override
            public void run()
            {
                allPhoto = getAllPhoto();
                Message msg = new Message();
                msg.obj = allPhoto;
                msg.arg1 = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }

    private ArrayList<PhotoInfo> getAllPhoto()
    {
        ArrayList<PhotoInfo> allPhotoList = new ArrayList<PhotoInfo>();
        try
        {
            String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            String orderBy = MediaStore.Images.Media._ID;
            Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (imagecursor != null && imagecursor.getCount() > 0)
            {
                while (imagecursor.moveToNext())
                {
                    PhotoInfo photoInfo = new PhotoInfo();
                    int columnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    photoInfo.sdcardPath = imagecursor.getString(columnIndex);
                    allPhotoList.add(photoInfo);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Collections.reverse(allPhotoList);
        return allPhotoList;
    }

    private void initView()
    {
        TextView tvHeadTilte = (TextView) findViewById(R.id.head_title);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setOnItemClickListener(this);
        ImageLoader imageLoader = ImageLoader.getInstance();
        adapter = new PhotoBaseAdapter(getApplicationContext(), imageLoader);
        gridGallery.setAdapter(adapter);
        gridGallery.setFastScrollEnabled(true);
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
        gridGallery.setOnScrollListener(listener);
        btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
        btnGalleryOk.setOnClickListener(this);

        String title = "";
        switch (way){
            case 1:
                title = "可选择" + size + "张图片";
                break;
            case 2:
                title = "选择需要更改的图片";
        }
        tvHeadTilte.setText(title);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_btn:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btnGalleryOk:
                onclickOk();
                break;

            default:
                break;
        }
    }

    private void onclickOk()
    {

        ArrayList<PhotoInfo> selectedList = adapter.getSelected();
        //        if (selectedList.size() == size)
        //        {
        String[] allPath = new String[selectedList.size()];
        for (int i = 0; i < allPath.length; i++)
        {
            allPath[i] = selectedList.get(i).sdcardPath;
        }

        Intent data = new Intent().putExtra("all_path", allPath);
        setResult(RESULT_OK, data);
        finish();
        //        }
        //        else
        //        {
        //            ToastUtil.showShortToast("请选择三张照片");
        //        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ArrayList<PhotoInfo> selectedLists = adapter.getSelected();
        if (selectedLists.size() > (size - 1))
        {
            if (adapter.getItem(position).isSeleted)
            {
                adapter.changeSelection(view, position);
            }
            else
            {
                ToastUtil.showShortToast("所选图片不能超过" + size + "张");
            }
        }
        else
        {
            adapter.changeSelection(view, position);
        }

    }
}
