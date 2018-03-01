package com.crazywah.fakewechat.module.fakemine.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.crazytools.util.SPUtil;
import com.crazywah.fakewechat.crazytools.util.WindowSizeHelper;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;
import com.crazywah.fakewechat.module.framework.activity.MainFrameworkActivity;
import com.crazywah.fakewechat.module.welcome.activity.LoginActivity;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by FungWah on 2018/3/1.
 */

public class MineSettingActivity extends NormalActionBarActivity implements View.OnClickListener {

    private LinearLayout securityLl;
    private LinearLayout notificationLl;
    private LinearLayout noDisturbLl;
    private LinearLayout chatLl;
    private LinearLayout privacyLl;
    private LinearLayout generationLl;
    private LinearLayout aboutLl;
    private LinearLayout feedbackLl;
    private LinearLayout pluginsLl;
    private LinearLayout switchLl;
    private LinearLayout logoutLl;

    private LinearLayout dialogLogoutLl;
    private LinearLayout dialogShutdownLl;

    private Dialog dialog;
    private View dialogView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_setting;
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg) {
        backIconImg.setOnClickListener(this);
        titleTv.setText("设置");
    }

    @Override
    protected void initView() {

        securityLl = findView(R.id.mine_setting_security_ll);
        notificationLl = findView(R.id.mine_setting_notification_ll);
        noDisturbLl = findView(R.id.mine_setting_no_disturb_ll);
        chatLl = findView(R.id.mine_setting_chat_ll);
        privacyLl = findView(R.id.mine_setting_privacy_ll);
        generationLl = findView(R.id.mine_setting_generation_ll);
        aboutLl = findView(R.id.mine_setting_about_ll);
        feedbackLl = findView(R.id.mine_setting_feedback_ll);
        pluginsLl = findView(R.id.mine_setting_plugins_ll);
        switchLl = findView(R.id.mine_setting_switch_ll);
        logoutLl = findView(R.id.mine_setting_logout_ll);

        dialog = new Dialog(this, R.style.CloseDialog);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_setting_logout, null);

        dialogLogoutLl = dialogView.findViewById(R.id.close_dialog_logout_Ll);
        dialogShutdownLl = dialogView.findViewById(R.id.close_dialog_shutdown_Ll);
    }

    @Override
    protected void setView() {
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(this.getResources().getDisplayMetrics().widthPixels, (int) (255 * WindowSizeHelper.getHelper().getProporationY())));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }

    @Override
    protected void initListener() {
        securityLl.setOnClickListener(this);
        notificationLl.setOnClickListener(this);
        noDisturbLl.setOnClickListener(this);
        chatLl.setOnClickListener(this);
        privacyLl.setOnClickListener(this);
        generationLl.setOnClickListener(this);
        aboutLl.setOnClickListener(this);
        feedbackLl.setOnClickListener(this);
        pluginsLl.setOnClickListener(this);
        switchLl.setOnClickListener(this);
        logoutLl.setOnClickListener(this);
        dialogLogoutLl.setOnClickListener(this);
        dialogShutdownLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_setting_security_ll:
                break;
            case R.id.mine_setting_notification_ll:
                break;
            case R.id.mine_setting_no_disturb_ll:
                break;
            case R.id.mine_setting_chat_ll:
                break;
            case R.id.mine_setting_privacy_ll:
                break;
            case R.id.mine_setting_generation_ll:
                break;
            case R.id.mine_setting_about_ll:
                break;
            case R.id.mine_setting_feedback_ll:
                break;
            case R.id.mine_setting_plugins_ll:
                break;
            case R.id.mine_setting_switch_ll:
                break;
            case R.id.mine_setting_logout_ll:
                dialog.show();
                break;
            case R.id.close_dialog_logout_Ll:
                JMessageClient.logout();
                try {
                    SPUtil.getInstance("userInfo").clearAll();
                    startActivity(LoginActivity.class);
                    finish();
                    Intent i = new Intent();
                    i.setAction("com.crazywah.action.EXIT_APP");
                    sendBroadcast(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.close_dialog_shutdown_Ll:
                startActivity(MainFrameworkActivity.class);
                Intent i = new Intent();
                i.setAction("com.crazywah.action.EXIT_APP");
                sendBroadcast(i);
                break;
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            finishWithAnim(R.anim.move_in_from_left, R.anim.move_out_from_left);
        }
    }

}
