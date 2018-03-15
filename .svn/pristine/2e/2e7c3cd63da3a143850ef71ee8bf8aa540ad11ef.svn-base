package com.feihua.jjcb.phone.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.view.MonPickerDialog;


public abstract class DatePickDialogUtil implements DatePickerDialog.OnDateSetListener
{

    public void showMonPicker(Context ctx,String date) {
        final Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(DateUtil.strToDate("yyyy-MM", date));
        MonPickerDialog dialog = new MonPickerDialog(ctx,R.style.AppTheme_Dialog, this, localCalendar.get(Calendar.YEAR), localCalendar.get(Calendar.MONTH), localCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}