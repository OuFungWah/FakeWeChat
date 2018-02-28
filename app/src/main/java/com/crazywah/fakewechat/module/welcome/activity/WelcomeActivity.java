package com.crazywah.fakewechat.module.welcome.activity;

import android.widget.ImageView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.tools.CrazyWahUtils;

public class WelcomeActivity extends BaseActivity {

    private ImageView backgroundImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome_main;
    }

    @Override
    protected void initView() {
        backgroundImg = findView(R.id.background_img);
        CrazyWahUtils.hideVirtualKeyBoard(this);
    }

    @Override
    protected void setView() {
        backgroundImg.setImageResource(R.drawable.splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1500);
                    startActivity(LoginActivity.class);
                    finish();
                }catch (Exception e){

                }
            }
        }).start();
    }

    @Override
    protected void initListener() {

    }
}
