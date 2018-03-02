package com.crazywah.fakewechat.module.fakemine.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by FungWah on 2018/3/2.
 */

public class ModifyNickNameActivity extends NormalActionBarActivity implements View.OnClickListener, TextWatcher {


    private static final String TAG = "ModifyNickNameActivity";

    private UserInfo userInfo;

    private EditText nicknameEt;

    private String nicknameStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_userinfo_modify_nickname;
    }

    @Override
    protected void initView() {
        userInfo = JMessageClient.getMyInfo();
        nicknameStr = userInfo.getNickname();
        nicknameEt = findView(R.id.modify_nickname_et);
    }

    @Override
    protected void setView() {
        nicknameEt.setText(nicknameStr);
    }

    @Override
    protected void initListener() {
        nicknameEt.addTextChangedListener(this);
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionbarRightBtn) {
        titleTv.setText("修改昵称");
        backIconImg.setOnClickListener(this);
        actionbarRightBtn.setTextColor(getResources().getColor(R.color.colorActionBarRightUnableText));
        actionbarRightBtn.setVisibility(View.VISIBLE);
        actionRightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
            case R.id.actionbar_right_btn:
                userInfo.setNickname(nicknameStr);
                JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.d(TAG, "gotResult: s = " + s);
                        if (s.equals("Success")) {
                            startActivityWithAnim(MineUserInfoActivity.class, R.anim.move_in_from_left, R.anim.move_out_from_left);
                            Intent updateInfoIntent = new Intent();
                            updateInfoIntent.setAction(AppConfig.ACTION_UPDATE_INFO);
                            sendBroadcast(updateInfoIntent);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishWithAnim(R.anim.move_in_from_left, R.anim.move_out_from_left);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        nicknameStr = nicknameEt.getText().toString();
        if (nicknameStr.indexOf('\n') == -1) {
            actionRightBtn.setEnabled(true);
            actionRightBtn.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            ToastUtil.showShort("昵称不允许换行");
            actionRightBtn.setEnabled(false);
            actionRightBtn.setTextColor(getResources().getColor(R.color.colorActionBarRightUnableText));
        }
    }
}
