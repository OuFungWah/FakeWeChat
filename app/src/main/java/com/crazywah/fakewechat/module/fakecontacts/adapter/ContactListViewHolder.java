package com.crazywah.fakewechat.module.fakecontacts.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by FungWah on 2018/3/4.
 */

public class ContactListViewHolder extends RecyclerView.ViewHolder {

    public TextView firstCharTv = null;
    public TextView nicknameTv = null;
    public SimpleDraweeView avatarSdv = null;

    public ContactListViewHolder(View itemView) {
        super(itemView);
        if ((int)itemView.getTag() == ContactListAdapter.type.HEAD.ordinal()) {
            firstCharTv = itemView.findViewById(R.id.contact_list_head_first_char_tv);
        } else {
            nicknameTv = itemView.findViewById(R.id.contact_user_nickname_tv);
            avatarSdv = itemView.findViewById(R.id.contact_user_avatar_sdv);
        }
    }
}
