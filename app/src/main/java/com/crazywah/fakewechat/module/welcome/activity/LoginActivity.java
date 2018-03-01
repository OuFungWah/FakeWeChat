package com.crazywah.fakewechat.module.welcome.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.crazytools.util.SPUtil;
import com.crazywah.fakewechat.module.framework.activity.MainFrameworkActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by FungWah on 2018/2/26.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "LoginActivity";

    private SimpleDraweeView avatarSdv;
    private EditText usernameEt;
    private EditText passwordEt;

    private TextView switchTv;

    private Button loginBtn;

    private TextView usernameTipsTv;
    private TextView passwordTipsTv;

    private String usernameStr = "";
    private String passwordStr = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome_login;
    }

    @Override
    protected void initView() {
        avatarSdv = findView(R.id.avatar_sdv);
        usernameEt = findView(R.id.username_et);
        passwordEt = findView(R.id.password_et);
        switchTv = findView(R.id.switch_tv);
        loginBtn = findView(R.id.login_btn);

        usernameTipsTv = findView(R.id.username_tips_tv);
        passwordTipsTv = findView(R.id.password__tips_tv);

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initListener() {
        switchTv.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        avatarSdv.setOnClickListener(this);

        usernameEt.addTextChangedListener(this);
        passwordEt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar_sdv:

                break;
            case R.id.switch_tv:
                startActivity(RegisterActivity.class);
                break;
            case R.id.login_btn:
                login();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        usernameStr = usernameEt.getText().toString();
        passwordStr = passwordEt.getText().toString();
        loginBtn.setEnabled(checkMessageEnable());
    }

    private void dismissTips(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    private void showTips(TextView textView, String tips) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(tips);
    }

    private boolean checkMessageEnable() {
        boolean usernameFlag = false;
        boolean passwordFlag = false;

        //鉴定 username
        if (usernameStr != null) {
            if (usernameStr.length() >= 6) {
                usernameFlag = true;
                dismissTips(usernameTipsTv);
            } else {
                if (usernameStr.length() >= 1)
                    showTips(usernameTipsTv, "用户名少于6个字符");
            }
        }

        //鉴定 password
        if (passwordStr != null) {
            if (passwordStr.length() >= 6) {
                passwordFlag = true;
                dismissTips(passwordTipsTv);
            } else {
                if (passwordStr.length() >= 1)
                    showTips(passwordTipsTv, "密码少于6个字符");
            }
        }

        return usernameFlag && passwordFlag;
    }


    private void login() {
        JMessageClient.login(usernameStr, passwordStr, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.d(TAG, "gotResult: code = " + i);
                Log.d(TAG, "gotResult: response = " + s + ".");
                if (s.equals("")) {
                    Log.d(TAG, "gotResult: 登陆成功");
                    try {
                        SPUtil.getInstance("userInfo").putString("username",usernameStr);
                        SPUtil.getInstance("userInfo").putString("password",usernameStr);
                        startActivity(MainFrameworkActivity.class);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (s.equals("invalid password")) {
                    Log.d(TAG, "gotResult: 密码错误");
                    showTips(passwordTipsTv, "密码错误");
                }
                if (s.equals("Invalid username.")) {
                    Log.d(TAG, "gotResult: 用户名不合规格");
                    showTips(usernameTipsTv, "用户名不合规格");
                }
                if (s.equals("user not exist")) {
                    Log.d(TAG, "gotResult: 用户不存在");
                    showTips(usernameTipsTv, "用户不存在");
                }
            }
        });
    }

}
