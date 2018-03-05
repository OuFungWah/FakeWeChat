package com.crazywah.fakewechat.module.framework.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by FungWah on 2018/3/5.
 */

public class NewFriendRequestReceiver extends BroadcastReceiver {

    private static final String TAG = "NewFriendReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //如果接收信息的对象实现了OnNewFriendListener
        if (context instanceof OnNewFriendListener)
            Log.d(TAG, "onReceive: 接收到了新朋友请求的广播");
            ((OnNewFriendListener) context).onNewFriendRequest(intent);
    }
}
