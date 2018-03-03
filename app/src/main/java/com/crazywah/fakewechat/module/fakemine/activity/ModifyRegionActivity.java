package com.crazywah.fakewechat.module.fakemine.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

public class ModifyRegionActivity extends NormalActionBarActivity implements View.OnClickListener, TextWatcher {

    private UserInfo userInfo;

    private EditText regionEt;

    private String regionStr;

    private boolean isUploading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_userinfo_modify_region;
    }

    @Override
    protected void initView() {
        regionEt = findView(R.id.modify_region_et);
        userInfo = JMessageClient.getMyInfo();
    }

    @Override
    protected void setView() {
        regionEt.setText(userInfo.getRegion());
    }

    @Override
    protected void initListener() {
        regionEt.addTextChangedListener(this);
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionRightBtn) {
        titleTv.setText("修改地区");
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
                }else{
                    ToastUtil.showShort("上传中...");
                }
                break;
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }

    private void updateRegion() {
        regionStr = regionEt.getText().toString();
        userInfo.setRegion(regionStr);
        isUploading = true;
        JMessageClient.updateMyInfo(UserInfo.Field.region, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.equals("Success")) {
                    Intent updateInfoIntent = new Intent();
                    updateInfoIntent.setAction(AppConfig.ACTION_UPDATE_INFO);
                    sendBroadcast(updateInfoIntent);
                    startActivityWithAnim(MineUserInfoActivity.class, R.anim.move_in_from_left, R.anim.move_out_from_left);
                } else {
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
        regionStr = regionEt.getText().toString();
        if (regionStr.length() > 0) {
            actionRightBtn.setEnabled(true);
        } else {
            actionRightBtn.setEnabled(false);
        }
    }
}
