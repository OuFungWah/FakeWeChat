package com.crazywah.fakewechat.module.fakemine.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by FungWah on 2018/3/3.
 */

public class ModifyTelephoneActivity extends NormalActionBarActivity implements TextWatcher, View.OnClickListener {

    private UserInfo userInfo;

    private EditText telEt;

    private String telStr;

    private boolean isUploading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_userinfo_modify_telephone;
    }

    @Override
    protected void initView() {
        telEt = findView(R.id.modify_telephone_et);
        userInfo = JMessageClient.getMyInfo();
    }

    @Override
    protected void setView() {
        telEt.setText(userInfo.getExtra("telephone"));
    }

    @Override
    protected void initListener() {
        telEt.addTextChangedListener(this);
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionRightBtn) {
        titleTv.setText("修改电话");
        backIconImg.setOnClickListener(this);
        actionRightBtn.setVisibility(View.VISIBLE);
        actionRightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_right_btn:
                if (!isUploading) {
                    updateTel();
                } else {
                    ToastUtil.showShort("上传中...");
                }
                break;
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }

    private void updateTel() {
        telStr = telEt.getText().toString();
        userInfo.setUserExtras("telephone", telStr);
        isUploading = true;
        JMessageClient.updateMyInfo(UserInfo.Field.extras, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.equals("Success")) {
                    ToastUtil.showShort("修改电话号码成功");
                    Intent updateInfoIntent = new Intent();
                    updateInfoIntent.setAction(AppConfig.ACTION_UPDATE_INFO);
                    sendBroadcast(updateInfoIntent);
                    startActivityWithAnim(MineUserInfoActivity.class, R.anim.move_in_from_left, R.anim.move_out_from_left);
                } else {
                    isUploading = false;
                    ToastUtil.showShort("修改电话号码失败");

                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        telStr = telEt.getText().toString();
        if (telStr.length() > 0) {
            actionRightBtn.setEnabled(true);
        } else {
            actionRightBtn.setEnabled(false);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
