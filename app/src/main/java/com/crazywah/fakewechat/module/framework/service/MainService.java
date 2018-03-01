package com.crazywah.fakewechat.module.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by FungWah on 2018/3/2.
 */

public class MainService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MainServieBinder();
    }

    public class MainServieBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

}
