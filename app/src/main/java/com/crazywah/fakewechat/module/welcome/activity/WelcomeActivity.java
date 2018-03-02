package com.crazywah.fakewechat.module.welcome.activity;

import android.util.Log;
import android.widget.ImageView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.crazytools.util.SPUtil;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.module.framework.activity.MainFrameworkActivity;
import com.crazywah.fakewechat.tools.CrazyWahUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

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
        try {
            final String usernameStr = SPUtil.getInstance("userInfo").getString("username", "");
            String passwordStr = SPUtil.getInstance("userInfo").getString("password", "");
            if (usernameStr.equals("") || passwordStr.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            startActivity(LoginActivity.class);
                            finish();
                        } catch (Exception e) {

                        }
                    }
                }).start();
            } else {
                JMessageClient.login(usernameStr, passwordStr, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (s.equals("")) {
                            Log.d(TAG, "gotResult: 登陆成功");
                            try {
                                startActivity(MainFrameworkActivity.class);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            ToastUtil.showShort("登录失败,请检查网络连接");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1500);
                                        startActivity(LoginActivity.class);
                                        finish();
                                    } catch (Exception e) {

                                    }
                                }
                            }).start();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initListener() {

    }
}
