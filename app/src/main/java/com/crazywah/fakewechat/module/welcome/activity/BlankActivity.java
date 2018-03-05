package com.crazywah.fakewechat.module.welcome.activity;

import android.view.View;
import android.widget.Button;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by FungWah on 2018/2/28.
 */

public class BlankActivity extends BaseActivity implements View.OnClickListener {

    private Button btn;

    private Button sendBtn;
    private Button addBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome_blank;
    }

    @Override
    protected void initView() {
        btn = findView(R.id.logout_btn);
        sendBtn = findView(R.id.send_btn);
        addBtn = findView(R.id.add_btn);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initListener() {
        btn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_btn:
                JMessageClient.logout();
                finish();
                break;
            case R.id.send_btn:
                JMessageClient.sendMessage(JMessageClient.createSingleTextMessage("123456", AppConfig.APP_KEY, "我爱你哟~"));
                break;
            case R.id.add_btn:
                ContactManager.sendInvitationRequest("123456", null, "我想添加你为好友", new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {

                    }
                });
                break;
        }

    }
}
