package com.crazywah.fakewechat.module.welcome.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by FungWah on 2018/2/28.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "RegisterActivity";

    //用户信息输入
    private SimpleDraweeView avatarSdv;
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText rePasswordEt;

    //转跳登录页面提示
    private TextView switchTv;

    //注册按键
    private Button registerBtn;

    //注册信息出错tips
    private TextView usernameTipsTv;
    private TextView passwordTipsTv;
    private TextView rePasswordTipsTv;

    private String usernameStr = "";
    private String passwordStr = "";
    private String rePasswordStr = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome_register;
    }

    @Override
    protected void initView() {
        avatarSdv = findView(R.id.avatar_sdv);
        usernameEt = findView(R.id.username_et);
        passwordEt = findView(R.id.password_et);
        rePasswordEt = findView(R.id.repassword_et);
        registerBtn = findView(R.id.register_btn);
        switchTv = findView(R.id.switch_tv);

        usernameTipsTv = findView(R.id.username_tips_tv);
        passwordTipsTv = findView(R.id.password_tips_tv);
        rePasswordTipsTv = findView(R.id.repassword_tips_tv);

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initListener() {
        registerBtn.setOnClickListener(this);
        switchTv.setOnClickListener(this);

        //每次输入操作完成即检测当前输入是否正常
        usernameEt.addTextChangedListener(this);
        passwordEt.addTextChangedListener(this);
        rePasswordEt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                register();
                break;
            case R.id.switch_tv:
                finish();
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
        if (usernameEt.isFocused()) {
            usernameStr = usernameEt.getText().toString();
        }
        if (passwordEt.isFocused()) {
            passwordStr = passwordEt.getText().toString();
        }
        if (rePasswordEt.isFocused()) {
            rePasswordStr = rePasswordEt.getText().toString();
        }
        registerBtn.setEnabled(checkMessageEnable());
    }

    /**
     * 消去提示
     *
     * @param textView
     */
    private void dismissTips(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    /**
     * 显示提示
     *
     * @param textView
     * @param tips
     */
    private void showTips(TextView textView, String tips) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(tips);
    }

    /**
     * 检测输入的数据是否合法
     *
     * @return 返回检验结果
     */
    private boolean checkMessageEnable() {
        boolean usernameFlag = false;
        boolean passwordFlag = false;
        boolean rePasswordFlag = false;

        //鉴定 Username
        if (usernameStr != null) {
            if (usernameStr.length() >= 6 && usernameStr.length() <= 10) {
                usernameFlag = true;
                dismissTips(usernameTipsTv);
            } else if (usernameStr.length() > 10) {
                showTips(usernameTipsTv, "用户名字符大于10个字符");
            } else {
                if (usernameStr.length() >= 1)
                    showTips(usernameTipsTv, "用户名字符小于6个字符");
            }
        }

        //鉴定 Password
        if (passwordStr != null) {
            if (passwordStr.length() >= 6 && passwordStr.length() <= 10) {
                passwordFlag = true;
                dismissTips(passwordTipsTv);
            } else if (passwordStr.length() > 10) {
                showTips(passwordTipsTv, "密码字符大于10个字符");
            } else {
                if (passwordStr.length() >= 1)
                    showTips(passwordTipsTv, "密码字符小于6个字符");
            }
        }


        //鉴定 rePassword
        if (rePasswordStr != null) {
            if (passwordStr.equals(rePasswordStr)) {
                rePasswordFlag = true;
                dismissTips(rePasswordTipsTv);
            } else {
                if (rePasswordStr.length() >= 1)
                    showTips(rePasswordTipsTv, "密码不一致");
            }
        }

        return usernameFlag && passwordFlag && rePasswordFlag;
    }

    /**
     * 注册
     */
    private void register() {
        RegisterOptionalUserInfo registerOptionalUserInfo = new RegisterOptionalUserInfo();
        JMessageClient.register(usernameStr, passwordStr, registerOptionalUserInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.d(TAG, "gotResult: code = " + i);
                Log.d(TAG, "gotResult: responseMessage = " + s);
                if (s.equals("Success")) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (s.equals("Invalid username.")) {
                    showTips(usernameTipsTv,"用户名只允许6~10位数字与英文字母组合");
                }
            }
        });
    }

}
