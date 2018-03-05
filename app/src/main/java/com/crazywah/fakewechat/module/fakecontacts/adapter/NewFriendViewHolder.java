package com.crazywah.fakewechat.module.fakecontacts.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by FungWah on 2018/3/4.
 */

public class NewFriendViewHolder extends RecyclerView.ViewHolder {

    public TextView nicknameTv;
    public TextView noteTextTv;
    public SimpleDraweeView avatarSdv;
    public Button acceptBtn;

    public NewFriendViewHolder(View itemView) {
        super(itemView);
        nicknameTv = itemView.findViewById(R.id.contact_new_friend_nickname_tv);
        noteTextTv = itemView.findViewById(R.id.contact_new_friend_note_tv);
        avatarSdv = itemView.findViewById(R.id.contact_new_friend_avatar_sdv);
        acceptBtn = itemView.findViewById(R.id.contact_new_friend_accept_btn);
    }
}
