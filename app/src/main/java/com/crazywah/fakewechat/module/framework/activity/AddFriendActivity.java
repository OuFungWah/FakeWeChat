package com.crazywah.fakewechat.module.framework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;
import com.crazywah.fakewechat.module.welcome.activity.BlankActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/3.
 */

public class AddFriendActivity extends NormalActionBarActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

    private static final String TAG = "AddFriendActivity";

    private EditText searchEt;
    private String friendNameStr;
    private TextView myUsernameTv;

    private RelativeLayout noUserRl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_add_friend;
    }

    @Override
    protected void initView() {
        searchEt = findView(R.id.add_friend_username_et);
        myUsernameTv = findView(R.id.add_friend_username_tv);
        noUserRl = findView(R.id.add_friend_no_user_rl);
    }

    @Override
    protected void setView() {
        myUsernameTv.setText(JMessageClient.getMyInfo().getUserName());
    }

    @Override
    protected void initListener() {
        searchEt.addTextChangedListener(this);
        searchEt.setOnEditorActionListener(this);
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionRightBtn) {
        titleTv.setText("添加Fake好友");
        backIconImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*文字变化检测*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        friendNameStr = searchEt.getText().toString();
        noUserRl.setVisibility(View.GONE);
    }
    /*文字变化检测*/

    //软键盘动作监听
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                friendNameStr = v.getText().toString();
                getUser(friendNameStr);
                break;
        }
        return false;
    }

    private void getUser(final String username) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Log.d(TAG, "gotResult: s = " + s);
                Log.d(TAG, "gotResult: code = " + i);
                if (s.equals("Success")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", userInfo.getUserName());
                    startActivity(bundle, UserDetailActivity.class);
                    overridePendingTransition(R.anim.move_in_from_right, R.anim.move_out_from_right);
                } else {
                    if (i == 898002) {
                        //无对应用户
                        noUserRl.setVisibility(View.VISIBLE);
                    }
                    if (i == 871310) {
                        //网络出错
                        ToastUtil.showShort("请检查你的网络连接");
                    }
                }
            }
        });
    }

}
