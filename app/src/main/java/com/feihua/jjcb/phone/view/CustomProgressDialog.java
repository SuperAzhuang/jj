package com.feihua.jjcb.phone.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.utils.L;

public class CustomProgressDialog extends Dialog
{

    private TextView tvMsg;


    public CustomProgressDialog(Context context)
    {
       this(context,null);
    }

    public CustomProgressDialog(Context context, String msg) {
        this(context, R.style.CustomProgressDialog, msg);
    }  
  
    public CustomProgressDialog(Context context, int theme, String msg) {
        super(context, theme);  
        this.setContentView(R.layout.progress_layout);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        tvMsg = (TextView) this.findViewById(R.id.text_progress);
        if (tvMsg != null && msg !=null) {
            tvMsg.setText(msg);
        }  
    }

    public void setProgressMsg(String msg)
    {
        L.w("ProgressDialogBar","setProgressMsg = " + msg);
        L.w("ProgressDialogBar","tvMsg = " + tvMsg);
        if (tvMsg != null){
            tvMsg.setText(msg);
        }
    }
  
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
  
        if (!hasFocus) {  
            dismiss();  
        }  
    }  
}  