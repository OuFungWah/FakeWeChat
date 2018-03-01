package com.crazywah.fakewechat.application;

import android.app.Application;

import com.crazywah.fakewechat.crazytools.util.SPUtil;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.crazytools.util.WindowSizeHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.imsdk.TIMManager;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by FungWah on 2018/2/27.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        SPUtil.init(this);
        WindowSizeHelper.init(this);
        Fresco.initialize(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
    }
}
