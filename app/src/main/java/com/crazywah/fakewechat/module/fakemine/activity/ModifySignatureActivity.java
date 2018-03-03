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

public class ModifySignatureActivity extends NormalActionBarActivity implements TextWatcher, View.OnClickListener {

    private UserInfo userInfo;

    private EditText signatureEt;

    private String signatureStr;

    private boolean isUploading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_userinfo_modify_signature;
    }

    @Override
    protected void initView() {
        signatureEt = findView(R.id.modify_region_et);
        userInfo = JMessageClient.getMyInfo();
    }

    @Override
    protected void setView() {
        signatureEt.setText(userInfo.getSignature());
    }

    @Override
    protected void initListener() {
        signatureEt.addTextChangedListener(this);
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionRightBtn) {
        titleTv.setText("修改个性签名");
        backIconImg.setOnClickListener(this);
        actionRightBtn.setVisibility(View.VISIBLE);
        actionRightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_right_btn:
                if (!isUploading) {
                    updateRegion();
                } else {
                    ToastUtil.showShort("上传中...");
                }
                break;
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }

    private void updateRegion() {
        signatureStr = signatureEt.getText().toString();
        userInfo.setSignature(signatureStr);
        isUploading = true;
        JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.equals("Success")) {
                    ToastUtil.showShort("上传签名成功");
                    Intent updateInfoIntent = new Intent();
                    updateInfoIntent.setAction(AppConfig.ACTION_UPDATE_INFO);
                    sendBroadcast(updateInfoIntent);
                    startActivityWithAnim(MineUserInfoActivity.class, R.anim.move_in_from_left, R.anim.move_out_from_left);
                } else {
                    ToastUtil.showShort("上传签名失败");
                    isUploading = false;
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
        signatureStr = signatureEt.getText().toString();
        if (signatureStr.length() > 0) {
            actionRightBtn.setEnabled(true);
        } else {
            actionRightBtn.setEnabled(false);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
