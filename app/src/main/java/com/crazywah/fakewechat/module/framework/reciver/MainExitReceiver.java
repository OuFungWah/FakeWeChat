package com.crazywah.fakewechat.module.framework.reciver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crazywah.fakewechat.crazytools.activity.BaseActivity;

/**
 * Created by FungWah on 2018/3/2.
 */

public class MainExitReceiver  extends BroadcastReceiver {

    private static final String TAG = "MainExitReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 接收到退出广播");
        ((BaseActivity)context).finish();
    }
}
