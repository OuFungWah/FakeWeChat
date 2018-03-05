package com.crazywah.fakewechat.module.fakecontacts.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazywah.fakewechat.R;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/4.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListViewHolder> {

    private static final String TAG = "ContactListAdapter";

    public enum type {
        HEAD,
        USER
    }

    private List<ContactListItem> contactListItemList;

    public ContactListAdapter(List<ContactListItem> list) {
        this.contactListItemList = list;
    }

    @Override
    public ContactListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == type.HEAD.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_head_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_user_item, parent, false);
        }
        view.setTag(viewType);
        return new ContactListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactListViewHolder holder, int position) {
        ContactListItem item = contactListItemList.get(position);
        if ((int) holder.itemView.getTag() == type.HEAD.ordinal()) {
            holder.firstCharTv.setText(item.getFirstChar());
        } else {
            try {
                Uri uri = Uri.fromFile(item.getUserInfo().getAvatarFile());
                holder.avatarSdv.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.nicknameTv.setText(item.getUserInfo().getNickname());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size 为" + contactListItemList.size());
        return contactListItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (contactListItemList.get(position).getUserInfo() == null) {
            return type.HEAD.ordinal();
        } else {
            return type.USER.ordinal();
        }
    }
}
