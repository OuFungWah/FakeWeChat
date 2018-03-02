package com.crazywah.fakewechat.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.crazywah.fakewechat.crazytools.activity.BaseActivity;

/**
 * Created by FungWah on 2018/3/2.
 */

public class InfoUpdateRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(context instanceof BaseActivity){
            ((BaseActivity)context).refreshData(intent);
        }
    }
}
