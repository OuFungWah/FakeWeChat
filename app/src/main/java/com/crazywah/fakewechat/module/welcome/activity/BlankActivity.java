package com.crazywah.fakewechat.module.welcome.activity;

import android.view.View;
import android.widget.Button;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by FungWah on 2018/2/28.
 */

public class BlankActivity extends BaseActivity implements View.OnClickListener {

    private Button btn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome_blank;
    }

    @Override
    protected void initView() {
        btn = findView(R.id.logout_btn);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initListener() {
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        JMessageClient.logout();
        finish();
    }
}
