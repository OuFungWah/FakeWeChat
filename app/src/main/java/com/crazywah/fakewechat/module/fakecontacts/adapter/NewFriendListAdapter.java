package com.crazywah.fakewechat.module.fakecontacts.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.module.framework.service.FriendRequestBean;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/4.
 */

public class NewFriendListAdapter extends RecyclerView.Adapter<NewFriendViewHolder> implements View.OnClickListener {

    private FriendRequestBean bean;

    private OnAcceptListener onAcceptListener = null;

    public NewFriendListAdapter(FriendRequestBean bean) {
        this.bean = bean;
    }

    @Override
    public NewFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_new_friend_item, parent, false);
        return new NewFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewFriendViewHolder holder, int position) {
        String username = bean.getUsernameList().get(position);
        holder.nicknameTv.setText(bean.getNickname().get(username));
        holder.noteTextTv.setText(bean.getReasonMap().get(username));
        holder.acceptBtn.setTag(position);
        if (bean.getIsFriendMap().get(username)) {
            holder.acceptBtn.setEnabled(false);
        }
        holder.acceptBtn.setOnClickListener(this);
        try {
            holder.avatarSdv.setImageURI(Uri.fromFile(bean.getAvatarMap().get(username)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bean.getUsernameList().size();
    }

    @Override
    public void onClick(View v) {
        if (onAcceptListener != null) {
            onAcceptListener.onAccept(bean.getUsernameList().get((int) v.getTag()));
        }
    }

    public OnAcceptListener getOnAcceptListener() {
        return onAcceptListener;
    }

    public void setOnAcceptListener(OnAcceptListener onAcceptListener) {
        this.onAcceptListener = onAcceptListener;
    }

    public interface OnAcceptListener {
        void onAccept(String username);
    }
}
