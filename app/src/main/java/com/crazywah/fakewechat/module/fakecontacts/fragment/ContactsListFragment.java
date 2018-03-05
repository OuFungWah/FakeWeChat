package com.crazywah.fakewechat.module.fakecontacts.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.common.util.DataToFileUtil;
import com.crazywah.fakewechat.crazytools.fragment.BaseFragment;
import com.crazywah.fakewechat.module.fakecontacts.activity.ContactNewFriendActivity;
import com.crazywah.fakewechat.module.fakecontacts.adapter.ContactListAdapter;
import com.crazywah.fakewechat.module.fakecontacts.adapter.ContactListItem;
import com.crazywah.fakewechat.module.fakecontacts.adapter.FirstCharComparator;
import com.crazywah.fakewechat.module.framework.reciver.OnNewFriendListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/2/28.
 */

public class ContactsListFragment extends BaseFragment implements View.OnClickListener, OnNewFriendListener, DataToFileUtil.OnNewFriendSaveListener {

    private static final String TAG = "ContactsListFragment";

    private static final int UPDATE_FRIEND_LIST = 0;
    private static final int NEW_FRIEND_REQUEST = 1;

    private LinearLayout newFriendLl;
    private LinearLayout groupChatLl;
    private LinearLayout tagLl;
    private LinearLayout subscriptionLl;

    private RecyclerView contactRv;
    private List<ContactListItem> itemList = new ArrayList<>();
    private List<UserInfo> userInfoList;
    private ContactListAdapter adapter;

    private RecyclerView.LayoutManager linearManager;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_FRIEND_LIST:
                    updateList();
                    break;
                case NEW_FRIEND_REQUEST:
                    int num = msg.getData().getInt("num");
                    if (num > 0) {
                        newFriendRedDotRl.setVisibility(View.VISIBLE);
                        newFriedRedDotNumTv.setText(num + "");
                    } else {
                        newFriendRedDotRl.setVisibility(View.GONE);
                    }
                    break;
            }
            return false;
        }
    });

    private RelativeLayout newFriendRedDotRl;
    private TextView newFriedRedDotNumTv;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_contacts_list;
    }

    @Override
    protected void initView(View parent) {
        newFriendLl = findView(R.id.contact_new_friend_ll);
        groupChatLl = findView(R.id.contact_group_chat_ll);
        tagLl = findView(R.id.contact_tag_ll);
        subscriptionLl = findView(R.id.contact_subscription_ll);
        contactRv = findView(R.id.contact_list_recycler_view);
        newFriendRedDotRl = findView(R.id.contact_new_friend_request_rl);
        newFriedRedDotNumTv = findView(R.id.contact_new_friend_request_num_tv);

        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                userInfoList = list;
                handler.sendEmptyMessage(UPDATE_FRIEND_LIST);
            }
        });

        linearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new ContactListAdapter(itemList);

        DataToFileUtil.registerOnNewFriendSaveListener(this);
    }

    private void updateList() {

//        for (int i = 0; i < 10; i++) {
//            userInfoList.add(JMessageClient.getMyInfo());
//        }

        for (int i = 0; i < userInfoList.size(); i++) {
            itemList.add(new ContactListItem(userInfoList.get(i)));
        }
        //根据用户名首字母排序
        Collections.sort(itemList, new FirstCharComparator());
        if (userInfoList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void setView() {
        //初始化刷新红点
        onNewFriendSave();
        contactRv.setAdapter(adapter);
        contactRv.setLayoutManager(linearManager);
    }

    @Override
    protected void initListener() {
        newFriendLl.setOnClickListener(this);
        groupChatLl.setOnClickListener(this);
        subscriptionLl.setOnClickListener(this);
        tagLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_new_friend_ll:
                startActivity(ContactNewFriendActivity.class);
                getActivity().overridePendingTransition(R.anim.move_in_from_right, R.anim.move_out_from_right);
                break;
            case R.id.contact_group_chat_ll:
                break;
            case R.id.contact_tag_ll:
                break;
            case R.id.contact_subscription_ll:
                break;
        }
    }

    /**
     * @param intent
     */
    @Override
    public void onNewFriendRequest(Intent intent) {
//        int num = DataToFileUtil.getFriendRequestFromFile().getNewFriendRequestNum();
//        refreshNewFriendNum(num);
    }

    /**
     * 刷新好友请求
     *
     * @param num
     */
    public void refreshNewFriendNum(int num) {
        Message newFriendMessage = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("num", num);
        newFriendMessage.setData(bundle);
        newFriendMessage.what = NEW_FRIEND_REQUEST;
        handler.sendMessage(newFriendMessage);
    }

    @Override
    public void onNewFriendSave() {
        int num = DataToFileUtil.getFriendRequestFromFile().getNewFriendRequestNum();
        refreshNewFriendNum(num);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataToFileUtil.unRegisterOnNewFriendSaveListener(this);
    }
}
