package com.crazywah.fakewechat.module.fakemine.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by FungWah on 2018/3/3.
 */

public class PickDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnGetDateListener onGetDateListener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private Date date;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            date = simpleDateFormat.parse(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
            if (onGetDateListener != null) {
                onGetDateListener.onGetDate(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setOnGetDateListener(OnGetDateListener onGetDateListener) {
        this.onGetDateListener = onGetDateListener;
    }

    public interface OnGetDateListener {
        void onGetDate(Date date);
    }

}
