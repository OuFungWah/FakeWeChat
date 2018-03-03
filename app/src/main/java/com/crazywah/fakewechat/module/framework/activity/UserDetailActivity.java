package com.crazywah.fakewechat.module.framework.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/3.
 */

public class UserDetailActivity extends NormalActionBarActivity implements View.OnClickListener {

    private static final String TAG = "UserDetailActivity";

    private UserInfo targetUserInfo;
    private String targetUsernameStr;

    private SimpleDraweeView avatarSdv;
    private TextView notenameTv;
    private TextView usernameTv;
    private TextView nicknameTv;

    private ImageView genderImg;

    private LinearLayout usernameLl;
    private LinearLayout nicknameLl;

    private TextView telTv;
    private LinearLayout telLl;
    private TextView birthdayTv;
    private RelativeLayout birthdayRl;
    private TextView regionTv;
    private LinearLayout regionLl;
    private TextView signatureTv;
    private LinearLayout signatureLl;

    private Button sendBtn;
    private Button addBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_user_detail;
    }

    @Override
    protected void initView() {
        avatarSdv = findView(R.id.user_detail_avatar_sdv);
        notenameTv = findView(R.id.user_detail_notename_tv);
        usernameTv = findView(R.id.user_detail_username_tv);
        nicknameTv = findView(R.id.user_detail_nickname_tv);
        genderImg = findView(R.id.user_detail_gender_img);
        usernameLl = findView(R.id.user_detail_username_ll);
        nicknameLl = findView(R.id.user_detail_nickname_ll);
        telTv = findView(R.id.user_detail_tel_tv);
        telLl = findView(R.id.user_detail_tel_ll);
        birthdayTv = findView(R.id.user_detail_birthday_tv);
        birthdayRl = findView(R.id.user_detail_birthday_rl);
        regionTv = findView(R.id.user_detail_region_tv);
        regionLl = findView(R.id.user_detail_region_ll);
        signatureTv = findView(R.id.user_detail_signature_tv);
        signatureLl = findView(R.id.user_detail_signature_ll);
        sendBtn = findView(R.id.user_detail_send_btn);
        addBtn = findView(R.id.user_detail_add_btn);

        targetUsernameStr = getIntent().getExtras().getString("username");

        getUserInfo();

    }

    private void getUserInfo() {
        JMessageClient.getUserInfo(targetUsernameStr, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Log.d(TAG, "gotResult: s = " + s);
                Log.d(TAG, "gotResult: code = " + i);
                if (s.equals("Success")) {
                    targetUserInfo = userInfo;
                    setView();
                } else {
                    if (i == 898002) {
                        //无对应用户
                    }
                    if (i == 871310) {
                        //网络出错
                        ToastUtil.showShort("请检查你的网络连接");
                    }
                }
            }
        });
    }

    @Override
    protected void setView() {
        if (targetUserInfo != null) {
            usernameTv.setText(targetUserInfo.getUserName());
            notenameTv.setText(targetUserInfo.getNotename());
            nicknameTv.setText(targetUserInfo.getNickname());
            if (targetUserInfo.getGender() == UserInfo.Gender.unknown) {
                genderImg.setVisibility(View.GONE);
            } else {
                genderImg.setImageResource(targetUserInfo.getGender() == UserInfo.Gender.male ? R.drawable.ic_male : R.drawable.ic_female);
            }
            telTv.setText(targetUserInfo.getExtra("telephone"));
            regionTv.setText(targetUserInfo.getRegion());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            birthdayTv.setText(simpleDateFormat.format(new Date(targetUserInfo.getBirthday())));
            signatureTv.setText(targetUserInfo.getSignature());
            if (targetUserInfo.getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                addBtn.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                nicknameLl.setVisibility(View.GONE);
                notenameTv.setText(targetUserInfo.getNickname());
            } else if (targetUserInfo.isFriend()) {
                if (targetUserInfo.getNotename() == null || targetUserInfo.getNotename().equals("")) {
                    notenameTv.setText(targetUserInfo.getNickname());
                }
                birthdayRl.setVisibility(View.VISIBLE);
                telLl.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.GONE);
                sendBtn.setVisibility(View.VISIBLE);
            } else {
                notenameTv.setText(targetUserInfo.getNickname());
                nicknameLl.setVisibility(View.GONE);
                telLl.setVisibility(View.GONE);
                addBtn.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initListener() {
        telTv.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionRightBtn) {
        titleTv.setText("详细资料");
        backIconImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_detail_tel_tv:
                break;
            case R.id.user_detail_add_btn:

                break;
            case R.id.user_detail_send_btn:
                break;
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }
}
