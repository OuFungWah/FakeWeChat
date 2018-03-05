package com.crazywah.fakewechat.module.fakecontacts.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.util.DataToFileUtil;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;
import com.crazywah.fakewechat.module.fakecontacts.adapter.NewFriendItem;
import com.crazywah.fakewechat.module.fakecontacts.adapter.NewFriendListAdapter;
import com.crazywah.fakewechat.module.framework.service.FriendRequestBean;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.event.CommandNotificationEvent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.tasks.GetEventNotificationTaskMng;

import static cn.jpush.im.android.api.event.ContactNotifyEvent.Type.invite_accepted;
import static cn.jpush.im.android.api.event.ContactNotifyEvent.Type.invite_declined;
import static cn.jpush.im.android.api.event.ContactNotifyEvent.Type.invite_received;

/**
 * Created by FungWah on 2018/3/4.
 */

public class ContactNewFriendActivity extends NormalActionBarActivity implements View.OnClickListener, DataToFileUtil.OnNewFriendSaveListener {

    private static final String TAG = "ContactNewFriend";

    private static final int GET_NEW_FRIEND_REQUEST = 0;

    private RecyclerView newFriendListRv;
    private NewFriendListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_NEW_FRIEND_REQUEST:
                    adapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    private FriendRequestBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_new_friend_notification;
    }

    @Override
    protected void initView() {
        newFriendListRv = findView(R.id.contact_new_friend_rv);

        bean = DataToFileUtil.getFriendRequestFromFile();
        adapter = new NewFriendListAdapter(bean);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        DataToFileUtil.registerOnNewFriendSaveListener(this);
    }

    @Override
    protected void setView() {
        newFriendListRv.setAdapter(adapter);
        newFriendListRv.setLayoutManager(layoutManager);
        //初始化好友申请列表
        onNewFriendSave();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionRightBtn) {
        titleTv.setText("新的朋友");
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
    public void onNewFriendSave() {
        FriendRequestBean data = DataToFileUtil.getFriendRequestFromFile();
        bean.setNewFriendRequestNum(data.getNewFriendRequestNum());
        bean.setAvatarMap(data.getAvatarMap());
        bean.setIsFriendMap(data.getIsFriendMap());
        bean.setNickname(data.getNickname());
        bean.setReasonMap(data.getReasonMap());
        bean.setUsernameList(data.getUsernameList());
        Log.d(TAG, "onNewFriendSave: 好友申请列表长度为:" + bean.getUsernameList().size());
        handler.sendEmptyMessage(GET_NEW_FRIEND_REQUEST);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataToFileUtil.unRegisterOnNewFriendSaveListener(this);
        //解绑监听新好友申请以后初始化好友红点数
        DataToFileUtil.initNewFriendNum();
    }
}