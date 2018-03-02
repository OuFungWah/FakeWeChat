package com.crazywah.fakewechat.module.fakemine.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.crazytools.util.WindowSizeHelper;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;
import com.crazywah.fakewechat.common.receiver.InfoUpdateRecevier;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by FungWah on 2018/3/2.
 */

public class MineUserInfoActivity extends NormalActionBarActivity implements View.OnClickListener {

    private UserInfo userInfo;

    private RelativeLayout avatarRl;
    private RelativeLayout nicknameRl;
    private RelativeLayout usernameRl;
    private RelativeLayout genderRl;
    private RelativeLayout addressRl;

    private TextView nicknameTv;
    private TextView usernameTv;
    private TextView genderTv;
    private TextView regionTv;

    private String nicknameStr;
    private String usernameStr;
    private String genderStr;
    private String regionStr;

    private Dialog genderDialog;
    private View genderDialogView;

    private RelativeLayout maleRl;
    private RelativeLayout femaleRl;

    private Button maleBtn;
    private Button femaleBtn;

    private BroadcastReceiver updateRecreiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData(intent);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_userinfo;
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionbarRightBtn) {
        backIconImg.setOnClickListener(this);
        titleTv.setText("个人信息");
    }

    @Override
    protected void initView() {

        userInfo = JMessageClient.getMyInfo();

        avatarRl = findView(R.id.userinfo_avatar_rl);
        nicknameRl = findView(R.id.userinfo_nickname_rl);
        usernameRl = findView(R.id.userinfo_username_rl);
        genderRl = findView(R.id.userinfo_gender_rl);
        addressRl = findView(R.id.userinfo_region_rl);

        nicknameTv = findView(R.id.userinfo_nickname_tv);
        usernameTv = findView(R.id.userinfo_username_tv);
        genderTv = findView(R.id.userinfo_gender_tv);
        regionTv = findView(R.id.userinfo_region_tv);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.ACTION_UPDATE_INFO);
        registerReceiver(updateRecreiver, intentFilter);

        genderDialog = new Dialog(this, R.style.CloseDialog);
        genderDialogView = getLayoutInflater().inflate(R.layout.dialog_gender, null);

        maleRl = genderDialogView.findViewById(R.id.dialog_male_rl);
        femaleRl = genderDialogView.findViewById(R.id.dialog_female_rl);

        maleBtn = genderDialogView.findViewById(R.id.dialog_male_btn);
        femaleBtn = genderDialogView.findViewById(R.id.dialog_female_btn);

    }

    @Override
    protected void setView() {
        nicknameStr = (!userInfo.getNickname().equals("") ? userInfo.getNickname() : "未设置昵称");
        usernameStr = (!userInfo.getUserName().equals("") ? userInfo.getUserName() : "未设置用户名");
        genderStr = userInfo.getGender() == UserInfo.Gender.male ? "男" : userInfo.getGender() == UserInfo.Gender.female ? "女" : "未知";
        regionStr = (!userInfo.getRegion().equals("") ? userInfo.getRegion() : "未设置地区");
        nicknameTv.setText(nicknameStr);
        usernameTv.setText(usernameStr);
        genderTv.setText(genderStr);
        regionTv.setText(regionStr);

        genderDialog.setContentView(genderDialogView, new ViewGroup.LayoutParams((int) (870 * WindowSizeHelper.getHelper().getProporationX()), (int) (510 * WindowSizeHelper.getHelper().getProporationY())));
        genderDialog.getWindow().setGravity(Gravity.CENTER);

        maleBtn.setEnabled(userInfo.getGender() == UserInfo.Gender.male);
        femaleBtn.setEnabled(userInfo.getGender() == UserInfo.Gender.female);
    }

    @Override
    protected void initListener() {
        avatarRl.setOnClickListener(this);
        nicknameRl.setOnClickListener(this);
        usernameRl.setOnClickListener(this);
        genderRl.setOnClickListener(this);
        addressRl.setOnClickListener(this);

        maleRl.setOnClickListener(this);
        femaleRl.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (genderDialog.isShowing()) {
            genderDialog.dismiss();
        } else {
            finishWithAnim(R.anim.move_in_from_left, R.anim.move_out_from_left);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
            case R.id.userinfo_avatar_rl:
                break;
            case R.id.userinfo_nickname_rl:
                startActivityWithAnim(ModifyNickNameActivity.class, R.anim.move_in_from_right, R.anim.move_out_from_right);
                break;
            case R.id.userinfo_username_rl:
                break;
            case R.id.userinfo_gender_rl:
                genderDialog.show();

                break;
            case R.id.userinfo_region_rl:
                break;
            case R.id.dialog_male_rl:
                updateGender(UserInfo.Gender.male);
                break;
            case R.id.dialog_female_rl:
                updateGender(UserInfo.Gender.female);
                break;
            default:
                break;
        }

    }

    /**
     * 更新性别信息
     *
     * @param gender
     */
    void updateGender(UserInfo.Gender gender) {
        userInfo.setGender(gender);
        maleBtn.setEnabled(gender == UserInfo.Gender.male);
        femaleBtn.setEnabled(gender == UserInfo.Gender.female);
        JMessageClient.updateMyInfo(UserInfo.Field.gender, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.equals("Success")) {
                    Intent intent = new Intent();
                    intent.setAction(AppConfig.ACTION_UPDATE_INFO);
                    sendBroadcast(intent);
                } else {
                    ToastUtil.showShort("请检查网络连接");
                }
                genderDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void refreshData(Intent intent) {
        super.refreshData(intent);
        //重新获取新的用户信息
        userInfo = JMessageClient.getMyInfo();
        onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateRecreiver);
    }
}
